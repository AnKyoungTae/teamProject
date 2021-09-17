<template>
  <Modal>
    <template v-slot:header>
      <div class="foodNameWrapper">
        <div class="foodName">
          {{ data.name }}
        </div>
      </div>
      <span class="badge bg-success m-2" v-if="data.status == 1">판매중</span>
      <span class="badge bg-warning m-2" v-else-if="data.status == 2"
        >품절</span
      >
      <span class="badge bg-danger m-2" v-else>판매중지</span>
    </template>
    <template v-slot:body>
      <!-- alt 에 이미지 없을때 비상용 이미지 추가? -->
      <div v-if="Desc">
        <div>
          <img :src="data.fileUrl" class="foodImage m-2" />
        </div>

        <hr />
        <div class="DescDiv">
          <span class="foodDesc">
            {{ data.description }}
          </span>
        </div>
        <div @click="moreDetails" v-if="divHidden">
          <a class="foodDesc2">더보기</a>
        </div>
      </div>
      <div v-else>
        <div @click="detailBoolean" style="text-align: left">
          <i class="fas fa-arrow-left"></i>
        </div>
        <FoodDesc
          v-bind:data="data.description"
          style="margin-top: 5px"
        ></FoodDesc>
      </div>
      <hr />
      <span class="foodPrice">{{ data.price }} 원</span>
    </template>
    <template v-slot:footer>
      <div class="btn btn-success" @click="addToCart(data)">
        장바구니에 추가하기
      </div>
      <div class="btn btn-danger" @click="SET_MODAL_ORDER(false)">취소</div>
    </template>
  </Modal>
</template>

<script>
import Modal from "./Modal_order.vue";
import { mapMutations, mapGetters } from "vuex";
import FoodDesc from "./FoodDesc.vue";

export default {
  props: ["data"],
  components: {
    Modal,
    FoodDesc,
  },
  data() {
    return {
      counter: 1,
      Desc: true,
      divHidden: false,
    };
  },
  mounted() {
    this.divheight();
  },
  computed: {
    ...mapGetters({ checkCart: "checkCart" }),
  },
  methods: {
    divheight() {
      var textLength = this.data.description.length;
      if (textLength >= 72) {
        this.divHidden = true;
      }
    },
    detailBoolean() {
      console.log("실행1");
      this.Desc = true;
    },
    moreDetails() {
      console.log("실행");
      if (this.Desc === true) {
        this.Desc = false;
      }
    },
    ...mapMutations({
      SET_MODAL_ORDER: "SET_MODAL_ORDER",
      addCart: "addCart",
    }),
    check() {
      console.log(this.checkCart);
    },
    addToCart(food) {
      if (food.status != 1) {
        alert("정상 판매중이 아닌 상품은 장바구니에 담을 수 없습니다");
        return;
      }
      this.addCart(food.foodId);
      this.SET_MODAL_ORDER(false);
    },
  },
};
</script>

<style scoped>
.foodNameWrapper {
  display: flex;
  justify-content: center;
  width: 100%;
}

.foodName {
  font-size: 30px;
}
.foodImage {
  width: 60%;
}
.foodPrice {
  font-size: 16px;
  padding: 10px 10px;
  margin: 10px;
}
.foodDesc {
  font-size: 12px;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-height: 72px;
}

.foodDesc2 {
  font-size: 12px;
  color: black;
  cursor: pointer;
}
</style>
