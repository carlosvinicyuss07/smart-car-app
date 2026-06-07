package com.glc.smartcar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.glc.smartcar.ui.auth.AuthScreen
import com.glc.smartcar.ui.history.HistoryScreen
import com.glc.smartcar.ui.theme.SmartCarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCarTheme {
                AuthScreen()
            }
        }
    }
}