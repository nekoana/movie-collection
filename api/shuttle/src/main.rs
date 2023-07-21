use std::sync::Arc;

use api_lib::health::{health_router, hello_world, version};
use axum::{routing::get, Router};
use shuttle_runtime::CustomError;
use sqlx::Executor;

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
        .with_state(db.clone())
        .nest("/", health_router());

    Ok(router.into())
}
