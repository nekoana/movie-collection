package component

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import modal.Film


data class FilmModifyState(
    val id: String,
    var title: MutableState<String>,
    var year: MutableState<Int>,
    var director: MutableState<String>,
    var poster: MutableState<String>,
    var createdAt: MutableState<String>,
    var updatedAt: MutableState<String?>,
)

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModifyFilmDialog(
    film: Film,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onModify: (Film) -> Unit,
) {

    val filmModifyState = FilmModifyState(
        id = film.id,
        title = remember { mutableStateOf(film.title) },
        year = remember { mutableStateOf(film.year) },
        director = remember { mutableStateOf(film.director) },
        poster = remember { mutableStateOf(film.poster) },
        createdAt = remember { mutableStateOf(film.createdAt) },
        updatedAt = remember { mutableStateOf(film.updatedAt) },
    )

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {

        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(
                    text = "Modify Film",
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    modifier = Modifier.fillMaxWidth().weight(1.0F),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Text(
                            text = "Title: ",
                            modifier = Modifier.width(80.dp)
                        )
                        TextField(
                            modifier = Modifier.weight(1.0F),
                            value = filmModifyState.title.value,
                            onValueChange = {
                                filmModifyState.title.value = it
                            }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Director: ",
                            modifier = Modifier.width(80.dp)
                        )
                        TextField(
                            value = filmModifyState.director.value,
                            onValueChange = {
                                filmModifyState.director.value = it
                            },
                            singleLine = true,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Year: ",
                            modifier = Modifier.width(80.dp)
                        )
                        TextField(
                            value = filmModifyState.year.value.toString(),
                            onValueChange = {
                                filmModifyState.year.value = it.toInt()
                            },
                            singleLine = true,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Poster: ",
                            modifier = Modifier.width(80.dp)
                        )
                        TextField(
                            value = filmModifyState.poster.value,
                            onValueChange = {
                                filmModifyState.poster.value = it
                            },
                            singleLine = true,
                            shape = TextFieldDefaults.outlinedShape,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodySmall) {
                            Text(
                                text = "Created At: ",
                                modifier = Modifier.width(80.dp),
                            )
                            TextField(
                                value = filmModifyState.createdAt.value,
                                onValueChange = {
                                    filmModifyState.createdAt.value = it
                                },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1.0F)
                            )

                            Text(
                                text = "Updated At: ",
                                modifier = Modifier.width(80.dp),
                            )
                            TextField(
                                value = filmModifyState.updatedAt.value ?: "",
                                onValueChange = {
                                    filmModifyState.updatedAt.value = it
                                },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1.0F)
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onCancel,
                    ) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onModify(
                                film.copy(
                                    title = filmModifyState.title.value,
                                    director = filmModifyState.director.value,
                                    year = filmModifyState.year.value,
                                    poster = filmModifyState.poster.value,
                                )
                            )
                        },
                    ) {
                        Text(text = "Modify")
                    }
                }
            }


        }
    )
}