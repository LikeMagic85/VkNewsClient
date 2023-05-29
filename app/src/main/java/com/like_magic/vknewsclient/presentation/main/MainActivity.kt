package com.like_magic.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.like_magic.vknewsclient.ui.theme.VkNewsClientTheme
import com.like_magic.vknewsclient.presentation.viewmodels.MainViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)
                val launcher =
                    rememberLauncherForActivityResult(contract = VK.getVKAuthActivityResultContract()) {
                        viewModel.performAuthResult(it)
                    }
                when(authState.value){
                    is AuthState.NotAuthorized ->{
                        LoginScreen {
                            launcher.launch(listOf(VKScope.WALL))
                        }
                    }
                    AuthState.Authorized -> {
                        MainScreen()
                    }
                    AuthState.Initial -> {}
                }
            }
        }
    }
}
