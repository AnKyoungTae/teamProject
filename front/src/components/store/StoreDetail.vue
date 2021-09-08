<template>
  <div class="container storeDetail" style="width:1000px;">
    <br>
    <!-- 업체정보 -->
    <h2 class="store-title">{{storeInfo.name}}</h2>
    <div class="row storeHeader">
      <div class="col" style="width:500px; height:410px;">
        <div>
          <!-- Main slider -->
          <splide :options="primaryOptions" ref="primary" style="margin-bottom:1rem;">
            <splide-slide v-for="file in storeFiles" :key="file">
              <img :src="file.name" class="splideImg" alt="??">
            </splide-slide>
          </splide>

          <!-- Thumbnail slider -->
          <splide :options="secondaryOptions" ref="secondary">
            <splide-slide v-for="file in storeFiles" :key="file">
              <img :src="file.name" alt="??">
            </splide-slide>
          </splide>
        </div>
      </div>  
      <div class="col" style="width:500px; height:410px;">
        <p style="color: gray; margin-bottom: 20px; padding-right:20px; text-align: center">
          {{ storeInfo.storeKind }}
        </p>
        <span style="float: left; padding-left:20px; padding-bottom:10px; font-weight: 600; font-size: 17px">
          업체정보
        </span>
        <hr style="margin-top: 5px; width: 90%" />
        <div>
          <table class="store-list">
            <tr>
              <th class="store-th">가게등록일</th>
              <td class="store-td">{{storeInfo.regDate}}</td>
            </tr>
            <tr>
              <th class="store-th">가게주소</th>
              <td class="store-td">{{storeInfo.address}}</td>
            </tr>
            <tr>
              <th class="store-th">상세주소</th>
              <td class="store-td">{{storeInfo.addressDetail}}</td>
            </tr>
            <tr>
              <th class="store-th">가게번호</th>
              <td class="store-td">{{ storeInfo.phone }}</td>
            </tr>
            <tr>
              <th class="store-th">가게평점</th>
              <td class="store-td">
                <Reviewicon :averageScore="this.averageScore"></Reviewicon>
              </td>
            </tr>
            <tr>
              <th class="store-th">가게설명:</th>
              <td class="store-td">{{ storeInfo.body }}</td>
            </tr>
          </table>
        </div>
        <br>
        <button type="button" @Click="edit()" class="btn btn-dark edit">수정</button>
      </div>
      <div
        class="btn-group"
        role="group"
        aria-label="Basic radio toggle button group"
        style="padding: 0px; width: 1000px; height: 45px; background-color: #fafafa"
      >
        <input
          type="radio"
          class="btn-check"
          name="btnradio"
          id="btnradio1"
          autocomplete="off"
          @click="setComponent('shopMap')"
          checked
        />
        <label class="btn btn-outline-primary" for="btnradio1">지도보기</label>

        <input
          type="radio"
          class="btn-check"
          name="btnradio"
          id="btnradio2"
          autocomplete="off"
          @click="setComponent('review')"
        />
        <label class="btn btn-outline-primary" for="btnradio2">후기</label>
      </div>
      <shopMap
        :localx="storeInfo.localx"
        :localy="storeInfo.localy"
        :shopname="storeInfo.name"
        v-if="currentComp === 'shopMap'"
      ></shopMap>
      <review v-else-if="currentComp === 'review'" :storeId="storeId"></review>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import Reviewicon from "@/components/shop/Reviewicon.vue";
import shopMap from "@/components/adminComponent/StoreMap.vue";
import review from "@/components/adminComponent/Review.vue";
import { Splide, SplideSlide } from '@splidejs/vue-splide';
import '@splidejs/splide/dist/css/themes/splide-default.min.css';

export default {
  props: ["storeInfo", "storeFiles", "isMyStore"],
  // isMyStore == true 일때 자신의 가게임.
  components: {
    Splide,
    SplideSlide,
    Reviewicon,
    shopMap,
    review, 
  },
  data() {
			return {
        averageScore: null,
        currentComp: "shopMap",
        storeId: null,
        primaryOptions: {
	      	type      : 'loop',
		      width     : 500,
          height    : 300,
					pagination: false,
          arrows    : false,
	      },
	      secondaryOptions: {
		      type        : 'slide',
		      rewind      : true,
		      width       : 500,
		      gap         : '1rem',
					pagination  : false,
		      fixedWidth  : 110,
          fixedHeight : 70,
          cover       : true,
		      focus       : 'center',
		      isNavigation: true,
	      },
			};
	},
  mounted() {
    // 슬라이드
    this.storeId = this.$route.query.storeInfo;
    this.$refs.primary.sync( this.$refs.secondary.splide );
    this.setComponent("shopMap");
    http
      .get("/review/getAverageScore", {
        params: {
          storeId: this.storeInfo.storeId,
        },
      })
      .then((response) => {
        this.averageScore = response.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    //수정버튼
    edit() {
      this.$emit("trueEdit", false)
    },
    setComponent(comp) {
      this.currentComp = comp;
    },
  }
};
</script>

<style scoped>
.storeDetail {
  background: rgba(234, 240, 237, 0.466);
}
.store-td{
  padding-left: 50px;
}
.store-list{
  margin: 20px;
  font-size: 20px;
  align-content: center;
  text-align: left;
}
div img{
  width: 100%;
}
.store-title{
  margin: 20px;
}
.storeHeader {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
}
.splideImg{
  width:500px; 
  height:300px;
}
input {
  background-color: gray;
}
.btn-group {
  background-color: white;
}
.btn-outline-primary {
  border-radius: 0px;
}
/* (메뉴, 지도보기, 후기)버튼 클릭했을 때 */
.btn-check:active + .btn-outline-primary,
.btn-check:checked + .btn-outline-primary,
.btn-outline-primary.active,
.btn-outline-primary.dropdown-toggle.show,
.btn-outline-primary:active {
  color: rgb(255, 194, 115);
  background-color: white;
  border-color: lightgray;
  font-weight: bold;
  border-bottom: 5px solid rgb(255, 205, 139);
  box-shadow: none;
  z-index: 0;
}
.btn-outline-primary {
  border-color: lightgray;
  color: black;
}
/* 마우스커서가 올라갔을 때 */
.btn-outline-primary:hover {
  color: black;
  background-color: white;
  border-color: lightgray;
  border: 2px solid lightgray;
}
.btn-outline-primary {
  padding: 10px;
}
.edit {
  margin:11px;
  margin-right:25px;
}
</style>
