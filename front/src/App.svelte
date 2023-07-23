<script lang="ts">
  import "./styles.css"
  import Greet from "./lib/Greet.svelte";
  import { FilmRepository } from "./api/film_collection";

  const film_repository = new FilmRepository("http://127.0.0.1:8000/v1/films");

  let list_films_promise;

  function list_films() {
    list_films_promise = film_repository.list_films();
  }
</script>

<main class="container">
  <h1>Welcome to Tauri!</h1>

  <div class="row">
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo vite" alt="Vite Logo" />
    </a>
    <a href="https://tauri.app" target="_blank">
      <img src="/tauri.svg" class="logo tauri" alt="Tauri Logo" />
    </a>
    <a href="https://svelte.dev" target="_blank">
      <img src="/svelte.svg" class="logo svelte" alt="Svelte Logo" />
    </a>
  </div>

  <p>Click on the Tauri, Vite, and Svelte logos to learn more.</p>

  <div class="row">
    <Greet />
  </div>

  <md-filled-button on:click={list_films}>List Films</md-filled-button>

  {#if list_films_promise}
    {#await list_films_promise then films}
      {#each films as film}
        <div>
          <p>{film.title}</p>
        </div>
      {/each}
    {/await}
  {/if}
</main>

<style>
  .logo.vite:hover {
    filter: drop-shadow(0 0 2em #747bff);
  }

  .logo.svelte:hover {
    filter: drop-shadow(0 0 2em #ff3e00);
  }
</style>
