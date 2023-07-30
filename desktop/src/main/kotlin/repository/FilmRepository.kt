package repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonBuilder
import modal.CreateFilm
import modal.Film


const val BASE_URL = "http://127.0.0.1:8000"

const val FILM_URL = "$BASE_URL/v1/films"

class FilmRepository(private val http: HttpClient) : IFilmRepository {
    override suspend fun getFilms(): List<Film> = withContext(Dispatchers.IO) {
        http.get(FILM_URL).body()
    }

    override suspend fun deleteFilms(film: Film): String =
        withContext(Dispatchers.IO) {
            http.delete("$FILM_URL/${film.id}").body()
        }

    override suspend fun updateFilm(film: Film): Film = withContext(Dispatchers.IO) {
        http.put(FILM_URL) {
            contentType(ContentType.Application.Json)
            setBody(film)
        }.body()
    }

    override suspend fun createFilm(createFilm: CreateFilm): Film = withContext(Dispatchers.IO) {
        http.post(FILM_URL) {
            contentType(ContentType.Application.Json)
            setBody(createFilm)
        }.body()
    }
}