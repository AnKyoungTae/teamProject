<template>
  <div>
    <div class="gray">
      <div id="staticMap"></div>
    </div>
    <div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["localx", "localy", "shopname"],
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=0feaa40f6d94ad4428a4f4f8a6cab340&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
  },
  methods: {
    initMap() {
      var marker = {
        position: new kakao.maps.LatLng(this.localy, this.localx),
        text: this.shopname,
      };

      var staticMapContainer = document.getElementById("staticMap"), // 이미지 지도를 표시할 div
        staticMapOption = {
          center: new kakao.maps.LatLng(this.localy, this.localx), // 이미지 지도의 중심좌표
          level: 3, // 이미지 지도의 확대 레벨
          marker: marker, // 이미지 지도에 표시할 마커
        };

      // 이미지 지도를 생성합니다
      var staticMap = new kakao.maps.StaticMap(
        staticMapContainer,
        staticMapOption
      );
    },
  },
};
</script>

<style scoped>
#staticMap {
  width: 100%;
  height: 100%;
  text-align: center;
}
.gray {
  background-color: gray;
  height: 20rem;
}
</style>
