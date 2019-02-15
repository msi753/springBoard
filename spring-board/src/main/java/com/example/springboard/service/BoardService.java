package com.example.springboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springboard.mapper.BoardMapper;
import com.example.springboard.vo.Board;

//트랜잭션 기능, 예외발생 시 나머지도 다 rollback시켜버림	
@Service
@Transactional
public class BoardService {
	
	//Controller와 Mapper 사이에 Service를 두고 거쳐간다(과장님)
	//스프링 빈 형태로 Mapper를 추가
	@Autowired
	private BoardMapper boardMapper;
	
	//게시글 입력
	public int addBoard(Board board) {
		return boardMapper.insertBoard(board);
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

