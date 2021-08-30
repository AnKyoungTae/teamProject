import { createApp } from "vue";
import App from "./App.vue";
import router from "@/router";
import store from "@/store";
import vuex from "vuex";
import Notifications from "@kyvg/vue3-notification";

// 부트스트랩 추가
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

//폰트 어썸
import "@fortawesome/fontawesome-free/js/all";

createApp(App)
  .use(store)
  .use(router)
  .use(vuex)
  .use(Notifications)
  .mount("#app");
