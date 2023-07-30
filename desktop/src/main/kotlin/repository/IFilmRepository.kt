package repository

import modal.CreateFilm
import modal.Film

interface IFilmRepository {
   suspend  fun getFilms(): List<Film>
   suspend fun deleteFilms(film: Film): String

   suspend fun updateFilm(film: Film): Film

   suspend fun createFilm(createFilm: CreateFilm):Film
}