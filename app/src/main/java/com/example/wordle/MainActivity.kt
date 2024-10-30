package com.example.wordle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.wordle.navigation.BottomBar
import com.example.wordle.navigation.NavHostComposable
import com.example.wordle.security.BiometricAuthManager
import com.example.wordle.ui.theme.WordleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var manager: BiometricAuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(manager)
        }

    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainScreen (biometricAuthManager: BiometricAuthManager) {
    var isAuthenticated by remember {
        mutableStateOf(false)
    }
    if (isAuthenticated) {
        // Show main content if authenticated
        val navController = rememberNavController()
        WordleTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    bottomBar = {
                        BottomBar { navController.navigate(it) }
                    }
                ) { innerPadding ->
                    NavHostComposable(innerPadding, navController)
                }
            }
        }
    } else {
        // Attempt biometric authentication
        BiometricLogin(
            biometricAuthManager = biometricAuthManager,
            onAuthSuccess = { isAuthenticated = true },
            onAuthError = { isAuthenticated = false }
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BiometricLogin(
    biometricAuthManager: BiometricAuthManager,
    onAuthSuccess: () -> Unit,
    onAuthError: () -> Unit
) {
    val context = LocalContext.current
    var _isAuthenticated = false
    val biometricManager = remember { BiometricManager.from(context) }
    val isBiometricAvailable = remember {
        biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
    }
    when (isBiometricAvailable) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometric features are available
            if(!_isAuthenticated) {
                biometricAuthManager.authenticate(
                    context,
                    onError = {
                        _isAuthenticated = false
                        Toast.makeText(
                            context,
                            context.getString(R.string.biometric_auth_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onSuccess = {
                        _isAuthenticated = true
                        onAuthSuccess()
                    },
                    onFail = {
                        _isAuthenticated = false
                        Toast.makeText(
                            context,
                            context.getString(R.string.biometric_auth_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Text(text = stringResource(id = R.string.biometric_no_hardware))
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Text(text = stringResource(id = R.string.biometric_unavailable))
        }
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            Text(text = stringResource(id = R.string.biometric_security_update_required))
        }
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            Text(text = stringResource(id = R.string.biometric_unsupported))
        }
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
            Text(text = stringResource(id = R.string.biometric_unknown_status))
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Text(text = stringResource(id = R.string.biometric_not_enrolled))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WordleTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
             //WordleScreen()
           // ProfileScreen()
        }
    }
}
