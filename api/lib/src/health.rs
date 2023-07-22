use std::str::FromStr;

use axum::{
    extract::State,
    http::{HeaderMap, HeaderName, HeaderValue},
    response::IntoResponse,
    Router,
    routing::get,
};

pub async fn hello_world() -> &'static str {
    "Hello, world!"
}

#[axum::debug_handler(state = sqlx::PgPool)]
pub async fn version(State(db): State<sqlx::PgPool>) -> impl IntoResponse {
    tracing::info!("Getting version");
    let version: Result<String, sqlx::Error> =
        sqlx::query_scalar("select version()").fetch_one(&db).await;

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

#[cfg(test)]
mod test {
    use axum::{
        body::Body,
        http::{Request, StatusCode},
    };
    use tower::ServiceExt;

    #[tokio::test]
    async fn test_header_version() {
        let (headers, _) = super::health().await;

        let version = headers.get("Version");
        assert_eq!(version.unwrap(), "V0.0.1");
    }
    #[tokio::test]
    async fn test_health_work() {
        let router = super::health_router();

        let response = router
            .oneshot(
                Request::builder()
                    .uri("/health")
                    .body(Body::empty())
                    .expect("Could not create request"),
            )
            .await
            .expect("Could not get response");

        assert_eq!(response.status(), StatusCode::OK);

        let body = response.into_body();

        let body = hyper::body::to_bytes(body)
            .await
            .expect("Could not get bytes");

        assert_eq!(&body[..], b"OK");
    }
}
