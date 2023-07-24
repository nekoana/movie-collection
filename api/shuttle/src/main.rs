use std::path::PathBuf;

use axum::extract::State;
use axum::response::{IntoResponse, Redirect};
use axum::{routing::get, Router};
use shuttle_runtime::CustomError;
use sqlx::Executor;

use api_lib::postgres_film_repository::PostgresFilmRepository;
use api_lib::{
    films::films_routers,
    health::{health_router, version},
};
use tower_http::cors::{Any, CorsLayer};
use tower_http::services::ServeDir;

async fn index() -> impl IntoResponse {
    Redirect::temporary("/assets/index.html")
}

#[shuttle_runtime::main]
async fn axum(
    #[shuttle_shared_db::Postgres(
        local_uri = "postgres://postgres:{secrets.PASSWORD}@127.0.0.1:5432/postgres"
    )]
    pool: sqlx::PgPool,
    #[shuttle_static_folder::StaticFolder(folder = "static")] static_folder: PathBuf,
) -> shuttle_axum::ShuttleAxum {
    pool.execute(include_str!("../../db/schema.sql"))
        .await
        .map_err(CustomError::new)?;

    let film_repository = State(PostgresFilmRepository::new(pool.clone()));

    let root = Router::new()
        .route("/", get(index))
        .nest("/", health_router())
        .nest("/", films_routers(film_repository));

    let router = Router::new()
        .route("/version", get(version))
        .with_state(pool.clone())
        .merge(root)
        .nest_service("/assets", ServeDir::new(static_folder))
        .layer(
            CorsLayer::new()
                .allow_origin(Any)
                .allow_methods(Any)
                .allow_headers(Any),
        );

    Ok(router.into())
}
