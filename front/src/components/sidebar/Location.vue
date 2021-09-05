<template>
  <div
    @click="refreshLocation"
    class="link"
    :class="{ isCollapsed: !collapsed }"
    v-if="!collapsed"
  >
    <i class="icon" :class="icon" />
    <!-- 트랜지션 태그는 vuejs에서 제공하는 태그로, DOM이 조작될때 CSS 애니메이션을 넣을 수 있다. -->
    <transition name="fade">
      <div>
        <span v-if="!collapsed && local" class="localText">
          {{ local.address_name }}
        </span>
        <span v-else> ... </span>
      </div>
    </transition>
  </div>
</template>

<script>
import { collapsed } from "./state";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      geocoder: null,
      coord: null,
    };
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
      console.log("리프레쉬로케이션");
      return navigator.geolocation.getCurrentPosition(
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
      this.$store.commit("SET_OBSERVE", true);
      console.log(latitude);
      console.log(longitude);
      if (window.kakao && window.kakao.maps) {
        console.log("카카오스크립트있음");
        this.findAddress(latitude, longitude);
      } else {
        console.log("카카오 스크립트 없음");
        const script = document.createElement("script");
        script.onload = () => kakao.maps.load(this.refreshLocation);
        script.src =
          "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=0feaa40f6d94ad4428a4f4f8a6cab340&libraries=services,clusterer,drawing";
        document.head.appendChild(script);
      }
    },
    findAddress(latitude, longitude) {
      console.log(latitude);
      console.log(longitude);
      this.geocoder = new kakao.maps.services.Geocoder();
      console.log(this.geocoder);
      this.coord = new kakao.maps.LatLng(latitude, longitude);
      const addressSearch = (coord) => {
        return new Promise((resolve, reject) => {
          this.geocoder.coord2Address(
            coord.getLng(),
            coord.getLat(),
            function (result, status) {
              if (status === kakao.maps.services.Status.OK) {
                resolve(result);
              } else {
                reject(status);
              }
            }
          );
        });
      };
      const setLocal = (result) => {
        console.log("셋 로칼");
        this.$store.commit("SET_LOCAL", result[0].address);
        console.log(this.local);
        console.log("여기까지 오긴옴");
      };
      // async
      (async () => {
        try {
          console.log("에싱크");
          const result = await addressSearch(this.coord);
          setLocal(result);
        } catch (e) {
          console.log(e);
        }
      })();
    },
  },
  mounted() {
    this.refreshLocation();
  },
};
</script>

<style scoped>
.isCollapsed {
  background-color: tomato;
  left: 10%;
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
  background-color: rgb(216, 120, 91);
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
