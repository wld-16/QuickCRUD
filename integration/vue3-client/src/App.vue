<template>
  <v-app>
    <v-row>
      <v-col cols="2">
        <RouteNavigationComponent></RouteNavigationComponent>
      </v-col>
      <v-col>
        <router-view></router-view>
      </v-col>
    </v-row>
  </v-app>
</template>

<script>
import RouteNavigationComponent from "@/components/RouteNavigationComponent.vue";

export default {
  name: 'App',
  components: {RouteNavigationComponent},
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
