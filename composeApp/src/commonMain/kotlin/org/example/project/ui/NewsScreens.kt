package org.example.project.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.data.NewsUiState
import org.example.project.viewmodel.NewsViewModel

@Composable
fun NewsListScreen(viewModel: NewsViewModel, onArticleClick: (String, String) -> Unit) {
    val state = viewModel.uiState

    Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 16.dp)) {
        // BAGIAN ATAS (Judul & Tombol) -> Padding top dihapus biar posisinya lebih naik dan pas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("News Reader", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            IconButton(onClick = { viewModel.fetchNews() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BAGIAN BAWAH (Isi Layar)
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is NewsUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sedang mengambil berita...")
                    }
                }
                is NewsUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Gagal Memuat 😭", color = Color.Red, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        // Teks error ini akan ngasih tau kita kalau ada yang salah sama Ktor/Internet
                        Text(state.message, color = Color.Red, textAlign = TextAlign.Center)
                    }
                }
                is NewsUiState.Success -> {
                    if (state.articles.isEmpty()) {
                        Text("Berita kosong/tidak ada data.")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.articles) { article ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable { onArticleClick(article.title, article.body) },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(article.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(article.body, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// LAYAR DETAIL BERITA
@Composable
fun NewsDetailScreen(title: String, body: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)) {
        Button(onClick = onBack, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Kembali")
        }
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(body, style = MaterialTheme.typography.bodyLarge)
    }
}