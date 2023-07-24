<script lang="ts">
  import { onMount } from "svelte";
  import { filmRepository } from "./repository/FilmRepository";
  import FilmCard from "./lib/FilmCard.svelte";
  import FileModal from "./lib/FilmModal.svelte";
  import { SvelteUIProvider, Stack, SimpleGrid } from "@svelteuidev/core";

  let films = [];

  let modifyFilm;
  let loading = false;

  function loadFilms() {
    filmRepository.list_films().then((l) => {
      films = l;
    });
  }

  function handleFilmDelete(event) {
    filmRepository.delete_film(event.detail.id).then(() => {
      loadFilms();
    });
  }

  function handleFilmModifyRequest(event) {
    modifyFilm = event.detail;
  }

  function handleFilmModify(event) {
    loading = true;
    filmRepository
      .update_film(event.detail)
      .then(() => {
        loadFilms();
      })
      .catch((e) => {
        console.error(e);
      })
      .finally(() => {
        modifyFilm = null;
        loading = false;
      });
  }

  onMount(() => {
    loadFilms();
  });
</script>

<SvelteUIProvider
  withGlobalStyles
  themeObserver="dark"
  align="center"
  justify="flex-start"
>
  <Stack>
    <SimpleGrid cols={4}>
      {#each films as film (film.id)}
        <FilmCard
          {film}
          on:delete={handleFilmDelete}
          on:modify={handleFilmModifyRequest}
        />
      {/each}
    </SimpleGrid>
  </Stack>

  <FileModal
    film={modifyFilm}
    {loading}
    on:close={() => (modifyFilm = null)}
    on:modify={handleFilmModify}
  />
</SvelteUIProvider>
