package com.example.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.NeuroPsychAiEngine
import com.example.data.PresetCasesRepository
import com.example.model.ConsensusAnalysisResult
import com.example.model.PresetClinicalCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AnalysisUiState {
    data object Idle : AnalysisUiState
    data object Loading : AnalysisUiState
    data class Success(val result: ConsensusAnalysisResult) : AnalysisUiState
    data class Error(val message: String) : AnalysisUiState
}

class NeuroPsychViewModel : ViewModel() {

    private val engine by lazy { NeuroPsychAiEngine() }

    // Input state
    var clinicalNotes = MutableStateFlow("")
        private set
    var neuroimagingFindings = MutableStateFlow("")
        private set
    var currentTherapeuticScheme = MutableStateFlow("")
        private set
    var attachedImage = MutableStateFlow<Bitmap?>(null)
        private set

    private val _uiState = MutableStateFlow<AnalysisUiState>(AnalysisUiState.Idle)
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    // History of past consensus analyses during the session
    private val _savedConsensusHistory = MutableStateFlow<List<ConsensusAnalysisResult>>(emptyList())
    val savedConsensusHistory: StateFlow<List<ConsensusAnalysisResult>> = _savedConsensusHistory.asStateFlow()

    fun updateClinicalNotes(text: String) {
        clinicalNotes.value = text
    }

    fun updateNeuroimagingFindings(text: String) {
        neuroimagingFindings.value = text
    }

    fun updateTherapeuticScheme(text: String) {
        currentTherapeuticScheme.value = text
    }

    fun setAttachedImage(bitmap: Bitmap?) {
        attachedImage.value = bitmap
    }

    fun loadPresetCase(preset: PresetClinicalCase) {
        clinicalNotes.value = preset.clinicalFragment
        neuroimagingFindings.value = preset.neuroimagingInput
        currentTherapeuticScheme.value = preset.currentTherapeuticScheme
    }

    fun clearInputs() {
        clinicalNotes.value = ""
        neuroimagingFindings.value = ""
        currentTherapeuticScheme.value = ""
        attachedImage.value = null
        _uiState.value = AnalysisUiState.Idle
    }

    fun runAnalysis() {
        val notes = clinicalNotes.value.trim()
        val imaging = neuroimagingFindings.value.trim()
        val scheme = currentTherapeuticScheme.value.trim()
        val bitmap = attachedImage.value

        if (notes.isEmpty() && imaging.isEmpty() && scheme.isEmpty() && bitmap == null) {
            _uiState.value = AnalysisUiState.Error("Insira ao menos um fragmento clínico, corte/achado de neuroimagem ou esquema terapêutico.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AnalysisUiState.Loading
            try {
                val result = engine.analyzeCase(
                    clinicalNotes = notes,
                    neuroimagingFindings = imaging,
                    therapeuticScheme = scheme,
                    inputBitmap = bitmap
                )
                _uiState.value = AnalysisUiState.Success(result)
                _savedConsensusHistory.value = listOf(result) + _savedConsensusHistory.value
            } catch (e: TimeoutCancellationException) {
                _uiState.value = AnalysisUiState.Error("A análise clínica excedeu a janela de tempo de 60 segundos. Tente refinar a consulta.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: "Falha ao processar consenso neuropsiquiátrico."
                _uiState.value = AnalysisUiState.Error(errorMsg)
            }
        }
    }

    fun showAnalysis(result: ConsensusAnalysisResult) {
        _uiState.value = AnalysisUiState.Success(result)
    }

    fun resetToInput() {
        _uiState.value = AnalysisUiState.Idle
    }
}
