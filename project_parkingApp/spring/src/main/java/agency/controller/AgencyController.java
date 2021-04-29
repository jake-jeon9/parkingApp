package agency.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import agency.bean.AgencyDTO;


@Controller
public class AgencyController {

	@Autowired
	AgencyService agencyService;
	
	@RequestMapping(value = "/agency/agency_insert.do")
	public ModelAndView agencyInsert(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : agency_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(insertAgency(request));
		
		JSONObject json = new JSONObject();
		json.put("agencyRT", agencyRT);
		
		System.out.println("-- 함수 종료 : agency_insert.do --\n");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/agency/agency_modify.do")
	public ModelAndView agencyModify(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : agency_modify.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(modifyAgency(request));
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : agency_modify.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/agency/agency_delete.do")
	public ModelAndView agencyDelete(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : agency_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String agencyRT = "FAIL";
		
		agencyRT = getResult(deleteAgency(request));
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
		System.out.println("-- 함수 종료 : agency_delete.do --\n");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/agency/agency_Select.do")
	public ModelAndView searchKeyword(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_Select.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		List<AgencyDTO> list = agencyService.agencySelect(memberNo);
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray agencyList = new JSONArray();
			int size = list.size();
			for(AgencyDTO agencyDTO : list) {
				agencyList.put(agencyDTO);
			}
			json.put("agencyList", agencyList);
			json.put("size", size);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : agency_Select.do --");
		return modelAndView(json);
	}
	

	public int insertAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : insertBoard");
		// 기본 정보
		int memberNo = convert(request.getParameter("memberNo"));
		String nameOfAgency = request.getParameter("nameOfAgency");
		String contactName= request.getParameter("contactName");
		String contacnPhone= request.getParameter("contacnPhone");
//		String issueOfDate= "sysdate";
		String expireOfDate= request.getParameter("expireOfDate");
//	    int countOfextend= 0;
	    int paid= convert(request.getParameter("paid"));
//	    int usedCount= 1;
//		
		// 보드 작성
		AgencyDTO agencyDTO = new AgencyDTO();
		agencyDTO.setMemberNo(memberNo);
		agencyDTO.setNameOfAgency(nameOfAgency);
		agencyDTO.setContacnPhone(contacnPhone);
		agencyDTO.setContactName(contactName);
//		agencyDTO.setIssueOfDate(issueOfDate);
		agencyDTO.setExpireOfDate(expireOfDate);
		agencyDTO.setPaid(paid);
		
		
		int result = agencyService.agencyInsert(agencyDTO);
		System.out.println("함수 종료 : insertBoard");
		return result;
	}
	
	public int modifyAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyAgency");
		int result = 0;
		// 기본 정보
		int agencyNo = convert(request.getParameter("agencyNo"));
		String nameOfAgency = request.getParameter("nameOfAgency");
		String contactName= request.getParameter("contactName");
		String contacnPhone= request.getParameter("contacnPhone");
		String issueOfDate= request.getParameter("issueOfDate");
		String expireOfDate= request.getParameter("expireOfDate");
	    int countOfextend=  convert(request.getParameter("countOfextend"));
	    int paid= convert(request.getParameter("paid"));
	    int usedCount= convert(request.getParameter("usedCount"));
		
		AgencyDTO agencyDTO = new AgencyDTO();
		agencyDTO.setAgencyNo(agencyNo);
		agencyDTO.setNameOfAgency(nameOfAgency);
		agencyDTO.setContacnPhone(contacnPhone);
		agencyDTO.setContactName(contactName);
		agencyDTO.setIssueOfDate(issueOfDate);
		agencyDTO.setExpireOfDate(expireOfDate);
		agencyDTO.setCountOfextend(countOfextend);
		agencyDTO.setPaid(paid);
		agencyDTO.setUsedCount(usedCount);
		
		result = agencyService.agencyModify(agencyDTO);
	    System.out.println("함수 종료 : modifyAgency");
		return result;
	}
	
	public int deleteAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : deleteAgency");
		int agencyNo = convert(request.getParameter("agencyNo"));
		int result = 0;
		AgencyDTO agencyDTO = new AgencyDTO();
		agencyDTO.setAgencyNo(agencyNo);
		result = agencyService.agencyDelete(agencyDTO);
		System.out.println("함수 종료 : deleteAgency");
		return result;
	}
	

	
	public int convert(String id) {
		System.out.println("함수 실행 : convert");
		if(id == null || id.equals("")) {
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
	    modelAndView.setViewName("board.jsp");
	    return modelAndView;
	}
	
	
}
