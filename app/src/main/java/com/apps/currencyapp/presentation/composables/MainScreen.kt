package com.apps.currencyapp.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.currencyapp.data.local.entity.CurrencyEntity
import com.apps.currencyapp.presentation.viewmodel.CurrencyInputType
import com.apps.currencyapp.presentation.viewmodel.CurrencySelectionModel
import com.apps.currencyapp.presentation.viewmodel.MainViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(viewModel: MainViewModel) {

    val currency1 by viewModel.currency1.collectAsStateWithLifecycle()
    val currency2 by viewModel.currency2.collectAsStateWithLifecycle()
    val currencies by viewModel.currencies.collectAsStateWithLifecycle()

    val onValueChange: (String, CurrencyInputType) -> Unit = remember(viewModel) {
        { amount, type ->
            viewModel.updateAmount(amount, type)
        }
    }
    val onCurrencySelected: (String, CurrencyInputType) -> Unit = remember {
        { entity, inputType ->
            viewModel.onCurrencySelected(entity, inputType)
        }
    }
    val onDisclaimerClicked = remember(viewModel) {
        {
            viewModel.onDisclaimerClicked(true)
        }
    }
    val onLicenseClicked = remember(viewModel) {
        {
            viewModel.onLicenseClicked(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDefaultUSDExchangeRate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD5FFFF))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainHeader()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            colors = CardDefaults.cardColors().copy(containerColor = Color.White)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)) {
                ListItem(CurrencyInputType.CURRENCY_1, onValueChange, onCurrencySelected, currency1, currencies)
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Exchanger()
                ListItem(CurrencyInputType.CURRENCY_2, onValueChange, onCurrencySelected, currency2, currencies)
            }
        }
        MainFooter(onLicenseClicked, onDisclaimerClicked, viewModel.getLastUpdatedTime())
    }
}