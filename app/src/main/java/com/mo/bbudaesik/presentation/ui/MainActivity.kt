package com.mo.bbudaesik.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.mo.bbudaesik.presentation.navigation.BDSNavHost
import com.mo.bbudaesik.presentation.ui.theme.BbudaesikTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BbudaesikTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets(top = 0),
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    BDSNavHost(innerPadding)
                }
            }
        }
    }
}
