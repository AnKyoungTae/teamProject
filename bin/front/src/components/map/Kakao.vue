<template>
  <div
    id="map"
    style="
      width: 100%;
      height: 450px;
      border: 1px solid gray;
      border-radius: 5px;
    "
  ></div>
</template>

<script>
export default {
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
      var container = document.getElementById("map"); //지도를 담을 영역의 DOM 레퍼런스
      var options = {
        //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(37.43884057982199, 126.675102369038), //지도의 중심좌표.
        level: 3, //지도의 레벨(확대, 축소 정도)
      };

      var map = new kakao.maps.Map(container, options); // eslint-disable-line no-unused-vars
      //지도 생성 및 객체 리턴
      const markerPosition = new kakao.maps.LatLng(
        35.19656853772262,
        129.0807270648317
      );

      const marker = new kakao.maps.Marker({
        position: markerPosition,
      });
      marker.setMap(map);
    },
  },
};
</script>

<style></style>
