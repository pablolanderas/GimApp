package com.example.gimapp.ui.viewModels.managers

import androidx.navigation.NavHostController
import com.example.gimapp.ui.views.GimScreens

object NavigateManager {

    private lateinit var navControler:  NavHostController

    fun setNavigate(nav: NavHostController) {
        navControler = nav
    }

    fun navigateTo(screen: GimScreens) {
        navControler.navigate(screen.name)
            ?: throw Exception("No se ha inicializado la navegacion")
    }

    fun navigateCleaningNavigation(screen: GimScreens) {
        navControler.popBackStack(route = screen.name, inclusive = true)
        navControler.navigate(screen.name)
    }

    fun isLastScreen(screen: GimScreens): Boolean {
        return navControler.previousBackStackEntry?.destination?.route == screen.name
    }

    fun popLastScreen() {
        navControler.popBackStack()
    }

}