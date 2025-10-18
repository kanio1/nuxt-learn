<script setup lang="ts">
const { status, data: session, signIn, signOut } = useAuth();

// Użyj 'useFetch' z nuxt-auth, który automatycznie dołącza token!
// Użyj '$fetch' (globalny composable Nuxta), który jest opakowany przez moduł nuxt-auth
const { data: apiResponse, error } = await useFetch('/api/v1/user/me', {
    lazy: true, // Nie blokuj renderowania strony
    server: false, // Wykonaj tylko po stronie klienta
});
</script>

<template>
  <div>
    <h1>Status Sesji: {{ status }}</h1>

    <div v-if="status === 'authenticated'">
      <p>Zalogowany jako: {{ session?.user?.email }}</p>
      <button @click="signOut()">Wyloguj</button>
    </div>
    <div v-else>
      <p>Niezalogowany</p>
      <button @click="signIn('keycloak')">Zaloguj przez Keycloak</button>
    </div>

    <hr />
    <h2>Status API Backendu:</h2>
    <div v-if="apiResponse">
      <pre>{{ apiResponse }}</pre>
    </div>
    <div v-else-if="error">
      <p style="color: red;">Błąd API: {{ error.message }}</p>
      <pre>{{ error.data }}</pre>
    </div>
    <div v-else>
      <p>Ładowanie danych z API...</p>
    </div>
  </div>
</template>
