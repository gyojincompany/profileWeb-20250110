package com.gyojincompany.profile.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gyojincompany.profile.dao.BoardDao;
import com.gyojincompany.profile.dao.MemberDao;
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
	
	
	
	
	
	
	

}
