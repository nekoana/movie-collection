<script lang="ts">
  import { onMount } from "svelte";
  import { filmRepository } from "./repository/FilmRepository";
  import FilmCard from "./lib/FilmCard.svelte";
  import { SvelteUIProvider, Stack, SimpleGrid } from "@svelteuidev/core";

  let list_films_promise;

   function handleFilmDelete(event) {
     filmRepository.delete_film(event.detail.id).then(() => {
       list_films_promise = filmRepository.list_films();
     })
  }

  function handleFilmModify(event) {
    alert(event.detail.id);
  }

  onMount(() => {
    list_films_promise = filmRepository.list_films();
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
      {#if list_films_promise}
        {#await list_films_promise then films}
          {#each films as film (film.id)}
            <FilmCard
              {film}
              on:delete={handleFilmDelete}
              on:modify={handleFilmModify}
            />
          {/each}
        {/await}
      {/if}
    </SimpleGrid>
  </Stack>
</SvelteUIProvider>
