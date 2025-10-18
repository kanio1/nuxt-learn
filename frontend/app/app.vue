<template>
  <div>
    <div v-if="status === 'authenticated'">
      <p>Welcome, {{ session.user.email }}</p>
      <button @click="signOut()">Sign out</button>
    </div>
    <div v-else>
      <p>You are not logged in.</p>
      <button @click="signIn('keycloak')">Sign in with Keycloak</button>
    </div>
  </div>
</template>

<script setup lang="ts">
const { status, data: session, signIn, signOut } = useAuth()
</script>
