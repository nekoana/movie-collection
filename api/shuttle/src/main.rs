use axum::{Router, routing::get};
use axum::extract::State;
use shuttle_runtime::CustomError;
use sqlx::Executor;
use tracing_subscriber::layer::SubscriberExt;
use tracing_subscriber::util::SubscriberInitExt;

use api_lib::{
    films::films_routers,
    health::{health_router, hello_world, version},
};
use api_lib::postgres_film_repository::PostgresFilmRepository;

#[shuttle_runtime::main]
async fn axum(
    #[shuttle_shared_db::Postgres(
        local_uri = "postgres://postgres:{secrets.PASSWORD}@127.0.0.1:5432/postgres"
    )]
    pool: sqlx::PgPool,
) -> shuttle_axum::ShuttleAxum {
    pool.execute(include_str!("../../db/schema.sql"))
        .await
        .map_err(CustomError::new)?;

    let film_repository = State(PostgresFilmRepository::new(pool.clone()));

    let router = Router::new()
        .route("/", get(hello_world))
        .route("/version", get(version))
        .with_state(pool.clone())
        .nest("/", health_router())
        .nest("/", films_routers(film_repository));

    Ok(router.into())
}
