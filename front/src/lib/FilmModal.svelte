<script lang="ts">
  import "@material/web/textfield/outlined-text-field";
  import "@material/web/dialog/dialog";
  import {
    Modal,
    useSvelteUITheme,
    Card,
    Text,
    TextInput,
    Textarea,
    Image,
    Button,
    Space,
  } from "@svelteuidev/core";
  import { type Film } from "../model/Film";
  import { createEventDispatcher } from "svelte";

  const theme = useSvelteUITheme();

  export let film: Film;
  export let loading = false;

  $: isOpen = !!film;

  $: poster = film?.poster;
  $: title = film?.title;
  $: director = film?.director;

  if (film) {
    poster = film.poster;
    title = film.title;
    director = film.director;
  }

  const dispatch = createEventDispatcher();

  function closeModal() {
    dispatch("close");
  }

  function modifyFilm() {
    const newFilm = {
      ...film,
      poster,
      title,
      director,
    };
    dispatch("modify", newFilm);
  }
</script>

{#if film}
  <Modal
    title="Modify Film"
    opened={isOpen}
    on:close={closeModal}
    centered
    overlayOpacity={0.55}
    overlayBlur={3}
  >
    <TextInput
      placeholder={film.poster}
      label="Poster"
      variant="filled"
      bind:value={poster}
    />
    <TextInput
      label="Title"
      placeholder={film.title}
      variant="filled"
      bind:value={title}
    />
    <Textarea
      placeholder={film.director}
      label="Director"
      variant="filled"
      resize="vertical"
      bind:value={director}
    />

    {#if film.updated_at}
      <TextInput placeholder={film.updated_at} label="Update At" disabled />
    {/if}

    <TextInput placeholder={film.created_at} label="Create At" disabled />
    <Space h="xl" />
    <Button ripple {loading} on:click={modifyFilm}>Ok</Button>
  </Modal>
{/if}
