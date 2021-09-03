package com.icia.wapoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.icia.wapoo.model.Article;
import com.icia.wapoo.model.Comment;
import com.icia.wapoo.model.ImageFile;
import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.paging.PagingA;

import java.util.List;

@Repository
@Mapper
public interface ArticleDao {

	//파일 등록
	public int insertArticleFile(ImageFile file);
	
	public int boardInsert(Article article);
	//댓글 등록
	public int commentInsert(Comment comment);
	//게시물 댓글수 증가
	public int childrenHit(long articleId);
	// 게시글 조회
	Article boardSelect(long articleId);
	// 게시글 이미지 조회
	List<ImageFile> imageFileList(long articleId);
	//게시글 삭제
	public int boardDelete(long articleId);
	//이미지 삭제
	int imageDelete(int fileId);
	

	//게시글 view
	Article boardList(@Param("articleId") long articleId);
	//조회수 증가
	int boardHit(@Param("articleId") long articleId);
	
	//총 게시글 개수 확인
	int getBoardListCnt(long boardId, Object search) throws Exception;
	// 페이징 개수
	public List<Article> getBoardList(PagingA paging, long boardId, Object search) throws Exception;
	//게시글 수정
	public int boardUpadte(Article article);
	
	//내글 모두 보기
	public List<Article> myList(long memberId, long boardId);
	
	//member 확인
	LoginInfo memberVerify(long memberId);
	
	//댓글 가져오기
	Comment getComment(int memberId, int commentId);
	
	//댓글 삭제
	int deleteComment(int commentId);
	//댓글 수 삭제
	int deleteChildren(int articleId);
	
	//클릭시 모든 댓글 가져오기
	List<Comment> getAllComment(int articleId);

	//게시글 신고
	int reportArticle(int articleId, String suspend);
	//댓글 신고
	int reportComment(int commentId, String suspend);
	
}
