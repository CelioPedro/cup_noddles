package me.dio.copa.catar.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Início")
    object Rounds : NavigationItem("rounds", Icons.Default.List, "Rodadas")
    object Betting : NavigationItem("betting", Icons.Default.Star, "Bolão")
    object Profile : NavigationItem("profile", Icons.Default.Person, "Perfil")
}