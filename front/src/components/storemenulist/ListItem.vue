<template>
  <ul v-show="list" class="list-item" @click="SET_MODAL_ORDER(true)">
    <div class="item-box">
      <div class="item-text" style="margin-left: 20px;">
        <div class="menu-name">
          <!--가게 음식메뉴 이름-->
          {{ list.name }}
        </div>
        <div class="menu-price" style="text-align: left;">{{ list.price }}원</div>
      </div>
      <div class="item-img">
        <img :src="list.fileUrl" class="item-img" />
      </div>
    </div>
  </ul>
</template>

<script>
import { mapMutations } from "vuex";
export default {
  props: ["list"],
  methods: { ...mapMutations(["SET_MODAL_ORDER"]) },
  components: {},
};
</script>

<style scoped>
.list-item {
  list-style: none;
}
.list-item .sub-items {
  padding: 10px;
  text-indent: 20px;
  color: #fefefe;
  background-color: #333;
}

.expand-enter-active,
expand-leave-active {
  transition: height 0.5s ease-in-out;
  overflow: hidden;
}

.item-box {
  width: 100%;
  height: 100px;
  background-color: white;
  border-top: 1px solid whitesmoke;
}
.item-text {
  float: left;
  padding-top: 20px;
  padding-bottom: 10px;
  margin-left: 10px;
}
.item-img {
  float: right;
  width: 90px;
  height: 90px;
  margin-top: 4px;
  margin-right: 10px;
}

.menu-name {
  font-size: 22px;
  font-weight: bold;
}
.menu-price {
  margin-top: 5px;
}
</style>
