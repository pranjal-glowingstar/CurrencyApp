package com.apps.currencyapp.presentation.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainHeader() {
    Text(
        text = "Currency Converter",
        style = MaterialTheme.typography.headlineMedium
    )
    Text(
        text = "Check live rates now",
        style = MaterialTheme.typography.bodyLarge
    )
}