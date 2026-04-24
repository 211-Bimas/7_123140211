package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import org.example.project.components.BottomNav
import org.example.project.navigation.BottomNavItem
import org.example.project.navigation.Screen
import org.example.project.ui.*
import org.example.project.viewmodel.ProfileViewModel
import org.example.project.viewmodel.NewsViewModel

@Composable
fun App() {
    val navController = rememberNavController()

    // ViewModels
    val profileViewModel = remember { ProfileViewModel() }
    val newsViewModel = remember { NewsViewModel() }

    val uiState by profileViewModel.uiState.collectAsState()
    val colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            bottomBar = { BottomNav(navController = navController) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                // =============== BOTTOM NAV TABS ===============
                composable(BottomNavItem.Notes.route) {
                    NoteListScreen(
                        onNoteClick = { noteId -> navController.navigate(Screen.NoteDetail.createRoute(noteId)) },
                        onAddClick = { navController.navigate(Screen.AddNote.route) }
                    )
                }

                composable(BottomNavItem.Favorites.route) {
                    FavoritesScreen()
                }

                // TAB BARU: NEWS
                composable(BottomNavItem.News.route) {
                    NewsListScreen(
                        viewModel = newsViewModel,
                        onArticleClick = { title, body ->
                            navController.navigate(Screen.NewsDetail.createRoute(title, body))
                        }
                    )
                }

                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(
                        uiState = uiState,
                        onEditProfile = { newName, newBio -> profileViewModel.updateProfile(newName, newBio) },
                        onToggleDarkMode = { isDark -> profileViewModel.toggleDarkMode(isDark) }
                    )
                }

                // =============== SCREEN DETAIL ===============
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    NoteDetailScreen(
                        noteId = noteId,
                        onBack = { navController.popBackStack() },
                        onEdit = { id -> navController.navigate(Screen.EditNote.createRoute(id)) }
                    )
                }

                composable(Screen.AddNote.route) {
                    AddNoteScreen(onBack = { navController.popBackStack() })
                }

                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    EditNoteScreen(noteId = noteId, onBack = { navController.popBackStack() })
                }

                // SCREEN BARU: DETAIL NEWS
                composable(Screen.NewsDetail.route) { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title") ?: ""
                    val body = backStackEntry.arguments?.getString("body") ?: ""

                    NewsDetailScreen(
                        title = title,
                        body = body,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}