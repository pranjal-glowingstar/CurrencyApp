package com.apps.currencyapp.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.currencyapp.R
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.presentation.viewmodel.MainViewModel
import com.apps.currencyapp.utils.AppConstants

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currencies by viewModel.currencies.collectAsStateWithLifecycle()
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val selectedCurrency by viewModel.selectedCurrency.collectAsStateWithLifecycle()
    val shouldShowLoader by viewModel.shouldShowLoader.collectAsStateWithLifecycle()
    val chunkedCurrencies by remember { derivedStateOf { currencies.chunked(AppConstants.CHUNCKED_CURRENCIES_LENGTH) } }
    var isDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val onValueChange: (String) -> Unit = remember(viewModel) {
        {
            viewModel.updateAmount(it)
        }
    }
    val onCurrencySelected: (CurrencyEntity) -> Unit = remember(viewModel) {
        {
            viewModel.onCurrencySelected(it.currencyName)
            isDropdownExpanded = false
        }
    }
    val onDisclaimerClicked = remember(viewModel) { {
        viewModel.onDisclaimerClicked(true)
    } }
    val onLicenseClicked = remember(viewModel) { {
        viewModel.onLicenseClicked(true)
    } }

    LaunchedEffect(Unit) {
        viewModel.fetchDefaultUSDExchangeRate()
    }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = amount,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(text = "${context.getString(R.string.amount)} [$selectedCurrency]")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = context.getString(R.string.selected_country),
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Black)
                    .padding(all = 8.dp)
                    .clickable { isDropdownExpanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCurrency,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "",
                    tint = Color.Black
                )
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 3),
                    scrollState = rememberScrollState()
                ) {
                    currencies.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(text = item.countryName) },
                            onClick = { onCurrencySelected(item) },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        if (index < currencies.lastIndex) {
                            HorizontalDivider(thickness = 1.dp, color = Color.White)
                        }
                    }
                }
            }
        }
        if (shouldShowLoader) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Green, modifier = Modifier.size(36.dp))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()){
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(bottom = 36.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(
                        chunkedCurrencies,
                        key = { _, item -> item[0].currencyName+item[0].countryName }) { _, item ->
                        CurrencyCard(item, selectedCurrency)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().height(24.dp).align(Alignment.BottomCenter), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(text = context.getString(R.string.disclaimer), modifier = Modifier.clickable { onDisclaimerClicked() }, color = Color.Black)
                    Text(text = context.getString(R.string.license), modifier = Modifier.clickable { onLicenseClicked() }, color = Color.Black)
                }
            }
        }
    }
}