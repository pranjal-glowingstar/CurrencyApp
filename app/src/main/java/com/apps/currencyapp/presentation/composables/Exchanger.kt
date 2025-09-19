package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.CompareArrows
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Exchanger(onCurrencySwapped: () -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.align(Alignment.Center))
        Icon(
            imageVector = Icons.AutoMirrored.TwoTone.CompareArrows,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(24.dp)
                .clickable { onCurrencySwapped() }
        )
    }
}