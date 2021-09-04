import { createStore } from "vuex";

import auth from "@/store/auth";
import cart from "@/store/cart";
import createPersistedState from "vuex-persistedstate";
import serviceCenter from "./serviceCenter";

export default createStore({
  state: {
    loading: false, // 로딩 컴포넌트를 위함

    loginModal: false, // 로그인 모달
    registerModal: false, // 레지스터 모달
    mapModal: false, // 맵 모달
    orderModal: false, // 주문 모달
    selectRegister: "BUYER",
    reviewModal: false,

    serviceCenters: 1, //서비스 센터 위치
    serviceCenterToggle: false, //사이드바 클릭

    selectedPlace: "주소찾기를 눌러주세요...", // 주소찾기에서 선택한 주소
    selectedAddressDetail: "", // 주소찾기에서 선택한 상세주소

    local: null,
    latitude: null,
    longitude: null,
    isObserved: false,
  },
  getters: {
    GET_LOCAL(state) {
      return state.local;
    },
    GET_LAT(state) {
      return state.latitude;
    },
    GET_LON(state) {
      return state.longitude;
    },
    GET_OBSERVED(state) {
      return state.isObserved;
    },
  },
  mutations: {
    SET_MODAL_LOGIN(state, boolean) {
      // 로그인 모달 실행
      state.loginModal = boolean;
    },
    SET_MODAL_REGISTER(state, boolean) {
      // boolean fals =>로그인, register 모달 닫기
      state.loginModal = false;
      state.registerModal = boolean; //boolean true => 회원가입 열기
    },
    SET_MODAL_ORDER(state, boolean) {
      state.orderModal = boolean;
      state.loginModal = false;
      state.registerModal = false;
      console.log(state.orderModal);
    },
    SET_SELECT_REGISTER(state, data) {
      state.selectRegister = data;
    },

    SET_MODAL_MAP(state, boolean) {
      state.mapModal = boolean;
    },
    SET_SELECTED_PLACE(state, [place, detail]) {
      state.selectedPlace = place;
      state.selectedAddressDetail = detail;
    },
    SET_LOCAL(state, local) {
      state.local = local;
    },
    SET_LAT(state, latitude) {
      state.latitude = latitude;
    },
    SET_LON(state, longitude) {
      state.longitude = longitude;
    },
    SET_OBSERVE(state, boolean) {
      state.isObserved = boolean;
    },
    SET_serviceCenters(state, payload) {
      //serviceCenter 에서 버튼 위치
      state.serviceCenters = payload;
    },
    SET_serviceCenterToggle(state, payload) {
      //sidebar에서 serviceCenter 클릭시
      state.serviceCenterToggle = payload;
    },
    SET_MODAL_REVIEW(state, boolean) {
      state.reviewModal = boolean;
    },
  },
  modules: {
    auth,
    serviceCenter,
    cart,
  },
  plugins: [
    createPersistedState({
      paths: ["auth", "cart"],
    }),
  ],
});
