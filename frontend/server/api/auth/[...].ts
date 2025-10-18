import { NuxtAuthHandler } from '#auth';
import KeycloakProvider from 'next-auth/providers/keycloak';

export default NuxtAuthHandler({
  // Potrzebny do podpisywania sesji Nuxt
  secret: process.env.NUXT_AUTH_SECRET || 'super-tajny-sekret-zmien-mnie',

  providers: [
    KeycloakProvider({
      clientId: 'eventmaster-frontend', // Zgodnie z tym, co jest w Keycloak
      clientSecret: 'dummy', // Niepotrzebne, ale czasem wymagane przez bibliotekę
      issuer: 'http://localhost:8180/realms/eventmaster', // Issuer z Keycloak
    }),
  ],
});
