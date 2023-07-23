const base_url = "http://127.0.0.1:8000/v1/films";

interface CreateFilm {
  title: string;
  director: string;
  year: number;
  poster: string;
}

interface Film {
  id: string;
  title: string;
  director: string;
  year: number;
  poster: string;
  created_at: string;
  updated_at: string;
}

interface IFilmRepository {
  list_films(): Promise<Film[]>;
  create_film(film: CreateFilm): Promise<Film>;
  update_film(film: Film): Promise<Film>;
  delete_film(id: string): Promise<string>;
}

class FilmRepository implements IFilmRepository {
  private readonly base_url: string;
  constructor(base_url: string) {
    this.base_url = base_url;
  }

  async list_films(): Promise<Film[]> {
    const response = await fetch(this.base_url, {
      method: "GET",
      mode:"cors"
    });
    return await response.json();
  }
  async create_film(film: CreateFilm): Promise<Film> {
    const response = await fetch(this.base_url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(film),
    });
    return await response.json();
  }
  async update_film(film: Film): Promise<Film> {
    const response = await fetch(this.base_url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(film),
    });
    return await response.json();
  }
  async delete_film(id: string): Promise<string> {
    const response = await fetch(this.base_url + "/" + id, {
      method: "DELETE",
    });
    return await response.json();
  }
}

export { FilmRepository };
export type { IFilmRepository, Film, CreateFilm };
