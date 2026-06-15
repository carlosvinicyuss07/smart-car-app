package com.glc.smartcar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.glc.smartcar.data.local.TokenManager
import com.glc.smartcar.ui.navigation.AppRoute
import com.glc.smartcar.ui.navigation.SmartCarNavHost
import com.glc.smartcar.ui.theme.SmartCarTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val startDestination = if (tokenManager.obterToken() != null) {
                AppRoute.MainGraph
            } else {
                AppRoute.AuthGraph
            }

            SmartCarTheme {
                SmartCarNavHost(startDestination = startDestination)
            }
        }
    }
}