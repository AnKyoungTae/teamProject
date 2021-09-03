package com.icia.wapoo.controller;


import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.jwt.service.JwtService;

import com.icia.wapoo.model.Article;
import com.icia.wapoo.model.Comment;
import com.icia.wapoo.model.ImageFile;
import com.icia.wapoo.paging.PagingA;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icia.wapoo.service.ArticleService;
import com.icia.wapoo.service.MemberService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

	@Autowired
    private ArticleService articleService;
	
	//JWT 값 받기
	@Autowired
    private JwtService jwtService;
    @Autowired
    private MemberService memberService;
    
    @Autowired
	private final S3Service s3Service = null;
    
    //멤버 아이디 
    private long getMemberIdByRequest(HttpServletRequest request) {
        System.out.println("받은 토큰으로 멤버를 검색합니다");
        long memberId = 0;
        try
		{
			//JWT 토큰값
			String JWT = jwtService.resolveToken(request);
			System.out.println(JWT);
			
			Map<String, Object> token = jwtService.getUserInfo(JWT);
			// token에서 memberId 값 가져오기
			 memberId = (long)((Integer) token.get("memberId")).intValue();
		}
		catch(Exception e)
		{
			System.out.println("memberId 없음");
		}
        return memberId;
    }

	/**
	 * 작성자 : 김건우
	 * @param boardId 1: 공지사항, 2: ... 여기에 값을 넣으면 그 카테고리에있는 게시물을 다 가져옴
	 * @return
	 */
    //본인 확인
    @PostMapping("/articleVerify")
    public ResponseEntity  verify(@RequestBody Map<String, Object> writeForm,HttpServletRequest request)
    {
    	System.out.println("본인 확인");
		
		long memberId = getMemberIdByRequest(request);
		
		
		Map<String, Object> params = (Map<String, Object>) writeForm.get("params");
		System.out.println(params);
		
		Article article = null;
		
		long articleId = (((Integer) params.get("articleId")).intValue());
		article = articleService.boardList(articleId);
		System.out.println(article);
		System.out.println("memberId : " + memberId);
		
		if(article != null)
		{
			if(!article.getStatus().equals("N"))
			{
				System.out.println("글 확인 가능");
				return new ResponseEntity("100", HttpStatus.OK);
			}
			else
			{
				if(article.getWriterId() == memberId)
				{
					System.out.println("비공개 글 확인 가능");
					return new ResponseEntity("100", HttpStatus.OK);
				}
				else
				{
					System.out.println("글 확인 불가");
					return new ResponseEntity("250", HttpStatus.OK);
				}
			}
		}
		else
		{
			System.out.println("글 없음");
			return new ResponseEntity("400", HttpStatus.OK);
		}
		
    	
    	
    }
    
	
	//글 업로드
	@PostMapping("/board/writeProc")
	public ResponseEntity writeProc( @RequestBody Map<String, Object> writeForm, HttpServletRequest request)
	{
		System.out.println("글작성.");
		long memberId = getMemberIdByRequest(request);
		
		if(memberId > 0)
		{
			System.out.println("회원이다.");
			
			Map<String, Object> writeData = (Map<String, Object>) writeForm.get("params");
			System.out.println("writeData : " + writeData);
			
			int count = 0;
			
			Article article = new Article();
			
			
			try
			{
				System.out.println("회원이다.:"+ Integer.parseInt((String) (writeData.get("parantId"))));
				
				article.setParantId(Integer.parseInt((String) (writeData.get("parantId"))));
				System.out.println("답글 작성");
			}
			catch(Exception e)
			{
				article.setParantId(0);
				System.out.println("게시글 작성");
			}
			
			article.setTitle((String)writeData.get("title"));
			
			article.setBody((String)writeData.get("body"));
			
			article.setStatus((String)writeData.get("status"));
			
			article.setBoardId( (((Integer) writeData.get("boardId")).intValue()));
			
			article.setWriterId(memberId);
			
			count = articleService.listInsert(article);
			
			if(count > 0)
			{
				return new ResponseEntity(article.getArticleId(), HttpStatus.OK);
			}
		}
		else
		{
			return new ResponseEntity("no", HttpStatus.OK);
		}
		//글 업로드 실패
		return new ResponseEntity("0", HttpStatus.OK);
	}
	
	//이미지 업로드  
		@PostMapping("/board/imageUpload")
	public ResponseEntity imageUpload( long articleId,@RequestPart(value="image", required = false) List<MultipartFile> image, HttpServletRequest request)
		{
			System.out.println("글 업로드");

			long memberId = this.getMemberIdByRequest(request);
			
			System.out.println("memberId : " + memberId);
			
			if(articleId > 0)
			{
				Article article = null;
				
				article = articleService.boardList(articleId);
				
				if(article != null)
				{
					
					if(articleService.registerFile(articleId, image) == image.size())
					{
						return new ResponseEntity("100", HttpStatus.OK);
					}
					else
					{
						return new ResponseEntity("300", HttpStatus.OK);
					}
				}
				else
				{
					return new ResponseEntity("400", HttpStatus.OK);
				}
			}
			else
			{
				return new ResponseEntity("400", HttpStatus.OK);
			}
		}

	//댓글 업로드
	@PostMapping("/board/commentProc")
	public ResponseEntity commentWrite(@RequestBody Map<String, Object> writeForm,HttpServletRequest request)
	{
		System.out.println("댓글 업로드");
		
		long memberId = getMemberIdByRequest(request);
		
		System.out.println("memberId : " + memberId);
		
		if(memberId > 0)//회원 확인
		{
			System.out.println("회원이다.");
			
			Map<String, Object> params = (Map<String, Object>) writeForm.get("params");
			System.out.println(params);
			
			long articleId = (long)(((Integer) params.get("parantId")).intValue());
			
			if(articleService.commentInsert(params, memberId) > 0)
			{
				//childrenHit
				if(articleService.childrenHit(articleId) > 0)
				{
					System.out.println("글작성 성공 \n");
					return new ResponseEntity("100", HttpStatus.OK);
				}
				else
				{
					System.out.println("DB 오류 \n");
					return new ResponseEntity("300: ", HttpStatus.OK);
				}
			}
			else
			{
				System.out.println("글작성 실패 \n");
				return new ResponseEntity("500", HttpStatus.OK);
			}
		}
		else
		{
			System.out.println("비회원 실패 \n");
			return new ResponseEntity("200", HttpStatus.OK);
		}
		
		
	}
	
	//게시글 죄회  
	@GetMapping("/board/list")
	public Map<String, Object> boardList(long articleId, HttpServletRequest request)
	{
		System.out.println("게시글 조회");
		//조회 값 없으면
		long memberId = 0;
		
		try
		{
			//JWT 토큰값
			String JWT = jwtService.resolveToken(request);
			System.out.println(JWT);
			
			Map<String, Object> token = jwtService.getUserInfo(JWT);
			// token에서 memberId 값 가져오기
			 memberId = (long)((Integer) token.get("memberId")).intValue();
		}
		catch(Exception e)
		{
			System.out.println("memberId 없음");
		}
		
		System.out.println("memberId : " + memberId);
		
		
		Map<String, Object> map = new HashMap<>();//여기 담아서 보냄
		boolean MYPAGE = false; 				  //작성자 여부
		List<Comment> list = null;				  //댓글 여기에 보냄		
		Article article = null;
		//파일
		List<ImageFile> articleImageFile = null;
		
		if(articleId > 0)
		{
			article = articleService.boardList(articleId);//게시글
			list = articleService.getAllComment((int)article.getArticleId());//댓글
			System.out.println("list[0] : " + list);
			//파일
			
			articleImageFile = articleService.imageFileList(articleId);
			
			if(articleImageFile != null)
			{
				System.out.println("이미지 있음");
			}
			else
			{
				System.out.println("이미지 없음");
			}

			if(articleService.boardHit(articleId) > 0)//조회수 증가
			{
				System.out.println("조회수 증가");
			}
			
			if(article != null && memberId != article.getWriterId())//글 존재 여부, 작성자여부 확인
			{	
				System.out.println("작성자 아님");
			}
			else
			{
				MYPAGE = true;//작성자여부 true
				
				System.out.println("조회수 뻥튀기 안됌");
			}	
		}
		
		map.put("MYPAGE", MYPAGE);
		map.put("article", article);
		map.put("list", list);
		map.put("articleImageFile", articleImageFile);
		
		
		return map;
	}
	
	
	//페이지 삭제
	@PostMapping("/board/delete")
	public ResponseEntity boardDelete(@RequestBody Map<String, Object> ListData, HttpServletRequest request)
	{
		System.out.println("페이지 삭제");
		
		//조큰 값 없으면
		long memberId = getMemberIdByRequest(request);

		Map<String, Object> params = (Map<String, Object>) ListData.get("params");
		System.out.println(params);
		
		long writerId = (long)(((Integer) params.get("writerId")).intValue());
		
		long articleId = Integer.parseInt((String) (params.get("articleId")));
		
		if(memberId != 0)
		{
			if(writerId == memberId)
			{
				Article article = null;
				
				article = articleService.boardList(articleId);
				
				if(article != null)//글 존재 여부
				{
					System.out.println("articleId : " +articleId +" 글 있다.");
						
					if(articleService.boardDelete(articleId) > 0)
					{
						System.out.println("글삭제: 성공 \n");
						return new ResponseEntity("ok", HttpStatus.OK);
					}
					else
					{
						System.out.println("글삭제: DB 오류 \n");
						return new ResponseEntity("no", HttpStatus.OK);
					}
				
				}
				else
				{
					System.out.println("글삭제 없음 \n");
					return new ResponseEntity("400", HttpStatus.OK);
				}
			}
			else
			{
				System.out.println("작성자 아님 \n");
				return new ResponseEntity("250", HttpStatus.OK);
			}
		}
		else
		{
			System.out.println("비회원 \n");
			return new ResponseEntity("200", HttpStatus.OK);
		}
		
	}
	
	//댓글 삭제
	@PostMapping("/board/deleteComment") //@RequestBody Map<String, Object> params int articleId int commentId
	public ResponseEntity commentDelete(@RequestBody Map<String, Object> params, HttpServletRequest request)
	{
		System.out.println("댓글 삭제");
		System.out.println("params: " + params);
		
		Map<String, Object> data = (Map<String, Object>) params.get("params");
		System.out.println("data: " + data);
		int commentId = (((Integer) data.get("commentId")).intValue());
		int articleId = (((Integer) data.get("articleId")).intValue());
		System.out.println("commentId: " + commentId);
		System.out.println("articleId: " + articleId);
		
		
		int memberId = (int)getMemberIdByRequest(request);
		
		if(memberId > 0)
		{
			Comment comment = articleService.getComment(memberId, commentId);
			
			if(comment != null)
			{
				if(articleService.deleteComment(commentId)> 0)
				{
					System.out.println("댓글 삭제 성공");
					if(articleService.deleteChildren(articleId)> 0)
					{
						System.out.println("댓글 감소 성공");
						return new ResponseEntity("ok", HttpStatus.OK);
					}
				}
			}
		}
		
		
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	
	//게시글 수정
	@PostMapping("/board/update")
	public ResponseEntity boardUpdate(@RequestBody Map<String, Object> ListData, HttpServletRequest request)
	{
		System.out.println("개시글 수정");
		long memberId = getMemberIdByRequest(request);

		//업데이트 글 정보
		Map<String, Object> params = (Map<String, Object>) ListData.get("params");
		System.out.println(params);
	
		String title = (String)params.get("title");
		String body = (String)params.get("body");
		
		ArrayList<Integer> fileIds = (ArrayList<Integer>) params.get("imageDelete");
		
		long articleId = (long) ((Integer) params.get("articleId")).intValue();
		long writerId = (long) ((Integer) params.get("writerId")).intValue();
		
		System.out.println("articleId : " +articleId);
		
		Article article = new Article();
		
		if(memberId != 0)
		{
			if(articleId > 0)
			{
				if(memberId == writerId)
				{
					article.setTitle(title);
					article.setBody(body);
					article.setArticleId(articleId);
			
					if(articleService.boardUpadte(article) > 0)
					{
						if(fileIds.size() > 0)
						{
							for(int fileId : fileIds)
							{
								System.out.println(fileId);
								articleService.imageDelete( (int)fileId);
							}
							System.out.println("이미지 성공");
							return new ResponseEntity(articleId, HttpStatus.OK);
							
						}
						else
						{
							System.out.println("이미지 파일 없음");
							return new ResponseEntity(articleId, HttpStatus.OK);
						}
					}
					else
					{
						System.out.println("글 수정 성공");
						return new ResponseEntity("ok", HttpStatus.OK);
					}
				}
			}	
		}
		else
		{
			System.out.println("비회원");
			return new ResponseEntity(0, HttpStatus.OK);
		}
		System.out.println("비회원");
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	
	//페이징처리 리스트 
	@GetMapping(value="/pagingBoard")
	public Map<String, Object> getBoardList(@RequestParam(required = false, defaultValue = "1") int page,  //현재 페이지
										    @RequestParam(required = false, defaultValue = "1") int range,		//버튼 prev,next클릭시 페이지 위치
										    @RequestParam(required = false, defaultValue = "10") int listSize,		//리스트 수
										    @RequestParam(required = false, defaultValue = "5") int rangeSize,	//페이지 수
										    @RequestParam long boardId,
										    @RequestParam(required = false) String search
										    
										    
										    ) throws Exception
	{	
		System.out.println("페이징 처리중");
		
		System.out.println("search: " + search);
		
		System.out.println("page: " + page);
		System.out.println("range: " + range);
		System.out.println("listSize: " + listSize);
		System.out.println("rangeSize: " + rangeSize);
		
		
		
		Map<String, Object> map = new HashMap<>();
		
		//전체 게시글 개수
		int total = articleService.getBoardListCnt(boardId, search);
		
		
		System.out.println("total: " + total);

	    //Pagination 객체생성
		PagingA paging = new PagingA();

		paging.pageInfo(page, range,listSize,rangeSize, total);
		//페이지 리스트 수가 rangeSize보다 작을 때
		if(range == 1 && paging.getTotalpage() <= paging.getEndPage())
		{
			
			paging.setEndPage(paging.getTotalpage());
			
			range = 1;
			
			paging.setNext(false);
			paging.setPrev(false);
		}
		
		
		List<Article>  list =  articleService.getBoardList(paging, boardId, search);
		
		map.put("paging", paging);
		map.put("list", list);
		map.put("search", search);
		
		
		
		return map;
	}
	
	//내글 보기
	@GetMapping(value="/pagingMyBoard")
	public Map<String, Object> getMyList(@RequestParam long boardId, HttpServletRequest request)
	{
		System.out.println("내글 보기");
		
		long memberId = getMemberIdByRequest(request);
		
		Map<String, Object> map = new HashMap<>();
		
		List<Article> list = articleService.myList(memberId, boardId);
		
		map.put("list", list);
		
		
		return map;
	}
	
	//글쓰기 고객확인
	@PostMapping("/memberCheck")
	public ResponseEntity memberVerify( HttpServletRequest request)
	{
		System.out.println("글 쓰기 본인 확인");
		boolean check = false;
		
		long memberId = getMemberIdByRequest(request);
		System.out.println("memberId : " + memberId);
		
		if(memberId >= 0)
		{
			
			check = articleService.memberVerify(memberId);
			
			if(check)
			{
				
				return new ResponseEntity(check, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity(check, HttpStatus.OK);
			}
			
		}
		else
		{
			
			return new ResponseEntity(check, HttpStatus.OK);
		}
		
		
	}
	
	//신고
	@GetMapping("/reportArticle")
	public ResponseEntity reportArticle(@RequestParam(required = false, defaultValue = "0") int articleId,
										@RequestParam(required = false, defaultValue = "0") int commentId,
										@RequestParam(required = false, defaultValue = "no") String suspend, HttpServletRequest request)
	{
		long memberId = getMemberIdByRequest(request);
		
		System.out.println("body: " + suspend);
		
		
		
		
		
		if(memberId == 0 || suspend == "no")
		{
			return new ResponseEntity("noMember", HttpStatus.OK);
		}
		
		System.out.println("신고하기");
		if(articleId > 0)
		{
			System.out.println("게시판 신고");
			
			if(articleService.reportArticle(articleId, suspend) > 0)
			{
				return new ResponseEntity("ok", HttpStatus.OK);
			}
			
		}
		else if(commentId > 0)
		{
			System.out.println("댓글 신고");
			
			if(articleService.reportComment(commentId, suspend) > 0)
			{
				return new ResponseEntity("ok", HttpStatus.OK);
			}
		}
		
		
		
		
		
		
		return new ResponseEntity("no", HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


    
    
    
    
    

