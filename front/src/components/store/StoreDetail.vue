<template>
  <div class="container storeDetail" style="width:1000px;">
    <br>
    <!-- 업체정보 -->
    <div class="row storeHeader">
      <div>
          <div class="store-list">
            <table>
              <tr>
                <td colspan="2" align=center>
                  <h2 class="store-title">{{storeInfo.name}}</h2>
                </td>
                <td rowspan="10" align=center>
                  <button type="button" @Click="edit()" class="btn btn-dark edit">수정하기</button>
                  <div v-if="storeFiles != null" style="width:400px;">
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
                </td>
              </tr>
              <tr>
                <th style="vertical-align: bottom;"><div class="store-th" style="vertical-align: bottom;">업체정보</div></th>
                <td style="vertical-align: bottom;"><div class="store-td" style="vertical-align: bottom; font-size:20px;">{{ storeInfo.storeKind }}</div></td>
              </tr>
              <tr>
                <td colspan="2">
                  <div style="width:500px; padding:0px; border-bottom: 1px solid gray;"></div>
                </td>
              </tr>
              <tr>
                <th style="padding-top:20px;"><div class="store-th">가게등록일</div></th>
                <td style="padding-top:20px;"><div class="store-td">{{storeInfo.regDate}}</div></td>
              </tr>
              <tr>
                <th class="store-th" style="padding: 5px 0px;"></th>
              </tr>
              <tr>
                <th><div class="store-th">가게주소</div></th>
                <td><div class="store-td">{{storeInfo.address}}</div></td>
              </tr>
              <tr>
                <th class="store-th" style="padding: 5px 0px;"></th>
              </tr>
              <tr>
                <th><div class="store-th">상세주소</div></th>
                <td><div class="store-td">{{storeInfo.addressDetail}}</div></td>
              </tr>
              <tr>
                <th class="store-th" style="padding: 5px 0px;"></th>
              </tr>
              <tr>
                <th><div class="store-th">가게번호</div></th>
                <td><div class="store-td">{{ storeInfo.phone }}</div></td>
              </tr>
              <tr>
                <th class="store-th" style="padding: 5px 0px;"></th>
              </tr>
              <tr>
                <th><div class="store-th">가게설명</div></th>
                <td colspan="2"><div class="store-td" style="width:auto;">{{ storeInfo.body }}</div></td>
              </tr>
            </table>
          </div>
        </div>
        
      <!-- 버튼 -->
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
      <!-- 내용 -->
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
    shopMap,
    review, 
  },
  data() {
			return {
        averageScore: null,
        currentComp: "shopMap",
        storeId: null,
        primaryOptions: {
	      	type       : 'loop',
		      width       : 400,
          height      : 250,
					pagination  : false,
	      },
	      secondaryOptions: {
		      type        : 'slide',
		      rewind      : true,
		      width       : 400,
		      gap         : '1rem',
					pagination  : false,
		      fixedWidth  : 110,
          fixedHeight : 70,
          cover       : true,
		      isNavigation: true,
          arrows      : false,
	      },
			};
	},
  mounted() {
    this.storeId = this.storeInfo.storeId;
    this.$refs.primary.sync( this.$refs.secondary.splide );
    this.setComponent("shopMap");
  },
  methods: {
    //수정버튼
    edit() {
      this.$emit("trueEdit", false)
    },
    setComponent(comp) {
      this.currentComp = comp;
    },
  },
};
</script>

<style scoped>
.storeDetail {
  background: rgba(234, 240, 237, 0.466);
}
.store-th {
  width: 120px;
}
.store-td {
  padding-left: 20px;
  padding-right: 50px;
  width: 400px;
  word-break:break-all;
}
.store-list table tr th, 
.store-list table tr td {
  vertical-align: top;
}
.store-list{
  width: 960px;
  font-size: 25px;
  align-content: center;
  text-align: left;
}
.store-title{
  margin: 20px;
}
.splideImg {
  width:400px; 
  height:250px;
}
.storeHeader {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  padding: 10px;
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
  margin:20px;
  font-size: 20px;
}
</style>
