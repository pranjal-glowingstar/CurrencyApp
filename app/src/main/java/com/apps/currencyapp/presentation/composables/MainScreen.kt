package com.apps.currencyapp.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.currencyapp.presentation.viewmodel.CurrencyInputType
import com.apps.currencyapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MainScreen(viewModel: MainViewModel) {

    val currency1 by viewModel.currency1.collectAsStateWithLifecycle()
    val currency2 by viewModel.currency2.collectAsStateWithLifecycle()
    val currencies by viewModel.currencies.collectAsStateWithLifecycle()
    var isRefreshing by remember { mutableStateOf(false) }

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
            viewModel.onDisclaimerClicked()
        }
    }
    val onLicenseClicked = remember(viewModel) {
        {
            viewModel.onLicenseClicked()
        }
    }
    val onCurrencySwapped = remember {
        {
            viewModel.onCurrencySwapped()
        }
    }
    val onRefresh = remember {
        {
            isRefreshing = true
            viewModel.refreshRates()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchDefaultUSDExchangeRate()
    }
    LaunchedEffect(currencies) {
        isRefreshing = false
    }
    PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = Color(0xFFD5FFFF))
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainHeader()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                ) {
                    ListItem(
                        currencyInputType = CurrencyInputType.CURRENCY_1,
                        onValueChange = onValueChange,
                        onCurrencySelected = onCurrencySelected,
                        currencySelectionModel = currency1,
                        currencies = currencies
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Exchanger(onCurrencySwapped = onCurrencySwapped)
                    Spacer(modifier = Modifier.height(16.dp))
                    ListItem(
                        currencyInputType = CurrencyInputType.CURRENCY_2,
                        onValueChange = onValueChange,
                        onCurrencySelected = onCurrencySelected,
                        currencySelectionModel = currency2,
                        currencies = currencies
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            MainFooter(onLicenseClicked, onDisclaimerClicked, viewModel.getLastUpdatedTime())
        }
    }
}