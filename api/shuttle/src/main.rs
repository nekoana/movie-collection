use axum::{routing::get, Router};
use shuttle_runtime::CustomError;
use sqlx::Executor;

async fn hello_world() -> &'static str {
    "Hello, world!"
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

    let router = Router::new().route("/", get(hello_world));

    Ok(router.into())
}
