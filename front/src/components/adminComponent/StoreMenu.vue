<template>
  <ul class="wrapper">
    <li v-for="food in foodList" :key="food">
      <ListItem :list="food" @click="popOrderModal(food)"></ListItem>
    </li>
  </ul>
  <order-modal
    :data="orderModalData"
    v-if="this.$store.state.orderModal == true"
  ></order-modal>
</template>

<script>
import ListItem from "../storemenulist/ListItem.vue";
import OrderModal from "@/components/modal/Order.vue";

import { mapMutations } from "vuex";
export default {
  props: ["foodList", "searchId"],
  components: {
    ListItem,
    OrderModal,
  },
  mounted() {
    if (this.searchId) {
      let searched = this.foodList.filter((food) => {
        return food.foodId == this.searchId;
      });
      this.orderModalData = searched[0];
      this.SET_MODAL_ORDER(true);
    }
  },
  data() {
    return {
      orderModalData: null,
    };
  },
  methods: {
    ...mapMutations(["SET_MODAL_ORDER"]),
    popOrderModal(food) {
      this.orderModalData = food;
      this.SET_MODAL_ORDER(true);
    },
  },
};
</script>

<style scoped>
.wrapper {
  list-style: none;
  background-color: lightgray;
  width: 100%;
  height: 100%;
}

ul {
  list-style: none;
  padding-left: 0px;
}
</style>
