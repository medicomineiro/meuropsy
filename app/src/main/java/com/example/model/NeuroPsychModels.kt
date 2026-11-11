package com.example.model

data class DrugOption(
    val drugName: String,
    val drugClass: String,
    val pharmacokineticProfile: String, // Half-life, CYP450 metabolism, receptors (e.g. 5-HT2A, D2, H1)
    val pros: List<String>,
    val cons: List<String>,
    val availabilityBrazil: String, // "SUS Atenção Básica", "SUS Componente Especializado", "Saúde Suplementar / Desembolso"
    val susDetails: String = ""
)

data class NeuroPerspective(
    val title: String = "Perspectiva Neuroclínica",
    val organicEtiologies: List<String>,
    val neuroimagingCorrelations: String,
    val biomarkersAndScreening: List<String>, // Inclui exclusão obrigatória de causas reversíveis: B12, TSH, Sífilis
    val summary: String
)

data class PsychPerspective(
    val title: String = "Perspectiva Psiquiátrica",
    val psychopathology: String,
    val longitudinalCourse: String,
    val stagingAndFunctionalImpact: String,
    val summary: String
)

data class AlgorithmStep(
    val stepNumber: Int,
    val stepTitle: String,
    val actionPlan: String,
    val monitoringMetrics: String // e.g. LaTeX $PHQ-9$, $MoCA$, $CDR$, $BPRS$
)

data class ConsensusAnalysisResult(
    val caseSummary: String,
    val neuroanatomicalCorrelation: String, // Circuito/Vias anatômicas (fronto-estriatal, límbica, etc.)
    val neuroPerspective: NeuroPerspective,
    val psychPerspective: PsychPerspective,
    val therapeuticComparison: List<DrugOption>,
    val practicalAlgorithm: List<AlgorithmStep>,
    val clinicalPearls: List<String> // 2 a 3 pontos-chave estritamente práticos
)

data class PresetClinicalCase(
    val id: String,
    val title: String,
    val category: String, // "Cognitivo / Demencial", "Afetivo / Psicótico", "Motor / Extrapiramidal"
    val clinicalFragment: String,
    val neuroimagingInput: String,
    val currentTherapeuticScheme: String
)
