package com.example.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboard.vo.Board;
@Mapper
public interface BoardMapper {
	//boardNo를 선택해 게시글 상세보기
	//수정할 때, 상세정보 볼 때 두 가지 경우에 사용할 수 있다
	Board selectBoard(int boardNo);
	
	//게시판 목록
	//map = vo, 매개변수 두 개를 한꺼번에 쓸 수 없다
	//int의 wrapper or box 타입인 Integer를 써준다
	List<Board> selectBoardList(Map<String, Integer> map);
	
	//게시글 수 세기
	int selectBoardCount();
	
	//insert update delete는 리턴타입이 int로 기본적으로 정해져있다
	//board타입을 매개변수로 갖는 삽입 수정 삭제 메서드
	//글 입력 처리
	int insertBoard(Board board); 
	
	//글 수정 처리
	int updateBoard(Board board);
	
	//글 삭제 처리
	int deleteBoard(Board board);
}

