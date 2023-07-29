package viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import modal.Film
import repository.IFilmRepository

class MainViewModel(private val filmRepository: IFilmRepository) {

    private val _films = MutableStateFlow(emptyList<Film>())

    val films: StateFlow<List<Film>>
        get() = _films

    suspend fun loadFilms() {
        val films = filmRepository.getFilms()
        _films.emit(films)
    }
}