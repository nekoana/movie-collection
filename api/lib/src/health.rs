use std::sync::Arc;

use axum::{extract::State, response::IntoResponse};

pub async fn hello_world() -> &'static str {
    "Hello, world!"
}

#[axum::debug_handler(state =Arc<sqlx::PgPool> )]
pub async fn version(State(db): State<Arc<sqlx::PgPool>>) -> impl IntoResponse {
    tracing::info!("Getting version");
    let version: Result<String, sqlx::Error> = sqlx::query_scalar("select version()")
        .fetch_one(db.as_ref())
        .await;

    match version {
        Ok(version) => version,
        Err(e) => format!("Error: {:?}", e),
    }
}

pub async fn health() -> &'static str {
    "V0.0.1"
}
