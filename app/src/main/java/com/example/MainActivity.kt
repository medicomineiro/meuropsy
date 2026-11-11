package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AnalysisUiState
import com.example.ui.NeuroPsychViewModel
import com.example.ui.components.ClinicalInputScreen
import com.example.ui.theme.NeuroPsychTheme
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val viewModel: NeuroPsychViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration with launch intent before UI/Firebase
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            NeuroPsychTheme {
                val uiState by viewModel.uiState.collectAsState()

                // Intercept back button when displaying full consensus report to return to inputs
                BackHandler(enabled = uiState is AnalysisUiState.Success) {
                    viewModel.resetToInput()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ClinicalInputScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
