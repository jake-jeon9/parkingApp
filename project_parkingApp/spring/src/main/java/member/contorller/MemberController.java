package member.contorller;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cost.controller.CostService;
import member.bean.MemberDTO;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	CostService costService;

	//회원가입시 아이디 조회
	@RequestMapping(value = "/member/check_id.do")
	public ModelAndView check_id(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : check_id.do --");
		request.setCharacterEncoding("UTF-8");
		
		String RT = "사용가능";
		int result = searchId(request);
		if(result == 1 ) { // 1이면  아이디가 존재
			RT = "이미 사용중";
		}
		
		JSONObject json = new JSONObject();
		json.put("RT", RT);
		System.out.println("-- 함수 실행 : check_id.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/member/member_insert.do")
	public ModelAndView memberWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String memberRT = "FAIL"; 
		String costRT = "FAIL";
	    
	    // 회원 등록 후 회원 아이디 가져오기
		int result = searchId(request); // 0이면 아이디 없음
		if( result ==0 ) {
			result = insertMember(request);
		}
		if(result >0) {
			memberRT = getResult(result);
		}
		MemberDTO memberDTO = insertSetCost(request);

		if(memberDTO != null) {
			costRT = getResult(result); 
		}
    
	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("costRT", costRT);
	    json.put("memberNo", memberDTO.getMemberNo());
	      
	    System.out.println("-- 함수 종료 : member_insert.do --\n");
	    return modelAndView(json);
	}

	@RequestMapping(value = "/member/member_modify.do")
	public ModelAndView memberModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_modify.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String memberRT = "FAIL";
		
		// 회원 수정
		memberRT = getResult(modifyMember(request));
	    
	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	      
	    System.out.println("-- 함수 종료 : member_modify.do --\n");
	    return modelAndView(json);
	}
	
	@RequestMapping(value = "/member/member_delete.do")
	public ModelAndView memberDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : member_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		String costRT = "FAIL";
		memberRT = getResult(deleteMember(request));
		
		if(memberRT.equals("OK")) {
			costRT = getResult(deleteCost(request));
		}
		
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("costRT", costRT);
	    System.out.println("-- 함수 종료 : member_delete.do --\n");
		return modelAndView(json);
	}
	
	public int searchId(HttpServletRequest request) {

		System.out.println("함수 실행 : searchId");
		int result = 0;
		
		String searchId = request.getParameter("memberId");
		MemberDTO memberDTO = memberService.memberSelect(searchId);
		
		if(memberDTO != null) result = 1;
		System.out.println("함수 종료 : searchId");
		return result;
	}
	public int insertMember(HttpServletRequest request) {
		System.out.println("함수 실행 : insertMember");
		// 기본 정보
		String memberId = request.getParameter("memberId");
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
	    String nameOfParkingArea = request.getParameter("nameOfParkingArea");
	    String phone = request.getParameter("phone");
	    
	    //String deviceToken = request.getParameter("deviceToken");
	    // 멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setMemberId(memberId);
	    memberDTO.setEmail(email);
	    memberDTO.setPw(pw);
	    memberDTO.setNameOfParkingArea(nameOfParkingArea);
	    memberDTO.setPhone(phone);
	    
	    int result = memberService.memberInsert(memberDTO);
		return result;
	}
	
	public MemberDTO insertSetCost(HttpServletRequest request) {
		System.out.println("함수 실행 : insertSetCost");
		String memberId = request.getParameter("memberId");
		System.out.println("함수 종료 : insertSetCost");
		return memberService.memberSelect(memberId);
	}
		
	public int modifyMember(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyMember");
		int result = 0;
		// 기본 정보
		int id = convertNo(request.getParameter("memberNo"));
		String pw = request.getParameter("pw");
	    String nameOfParkingArea = request.getParameter("nameOfParkingArea");
	    String phone = request.getParameter("phone");

	    //멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setPw(pw);
	    memberDTO.setNameOfParkingArea(nameOfParkingArea);
	    memberDTO.setPhone(phone);
	    
	    result = memberService.memberModify(memberDTO);
	    System.out.println("함수 종료 : modifyMember");
		return result;
	}
	
	public int deleteMember(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteMember");
		int memberNo = convertNo(request.getParameter("memberNo"));
		int result = 0;
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberNo(memberNo);
		result = memberService.memberDelete(memberDTO);
		System.out.println("함수 종료 : deleteMember");
		return result;
	}
	
	public int deleteCost(HttpServletRequest request) {
		System.out.println("함수 실행 : insertSetCost");
		int memberNo = convertNo(request.getParameter("memberNo"));
		System.out.println("함수 종료 : insertSetCost");
		return costService.costDelete(memberNo);
	}
	
	public int convertNo(String id) {
		System.out.println("함수 실행 : haveId");
		if(id == null) {
			System.out.println("함수 종료 : haveId");
			return 0;
		} else {
			System.out.println("함수 종료 : haveId");
			return Integer.parseInt(id);
		}
	}
	public String getResult(int result) {
		return result > 0 ? "OK" : "FAIL";
	}
	public ModelAndView modelAndView(JSONObject json) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("member.jsp");
	    return modelAndView;
	}
	
	
}
