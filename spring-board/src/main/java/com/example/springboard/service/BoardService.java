package com.example.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboard.mapper.BoardFileMapper;
import com.example.springboard.mapper.BoardMapper;
import com.example.springboard.vo.Board;
import com.example.springboard.vo.BoardRequest;
import com.example.springboard.vo.Boardfile;

//트랜잭션 기능, 예외발생 시 나머지도 다 rollback시켜버림	
//파일을 입력받을 때 DB에 입력할 때와 하드웨어에 저장할 때가 있는데 
//둘 중에 하나가 실패하면 파일을 삭제하기 위해 사용
@Service
@Transactional
public class BoardService {
	
	//Controller와 Mapper 사이에 Service를 두고 거쳐간다(과장님)
	//스프링 빈 형태로 Mapper를 추가
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardFileMapper boardFileMapper;
	
	//게시글 입력
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
	}
	
	/*
	 * 게시글 입력 
	 * board안에 있는 fileList를 분해해 DB에 들어갈 수 있는 형태로 만든다(Service)
	 */
	public void addBoard(BoardRequest boardRequest, String path) {				
		/*
		 * 1. BaordRequest를 분리시킨다: Board(vo), file(진짜 파일), boardfile(파일 정보)
		 * board->boardVo 
		 * file정보->boardFileVo 
		 * file->물리적 장치에 저장(경로 필요)
		 * 	호스팅서버를 빌리면 (클라우드) (tomcat환경과 mysql환경) 파일경로가 동적이다. 
		 * 	request를 쓰면 톰캣이 설치된  home directory webapp아래
		 */
		//1. 파일 분리 board
		Board board = new Board();		
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardTitle(boardRequest.getBoardTitle());
		board.setBoardContent(boardRequest.getBoardContent());
		board.setBoardUser(boardRequest.getBoardUser());
		
		//2. 파일 입력 후 방금 입력한 결과인 boardNo를 리턴받는 쿼리를 쓴다(insert할 때 db가 잠금상태가 된다)
		//insert와 select를 분리해서 호출할 경우, 동시 사용자가 있을 경우 select를 할 수 없기 때문이다
		boardMapper.insertBoard(board);
		System.out.println(board.getBoardNo()+"<-입력 후 BoardNo");
		
		//3. 파일분리 boardFile
		List<MultipartFile> files = boardRequest.getFiles();
		for(MultipartFile f : files) {
			//f -> boardfile
			Boardfile boardfile = new Boardfile();
			boardfile.setBoardNo(board.getBoardNo());	//방금 입력했던 boardNo
			boardfile.setFileType(f.getContentType());
			boardfile.setFileSize(f.getSize());
						
			String originalFilename = f.getOriginalFilename();
			int i = originalFilename.lastIndexOf(".");	//0부터 시작
			String ext = originalFilename.substring(i+1);	//.다음부터 글자를 자른다
			boardfile.setFileExt(ext);
			
			String fileName = UUID.randomUUID().toString();
			//UUID 16진수를 랜덤으로 만들어주는 함수를. 문자열로 바꾼다
			boardfile.setFileName(fileName);
			//전체작업이 롤백되면 파일삭제작업은 직접해야 한다
			
			boardFileMapper.insertBoardFile(boardfile);
			
			//3. 파일저장
			try {
				f.transferTo(new File(path+"/"+fileName+"."+ext));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//빈 파일을 만들고
			}

		/*
		 * boardFileMapper.insertBoard(boardFile);
		 */	
		}
	
	//Service가 필요한 이유(과장님 역할)
	//controller는 service만을 호출할 수 있고, view를 보여준다
	//예를 들어, 1. 정보를 생산하고 2. dao(mapper)를 호출한다
	public Map<String, Object> selectBoardList(int currentPage) {
		//1.
		final int ROW_PER_PAGE = 10;	
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("currentPage", currentPage);
		map.put("rowPerPage", ROW_PER_PAGE);
		//2.
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int)(Math.ceil(boardCount/ROW_PER_PAGE));
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("lastPage", lastPage);
		return returnMap;
	}
	
	
	//게시글 상세보기
	public Board getBoard(int boardNo) {
		return boardMapper.selectBoard(boardNo);
	}
	
	//boardPw와 boardNo를 받아 게시글 수정하기
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);
	}
	
	//게시글 삭제
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);
	}
	
	public int getBoardCount() {
		return boardMapper.selectBoardCount();
	}
	
}

