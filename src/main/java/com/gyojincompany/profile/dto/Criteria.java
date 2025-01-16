package com.gyojincompany.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Criteria {
	
	private int amount = 10;//한 페이지당 보여질 글의 개수(10개)
	private int pageNum = 1;
	//사용자가 클릭한 페이지의 번호가 저장될 변수(초기값 1->게시판을 클릭했을때 처음 보여지는 페이지가 무조건 1페이지)
	private int startNum;//사용자가 선택한 페이지에서 시작할 글의 번호(ROWNUM)
	
}
