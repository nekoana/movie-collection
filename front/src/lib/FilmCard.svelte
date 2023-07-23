<script lang="ts">
  import { type Film } from "../model/Film";

  import { createEventDispatcher } from "svelte";
  import {
    Badge,
    Button,
    Card,
    Group,
    Image,
    Text,
    Flex,
    Space,
  } from "@svelteuidev/core";

  const dispatch = createEventDispatcher();

  function onFilmDelete() {
    dispatch("delete", film);
  }

  function onFilmModify() {
    dispatch("modify", film);
  }

  export let film: Film;
</script>

<Card shadow="sm" padding="lg">
  <Card.Section padding="lg">
    <Image radius="md" src={film.poster} height={160} alt="Portugal" />
  </Card.Section>

  <Group position="apart">
    <Text weight={500}>{film.title}</Text>
  </Group>

  <Text size="sm">
    {film.director}
  </Text>
  <Group position="right" spacing="md">
    <Button on:click={onFilmDelete}>Delete</Button>
    <Button on:click={onFilmModify}>Modify</Button>
  </Group>
</Card>
