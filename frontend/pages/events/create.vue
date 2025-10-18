<script setup lang="ts">
import { ref } from 'vue';
import { useToast } from 'primevue/usetoast';
import { z } from 'zod';
import { useForm } from 'vee-validate';
import { toTypedSchema } from '@vee-validate/zod';
import InputText from 'primevue/inputtext';
import Editor from 'primevue/editor';
import Calendar from 'primevue/calendar';
import Button from 'primevue/button';

definePageMeta({
  middleware: 'auth'
});

const toast = useToast();
const router = useRouter();
const isLoading = ref(false);

const CreateEventSchema = z.object({
  title: z.string().nonempty('Tytuł nie może być pusty'),
  description: z.string().optional(),
  eventDate: z.date().min(new Date(), 'Data musi być w przyszłości'),
});

type CreateEventRequest = z.infer<typeof CreateEventSchema>;

const { handleSubmit, defineInputBinds, errors } = useForm({
  validationSchema: toTypedSchema(CreateEventSchema),
});

const title = defineInputBinds('title');
const description = defineInputBinds('description');
const eventDate = defineInputBinds('eventDate');

const onSubmit = handleSubmit(async (formData: CreateEventRequest) => {
    isLoading.value = true;
    try {
        await $fetch('/api/v1/events', {
            method: 'POST',
            body: {
              ...formData,
              eventDate: formData.eventDate.toISOString()
            },
        });

        toast.add({
            severity: 'success',
            summary: 'Przyjęto',
            detail: 'Twoje wydarzenie jest przetwarzane.',
            life: 3000
        });
        router.push('/');

    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Błąd',
            detail: 'Nie udało się przyjąć polecenia.',
            life: 3000
        });
    } finally {
        isLoading.value = false;
    }
});
</script>

<template>
  <div class="p-card p-4">
    <h1 class="text-2xl font-bold mb-4">Utwórz Nowe Wydarzenie</h1>
    <form @submit.prevent="onSubmit" class="flex flex-col gap-4">
      <div class="flex flex-col">
        <label for="title" class="mb-2">Tytuł</label>
        <InputText id="title" v-bind="title" />
        <small class="p-error">{{ errors.title }}</small>
      </div>
      <div class="flex flex-col">
        <label for="description" class="mb-2">Opis</label>
        <Editor id="description" v-bind="description" editorStyle="height: 320px" />
        <small class="p-error">{{ errors.description }}</small>
      </div>
      <div class="flex flex-col">
        <label for="eventDate" class="mb-2">Data wydarzenia</label>
        <Calendar id="eventDate" v-bind="eventDate" />
        <small class="p-error">{{ errors.eventDate }}</small>
      </div>
      <Button type="submit" label="Utwórz" :loading="isLoading" />
    </form>
  </div>
</template>
