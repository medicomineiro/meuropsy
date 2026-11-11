package com.example.data

import com.example.model.PresetClinicalCase

object PresetCasesRepository {
    val presetCases = listOf(
        PresetClinicalCase(
            id = "case_ftd_bv",
            title = "Desinibição Precoce vs. Mania de Início Tardio",
            category = "Cognitivo / Demencial",
            clinicalFragment = "Paciente 58 anos, sexo masculino, previamente funcional como diretor financeiro. Há 18 meses com apatia inicial alternada com desinibição comportamental súbita, hiperoralidade (ingestão compulsiva de doces) e jocosidade inadequada no ambiente de trabalho. Sem histórico prévio de transtorno de humor.",
            neuroimagingInput = "Ressonância Magnética encefálica evidenciando atrofia proeminente fronto-insular bilateral com assimetria à esquerda. Ausência de leucoencefalopatia microangiopática significativa (Fazekas 0).",
            currentTherapeuticScheme = "Tentativa recente com Sertralina 100 mg/dia sem resposta comportamental e Quetiapina 50 mg/noite que gerou sonolência excessiva e instabilidade postural."
        ),
        PresetClinicalCase(
            id = "case_lewy_body",
            title = "Alucinações Visuais com Flutuação e Rigidez",
            category = "Motor / Extrapiramidal",
            clinicalFragment = "Mulher, 72 anos. Trazida por episódios recorrentes de alucinações visuais cênicas bem formadas (enxerga crianças e pequenos animais no quarto à noite, mantendo crítica parcial) e flutuação da atenção ao longo do dia com sonolência diurna excessiva. Relato de transtorno comportamental do sono REM há 4 anos.",
            neuroimagingInput = "TC de crânio com preservação relativa do volume hipocampal medial, sem atrofia lobar focal. Discreta gliose periventricular inespecífica.",
            currentTherapeuticScheme = "Iniciada Haloperidol 2.5 mg/dia pelo plantão de pronto-atendimento com piora dramática imediata da rigidez plástica em roda denteada e acinesia severa (hipersensibilidade a antipsicóticos típicos)."
        ),
        PresetClinicalCase(
            id = "case_vascular_depression",
            title = "Depressão Vascular e Retardo Psicomotor",
            category = "Afetivo / Psicótico",
            clinicalFragment = "Homem, 68 anos, hipertenso e diabético de longa data com controle irregular. Apresenta anedonia profunda, abulia, lentificação psicomotora e declínio das funções executivas há 10 meses. Refratário a dois esquemas sequenciais de ISRS (Escitalopram e Fluoxetina em doses otimizadas).",
            neuroimagingInput = "RM de crânio ponderada em T2/FLAIR demonstrando leucoencefalopatia isquêmica microangiopática confluente periventricular e subcortical difusa (Fazekas grau 3) com infartos lacunares silentes no tálamo e cabeça do caudado.",
            currentTherapeuticScheme = "Em uso de Venlafaxina 150 mg/dia, Losartana 50 mg/dia, Metformina 1700 mg/dia e AAS 100 mg/dia."
        ),
        PresetClinicalCase(
            id = "case_nmdar_encephalitis",
            title = "Psicose Aguda com Instabilidade Autonômica",
            category = "Afetivo / Psicótico",
            clinicalFragment = "Mulher, 24 anos. Quadro subagudo com início há 3 semanas caracterizado por agitação psicomotora, delírios paranoides bizarros, ecolalia, disquinesias orofaciais e episódios de taquicardia paroxística e hipertermia inexplicada. Família nega histórico psiquiátrico prévio.",
            neuroimagingInput = "RM encefálica 3T inicial sem alterações parenquimatosas grosseiras ou realce por gadolínio. EEG com lentificação delta-teta difusa com padrão sugestivo de 'extreme delta brush'.",
            currentTherapeuticScheme = "Olanzapina 15 mg/dia e Clonazepam 2 mg/dia com resposta refratária e agravamento das disautonomias."
        )
    )
}
