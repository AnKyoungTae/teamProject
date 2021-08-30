import jwt from "@/api/jwt";
import http from "@/api/http";
import router from "@/router";

export default {
  namespaced: true,
  state: {
    token: {
      accessToken: jwt.getToken(),
    },
    isAuthenticated: !!jwt.getToken(),
    userRole: "null",
    userNickname: "null",
    userEmail: "null",
    userName: "null",
    userPhone: "null",
    myStore: null,
  },
  getters: {
    getAccessToken: function (state) {
      return state.token.accessToken;
    },
    getUserInfo: function (state) {
      const info = [
        state.isAuthenticated,
        state.userRole,
        state.userNickname,
        state.userPhone,
        state.userEmail,
      ];
      return info;
    },
    getUserRole(state) {
      const jwt = require("jsonwebtoken");
      const decodeAccessToken = jwt.decode(state.token.accessToken);
      if (decodeAccessToken != null) {
        console.log(decodeAccessToken);
        return decodeAccessToken.role;
      }
      return "BUYER";
    },
    getMyStore(state) {
      return state.myStore;
    },
    getUserId(state) {
      const jwt = require("jsonwebtoken");
      const decodeAccessToken = jwt.decode(state.token.accessToken);
      if (decodeAccessToken != null) {
        console.log(decodeAccessToken);
        return decodeAccessToken.memberId;
      }
      return 0;
    },
    getUserNickname(state) {
      const jwt = require("jsonwebtoken");
      const decodeAccessToken = jwt.decode(state.token.accessToken);
      if (decodeAccessToken != null) {
        console.log(decodeAccessToken);
        return decodeAccessToken.nickname;
      }
      return "unknown";
    },
  },
  mutations: {
    setToken(state, payload) {
      // 토큰을 state에 저장하는 함수.
      (state.token.accessToken = payload), //필요한가? 어차피 accessToken은 세션에서 불러오는데
        (state.isAuthenticated = true),
        jwt.saveToken(payload);
    },
    delToken(state) {
      (state.token.accessToken = ""),
        (state.isAuthenticated = false),
        jwt.destroyToken();
    },
    setUserInfo(state, userdata) {
      state.isAuthenticated = true;
      state.userRole = userdata.role;
      state.userNickname = userdata.nickname;
      state.userPhone = userdata.phone;
      state.userEmail = userdata.email;
    },
    delUserInfo(state) {
      state.isAuthenticated = false;
      state.userRole = null;
      state.userNickname = null;
      state.userPhone = null;
      state.userEmail = null;
    },
    SET_MY_STORE(state, store) {
      state.myStore = store;
    },
  },
  actions: {
    login(context, { userId, password }) {
      http
        .post("/api/member/login", {
          loginId: userId,
          password: password,
        })
        .then((res) => {
          if (res.status === 200) {
            // 응답이 200인지 확인
            // 토큰을 저장한다.
            context.commit("setToken", res.headers.authorization);
            // 토큰을 이용해서 유저정보 불러오기
            return Promise.resolve(res);
          }
        })
        .catch((error) => {
          return Promise.reject(error);
        });
    },
    getInfo(context) {
      http.post("/api/member/getInfo").then((res) => {
        console.log("로그인 유지를 위한 정보 요청을 보냅니다");
        context.commit("setUserInfo", res.data);
      });
    },
    logout(context) {
      // 로그아웃시 토큰삭제
      return new Promise((resolve) => {
        setTimeout(function () {
          context.commit("delToken");
          context.commit("delUserInfo");
          console.log("로그아웃 했음.");
          router.push({ path: "/" });
          resolve();
        }, 500); // 0.5초 후 로그아웃됨
      });
    },
  },
};
