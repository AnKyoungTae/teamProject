<template>
  <div class="wrapper">
    <div class="service-center m-2 upper">
      <div class="service-group mb-5">
        <router-link
          class="btn me-5 col-2 serviceMenu"
          :to="{ name: 'Notice' }"
          @click="clickPages(1)"
          :class="this.$store.state.serviceCenters == 1 ? 'active' : ''"
          >공지사항</router-link
        >

        <router-link
          class="btn mx-5 col-2 serviceMenu"
          :to="{ name: 'Q&A' }"
          @click="clickPages(2)"
          :class="this.$store.state.serviceCenters == 2 ? 'active' : ''"
          >Q&A</router-link
        >

        <router-link
          class="btn ms-5 col-2 serviceMenu"
          :to="{ name: 'FAQ' }"
          @click="clickPages(3)"
          :class="this.$store.state.serviceCenters == 3 ? 'active' : ''"
          >FAQ</router-link
        >
      </div>
      <notice v-if="this.$store.state.serviceCenterToggle == false"></notice>
      <router-view v-else></router-view>
    </div>
  </div>
  <Footer></Footer>
</template>

<script>
import notice from "@/components/service/Notice.vue";
import Footer from "@/components/footer/Footer.vue";

export default {
  components: { notice, Footer },
  data() {
    return {};
  },

  methods: {
    //클릭시 현재 페이지 표시
    clickPages(num) {
      this.$store.commit("SET_serviceCenters", num); // 주소찾기에서 선택한 주소
      this.$store.commit("SET_serviceCenterToggle", true); //sidebar에서 serviceCenter 클릭시
    },
  },
};
</script>

<style scoped>
.serviceMenu {
  font-size: 1.5em;
  padding: 10px;
  font-weight: bolder;
  background: #ffda77;
  border: 2px solid #fd3a69;
  color: black;
  border-radius: 10px 10px 10px 0px;
}
.serviceMenu:hover {
  background: orange;
  color: white;
}
.active {
  color: #fd3a69;
  text-decoration-line: underline;
  text-underline-position: under;
}
.service-center {
  /* width: 90vw; */
}
.wrapper {
  display: flex;
  flex-direction: column;
  align-content: space-between;
}
</style>
