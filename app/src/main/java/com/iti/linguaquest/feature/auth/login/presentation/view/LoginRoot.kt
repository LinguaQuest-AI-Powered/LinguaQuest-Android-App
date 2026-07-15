package com.iti.linguaquest.feature.auth.login.presentation.view

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iti.linguaquest.feature.auth.login.presentation.viewmodel.LoginViewModel


@Composable
fun AddressRoot(
    onBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {

    LoginScreen()
}