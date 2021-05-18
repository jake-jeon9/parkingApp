package member.contorller;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cost.bean.CostDTO;
import cost.controller.CostService;
import member.bean.MemberDTO;

@Controller
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	CostService costService;
	
	@Autowired
	BCryptPasswordEncoder pwEncoder;

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
	
	@RequestMapping(value = "/member/member_Login.do")
	public ModelAndView memberLogin(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_Login.do --");
		request.setCharacterEncoding("UTF-8");
		
		String memberRT = "FAIL"; 
		String costRT = "FAIL";
		MemberDTO memberDTO = getMyinfo(request);
		CostDTO costDTO = getMyCostInfo(memberDTO.getMemberNo());
		
		JSONObject DTO = null;
		JSONObject COST = null;
		if(memberDTO !=null) {

			memberRT = "OK";
			costRT = "OK";
			DTO = new JSONObject();
			COST = new JSONObject();
			int memberNo = memberDTO.getMemberNo();
			String memberId = memberDTO.getMemberId();
			String email = memberDTO.getEmail();
			String pw = memberDTO.getPw();
		    String nameOfParkingArea = memberDTO.getNameOfParkingArea();
		    String phone = memberDTO.getPhone();
		    DTO.put("memberNo", memberNo);
		    DTO.put("memberId", memberId);
		    DTO.put("email", email);
		    DTO.put("pw", pw);
		    DTO.put("nameOfParkingArea", nameOfParkingArea);
		    DTO.put("phone", phone);
			
		    int additionalCost = costDTO.getAdditionalCost();
		    int additionalTiem =costDTO.getAdditionalTiem(); 
		    int baseCost =costDTO.getBaseCost();
		    int baseTime =costDTO.getBaseTime();
		    int maxcost = costDTO.getMaxcost();
		    int maxtime = costDTO.getMaxtime();
		    int maxArea = costDTO.getMaxArea();
		    
		    COST.put("additionalCost",additionalCost );
		    COST.put("additionalTiem",additionalTiem );
		    COST.put("baseCost",baseCost );
		    COST.put("baseTime",baseTime );
		    COST.put("maxcost",maxcost );
		    COST.put("maxtime",maxtime );
		    COST.put("maxArea",maxArea );
		    
		}
		
	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("costRT", costRT);
	    json.put("memberDTO", DTO);
	    json.put("CostDTO", COST);
	    System.out.println("-- 함수 종료 : member_Login.do --\n");
	    return modelAndView(json);
	}
	



	@RequestMapping(value = "/member/member_insert.do")
	public ModelAndView memberWrite(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : member_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String memberRT = "FAIL"; 
		String costRT = "FAIL";
		
		int result = insertMember(request);
		
		if(result != 0) {
			memberRT = getResult(result);
			costRT = getResult(insertSetCost(result));
		}

	    JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    json.put("costRT", costRT);
	    json.put("memberNo", result);
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
			costRT ="OK";
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
		//System.out.println("searchId : "+searchId);
		String searchedId = memberService.memberSelect(searchId);
		
		if(searchedId != null) result = 1;
		System.out.println("함수 종료 : searchId");
		return result;
	}
	
	private MemberDTO getMyinfo(HttpServletRequest request) {
		System.out.println("함수 실행 : getMyinfo");
		// 기본 정보
		String memberId = request.getParameter("memberId");
		String pw = request.getParameter("pw");
		pw = pwEncoder.encode(pw);
		//BCrypt.gensalt()
		MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setMemberId(memberId);
	    memberDTO.setPw(pw);
	    memberDTO = memberService.memberLogin(memberDTO);
		
	    System.out.println("함수 종료 : getMyinfo");
		return memberDTO;
	}
	
	private CostDTO getMyCostInfo(int memberNo) {
		System.out.println("함수 실행 : getMyCostInfo");
		CostDTO costDTO = costService.costSelect(memberNo);
		System.out.println("함수 종료 : getMyCostInfo");
		return costDTO;
	}

	public int insertMember(HttpServletRequest request) {
		System.out.println("함수 실행 : insertMember");
		// 기본 정보
		String memberId = request.getParameter("memberId");
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		System.out.println("BEFORE pw?" + pw);
		pw = pwEncoder.encode(pw);
		System.err.println("AFTER pw?"+pw);
	    String nameOfParkingArea = request.getParameter("nameOfParkingArea");
	    String phone = request.getParameter("phone");
	    String device_token = request.getParameter("device_token");
	    
	    //String deviceToken = request.getParameter("deviceToken");
	    // 멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setMemberId(memberId);
	    memberDTO.setEmail(email);
	    memberDTO.setPw(pw);
	    memberDTO.setNameOfParkingArea(nameOfParkingArea);
	    memberDTO.setPhone(phone);
	    memberDTO.setDevice_token(device_token);
	    
	    memberService.memberInsert(memberDTO);
	    int result = memberDTO.getMemberNo();
		return result;
	}
	
	public int insertSetCost(int memberNo) {
		System.out.println("함수 실행 : insertSetCost");
		int result = costService.costInsert(memberNo);
		System.out.println("함수 종료 : insertSetCost");
		return result;
	}
		
	public int modifyMember(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyMember");
		int result = 0;
		// 기본 정보
		int memberNo = convertNo(request.getParameter("memberNo"));
		String pw = request.getParameter("pw");
		String email = request.getParameter("email");
	    String nameOfParkingArea = request.getParameter("nameOfParkingArea");
	    String phone = request.getParameter("phone");

	    //멤버 작성
	    MemberDTO memberDTO = new MemberDTO();
	    memberDTO.setMemberNo(memberNo);
	    memberDTO.setPw(pw);
	    memberDTO.setNameOfParkingArea(nameOfParkingArea);
	    memberDTO.setPhone(phone);
	    memberDTO.setEmail(email);
	    
	    result = memberService.memberModify(memberDTO);
	    System.out.println("함수 종료 : modifyMember");
		return result;
	}
	
	public int deleteMember(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteMember");
		int memberNo = convertNo(request.getParameter("memberNo"));
		int result = 0;
		result = memberService.memberDelete(memberNo);
		System.out.println("함수 종료 : deleteMember");
		if(result >0) result = deleteCost(memberNo);
		
		return result;
	}
	
	public int deleteCost(int memberNo) {
		System.out.println("함수 실행 : insertSetCost");
		int result = costService.costDelete(memberNo);
		System.out.println("함수 종료 : insertSetCost");
		return result ; 
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
