package repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modal.Film


const val BASE_URL = "http://127.0.0.1:8000"

const val FILM_URL = "$BASE_URL/v1/films"

class FilmRepository(private val http: HttpClient) : IFilmRepository {
    override suspend fun getFilms(): List<Film> = withContext(Dispatchers.IO) {
        http.get(FILM_URL).body()
    }
}