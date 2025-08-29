package alireza.nezami.home.navigation

import alireza.nezami.home.presentation.HomeScreen
import alireza.nezami.model.domain.Note
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
        onNoteClick: (note: Note?) -> Unit,
) {
    composable(
        route = homeNavigationRoute,
    ) {
        HomeScreen(
            onNoteClick = onNoteClick,
        )
    }
}
