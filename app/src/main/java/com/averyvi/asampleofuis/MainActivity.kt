package com.averyvi.bilbo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.averyvi.bilbo.appuis.AppUI
import com.averyvi.bilbo.notuis.InstrumentStyling
import com.averyvi.bilbo.appuis.MainScaffold
import com.averyvi.bilbo.appuis.InstrumentSelectScreen
import com.averyvi.bilbo.ui.theme.BilboTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilboTheme {
                AppUI()
            }
        }
    }
}