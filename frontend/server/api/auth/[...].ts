import { NuxtAuthHandler } from '#auth'
import KeycloakProvider from 'next-auth/providers/keycloak'

export default NuxtAuthHandler({
  providers: [
    // @ts-expect-error You need to use .default here for it to work during SSR. May be fixed via Vite at some point
    KeycloakProvider.default({
      clientId: 'eventmaster-frontend',
      issuer: 'http://localhost:8180/realms/eventmaster'
    })
  ]
})
