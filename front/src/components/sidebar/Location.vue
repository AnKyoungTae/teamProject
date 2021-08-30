<template>
  <div
    @click="refreshLocation"
    class="link"
    :class="{ isCollapsed: !collapsed }"
  >
    <i class="icon" :class="icon" />
    <!-- 트랜지션 태그는 vuejs에서 제공하는 태그로, DOM이 조작될때 CSS 애니메이션을 넣을 수 있다. -->
    <transition name="fade">
      <span v-if="!collapsed && local" class="localText">
        {{ local.address_name }}
      </span>
      <span v-else> 위치를 알수없습니다! </span>
    </transition>
  </div>
</template>

<script>
import { collapsed } from "./state";
import { mapGetters } from "vuex";

export default {
  data() {
    return {};
  },
  props: {
    icon: { type: String, required: true },
  },
  setup() {
    return { collapsed };
  },
  computed: {
    ...mapGetters({
      local: "GET_LOCAL",
    }),
  },
  methods: {
    refreshLocation() {
      return navigator.geolocation.watchPosition(
        (position) => {
          this.setLocation(position.coords.latitude, position.coords.longitude);
        },
        (error) => {
          console.log("위치정보를 갱신할 수 없습니다" + error);
        }
      );
    },
    setLocation(latitude, longitude) {
      this.$store.commit("SET_LAT", latitude);
      this.$store.commit("SET_LON", longitude);
      console.log("사용자 위치 추적: " + latitude + ", " + longitude);
      this.$store.commit("SET_OBSERVE", true);
    },
  },
};
</script>

<style scoped>
.isCollapsed {
  left: 10%;
  background-color: tomato;
}
.link {
  display: flex;
  align-items: center;

  cursor: pointer;
  position: relative;
  font-weight: 400;
  user-select: none;
  width: 80%;
  margin: 0.2em 0;
  padding: 0.8em;
  border-radius: 0.25em;
  height: 2rem;
  color: white;
  text-decoration: none;
  font-size: 12px;
}

.link:hover {
  background-color: var(--sidebar-item-hover);
}

.link .icon {
  flex-shrink: 0;
  width: 25px;
  margin-right: 10px;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.localText {
  white-space: normal;
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
</style>
