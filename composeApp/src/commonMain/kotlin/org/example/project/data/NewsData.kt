package org.example.project.data

import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// 1. Model Data Berita
@Serializable
data class NewsArticle(
    val id: Int,
    val title: String,
    val body: String
)

// 2. UI State (Syarat tugas: Loading, Success, Error)
sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<NewsArticle>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

// 3. Repository (Syarat tugas: Repository Pattern & API Call)
class NewsRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getArticles(): List<NewsArticle> =
        client.get("https://jsonplaceholder.typicode.com/posts").body()
}