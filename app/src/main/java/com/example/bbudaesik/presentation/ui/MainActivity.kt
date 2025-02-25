    package com.example.bbudaesik.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.bbudaesik.presentation.navigation.BDSNavHost
import com.example.bbudaesik.presentation.ui.componenets.BDSTopAppBar
import com.example.bbudaesik.presentation.ui.theme.BbudaesikTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BbudaesikTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { BDSTopAppBar() }
                ) { innerPadding ->
                    BDSNavHost(innerPadding)
                }
            }
        }
    }
}