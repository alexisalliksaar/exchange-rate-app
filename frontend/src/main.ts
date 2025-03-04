import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import PrimeVue from 'primevue/config';
import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';
import 'primeflex/primeflex.css'

import '@/assets/styles.scss';
import preset from './theme';
import { VueQueryPlugin } from '@tanstack/vue-query'
import { queryClient } from './query';

const app = createApp(App);

app.use(router)

app.use(VueQueryPlugin, {
    enableDevtoolsV6Plugin: true,
    queryClient
})

app.use(PrimeVue, {
    theme: {
        preset: preset,
        options: {
            darkModeSelector: '.app-dark'
        }
    }
})
app.use(ToastService)
app.use(ConfirmationService)

app.mount('#app')
