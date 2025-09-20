package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.utils.AppConstants

@Composable
fun DropDown(
    currencies: List<CurrencyEntity>,
    onCurrencySelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(AppConstants.EMPTY) }
    var filteredList by remember { mutableStateOf(listOf<CurrencyEntity>()) }
    val onTextChange: (String) -> Unit = remember {
        {
            searchText = it
        }
    }
    LaunchedEffect(searchText, currencies) {
        filteredList = if (searchText.isEmpty()) {
            currencies
        } else {
            currencies.filter { it.countryName.contains(searchText, true) }
        }
    }

    Row(
        modifier = Modifier
            .clickable { isExpanded = true }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "",
        )
    }

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        modifier = Modifier.heightIn(max = 200.dp).width(300.dp)
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onTextChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
            cursorBrush = SolidColor(Color.Blue),
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .height(36.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        filteredList.forEach { item ->
            DropdownMenuItem(
                text = {
                    Column {
                        Text(
                            text = item.currencyName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.countryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                onClick = {
                    onCurrencySelected(item.currencyName)
                    isExpanded = false
                }
            )
        }
    }
}