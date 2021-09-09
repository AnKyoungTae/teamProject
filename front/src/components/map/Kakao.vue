<template>
  <div
    id="map"
    style="
      width: 100%;
      height: 525px;
      border: 1px solid gray;
      border-radius: 5px;
    "
    @change="refreshLocation"
  ></div>
</template>

<script>
import { mapMutations, mapGetters } from "vuex";

export default {
  props: ["shopList"],
  computed: {
    ...mapGetters(["GET_LOCAL", "GET_LAT", "GET_LON"]),
    latitude() {
      return this.GET_LAT != null ? this.GET_LAT : this.baselatitude;
    },
    longitude() {
      return this.GET_LON != null ? this.GET_LON : this.baselongitude;
    },
  },
  data() {
    return {
      baselatitude: 37.43884057982199,
      baselongitude: 126.675102369038,
      local: null,
      map: null,
      bounds: null,
    };
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.$nextTick(function () {
        this.initMap();
      });
    } else {
      const script = document.createElement("script");
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=0feaa40f6d94ad4428a4f4f8a6cab340&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
  },

  methods: {
    ...mapMutations({ SET_LOCAL: "SET_LOCAL", SET_OBSERVE: "SET_OBSERVE" }),
    initMap() {
      this.refreshLocation();
      var container = document.getElementById("map"); //지도를 담을 영역의 DOM 레퍼런스
      var options = {
        //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(
          this.GET_LAT != null ? this.GET_LAT : this.latitude,
          this.GET_LON != null ? this.GET_LAT : this.longitude
        ), //지도의 중심좌표.
        level: 3, //지도의 레벨(확대, 축소 정도)
      };

      this.map = new kakao.maps.Map(container, options); // eslint-disable-line no-unused-vars
      //지도 생성 및 객체 리턴
      const markerPosition = new kakao.maps.LatLng(
        this.latitude,
        this.longitude
      );

      const marker = new kakao.maps.Marker({
        position: markerPosition,
      });

      marker.setMap(this.map);
    },
    refreshLocation() {
      console.log("refreshLocation");
      return navigator.geolocation.getCurrentPosition(
        (position) => {
          this.setLocation(position.coords.latitude, position.coords.longitude);
          console.log("gotoSetLocation");
        },
        (error) => {
          console.log("아니 위치정보받는데 에러가난다고" + error);
        }
      );
    },
    setLocation(latitude, longitude) {
      this.$store.commit("SET_LAT", latitude);
      this.$store.commit("SET_LON", longitude);
      this.$store.commit("SET_OBSERVE", true);

      let geocoder = new kakao.maps.services.Geocoder();

      let coord = new kakao.maps.LatLng(latitude, longitude);

      const addressSearch = (coord) => {
        return new Promise((resolve, reject) => {
          geocoder.coord2Address(
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
        this.local = result[0].address;
        this.$store.commit("SET_LOCAL", result[0].address);
        var coords = new kakao.maps.LatLng(this.latitude, this.longitude);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
          map: this.map,
          position: coords,
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
          content:
            '<div style="width:150px;text-align:center;padding:6px 0;">현재위치</div>',
        });
        infowindow.open(this.map, marker);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        console.log("지도이동");
        this.map.setCenter(coords);
      };
      console.log("오나욧");
      // async
      (async () => {
        try {
          const result = await addressSearch(coord);
          setLocal(result);
          if (this.shopList != null) {
            this.makeMakers(this.shopList);
          }
        } catch (e) {
          console.log(e);
        }
      })();
    },
    makeMakers(shops) {
      let positions = [];

      for (let shop of shops) {
        positions.push({
          title: shop.name,
          latlng: new kakao.maps.LatLng(shop.localy, shop.localx),
        });
      }
      // 마커 이미지의 이미지 주소입니다
      var imageSrc =
        "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

      this.bounds = new kakao.maps.LatLngBounds();
      const myPosition = new kakao.maps.LatLng(this.latitude, this.longitude);
      this.bounds.extend(myPosition);
      for (let i = 0; i < positions.length; i++) {
        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(24, 35);

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
          map: this.map, // 마커를 표시할 지도
          position: positions[i].latlng, // 마커를 표시할 위치
          title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage, // 마커 이미지
        });

        this.bounds.extend(positions[i].latlng);
        // 마커에 표시할 인포윈도우를 생성합니다
        var infowindow = new kakao.maps.InfoWindow({
          content: positions[i].title, // 인포윈도우에 표시할 내용
        });
        (function (marker, infowindow) {
          // 마커에 mouseover 이벤트를 등록하고 마우스 오버 시 인포윈도우를 표시합니다
          kakao.maps.event.addListener(marker, "mouseover", function () {
            infowindow.open(this.map, marker);
          });

          // 마커에 mouseout 이벤트를 등록하고 마우스 아웃 시 인포윈도우를 닫습니다
          kakao.maps.event.addListener(marker, "mouseout", function () {
            infowindow.close();
          });
        })(marker, infowindow);
      }
      this.map.setBounds(this.bounds);
    },
    // 지도이동
  },
};
</script>

<style></style>
