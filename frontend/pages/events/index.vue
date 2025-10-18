<script setup lang="ts">
import { ref, onMounted } from 'vue';

const events = ref([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const response = await fetch('/api/v1/events');
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    events.value = await response.json();
  } catch (error) {
    console.error('There was a problem with the fetch operation:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div>
    <h1>Events</h1>
    <div v-if="loading">
      <p>Ładowanie...</p>
    </div>
    <div v-else>
      <div v-if="events.length > 0">
        <ul>
          <li v-for="event in events" :key="event.eventId">
            <h2>{{ event.title }}</h2>
            <p>{{ new Date(event.eventDate).toLocaleString() }}</p>
          </li>
        </ul>
      </div>
      <div v-else>
        <p>Aktualnie nie ma żadnych nadchodzących wydarzeń.</p>
      </div>
    </div>
  </div>
</template>
