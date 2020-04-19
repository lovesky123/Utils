package com.example.utils

import androidx.core.hardware.fingerprint.FingerprintManagerCompat

interface OnCallBackListener {
    abstract fun onSupportFailed()

    abstract fun onInsecurity()

    abstract fun onEnrollFailed()

    abstract fun onAuthenticationStart()

    abstract fun onAuthenticationError(errMsgId: Int, errString: CharSequence)

    abstract fun onAuthenticationFailed()

    abstract fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence)

    abstract fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult)
}