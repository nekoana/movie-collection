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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import component.FilmCard
import di.httpClientModule
import di.repositoryModule
import di.viewModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import viewmodel.MainViewModel
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val viewModel by inject<MainViewModel>(MainViewModel::class.java)

    val state = viewModel.films.collectAsState()

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
                contentPadding = PaddingValues(20.dp),
            ) {
                items(
                    state.value.size,
                    key = { index -> state.value[index].id }) { index ->
                    FilmCard(
                        modifier = Modifier
                            .defaultMinSize(180.dp, 280.dp)
                            .requiredWidth(180.dp),
                        film = state.value[index]

                    )
                }
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
