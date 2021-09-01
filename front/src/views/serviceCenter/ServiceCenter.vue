<template>
  <div class="service-center m-5">
    <div class="service-group mb-5">
      <router-link
        class="btn btn-success me-5 col-2 serviceMenu"
        :to="{ name: 'Notice' }"
        @click="clickPages(1)"
        v-if="this.$store.state.serviceCenters == 1"
        >공지사항</router-link
      >
      <router-link
        class="btn btn-warning me-5 col-2 serviceMenu"
        :to="{ name: 'Notice' }"
        @click="clickPages(1)"
        v-else
        >공지사항</router-link
      >

      <router-link
        class="btn btn-success mx-5 col-2 serviceMenu"
        :to="{ name: 'Q&A' }"
        @click="clickPages(2)"
        v-if="this.$store.state.serviceCenters == 2"
        >Q&A</router-link
      >
      <router-link
        class="btn btn-warning mx-5 col-2 serviceMenu"
        :to="{ name: 'Q&A' }"
        @click="clickPages(2)"
        v-else
        >Q&A</router-link
      >

      <router-link
        class="btn btn-success ms-5 col-2 serviceMenu"
        :to="{ name: 'FAQ' }"
        @click="clickPages(3)"
        v-if="this.$store.state.serviceCenters == 3"
        >FAQ</router-link
      >
      <router-link
        class="btn btn-warning ms-5 col-2 serviceMenu"
        :to="{ name: 'FAQ' }"
        @click="clickPages(3)"
        v-else
        >FAQ</router-link
      >
    </div>
    <notice v-if="this.$store.state.serviceCenterToggle == false"></notice>
    <router-view v-else></router-view>
  </div>
</template>

<script>
import notice from "@/components/service/Notice.vue";

export default {
  components:{notice},
  data() {
    return {
      
    };
  },
  
  methods: {
    //클릭시 현재 페이지 표시 
    clickPages(num) {   
      this.$store.commit("SET_serviceCenters", num)         // 주소찾기에서 선택한 주소
      this.$store.commit("SET_serviceCenterToggle", true)  //sidebar에서 serviceCenter 클릭시
    },

    
    
    
  },
  

}
</script>

<style scoped>
.serviceMenu {
  font-size: 20px;
  padding: 10px;
}
</style>
