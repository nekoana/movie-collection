package component

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import modal.CreateFilm
import modal.Film


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateFilmDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onCreate: (CreateFilm) -> Unit,
) {
    var createFilm by remember {
        mutableStateOf(
            CreateFilm(
                title = "",
                director = "",
                year = 0,
                poster = "",
            )
        )
    }

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
                            value = createFilm.title,
                            onValueChange = {
                                createFilm = createFilm.copy(title = it)
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
                            value = createFilm.director,
                            onValueChange = {
                                createFilm = createFilm.copy(director = it)
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
                            value =  createFilm.year.toString(),
                            onValueChange = {
                              it.toIntOrNull()?.let {
                                    createFilm = createFilm.copy(year = it)
                                }
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
                            value =  createFilm.poster,
                            onValueChange = {
                                createFilm = createFilm.copy(poster = it)
                            },
                            singleLine = true,
                            shape = TextFieldDefaults.outlinedShape,
                        )
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
                            onCreate(createFilm)
                        },
                    ) {
                        Text(text = "Create")
                    }
                }
            }
        }
    )
}