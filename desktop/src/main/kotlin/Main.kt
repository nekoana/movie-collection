import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import component.CreateFilmDialog
import component.FilmCard
import component.ModifyFilmDialog
import di.httpClientModule
import di.repositoryModule
import di.viewModule
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import viewmodel.ICreateState
import viewmodel.IModifyState
import viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val viewModel by inject<MainViewModel>(MainViewModel::class.java)

    val films = viewModel.films.collectAsState()
    val modifyState = viewModel.modifyState.collectAsState()
    val createState = viewModel.createState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFilms()
    }

    val scope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            floatingActionButton = {
                Column(
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                viewModel.updateCreateFilmState(ICreateState.Create)
                            }
                        }
                    ){
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "add",
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                viewModel.loadFilms()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = "refresh",
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                items(
                    films.value.size,
                    key = { index -> films.value[index].id }) { index ->
                    FilmCard(
                        modifier = Modifier
                            .defaultMinSize(180.dp, 280.dp)
                            .requiredWidth(180.dp),
                        film = films.value[index],
                        onDelete = {
                            scope.launch {
                                viewModel.deleteFilm(it)
                            }
                        },
                        onModify = {
                            scope.launch {
                                viewModel.updateModifyFilmState(IModifyState.Modify(it))
                            }
                        }

                    )
                }
            }

            val filmState = modifyState.value
            if (filmState is IModifyState.Modify) {
                ModifyFilmDialog(
                    film = filmState.film,
                    onDismissRequest = {
                        scope.launch {
                            viewModel.updateModifyFilmState(IModifyState.None)
                        }
                    },
                    modifier = Modifier.size(480.dp, 400.dp),
                    onCancel = {
                        scope.launch {
                            viewModel.updateModifyFilmState(IModifyState.None)
                        }
                    },
                    onModify = {
                        scope.launch {
                            viewModel.updateFilm(it)
                            viewModel.updateModifyFilmState(IModifyState.None)
                        }
                    }
                )
            }

            val createState = createState.value
            if(createState is ICreateState.Create) {
                CreateFilmDialog(
                    modifier = Modifier.size(480.dp, 400.dp),
                    onDismissRequest = {
                        scope.launch {
                            viewModel.updateCreateFilmState(ICreateState.None)
                        }
                    },
                    onCancel = {
                        scope.launch {
                            viewModel.updateCreateFilmState(ICreateState.None)
                        }
                    },
                    onCreate = {
                        scope.launch {
                            viewModel.createFilm(it)
                            viewModel.updateCreateFilmState(ICreateState.None)
                            viewModel.loadFilms()
                        }
                    })
            }
        }
    }
}

fun main() = application {

    startKoin {
        modules(viewModule + httpClientModule + repositoryModule)
    }


    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
