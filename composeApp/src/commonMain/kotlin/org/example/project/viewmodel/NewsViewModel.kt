package org.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.data.NewsRepository
import org.example.project.data.NewsUiState

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    var uiState by mutableStateOf<NewsUiState>(NewsUiState.Loading)
        private set

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            uiState = NewsUiState.Loading
            try {
                val data = repository.getArticles()
                uiState = NewsUiState.Success(data)
            } catch (e: Exception) {
                uiState = NewsUiState.Error("Gagal mengambil data: ${e.message}")
            }
        }
    }
}