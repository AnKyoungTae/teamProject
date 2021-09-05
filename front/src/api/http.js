/** axios 공통 모듈화
 * 작성자 : 노철희
 * 사용법 :
 *  import http from '@/api/http;
 *  ...
 *  http.post('컨트롤러 매핑주소', { 페이로드 }).then(...).catch(...);
 *  > {페이로드} 는 넘길 객체를 의미합니다.
 */
import axios from "axios";
import JWT from "@/api/jwt";
import store from "@/store";

const instance = axios.create({
  baseURL: "http://localhost:8083", // 스프링 BackEnd 의 주소,
  headers: {
    "content-type": "application/json",
  },
});

instance.interceptors.request.use(
  function (config) {
    try {
      let access_token = JWT.getToken();
      if (access_token != null) {
        config["headers"] = {
          Authorization: access_token,
        };
        const jwt = require("jsonwebtoken");
        const decodeAccessToken = jwt.decode(access_token);
        if (decodeAccessToken.exp < Date.now() / 1000 + 60) {
          console.log("토큰 만료됨");
          store.dispatch("auth/logout");
        }
      } else {
        JWT.destroyToken();
      }
      return config;
    } catch (err) {
      console.log("토큰을 헤더에 싣는데 실패했습니다. :" + err);
    }
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default instance;
