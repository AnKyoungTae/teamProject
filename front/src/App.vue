<template>
  <div style="height: 100vh; overflow: auto; overflow-x: hidden">
    <Sidebar />
    <transition name="page">
      <div class="App-box" :style="{ 'margin-left': sidebarWidth }">
        <router-view />
      </div>
    </transition>
    <Footer></Footer>
  </div>
</template>

<script>
import Sidebar from "@/components/sidebar/Sidebar";
import { sidebarWidth } from "@/components/sidebar/state";
import Footer from "@/components/footer/Footer";

export default {
  components: {
    Sidebar,
    Footer,
  },
  setup() {
    return { sidebarWidth };
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Poor+Story&display=swap");
html,
body {
  background: white;
  overflow-x: hidden;
  overflow: hidden;
}
Footer {
  background-color: #355f6e;
}
.App-box {
  flex: 0 0 auto;
  min-height: 70vh;
}
#app {
  font-family: "Poor Story", cursive;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-size: 18px;
  text-align: center;
  background: white;
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
