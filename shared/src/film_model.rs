#[derive(serde::Serialize, serde::Deserialize, Clone, Debug, PartialEq, Eq, PartialOrd, Ord)]
#[cfg_attr(feature = "backend", derive(sqlx::FromRow))]
pub struct Film {
    pub id: uuid::Uuid,
    pub title: String,
    pub director: String,
    #[cfg_attr(feature = "backend", sqlx(try_from = "i16"))]
    pub year: u16,
    pub poster: String,
    pub created_at: Option<chrono::DateTime<chrono::Utc>>,
    pub updated_at: Option<chrono::DateTime<chrono::Utc>>,
}

#[derive(serde::Serialize, serde::Deserialize, Clone, Debug, PartialEq, Eq, PartialOrd, Ord)]
#[cfg_attr(feature = "backend", derive(sqlx::FromRow))]
pub struct CreateFilm {
    pub title: String,
    pub director: String,
    #[cfg_attr(feature = "backend", sqlx(try_from = "i16"))]
    pub year: u16,
    pub poster: String,
}
