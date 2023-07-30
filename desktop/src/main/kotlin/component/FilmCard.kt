package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import modal.Film
import java.net.URL


@Composable
fun FilmCard(
    film: Film,
    onDelete: (Film) -> Unit,
    onModify: (Film) -> Unit,
    modifier: Modifier = Modifier.size(180.dp, 280.dp)
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = film.title,
                style = MaterialTheme.typography.h5,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            AsyncImage(
                modifier = Modifier
                    .size(160.dp, 200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                load = { loadImageBitmap(film.poster) },
                default = rememberVectorPainter(Icons.Outlined.Info),
                painterFor = {BitmapPainter(it) },
                contentDescription = "Diamond SVG",
            )

            Text(
                text = film.director,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    onDelete(film)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "delete")
                }

                IconButton(onClick = {
                    onModify(film)
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "edit")
                }
            }
        }
    }
}


@Composable
fun <T> AsyncImage(
    load: suspend () -> T,
    default: Painter,
    painterFor: @Composable (T) -> Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {

    val image: T? by produceState<T?>(null) {
        value = withContext(Dispatchers.IO) {
            try {
                load()
            } catch (e: IOException) {
                // instead of printing to console, you can also write this to log,
                // or show some error placeholder
                e.printStackTrace()
                null
            }
        }
    }

    val sale by remember {
        derivedStateOf {
            if (image == null) ContentScale.Inside
            else ContentScale.Crop
        }
    }

    Image(
        painter = if (image != null) painterFor(image!!) else default,
        contentDescription = contentDescription, contentScale = sale,
        modifier = modifier
    )
}

/* Loading from file with java.io API */


/* Loading from network with java.net API */

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use(::loadImageBitmap)

/* Loading from network with Ktor client API (https://ktor.io/docs/client.html). */

/*

suspend fun loadImageBitmap(url: String): ImageBitmap =
    urlStream(url).use(::loadImageBitmap)

suspend fun loadSvgPainter(url: String, density: Density): Painter =
    urlStream(url).use { loadSvgPainter(it, density) }

suspend fun loadXmlImageVector(url: String, density: Density): ImageVector =
    urlStream(url).use { loadXmlImageVector(InputSource(it), density) }

@OptIn(KtorExperimentalAPI::class)
private suspend fun urlStream(url: String) = HttpClient(CIO).use {
    ByteArrayInputStream(it.get(url))
}

 */

@Preview
@Composable
fun PreviewFilmCard() {
    FilmCard(Film(
        "12345",
        "title",
        "director",
        123,
        "poster",
        "12/23",
        "genre"
    ),
        onDelete = {

        },
        onModify = {

        })
}