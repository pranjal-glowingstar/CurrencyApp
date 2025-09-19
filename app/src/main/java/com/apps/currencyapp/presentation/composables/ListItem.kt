package com.apps.currencyapp.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.apps.currencyapp.R
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.presentation.viewmodel.CurrencyInputType
import com.apps.currencyapp.presentation.viewmodel.CurrencySelectionModel

@Composable
fun ListItem(
    currencyInputType: CurrencyInputType,
    onValueChange: (String, CurrencyInputType) -> Unit,
    onCurrencySelected: (String, CurrencyInputType) -> Unit,
    currencySelectionModel: CurrencySelectionModel,
    currencies: List<CurrencyEntity>
) {

    val onTextChange: (String) -> Unit = remember {
        {
            onValueChange(it, currencyInputType)
        }
    }

    val onCurrencySelected: (String) -> Unit = remember {
        {
            onCurrencySelected(it, currencyInputType)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Text(
            text = if (currencyInputType == CurrencyInputType.CURRENCY_1) stringResource(R.string.amount1) else stringResource(R.string.amount2),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onCurrencySelected(currencySelectionModel.currencyName, currencyInputType) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currencySelectionModel.currencyName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                DropDown(currencies, onCurrencySelected)
            }

            Spacer(modifier = Modifier.width(16.dp))

            TextField(
                value = currencySelectionModel.currentAmount,
                onValueChange = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    cursorColor = Color.Blue,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedPlaceholderColor = Color.Gray.copy(alpha = 0.5f),
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}