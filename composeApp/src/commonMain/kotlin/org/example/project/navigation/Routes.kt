package org.example.project.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
    // Rute Baru Untuk Detail Berita
    object NewsDetail : Screen("news_detail/{title}/{body}") {
        fun createRoute(title: String, body: String) = "news_detail/$title/$body"
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Notes : BottomNavItem("notes", Icons.Default.Home, "Notes")
    object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
    // Tab Baru Untuk Berita
    object News : BottomNavItem("news", Icons.Default.List, "News")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}