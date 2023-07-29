package di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import repository.FilmRepository
import repository.IFilmRepository
import viewmodel.MainViewModel

const val BASE_URL = "http://127.0.0.1:8000"


val viewModule = module {
    single {
        MainViewModel(get())
    }
}


val httpClientModule = module {
    single {
        HttpClient(CIO) {
            defaultRequest {
                url(BASE_URL)
            }
            // Configure HTTP Client here
            install(Logging)
            //install json
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }
}

val repositoryModule = module {
    single<IFilmRepository> { FilmRepository(get()) }
}

