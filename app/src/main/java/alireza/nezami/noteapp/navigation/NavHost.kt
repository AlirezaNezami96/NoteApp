package alireza.nezami.noteapp.navigation

import alireza.nezami.home.navigation.homeNavigationRoute
import alireza.nezami.home.navigation.homeScreen
import alireza.nezami.note.navigation.editNoteScreen
import alireza.nezami.note.navigation.navigateToEditNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

/**
 * Top-level navigation graph.
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AppNavHost(
        appState: AppState,
        modifier: Modifier = Modifier,
        startDestination: String = homeNavigationRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onNoteClick = navController::navigateToEditNote
        )

        editNoteScreen(
            navigateUp = navController::popBackStack
        )


    }
}

