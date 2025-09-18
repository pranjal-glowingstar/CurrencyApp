package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.apps.currencyapp.data.local.entity.CurrencyEntity

@Composable
fun DropDown(
    currencies: List<CurrencyEntity>,
    onCurrencySelected: (String) -> Unit
) {

    var isExpanded by remember { mutableStateOf(false) }
    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "", modifier = Modifier.clickable{ isExpanded = !isExpanded })
    DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = !isExpanded}) {
        currencies.forEach { item ->
            DropdownMenuItem(text = {
                Text(text = item.currencyName)
            }, onClick = { onCurrencySelected(item.currencyName); isExpanded = !isExpanded })
        }
    }
}