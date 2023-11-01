import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import vuetify from "@/vuetify";


let app = createApp(App);

app.use(router);
app.use(vuetify)

app.mount('#app')
