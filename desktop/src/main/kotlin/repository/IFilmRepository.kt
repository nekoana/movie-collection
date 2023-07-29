package repository

import modal.Film

interface IFilmRepository {
   suspend  fun getFilms(): List<Film>
}