use axum::{
    extract::Path,
    response::IntoResponse,
    routing::{delete, get, post, put},
    Json, Router,
};
use hyper::StatusCode;
use tracing::info;

// GET /v1/films: returns a list of films.
// GET /v1/films/{id}: returns a film by id.
// POST /v1/films: creates a new film.
// PUT /v1/films: updates a film.
// DELETE /v1/films/{id}: deletes a film by id.

async fn list_films() -> impl IntoResponse {
    StatusCode::OK
}

async fn query_film_by_id(_id: Path<usize>) -> impl IntoResponse {
    StatusCode::OK
}

async fn create_film(json: Json<serde_json::Value>) -> impl IntoResponse {
    info!("Creating film: {:?}", json);
    StatusCode::OK
}

async fn update_film() -> impl IntoResponse {
    StatusCode::OK
}

async fn delete_film_by_id(_id: Path<usize>) -> impl IntoResponse {
    StatusCode::OK
}

pub fn films_routers() -> Router {
    Router::new()
        .route("/v1/films", get(list_films))
        .route("/v1/films/:id", get(query_film_by_id))
        .route("/v1/films", post(create_film))
        .route("/v1/films", put(update_film))
        .route("/v1/films/:id", delete(delete_film_by_id))
}
