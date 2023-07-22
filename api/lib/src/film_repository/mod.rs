use axum::async_trait;

pub type FilmErr = String;
pub type FilmResult<T> = Result<T, FilmErr>;

#[async_trait]
pub trait FilmRepository {
    async fn list_films(&self) -> FilmResult<Vec<shared::film_model::Film>>;
    async fn query_film_by_id(&self, id: &uuid::Uuid) -> FilmResult<shared::film_model::Film>;
    async fn create_film(
        &self,
        film: &shared::film_model::CreateFilm,
    ) -> FilmResult<shared::film_model::Film>;
    async fn update_film(
        &self,
        film: &shared::film_model::Film,
    ) -> FilmResult<shared::film_model::Film>;
    async fn delete_film_by_id(&self, id: &uuid::Uuid) -> FilmResult<uuid::Uuid>;
}
