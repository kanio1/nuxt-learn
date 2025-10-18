# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - heading "500" [level=1] [ref=e2]
  - paragraph [ref=e3]: Cannot access 'renderer$1' before initialization
  - link "Customize this page" [ref=e4] [cursor=pointer]:
    - /url: https://nuxt.com/docs/4.x/getting-started/error-handling?utm_source=nuxt-error-dev-page
  - generic [ref=e6]: Cannot access 'renderer$1' before initialization at <anonymous> (/app/frontend/node_modules/.pnpm/nuxt@4.1.3_@parcel+watcher@2.5.1_@vue+compiler-sfc@3.5.22_db0@0.3.4_ioredis@5.8.1_magic_af5460b03a101290cbff9b616a95e241/node_modules/nuxt/dist/core/runtime/nitro/handlers/island.js:100:1) at async file:///app/frontend/node_modules/.pnpm/h3@1.15.4/node_modules/h3/dist/index.mjs:2004:19) at async Object.callAsync (/app/frontend/node_modules/.pnpm/unctx@2.4.1/node_modules/unctx/dist/index.mjs:72:16) at async toNodeHandle (/app/frontend/node_modules/.pnpm/h3@1.15.4/node_modules/h3/dist/index.mjs:2296:7) at async b (/app/frontend/node_modules/.pnpm/node-mock-http@1.0.3/node_modules/node-mock-http/dist/index.mjs:1:6876) at async C (/app/frontend/node_modules/.pnpm/node-mock-http@1.0.3/node_modules/node-mock-http/dist/index.mjs:1:7159) at async errorhandler (/app/frontend/node_modules/.pnpm/nuxt@4.1.3_@parcel+watcher@2.5.1_@vue+compiler-sfc@3.5.22_db0@0.3.4_ioredis@5.8.1_magic_af5460b03a101290cbff9b616a95e241/node_modules/nuxt/dist/core/runtime/nitro/handlers/error.js:30:41) at async errorHandler (/app/frontend/node_modules/.pnpm/nitropack@2.12.7/node_modules/nitropack/dist/runtime/internal/error/dev.mjs:134:1) at async Server.toNodeHandle (/app/frontend/node_modules/.pnpm/h3@1.15.4/node_modules/h3/dist/index.mjs:2304:9)
```