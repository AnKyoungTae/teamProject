<template>
  <div>
    <Sidebar />
    <transition name="page">
      <div class="col-md-9" :style="{ 'margin-left': sidebarWidth }">
        <router-view />
      </div>
    </transition>
    <Footer></Footer>
  </div>
</template>

<script>
import Sidebar from "@/components/sidebar/Sidebar";
import { sidebarWidth } from "@/components/sidebar/state";

export default {
  components: {
    Sidebar,
  },
  setup() {
    return { sidebarWidth };
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Poor+Story&display=swap");
#app {
  font-family: "Poor Story", cursive;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  position: relative;
  width: 97.5vw;
  height: 100vh;
}
#nav .middle {
  min-height: 90vh;
  margin-bottom: 3vh;
  left: 2vw;
  right: 2vw;
  margin: 0 auto;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
.page-enter-active,
.page-leave-active {
  transition: opacity 0.1s;
}

.page-enter,
.page-leave-to {
  opacity: 0;
}
</style>
