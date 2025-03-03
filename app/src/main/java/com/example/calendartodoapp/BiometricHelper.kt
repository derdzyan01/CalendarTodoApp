package com.example.calendartodoapp

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class BiometricHelper(private val context: Context) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    fun authenticate(
        activity: FragmentActivity, // Need a FragmentActivity for BiometricPrompt
        onSuccess: () -> Unit,
        onFailure: (errorCode: Int, errString: CharSequence) -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        executor = ContextCompat.getMainExecutor(context)

        biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Biometric authentication failed (e.g., too many attempts, no match).
                    // You might not want to show a detailed error to the user for security.
                    onFailure(BiometricPrompt.ERROR_NEGATIVE_BUTTON, "Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock App")
            .setSubtitle("Use your fingerprint to unlock the app")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) //Allow use fingerprint or device credentials
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun canAuthenticate(): Int{
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }
}