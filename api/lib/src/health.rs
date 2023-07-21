use std::{str::FromStr, sync::Arc};

use axum::{
    extract::State,
    http::{HeaderMap, HeaderName, HeaderValue},
    response::IntoResponse,
    routing::get,
    Router,
};

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

pub async fn health() -> (HeaderMap, &'static str) {
    let mut headers = HeaderMap::new();

    let name = HeaderName::from_str("Version").expect("Could not create header name");
    let value = HeaderValue::from_str("V0.0.1").expect("Could not create header value");
    headers.insert(name, value);

    (headers, "OK")
}

pub fn health_router() -> Router {
    Router::new().route("/health", get(health))
}
