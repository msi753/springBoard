package com.example.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springboard.service.BoardService;
import com.example.springboard.vo.Board;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    // 입력페이지 요청
    @GetMapping(value="/boardAdd")
    public String boardAdd() {
        System.out.println("boardAdd 폼 요청");
        return "boardAdd";
    }
    
    // 입력(액션) 요청
    @PostMapping(value="/boardAdd")
    public String boardAdd(Board board) {
        System.out.println("boardAdd 요청");
    	//커맨드 객체 -> 필드=input type name -> setter
        boardService.addBoard(board);
        return "redirect:/boardList"; // 글입력후 "/boardList"로 리다이렉트(재요청)
    }
    
    // 리스트 요청
    @GetMapping(value="/boardList")
    public String boardList(Model model
                            , @RequestParam(value="currentPage", required=false, defaultValue="1") int currentPage) {
        					//필수로 받아야 하는 값이 아니고 디폴트 값이 1이다(int data type으로 리턴)
    	System.out.println("boardList 요청");
    	Map<String, Object> returnMap = boardService.selectBoardList(currentPage);
    	model.addAttribute("list", returnMap.get("list"));
    	model.addAttribute("boardCount", returnMap.get("boardCount"));
    	model.addAttribute("lastPage", returnMap.get("lastPage"));
    	model.addAttribute("currentPage", currentPage);
    	return "boardList";
    }   
    
    // 글 상세 내용 요청 
    @GetMapping(value="/boardView")
    public String boardView(Model model, @RequestParam(value="boardNo") int boardNo) {
        System.out.println("boardView 요청");
    	Board board = boardService.getBoard(boardNo);
        model.addAttribute("board", board);
        return "boardView";
    }
    
    // 글 수정 화면 요청
    @GetMapping(value="/boardModify")
    public String boardModify() {
		return null;
    }
    
    // 글 수정 요청
    @RequestMapping(value="/boardModifyAction", method=RequestMethod.POST)
    public String boardModifyAction(Board board) {
        System.out.println("boardModify 요청");
    	boardService.modifyBoard(board);
    	return "redirect:/boardList";
    }
    
    // 글 삭제 요청
    @RequestMapping(value="/boardRemoveAction", method=RequestMethod.POST)
    public String boardRemoveAction(Board board) {
        System.out.println("boardRemove 액션");
    	boardService.removeBoard(board);
    	return "redirect:/boardList";
    }
    
    // 글 삭제 화면 요청
    @GetMapping(value="/boardRemove")
    public String boardRemove(Model model, @RequestParam(value="boardNo") int boardNo) {
        System.out.println("boardRemove 화면");
        model.addAttribute("boardNo", boardNo);
		return "boardRemove";	
    }
    

}


