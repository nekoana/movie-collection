package viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import modal.CreateFilm
import modal.Film
import repository.IFilmRepository


sealed interface IModifyState {
    object None : IModifyState
    class Modify(val film: Film) : IModifyState
}


sealed interface ICreateState {
    object None : ICreateState
    object Create : ICreateState
}

class MainViewModel(private val filmRepository: IFilmRepository) {

    private val _films = MutableStateFlow(emptyList<Film>())
    private val _modifyState = MutableStateFlow<IModifyState>(IModifyState.None)
    private val _createState = MutableStateFlow<ICreateState>(ICreateState.None)

    val films: StateFlow<List<Film>>
        get() = _films

    val modifyState: StateFlow<IModifyState>
        get() = _modifyState

    val createState: StateFlow<ICreateState>
        get() = _createState

    suspend fun loadFilms() {
        val films = filmRepository.getFilms()
        _films.emit(films)
    }

    suspend fun deleteFilm(film: Film) {
        runCatching {
            val id = filmRepository.deleteFilms(film)
            println(id)
        }.onFailure {
            it.printStackTrace()
        }.onSuccess {
            loadFilms()
        }
    }

    suspend fun updateModifyFilmState(state: IModifyState) {
        _modifyState.emit(state)
    }

    suspend fun updateFilm(film: Film) {
        runCatching {
            val film = filmRepository.updateFilm(film)
            println(film)
        }.onFailure {
            it.printStackTrace()
        }.onSuccess {
            loadFilms()
        }
    }

    suspend fun createFilm(createFilm: CreateFilm) {
        runCatching {
            val film = filmRepository.createFilm(createFilm)
            println(film)
        }.onFailure {
            it.printStackTrace()
        }.onSuccess {
            loadFilms()
        }
    }

    suspend fun updateCreateFilmState(state: ICreateState) {
        _createState.emit(state)
    }
}