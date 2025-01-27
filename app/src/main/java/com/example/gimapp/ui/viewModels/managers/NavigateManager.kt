package com.example.gimapp.ui.viewModels.managers

import com.example.gimapp.ui.views.GimScreens

object NavigateManager {

    private var navigateObjetc: ((String) -> Unit)? = null

    fun setNavigate(f: (String) -> Unit) {
        navigateObjetc = f
    }

    fun navigateTo(screen: GimScreens) {
        navigateObjetc?.invoke(screen.name)
            ?: throw Exception("No se ha inicializado la navegacion")
    }

}