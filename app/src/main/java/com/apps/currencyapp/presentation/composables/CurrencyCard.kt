package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.utils.AppConstants

@Composable
fun CurrencyCard(item: List<CurrencyEntity>, selectedCurrency: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item.forEach { iterator ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .weight(1f)
                    .background(color = if(selectedCurrency == iterator.currencyName) Color(0xFF66BB6A) else Color(0xFFF44336))
                    .border(width = 2.dp, color = Color.Black)
                    .padding(all = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${iterator.countryName} [${iterator.currencyName}]", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = Color.Black)
                Text(text = iterator.amount.toString(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = Color.Black)
            }
        }
        val placeholdersToAdd = AppConstants.CHUNCKED_CURRENCIES_LENGTH - item.size
        repeat(placeholdersToAdd) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(75.dp)
                    .background(color = Color.Red)
                    .border(width = 2.dp, color = Color.Black)
                    .padding(all = 8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.padding(top = 6.dp))
}