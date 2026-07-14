package com.iti.linguaquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iti.linguaquest.core.navigation.AppNavigation
import com.iti.linguaquest.ui.theme.LinguaQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinguaQuestTheme {
                AppNavigation()
            }
        }
    }
}
