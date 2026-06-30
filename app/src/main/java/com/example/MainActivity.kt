package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.data.local.AppDatabase
import com.example.data.network.TikWmApiService
import com.example.data.repository.TikTokRepository
import com.example.ui.TikTokViewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val apiService by lazy { TikWmApiService.create() }
    private val database by lazy { AppDatabase.getDatabase(applicationContext) }
    private val repository by lazy { TikTokRepository(apiService, database.downloadHistoryDao()) }

    private val viewModel: TikTokViewModel by viewModels {
        TikTokViewModel.Factory(application, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle text shared from another app (e.g. TikTok share sheet)
        val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding),
                        initialSharedText = sharedText
                    )
                }
            }
        }
    }
}
