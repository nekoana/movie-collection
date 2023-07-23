use std::sync::Arc;

use axum::extract::State;
use axum::{
    extract::Path,
    response::IntoResponse,
    routing::{delete, get, post, put},
    Extension, Json, Router,
};

use shared::film_model::{CreateFilm, Film};

use crate::film_repository::FilmRepository;

// GET /v1/films: returns a list of films.
// GET /v1/films/{id}: returns a film by id.
// POST /v1/films: creates a new film.
// PUT /v1/films: updates a film.
// DELETE /v1/films/{id}: deletes a film by id.

async fn list_films<R: FilmRepository>(
    Extension(repo): Extension<Arc<State<R>>>,
) -> impl IntoResponse {
    Json(repo.list_films().await)
}

async fn query_film_by_id<R: FilmRepository>(
    repo: Extension<Arc<State<R>>>,
    id: Path<uuid::Uuid>,
) -> impl IntoResponse {
    Json(repo.query_film_by_id(&id).await)
}

async fn create_film<R: FilmRepository>(
    repo: Extension<Arc<State<R>>>,
    Json(create_file): Json<CreateFilm>,
) -> impl IntoResponse {
    Json(repo.create_film(&create_file).await)
}

async fn update_film<R: FilmRepository>(
    repo: Extension<Arc<State<R>>>,
    Json(film): Json<Film>,
) -> impl IntoResponse {
    Json(repo.update_film(&film).await)
}

async fn delete_film_by_id<R: FilmRepository>(
    repo: Extension<Arc<State<R>>>,
    id: Path<uuid::Uuid>,
) -> impl IntoResponse {
    Json(repo.delete_film_by_id(&id).await)
}

pub fn films_routers<R: FilmRepository + Send + Sync + 'static>(repo: State<R>) -> Router {
    Router::new()
        .route("/v1/films", get(list_films::<R>))
        .route("/v1/films/:id", get(query_film_by_id::<R>))
        .route("/v1/films", post(create_film::<R>))
        .route("/v1/films", put(update_film::<R>))
        .route("/v1/films/:id", delete(delete_film_by_id::<R>))
        .layer(Extension(Arc::new(repo)))
}
