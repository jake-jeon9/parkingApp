package cost.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cost.bean.CostDTO;

@Controller
public class CostController {

	@Autowired
	CostService costService;

	/*
	 생성 및 삭제는 memberController에서 관리되며,
	 member 생성시  default 값으로 생성되며,
	 탈퇴 시 요금정보도 삭제됨.
	 */
	
	
	@RequestMapping(value = "/cost/cost_modify.do")
	public ModelAndView memberModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : cost_modify.do --");
		request.setCharacterEncoding("UTF-8");
		String costRT = "FAIL";
		
		// 비용수정
		costRT = getResult(modifyCost(request));
	    
		//갱신
		int memberNo = convertNo(request.getParameter("memberNo"));
		CostDTO costDTO = null;
		JSONObject COST = null;
		if(costRT.equals("OK")) {
			costDTO = getMyCostInfo(memberNo);
			COST = new JSONObject();
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
	    json.put("costRT", costRT);
	    json.put("CostDTO", COST);
	      
	    System.out.println("-- 함수 종료 : cost_modify.do --\n");
	    return modelAndView(json);
	}
	
	private CostDTO getMyCostInfo(int memberNo) {
		System.out.println("함수 실행 : getMyCostInfo");
		CostDTO costDTO = costService.costSelect(memberNo);
		System.out.println("함수 종료 : getMyCostInfo");
		return costDTO;
	}
		
	public int modifyCost(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyCost");
		int result = 0;
		// 기본 정보
		int memberNo = convertNo(request.getParameter("memberNo"));
	    int additionalCost = convertNo(request.getParameter("additionalCost"));
	    int additionalTiem = convertNo(request.getParameter("additionalTiem"));
	    int baseCost =convertNo(request.getParameter("baseCost"));
	    int baseTime = convertNo(request.getParameter("baseTime"));
	    int maxcost = convertNo(request.getParameter("maxcost"));
	    int maxtime = convertNo(request.getParameter("maxtime"));
	    int maxArea = convertNo(request.getParameter("maxArea"));

	    //멤버 작성
	    CostDTO costDTO = new CostDTO();
	    costDTO.setMemberNo(memberNo);
	    costDTO.setAdditionalCost(additionalCost);
	    costDTO.setAdditionalTiem(additionalTiem);
	    costDTO.setBaseCost(baseCost);
	    costDTO.setBaseTime(baseTime);
	    costDTO.setMaxArea(maxArea);
	    costDTO.setMaxcost(maxcost);
	    costDTO.setMaxArea(maxArea);
	   
	    result = costService.costModify(costDTO);
	    System.out.println("함수 종료 : modifyCost");
		return result;
	}
	
	
	public int convertNo(String id) {
		System.out.println("함수 실행 : convert");
		if(id == null) {
			System.out.println("함수 종료 : convert");
			return 0;
		} else {
			System.out.println("함수 종료 : convert");
			return Integer.parseInt(id);
		}
	}
	

	public String getResult(int result) {
		return result > 0 ? "OK" : "FAIL";
	}
	public ModelAndView modelAndView(JSONObject json) {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.addObject("json", json);
	    modelAndView.setViewName("cost.jsp");
	    return modelAndView;
	}
}
