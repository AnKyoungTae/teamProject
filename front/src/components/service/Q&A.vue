<template>
<div class="container" style="width: 1000px;">
  <div class="notice col-12 h-50">
    <!-- 검색 -->
    <div id="Search" class="input-group mt-3">
      <input
        type="text"
        class="form-control"
        placeholder="검색"
        aria-label="Recipient's username"
        aria-describedby="button-addon2"
        v-model="search"
      />
      <button
        class="btn btn-outline-secondary"
        type="button"
        id="button-addon2"
        @click="searching"
        style="z-index: 0;"
      >
        검색
      </button>
    </div>
    
    <!-- 글쓰기 -->
    <div class="">
      <button type="button" class="btn btn-primary  mb-2 mr-3" @click="myPage">내글조회</button>
      <button class="btn btn-primary left-30 write mb-2 mx-3" @click="memberCheck" 
        >글쓰기</button
      >
    </div>

    <br />
    <!-- 리스트 -->
    <table class="table table-striped">
      <tbody>
        <tr>
          <th>번호</th>
          <th>구분</th>
          <th>제목</th>
          <th>날짜</th>
          <th>조회수</th>
          <th>공개 여부</th>
        </tr>
        <tr
          style="cursor: pointer;"
          v-for="(qn, index) in this.queAn"
          :key="index"
          @click="listPage(qn.articleId)"
        >
          <td class="col-1">{{ ++index }}</td>
          <td class="col-1">주문</td>
          <td class="col-5 text-left">
            <span>
              {{ qn.title }}
            </span>
            <span v-if="qn.children > 0"> [{{ qn.children }}] </span>
          </td>
          <td class="col-2">{{ qn.regDate }}</td>
          <td class="col-1">{{ qn.hit }}</td>
          <td class="col-1" v-if="privateToggle(qn.status)">
            <i class="fas fa-lock-open"></i>
          </td>
          <td class="col-1" v-else>
            <i class="fas fa-lock"></i>
          </td>
        </tr>
        <tr v-if="queAn.length < 0">
          <td colspan="6" style="font-weight: 700; font-size: 2vh">글이 없습니다</td>
        </tr>
      </tbody>
    </table>

      <!-- 순서 버튼 -->
    <nav aria-label="Page navigation example " class="mt-5 mb-5 position-relative .center-block" v-if="myCount">
      <ul class="pagination position-absolute" style="left: 30vw" >
        <!-- 이전 순서 버튼 -->
        <li
          class="page-item"
          v-if="this.paging.prev == true"
          @click="prevBotton(paging.range, paging.rangeSize, this.listSize)"
        >
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <!-- 숫자 순서 버튼 -->
        <li class="page-item" v-for="num in this.pageList" :key="num"  :style="{ontSize: '20px'}">        
          <a class="page-link"  href="#" @click="numPage(num, this.paging.range, this.listSize,this.rangeSize, this.search)" v-if="paging.page != num">{{num}}</a>
          <a class="page-link"  href="#" @click="numPage(num, this.paging.range, this.listSize,this.rangeSize, this.search)" style="background-color: #0d6efd; color: #fff;" v-else >{{num}}</a>
        </li>

        <!-- 다음 순서 버튼 -->
        <li
          class="page-item"
          v-if="this.paging.next == true"
          @click="nextBotton(paging.range, paging.rangeSize, this.listSize)"
        >
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>  
  </div>
</div>
</template>

<script>
import * as authAPI from "@/api/article.js";


export default {
  data() {
    return {
      paging:[],        //페이징
      queAn:[],         //Q&A리스트

      pageList:[],

      listSize: 10,     //리스트 수
      rangeSize: 5,      //버튼 수,

      search: null,     //검색

      myCount: true      //myCount

    }
  },
 

  methods: {
    
    //글쓰기전 본인 확인
    memberCheck() {
      authAPI
      .memberCheck()
      .then(res => {
        console.log(res)
        if(res.data){
          return this.$router.push({name: 'WriteForm'})
          
        }else{
          alert("회원 권한입니다.")
          return this.SET_MODAL_LOGIN(true)
        }
      })
      .catch(err => {
        console.log(err)
      })
    },

    listPage(articleId) {
      //페이지 이동
      let params = {
        articleId: articleId,
      };

      authAPI
        .articleVerify(params) //페이지 검사
        .then((res) => {
          console.log(res);
          if (res.data == 100) {
            return (this.$router.push({name: 'BoardList', query: { "board": "qna", "articleId": articleId}}))
              //location.href ="/boardList?board=qna&articleId=" + articleId);
          } else if (res.data == 250) {
            return alert("비공개 입니다");
          } else if (res.data == 400) {
            return alert("글 없습니다");
          } else {
            return alert("다시 시도해 주세요");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //공개 비공개 검사
    privateToggle(status) {

      if (status === "Y" || status === "S") {
        return true;
      }
    },
    prevBotton(range1, rangeSize, listSize) {
      var page = (range1 - 2) * rangeSize + 1;
      var range = range1 - 1;

      this.numPage(page, range, rangeSize, listSize);
    },
    nextBotton(range1, rangeSize, listSize) {
     
      let page = parseInt((range1 * rangeSize)) + 1
		  let range = range1 + 1
   
      this.numPage(page, range, rangeSize, listSize)
    },

    numPage(page, range, listSize, rangeSize, search=null) {           //페이지 번호로 이동 axios
      authAPI
      .getBoardList(2, page, range, listSize, rangeSize, search)
      .then(res => {
        console.log(res)

        this.search = res.data.search
        this.paging =  res.data.paging
        
        this.queAn = res.data.list
      })
    },
    //검색
    searching() {
      this.numPage(1, this.paging.range, this.listSize,this.rangeSize, this.search)
    },
    // 내글 보기
    myPage() {
      this.search = ''
      this.myCount = false

      authAPI
      .pagingMyBoard(2)
      .then(res => {
        console.log(res)
       
        this.queAn = res.data.list

      })
    },

    //Q&A 리스트 불러오기
    downAllList(boardId, page, range) {
      //리스트 axios 통신 query = boardId  page:페이지  range: 범위  boardId
      authAPI
        .getBoardList(boardId, page, range)
        .then((res) => {
          console.log(res.data)
          //페이징
          this.paging = res.data.paging
         
          //리스트
          this.queAn = res.data.list

          //페이징 리스트
          this.pageLists()
          
        })
        .catch((err) => {
          console.log(err);
        });
    },

    //페이지 리스트
    pageLists() {
      for(let i = this.paging.startPage; i <= this.paging.endPage; i++){
        this.pageList.push(i)
      }
    }
    
  },

  
  
  //고객센터통해 접근할 경우
  async mounted() {
    await this.downAllList(2, 1, 1)
    
    
  },
  //버튼 클릭시
  updated() {
    
  },
 
}
</script>

<style scoped>
.write{
  left: 30vw
}

#Search {
  width: 35%;
  left: 35%;
}
button {
  width: 100px;
  float: right;
}
</style>
