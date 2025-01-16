package com.gyojincompany.profile.controller;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gyojincompany.profile.dao.BoardDao;
import com.gyojincompany.profile.dao.MemberDao;
import com.gyojincompany.profile.dto.BoardDto;
import com.gyojincompany.profile.dto.MemberDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@GetMapping(value = "/write")
	public String write(HttpSession session, Model model) {
		
		String sid = (String) session.getAttribute("sessionid");
		
		if(sid != null) { //로그인 상태
			
			model.addAttribute("bid", sid);
			
			MemberDao mDao = sqlSession.getMapper(MemberDao.class);
			MemberDto mDto = mDao.memberInfoDao(sid);
			model.addAttribute("bname", mDto.getMname());//현재 로그인한 회원의 이름 보내기
			
			return "writeForm";
		} else { //비로그인 상태
			
			model.addAttribute("msg", "로그인한 회원만 글쓰기가 가능합니다. 로그인해주세요.");
			model.addAttribute("url", "login");
			
			return "alert/alert";
		}
	}
	
	@PostMapping(value = "/writeOk")
	public String writeOk(HttpServletRequest request, Model model) {
		
		String bid = request.getParameter("bid");
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		bDao.writeDao(bid, bname, btitle, bcontent);//글쓰기		
		
		return "redirect:list";
	}
	
	@GetMapping(value = "/list")
	public String board(HttpServletRequest request, Model model) {
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> bDtos = bDao.listDao();//모든 글 가져오기
		
		model.addAttribute("bDtos", bDtos);
		
		return "board";
	}
	
	@GetMapping(value = "/contentView")
	public String contentView(HttpServletRequest request, Model model) {
		
		String bnum = request.getParameter("bnum");//사용자가 클릭한 글의 번호
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		BoardDto bDto = bDao.contentViewDao(bnum);
		
		model.addAttribute("bDto", bDto);
		
		return "contentView";
	}
	@GetMapping(value = "/contentModify")
	public String contentModify(HttpServletRequest request, Model model, HttpSession session) {
		
		String bnum = request.getParameter("bnum");//사용자가 클릭한 글의 번호
		
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		BoardDto bDto = bDao.contentViewDao(bnum);
		
		String sid = (String) session.getAttribute("sessionid");
		
		if(sid.equals(bDto.getBid())) {//글쓴이와 현재 로그인 중인 아이디와 비교
			model.addAttribute("bDto", bDto);		
			
			return "contentModify";
		} else {
			
			model.addAttribute("msg", "글을 작성한 사용자만 수정 권한이 있습니다.");
			
			return "alert/alert2";
		}
		
		
	}
	
	
	@PostMapping(value = "/contentModifyOk")
	public String contentModifyOk(HttpServletRequest request, Model model) {
		
		String bnum = request.getParameter("bnum");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		bDao.contentModifyDao(bnum, btitle, bcontent);//글 수정
		
		return "redirect:list";
	}
	
	@GetMapping(value = "/contentDelete")
	public String contentDelete(HttpServletRequest request, Model model, HttpSession session) {
		
		String bnum = request.getParameter("bnum");//삭제할 글의 번호
		
		BoardDao bDao = sqlSession.getMapper(BoardDao.class);
		BoardDto bDto = bDao.contentViewDao(bnum);//해당 글 번호의 모든 정보 가져오기
		
		String sid = (String) session.getAttribute("sessionid");//현재 로그인한 사용자의 아이디		
		
		if(sid.equals(bDto.getBid())) {//현재 로그인한 사용자 아이디와 글쓴사용자의 아이디 비교			
			if(bDao.contentDeleteDao(bnum) == 1) {//참이면 삭제 성공
				model.addAttribute("msg", "글이 성공적으로 삭제되었습니다.");
				model.addAttribute("url", "list");
				
				return "alert/alert";
			} else {
				model.addAttribute("msg", "글 삭제가 실패하였습니다.");
				model.addAttribute("url", "list");
				
				return "alert/alert";//삭제 실패시 리스트로 돌아가기
			}
		} else {
			model.addAttribute("msg", "해당 글의 삭제 권한이 없습니다.");			
			return "alert/alert2";
		}
		
		
		
	}
	
	
	
	
	
	
}
