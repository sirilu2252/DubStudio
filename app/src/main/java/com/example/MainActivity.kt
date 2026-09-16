package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AppScreen
import com.example.ui.DubStudioViewModel
import com.example.ui.screens.PipelineProcessingScreen
import com.example.ui.screens.StudioEditorScreen
import com.example.ui.screens.UploadSetupScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: DubStudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    Crossfade(
                        targetState = uiState.currentScreen,
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.UPLOAD_SETUP -> {
                                UploadSetupScreen(
                                    state = uiState,
                                    onSelectSample = { viewModel.loadSampleVideo(it) },
                                    onVideoUploaded = { name, format, size, duration, uri ->
                                        viewModel.setUploadedVideo(name, format, size, duration, uri)
                                    },
                                    onSourceLanguageSelected = { viewModel.setSourceLanguage(it) },
                                    onTargetLanguageSelected = { viewModel.setTargetLanguage(it) },
                                    onToggleAutoDetect = { viewModel.toggleAutoDetect(it) },
                                    onLipSyncQualitySelected = { viewModel.setLipSyncQuality(it) },
                                    onVoiceProfileSaved = { viewModel.updateVoiceProfile(it) },
                                    onStartPipeline = { viewModel.startPipeline() }
                                )
                            }
                            AppScreen.PIPELINE_PROCESSING -> {
                                PipelineProcessingScreen(
                                    state = uiState,
                                    onCancel = { viewModel.returnToSetup() }
                                )
                            }
                            AppScreen.STUDIO_EDITOR -> {
                                StudioEditorScreen(
                                    state = uiState,
                                    onPlayPause = { viewModel.togglePlayPause() },
                                    onSeekTo = { viewModel.seekTo(it) },
                                    onPlaySegmentAudio = { viewModel.playSegmentAudio(it) },
                                    onUpdateScript = { id, text -> viewModel.updateSegmentScript(id, text) },
                                    onUpdateEmotion = { id, emo -> viewModel.updateSegmentEmotion(id, emo) },
                                    onUpdateStems = { viewModel.updateAudioStems(it) },
                                    onUpdateSpeaker = { viewModel.updateSpeaker(it) },
                                    onUpdateVoiceProfile = { viewModel.updateVoiceProfile(it) },
                                    onSetLipSyncQuality = { viewModel.setLipSyncQuality(it) },
                                    onReturnToSetup = { viewModel.returnToSetup() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

