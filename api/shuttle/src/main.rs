use std::sync::Arc;

use axum::{extract::State, response::IntoResponse, routing::get, Router};
use shuttle_runtime::CustomError;
use sqlx::Executor;

async fn hello_world() -> &'static str {
    "Hello, world!"
}

#[axum::debug_handler(state =Arc<sqlx::PgPool> )]
async fn version(State(db): State<Arc<sqlx::PgPool>>) -> impl IntoResponse {
    let version: Result<String, sqlx::Error> = sqlx::query_scalar("select version()")
        .fetch_one(db.as_ref())
        .await;

    match version {
        Ok(version) => version,
        Err(e) => format!("Error: {:?}", e),
    }
}

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

    let db = Arc::new(pool);

    let router = Router::new()
        .route("/", get(hello_world))
        .route("/version", get(version))
        .with_state(db.clone());

    Ok(router.into())
}
