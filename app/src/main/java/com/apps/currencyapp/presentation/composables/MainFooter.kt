package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainFooter(onLicenseClicked: () -> Unit, onDisclaimerClicked: () -> Unit, lastTime: String){
    Text(text = "Last Updated at: $lastTime")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "License", modifier = Modifier.clickable { onLicenseClicked.invoke() })
        Text(text = "Disclaimer", modifier = Modifier.clickable { onDisclaimerClicked.invoke() })
    }
}