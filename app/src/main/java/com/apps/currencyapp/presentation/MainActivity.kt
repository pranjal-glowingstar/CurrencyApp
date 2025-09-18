package com.apps.currencyapp.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.apps.currencyapp.presentation.composables.MainScreen
import com.apps.currencyapp.presentation.viewmodel.MainViewModel
import com.apps.currencyapp.ui.theme.CurrencyAppTheme
import com.apps.currencyapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.warmViewModelWithDefaults()
        enableEdgeToEdge()
        setContent {
            CurrencyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    MainScreen(viewModel)
                }
            }
        }
        addObservers()
    }
    private fun addObservers(){
        lifecycleScope.launch {
            launch{
                viewModel.disclaimer.collect {
                    if(it){
                        viewModel.onDisclaimerClicked(false)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.DISCLAIMER_URL))
                        startActivity(intent)
                    }
                }
            }
            launch{
                viewModel.license.collect {
                    if(it){
                        viewModel.onLicenseClicked(false)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.LICENSE_URL))
                        startActivity(intent)
                    }
                }
            }
        }
    }
}