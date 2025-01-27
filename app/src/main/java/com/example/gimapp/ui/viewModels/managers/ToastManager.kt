package com.example.gimapp.ui.viewModels.managers

import android.content.Context
import android.widget.Toast

object ToastManager {

    private var currentToast: Toast? = null

    fun showToast(message: String, context: Context, length: Int = Toast.LENGTH_SHORT) {
        // Cancela el Toast actual si existe
        currentToast?.cancel()

        // Crea y muestra el nuevo Toast
        currentToast = Toast.makeText(context, message, length).apply {
            show()
        }
    }

}