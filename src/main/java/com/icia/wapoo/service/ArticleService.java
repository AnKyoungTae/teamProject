package com.icia.wapoo.service;

import com.icia.wapoo.model.Article;
import com.icia.wapoo.model.Comment;
import com.icia.wapoo.model.ImageFile;
import com.icia.wapoo.model.LoginInfo;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.paging.PagingA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.dao.ArticleDao;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class ArticleService {

	@Autowired
	private final S3Service s3Service = null;
	
	@Autowired
	private ArticleDao articleDao;
	
	//이지미 아마존
	public int registerFile(long articleId, List<MultipartFile> files)
	{
        int count = 0;
        for( MultipartFile file : files) {

            String fileURL = null;
            try {
                fileURL = s3Service.upload(file, "store_"+articleId);
            } catch (IOException e) {
                throw new RuntimeException("S3 업로드중 오류발생!");
            }
            ImageFile ImageFile = new ImageFile();
            ImageFile.setRef_id( (int)articleId);
            ImageFile.setFilesize(file.getSize());
            ImageFile.setFiletype(file.getContentType());
            ImageFile.setName(fileURL);
            ImageFile.setOrgName(file.getName());
            try {
            	articleDao.insertArticleFile(ImageFile);
            }catch(Exception e) {
            	System.out.println("registerFile : "+ e);
            }
            
            count++;
        }
        return count;
    }
	
	
	//이미지 등록
    public int registerStore(ImageFile file){
        System.out.println("registerStore 실행 " );
        
        int count = 0;
        
        try
        {
        	count = articleDao.insertArticleFile(file);				//파일 DB실행함
        }
        catch (Exception e) 
        {
            throw new RuntimeException("registerStore 오류발생!" + e);
        }
        
		
        return count;
        
    }
	
	//게시글 조회
	public Article boardList(long articleId) 
	{
		Article article = null;
		
		try 
		{
			article = articleDao.boardList(articleId);
		}
		catch(Exception e)
		{
			System.out.println();
			System.out.println(e);
		}
		
		System.out.println(article);
		
		
		return article;
	}
	
	//이미지 조회
	public List<ImageFile> imageFileList(long articleId)
	{
		List<ImageFile> articleImageFile = null;
		
		try
		{
			articleImageFile = articleDao.imageFileList(articleId);
		}
		catch(Exception e)
		{
			System.out.println("imageFileList 오류: " + e);
		}
		return articleImageFile;
	}
	
	//조회수 증가
	public int boardHit(long articleId)
	{
		int count = 0;
		
		try 
		{
			count = articleDao.boardHit(articleId);	
		}
		catch(Exception e)
		{
			System.out.println("boardHit 오류 : "+ e);
		}
		
		return count;
	}
	
	//게시글 등록
	public int listInsert(Article article)
	{
		
		int count = 0;
		
		try
		{
			
			count = articleDao.boardInsert(article);
			
			
			if(count > 0)
			{
				System.out.println("데이터 DB 저장");
			}
		}
		catch(Exception e)
		{
			System.out.println("오류: "+ e);
		}
		return count;
	}
	
	//댓글 등록
	public int commentInsert(Map<String, Object> params, long memberId)
	{
		int count = 0;
		
		Comment comment = new Comment();
		
		comment.setBody((String)params.get("title"));;
		
		comment.setArticleId((((Integer) params.get("parantId")).intValue()));;
		
		comment.setWriterId(memberId);
		
		try
		{
			count = articleDao.commentInsert(comment);
		}
		catch(Exception e)
		{
			System.out.println("commentInsert DB오류"+ e);
		}
		
		return count;
	}
	
	//게시글 댓글 수 증가
	public int childrenHit(long articleId)
	{
		int count = 0;
		
		try 
		{
			count = articleDao.childrenHit(articleId);	
			
		}
		catch(Exception e)
		{
			System.out.println("childrenHit 오류 : "+ e);
		}
		
		return count;
	}
	
	
	//게시글 삭제
	public int boardDelete(long articleId)
	{
		int count = 0;
		
		try
		{
			count = articleDao.boardDelete(articleId);
		}
		catch(Exception e)
		{
			System.out.println("boardDelete DB오류"+ e);
		}
			
		if(count > 0)
		{
			//파일등 다른 작업 필요시 여기서 함
		}
		return count;
	}
	
	
	
	//이지미 삭제
	public int imageDelete(int fileId)
	{
		int count = 0;
		
		try
		{
			count = articleDao.imageDelete(fileId);
		}
		catch(Exception e)
		{
			System.out.println("imageDelete DB오류: "+ e);
		}
		
		return count;
	}
	
	//총게시글 수
	public int getBoardListCnt(long boardId, Object search) throws Exception {
		
		int count = 0;
		
		try
		{
			count = articleDao.getBoardListCnt(boardId, search);
		}
		catch(Exception e)
		{
			System.out.println("getBoardListCnt DB오류 : "+ e);
		}
		return count;
	}

	//페이징 번호 맞게 출력
	public List<Article> getBoardList(PagingA paging, long boardId, Object search) throws Exception {

		List<Article> list = null;
		try
		{
			list = articleDao.getBoardList(paging, boardId, search);
		}
		catch(Exception e)
		{
			System.out.println("getBoardList 오류 : " + e);
		
		}
		
		
		return list;
	}
	
	//게시글 업데이트
	public int boardUpadte(Article article)
	{
		int count = 0;
		
		try
		{
			count = articleDao.boardUpadte(article);
		}
		catch(Exception e)
		{
			System.out.println("게시글 업데이트 오류");
			System.out.println(e);
		}
		return count;
	}
	
	
	
	//내글 모두 출력
	public List<Article> myList(long memberId, long boardId)
	{
		List<Article> list = null;
		
		try
		{
			list = articleDao.myList(memberId, boardId);
		}
		catch(Exception e)
		{
			System.out.println("MyList 오류: " + e);
		}
		return list;
	}
	//회원 확인
	public boolean memberVerify(long memberId)
	{
		boolean check = false;
		LoginInfo login = null;
		
		try
		{
			login = articleDao.memberVerify(memberId);
			if(login != null)
			{
				check = true;
			}
		}
		catch(Exception e)
		{
			System.out.println("memberVerify 오류: " + e);
		}
		
		return check;
	}

	//댓글 가져오기
	public Comment getComment(int memberId, int commentId)
	{
		Comment comment = null;
		
		try
		{
			comment = articleDao.getComment(memberId, commentId);
		}
		catch(Exception e)
		{
			System.out.println("getComment 오류: " + e);
		}

		return comment;
	}
	
	//댓글 삭제
	public int deleteComment(int commentId)
	{
		int count = 0;
		
		try
		{
			count = articleDao.deleteComment(commentId);
		}
		catch(Exception e)
		{
			System.out.println("deleteComment 오류: " + e);
		}
		return count;
	}
	
	//댓글수 감소
	public int deleteChildren(int articleId)
	{
		int count = 0;
		
		try
		{
			count = articleDao.deleteChildren(articleId);
		}
		catch(Exception e)
		{
			System.out.println("deleteChildren 오류: " + e);
		}
		
		return count;
	}
	
	//클릭 댓글 모두 가져오기
	public List<Comment> getAllComment(int articleId)
	{
		List<Comment> list = null;
		
		try
		{
			list = articleDao.getAllComment(articleId);
		}
		catch(Exception e)
		{
			System.out.println("getAllComment 오류: " + e);
		}
		return list;
	}
	
	//게시판 신고
	public int reportArticle(int articleId, String suspend)
	{
		int count = 0;
		
		try
		{
			count = articleDao.reportArticle(articleId, suspend);
		}
		catch(Exception e)
		{
			System.out.println("reportArticle 오류: " + e);
		}
		
		return count;
	}
	
	//댓글 신고
		public int reportComment(int commentId, String suspend)
		{
			int count = 0;
			
			try
			{
				count = articleDao.reportComment(commentId, suspend);
			}
			catch(Exception e)
			{
				System.out.println("reportComment 오류: " + e);
			}
			
			return count;
		}
	
	
	
	
	
	
	
	
	
	
	
		
}

