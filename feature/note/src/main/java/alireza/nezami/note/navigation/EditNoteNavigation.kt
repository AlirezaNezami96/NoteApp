package alireza.nezami.note.navigation

import alireza.nezami.common.helper.UriDecoder
import alireza.nezami.model.domain.Note
import alireza.nezami.note.EditNoteScreen
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal const val noteArg = "note"

internal class DetailArgs(val note: Note?) {
    companion object {
        fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): DetailArgs? {
            val noteJson = savedStateHandle.get<String>(noteArg) ?: return null
            val decodedJson = UriDecoder().decodeString(noteJson)
            val note = Json.decodeFromString<Note>(decodedJson)
            return DetailArgs(note)
        }
    }
}

fun NavController.navigateToEditNote(note: Note?) {
    val encodedNote = note?.let {
        val noteJson = Json.encodeToString(it)
        android.net.Uri.encode(noteJson)
    }
    this.navigate("edit_note_route/${encodedNote ?: "null"}") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.editNoteScreen(
        navigateUp: () -> Unit,
) {
    composable(
        route = "edit_note_route/{$noteArg}",
        arguments = listOf(
            navArgument(noteArg) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        EditNoteScreen(
            onBackPress = navigateUp
        )
    }
}