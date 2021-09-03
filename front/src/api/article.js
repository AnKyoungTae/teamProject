import http from "@/api/http";

// notice, Q&A FAQ  리스트
export async function allList(query) {
  return http.get("/article/board?boardId=" + query);
}
//memberCheck
export function memberCheck() {
  return http.post("/article/memberCheck");
}
//게시글 죄회
export function list(query) {
  return http.get("/article/board/list?articleId=" + query);
}
//게시글 등록
export async function writeProc(params) {
  return http.post("/article/board/writeProc", {
    params,
  });
}
//댓글 등록
export async function commentProc(params) {
  return http.post("/article/board/commentProc", {
    params,
  });
}
//게시글 삭제
export async function listDelete(params) {
  return http.post("/article/board/delete", {
    params,
  });
}
//페이징 리스트
export async function getBoardList(boardId, page, range, rangeSize, listSize, search=null){
  return http.get("/article/pagingBoard",{
    params: {
      boardId: boardId,
      page: page,
      range: range,
      listSize: listSize,
      rangeSize: rangeSize,
      search: search
    }
  })
}
//수정 페이지
export function update(params) {
  return http.post("/article/board/update", {
    params,
  });
}

//본인 확인
export async function articleVerify(params) {
  return http.post("/article/articleVerify", {
    params,
  });
}

//이미지 업로드  
export function write12(form, articleId) {
  return http.post("/article/board/imageUpload", form, {
    headers: {'Content-Type': 'multipart/form-data'},
    params: {
      articleId: articleId
    }
  })
}
//작성자가쓴 글 
export async function pagingMyBoard(boardId){
  return http.get("/article/pagingMyBoard",{
    params: {
      boardId: boardId
    }
  })
}

//댓글 삭제  
export function deleteComment(articleId, commentId) {
  return http.post("/article/board/deleteComment",{
    params: {
      articleId: articleId,
      commentId: commentId
    }
  } )
}
//신고하기!!!!!!!!11
//게시판
export function reportArticle(articleId, suspend){
  return http.get("/article/reportArticle", {
    params: {
      articleId: articleId,
      suspend: suspend
    }
  })
}

//댓글
export function reportComment(commentId, suspend){
  return http.get("/article/reportArticle", {
    params: {
      commentId: commentId,
      suspend: suspend
    }
  })
}
