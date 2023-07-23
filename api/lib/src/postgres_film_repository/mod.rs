use axum::async_trait;

use shared::film_model::{CreateFilm, Film};

use crate::film_repository::FilmRepository;

pub struct PostgresFilmRepository {
    pool: sqlx::PgPool,
}

impl PostgresFilmRepository {
    pub fn new(pool: sqlx::PgPool) -> Self {
        Self { pool }
    }
}

#[async_trait]
impl FilmRepository for PostgresFilmRepository {
    async fn list_films(
        &self,
    ) -> crate::film_repository::FilmResult<Vec<shared::film_model::Film>> {
        sqlx::query_as(
            r#"
        select id,title,director,year,poster,created_at,updated_at from films
       "#,
        )
        .fetch_all(&self.pool)
        .await
        .map_err(|e| e.to_string())
    }

    async fn query_film_by_id(&self, id: &uuid::Uuid) -> crate::film_repository::FilmResult<Film> {
        sqlx::query_as(
            r#"
        select id,title,director,year,poster,created_at,updated_at from films where id = $1
       "#,
        )
        .bind(id)
        .fetch_one(&self.pool)
        .await
        .map_err(|e| e.to_string())
    }

    async fn create_film(
        &self,
        create_film: &CreateFilm,
    ) -> crate::film_repository::FilmResult<Film> {
        sqlx::query_as(
            r#"
        insert into films (title,director,year,poster) values ($1,$2,$3,$4) returning id,title,director,year,poster,created_at,updated_at
            "#)
            .bind(&create_film.title)
            .bind(&create_film.director)
            .bind(create_film.year as i16)
            .bind(&create_film.poster)
            .fetch_one(&self.pool)
            .await
            .map_err(|e| e.to_string())
    }

    async fn update_film(
        &self,
        film: &shared::film_model::Film,
    ) -> crate::film_repository::FilmResult<shared::film_model::Film> {
        sqlx::query_as(
            r#"
        update films set title = $1, director = $2, year = $3, poster = $4, updated_at = now() where id = $5 returning id,title,director,year,poster,created_at,updated_at
            "#)
            .bind(&film.title)
            .bind(&film.director)
            .bind(film.year as i16)
            .bind(&film.poster)
            .bind(&film.id)
            .fetch_one(&self.pool)
            .await
            .map_err(|e| e.to_string())
    }

    async fn delete_film_by_id(
        &self,
        id: &uuid::Uuid,
    ) -> crate::film_repository::FilmResult<uuid::Uuid> {
        sqlx::query_as(
            r#"
        delete from films where id = $1 returning id
            "#,
        )
        .bind(id)
        .fetch_one(&self.pool)
        .await
        .map(|film: Film| film.id)
        .map_err(|e| e.to_string())
    }
}
