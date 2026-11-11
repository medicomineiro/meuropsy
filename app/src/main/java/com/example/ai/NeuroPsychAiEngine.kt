package com.example.ai

import android.graphics.Bitmap
import android.util.Log
import com.example.model.AlgorithmStep
import com.example.model.ConsensusAnalysisResult
import com.example.model.DrugOption
import com.example.model.NeuroPerspective
import com.example.model.PsychPerspective
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "NeuroPsychAiEngine"

class NeuroPsychAiEngine {

    // Strict JSON schema matching the mandated output architecture
    private val analysisSchema = Schema.obj(
        properties = mapOf(
            "caseSummary" to Schema.string(description = "Síntese técnica concisa do quadro clínico neuropsiquiátrico"),
            "neuroanatomicalCorrelation" to Schema.string(
                description = "Mecanismo subjacente direto conectando a fenomenologia clínica aos circuitos neuronais/vias envolvidas (ex.: alça fronto-estriatal, sistema límbico, vias dopaminérgicas/serotoninérgicas)"
            ),
            "neuroPerspective" to Schema.obj(
                properties = mapOf(
                    "organicEtiologies" to Schema.array(
                        items = Schema.string(),
                        description = "Causas orgânicas e mimetizadores neurológicos"
                    ),
                    "neuroimagingCorrelations" to Schema.string(
                        description = "Padrão de lesão/neuroimagem e correlação radiológica"
                    ),
                    "biomarkersAndScreening" to Schema.array(
                        items = Schema.string(),
                        description = "Necessidade de biomarcadores ou screening complementar (incluindo exclusão obrigatória de causas reversíveis em declínio cognitivo: Vit B12, TSH, Sífilis/VDRL)"
                    ),
                    "summary" to Schema.string(description = "Conclusão e posicionamento da neurologia")
                )
            ),
            "psychPerspective" to Schema.obj(
                properties = mapOf(
                    "psychopathology" to Schema.string(description = "Psicopatologia pura"),
                    "longitudinalCourse" to Schema.string(description = "Curso longitudinal"),
                    "stagingAndFunctionalImpact" to Schema.string(description = "Estadiamento e impacto cognitivo-funcional"),
                    "summary" to Schema.string(description = "Conclusão e posicionamento da psiquiatria")
                )
            ),
            "therapeuticComparison" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "drugName" to Schema.string(description = "Nome do fármaco"),
                        "drugClass" to Schema.string(description = "Classe farmacológica"),
                        "pharmacokineticProfile" to Schema.string(
                            description = "Meia-vida, metabolismo CYP450, receptorologia relevante (ex: 5-HT2A, D2, H1, anticolinérgico)"
                        ),
                        "pros" to Schema.array(items = Schema.string(), description = "Eficácia específica"),
                        "cons" to Schema.array(
                            items = Schema.string(),
                            description = "Efeitos adversos motores/metabólicos/cognitivos e interações"
                        ),
                        "availabilityBrazil" to Schema.string(
                            description = "SUS Atenção Básica (RENAME), SUS Componente Especializado (Alto Custo) ou Saúde Suplementar / Desembolso direto"
                        ),
                        "susDetails" to Schema.string(description = "Critérios de PCDT ou protocolo no SUS")
                    )
                ),
                description = "Tabela comparativa de fármacos avaliados"
            ),
            "practicalAlgorithm" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "stepNumber" to Schema.integer(description = "Número da etapa sequencial"),
                        "stepTitle" to Schema.string(description = "Título da conduta (Passo 1, Passo 2, Resgate/Refratariedade)"),
                        "actionPlan" to Schema.string(description = "Ação objetiva e terapêutica"),
                        "monitoringMetrics" to Schema.string(
                            description = "Métricas e scores de acompanhamento em formato LaTeX (ex.: \$BPRS\$, \$PHQ-9\$, \$CDR\$, \$MoCA\$)"
                        )
                    )
                ),
                description = "Algoritmo de conduta prática sequencial"
            ),
            "clinicalPearls" to Schema.array(
                items = Schema.string(),
                description = "2 a 3 pontos-chave estritamente práticos e diretos que mudam o desfecho do caso ou evitam iatrogenias comuns"
            )
        )
    )

    private val genConfig = generationConfig {
        responseMimeType = "application/json"
        responseSchema = analysisSchema
    }

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = genConfig,
            systemInstruction = content {
                text(
                    """
                    Você é o "NeuroPsych Consensus Engine", uma ferramenta analítica de suporte à decisão clínica e estudo avançado, operando com rigor médico estrito, jargão técnico apropriado e fundamentação em evidências consolidadas (hierarquia: UpToDate, diretrizes brasileiras recentes <2 anos, e diretrizes internacionais de referência).
                    
                    OBJETIVO:
                    Analisar casos clínicos, anotações de evolução psiquiátrica e inputs de neuroimagem/escalas, entregando sínteses diagnósticas bifocais (Neurologia vs. Psiquiatria), análise de interações farmacológicas complexas e diretrizes de conduta objetivas, contextualizadas para o sistema de saúde brasileiro (SUS - RENAME Atenção Básica vs. Componente Especializado/Alto Custo, e Saúde Suplementar).
                    
                    OUTPUT MANDATÓRIO:
                    1. Correlação Neuroanatômica & Fisiopatológica: Mecanismo subjacente conectando fenomenologia aos circuitos/vias (alça fronto-estriatal, sistema límbico, vias dopaminérgicas/serotoninérgicas/colinérgicas).
                    2. Painel de Debate / Consenso (Neurologia vs. Psiquiatria):
                       - Perspectiva Neuroclínica: Causas orgânicas, mimetizadores, neuroimagem, biomarcadores. EM DECLÍNIO COGNITIVO, EXCLUA SEMPRE causas reversíveis (B12, TSH, Sífilis).
                       - Perspectiva Psiquiátrica: Psicopatologia pura, curso longitudinal, estadiamento e impacto cognitivo-funcional.
                    3. Análise Terapêutica Comparativa:
                       - Fármaco/Classe, Perfil Farmacológico (meia-vida, CYP450, receptorologia), Prós e Contras (motores/metabólicos/cognitivos), Disponibilidade Brasil (SUS RENAME Atenção Básica vs. Componente Especializado/Alto Custo vs. Saúde Suplementar/Desembolso).
                    4. Algoritmo de Conduta Prática: Passos sequenciais diretos (Passo 1, Passo 2, Resgate/Refratariedade) com métricas e scores em formato LaTeX quando aplicável (${'$'}BPRS${'$'}, ${'$'}PHQ-9${'$'}, ${'$'}CDR${'$'}, ${'$'}MoCA${'$'}, ${'$'}UPDRS${'$'}).
                    5. Clinical Pearls: 2 a 3 pontos-chave estritamente práticos e diretos que mudam o desfecho do caso ou evitam iatrogenias comuns.
                    
                    STYLE GUARDRAILS:
                    - Seja direto, conciso e técnico. Dispense cumprimentos e introduções genéricas.
                    - Elimine advertências, disclaimers institucionais ou avisos de "consulte um médico", pois o ambiente é exclusivo para uso e estudo médico.
                    """.trimIndent()
                )
            }
        )
    }

    suspend fun analyzeCase(
        clinicalNotes: String,
        neuroimagingFindings: String,
        therapeuticScheme: String,
        inputBitmap: Bitmap? = null
    ): ConsensusAnalysisResult {
        return withTimeout(60_000L) {
            val promptText = buildString {
                appendLine("ANÁLISE DE CASO CLÍNICO NEUROPSIQUIÁTRICO:")
                appendLine("1. FRAGMENTOS DE EVOLUÇÃO CLÍNICA / ANAMNESE:")
                appendLine(clinicalNotes.ifBlank { "Não informado especificamente." })
                appendLine()
                appendLine("2. ACHADOS OU CORTES DE NEUROIMAGEM / EXAMES COMPLEMENTARES:")
                appendLine(neuroimagingFindings.ifBlank { "Sem achados de neuroimagem anexados." })
                appendLine()
                appendLine("3. HIPÓTESES DIAGNÓSTICAS / ESQUEMA TERAPÊUTICO ATUAL:")
                appendLine(therapeuticScheme.ifBlank { "Nenhum esquema em curso." })
            }

            val response = if (inputBitmap != null) {
                model.generateContent(
                    content {
                        text(promptText)
                        image(inputBitmap)
                    }
                )
            } else {
                model.generateContent(promptText)
            }

            val jsonString = response.text ?: throw IllegalStateException("Model returned empty text.")
            parseAnalysisResponse(jsonString)
        }
    }

    private fun parseAnalysisResponse(jsonString: String): ConsensusAnalysisResult {
        val root = JSONObject(jsonString)

        val caseSummary = root.optString("caseSummary", "")
        val neuroanatomical = root.optString("neuroanatomicalCorrelation", "")

        val neuroJson = root.optJSONObject("neuroPerspective") ?: JSONObject()
        val neuroPerspective = NeuroPerspective(
            title = "Perspectiva Neuroclínica",
            organicEtiologies = jsonArrayToList(neuroJson.optJSONArray("organicEtiologies")),
            neuroimagingCorrelations = neuroJson.optString("neuroimagingCorrelations", ""),
            biomarkersAndScreening = jsonArrayToList(neuroJson.optJSONArray("biomarkersAndScreening")),
            summary = neuroJson.optString("summary", "")
        )

        val psychJson = root.optJSONObject("psychPerspective") ?: JSONObject()
        val psychPerspective = PsychPerspective(
            title = "Perspectiva Psiquiátrica",
            psychopathology = psychJson.optString("psychopathology", ""),
            longitudinalCourse = psychJson.optString("longitudinalCourse", ""),
            stagingAndFunctionalImpact = psychJson.optString("stagingAndFunctionalImpact", ""),
            summary = psychJson.optString("summary", "")
        )

        val drugsArray = root.optJSONArray("therapeuticComparison")
        val therapeuticList = mutableListOf<DrugOption>()
        if (drugsArray != null) {
            for (i in 0 until drugsArray.length()) {
                val drugObj = drugsArray.getJSONObject(i)
                therapeuticList.add(
                    DrugOption(
                        drugName = drugObj.optString("drugName", ""),
                        drugClass = drugObj.optString("drugClass", ""),
                        pharmacokineticProfile = drugObj.optString("pharmacokineticProfile", ""),
                        pros = jsonArrayToList(drugObj.optJSONArray("pros")),
                        cons = jsonArrayToList(drugObj.optJSONArray("cons")),
                        availabilityBrazil = drugObj.optString("availabilityBrazil", "SUS / Saúde Suplementar"),
                        susDetails = drugObj.optString("susDetails", "")
                    )
                )
            }
        }

        val algorithmArray = root.optJSONArray("practicalAlgorithm")
        val algorithmList = mutableListOf<AlgorithmStep>()
        if (algorithmArray != null) {
            for (i in 0 until algorithmArray.length()) {
                val stepObj = algorithmArray.getJSONObject(i)
                algorithmList.add(
                    AlgorithmStep(
                        stepNumber = stepObj.optInt("stepNumber", i + 1),
                        stepTitle = stepObj.optString("stepTitle", "Passo ${i + 1}"),
                        actionPlan = stepObj.optString("actionPlan", ""),
                        monitoringMetrics = stepObj.optString("monitoringMetrics", "")
                    )
                )
            }
        }

        val pearlsList = jsonArrayToList(root.optJSONArray("clinicalPearls"))

        return ConsensusAnalysisResult(
            caseSummary = caseSummary,
            neuroanatomicalCorrelation = neuroanatomical,
            neuroPerspective = neuroPerspective,
            psychPerspective = psychPerspective,
            therapeuticComparison = therapeuticList,
            practicalAlgorithm = algorithmList,
            clinicalPearls = pearlsList
        )
    }

    private fun jsonArrayToList(jsonArray: org.json.JSONArray?): List<String> {
        if (jsonArray == null) return emptyList()
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.optString(i))
        }
        return list
    }
}
