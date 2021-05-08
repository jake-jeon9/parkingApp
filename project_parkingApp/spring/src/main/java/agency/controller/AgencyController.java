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
	public ModelAndView agencyInsert(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = "FAIL";
		boolean check = checkAgency(request);
		if(check) {
			agencyRT = getResult(insertAgency(request));
		}else {
			agencyRT = "이미 등록 됨";
		}
		
		JSONObject json = new JSONObject();
		json.put("agencyRT", agencyRT);
		
		System.out.println("-- 함수 종료 : agency_insert.do --\n");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/agency/agency_modify.do")
	public ModelAndView agencyModify(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_modify.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(modifyAgency(request));
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : agency_modify.do --\n");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/agency/agency_updateUsedcount.do")
	public ModelAndView agency_updateUsedcount(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_updateUsedcount.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(updateUsedCount(request));
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : agency_updateUsedcount.do --\n");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/agency/agency_extension.do")
	public ModelAndView agencyExtension(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_extension.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(extension(request));
		
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : agency_extension.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/agency/agency_delete.do")
	public ModelAndView agencyDelete(HttpServletRequest request) throws Exception {
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
	public ModelAndView agencySelect(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : agency_Select.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		int type = convert(request.getParameter("type"));
		
		List<AgencyDTO> list = agencyService.agencySelect(memberNo,type);
		JSONObject json = new JSONObject();
		if(list != null) {
			RT = "OK";
			JSONArray agencyList = new JSONArray();
			int size = list.size();
			for(AgencyDTO agencyDTO : list) {
				//agencyList.put(agencyDTO);
				JSONObject temp = new JSONObject();
				temp.put("agencyNo",agencyDTO.getAgencyNo());
				temp.put("nameOfAgency",agencyDTO.getNameOfAgency());
				temp.put("contactName",agencyDTO.getContactName());
				temp.put("contactPhone",agencyDTO.getContactPhone());
				temp.put("issueOfDate",agencyDTO.getIssueOfDate());
				temp.put("expireOfDate",agencyDTO.getExpireOfDate());
				temp.put("countOfextend",agencyDTO.getCountOfextend());
				temp.put("paid",agencyDTO.getPaid());
				temp.put("usedCount",agencyDTO.getUsedCount());
				temp.put("reg_date",agencyDTO.getReg_date());
				agencyList.put(temp);
				
			}
			json.put("agencyList", agencyList);
			json.put("size", size);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : agency_Select.do --");
		return modelAndView(json);
	}
	
	private boolean checkAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : checkAgency");
		boolean result = false;
		String targetName = request.getParameter("nameOfAgency");
		int memberNo = convert(request.getParameter("memberNo"));
		AgencyDTO agencyDTO = agencyService.agencySelectSearchName(targetName,memberNo);
		if(agencyDTO == null ) result = true;
		
		System.out.println("함수 종료 : checkAgency");
		return result;
	}

	public int insertAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : insertAgency");
		// 기본 정보
		int memberNo = convert(request.getParameter("memberNo"));
		String nameOfAgency = request.getParameter("nameOfAgency");
		String contactName= request.getParameter("contactName");
		String contactPhone= request.getParameter("contactPhone");
		String expireOfDate= request.getParameter("expireOfDate");
	    int paid= convert(request.getParameter("paid"));

	    // 보드 작성
		AgencyDTO agencyDTO = new AgencyDTO();
		agencyDTO.setMemberNo(memberNo);
		agencyDTO.setNameOfAgency(nameOfAgency);
		agencyDTO.setContactPhone(contactPhone);
		agencyDTO.setContactName(contactName);
		agencyDTO.setExpireOfDate(expireOfDate);
		agencyDTO.setPaid(paid);
		
		
		int result = agencyService.agencyInsert(agencyDTO);
		System.out.println("함수 종료 : insertAgency");
		return result;
	}
	
	public int modifyAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyAgency");
		int result = 0;
		// 기본 정보
		int agencyNo = convert(request.getParameter("agencyNo"));
		String nameOfAgency = request.getParameter("nameOfAgency");
		String contactName= request.getParameter("contactName");
		String contacnPhone= request.getParameter("contactPhone");
		String issueOfDate= request.getParameter("issueOfDate");
		String expireOfDate= request.getParameter("expireOfDate");
	    int countOfextend=  convert(request.getParameter("countOfextend"));
	    int paid= convert(request.getParameter("paid"));
	    int usedCount= convert(request.getParameter("usedCount"));
		
		AgencyDTO agencyDTO = new AgencyDTO();
		agencyDTO.setAgencyNo(agencyNo);
		agencyDTO.setNameOfAgency(nameOfAgency);
		agencyDTO.setContactPhone(contacnPhone);
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
	
	private int extension(HttpServletRequest request) {
		System.out.println("함수 시작 : extension");
		int result = 0;
		
		int agencyNo = convert(request.getParameter("agencyNo"));
		int addMonths = convert(request.getParameter("addMonths"));
		int cost =  convert(request.getParameter("cost"));
		
		result =  agencyService.agencyExtension(agencyNo, addMonths, cost);
		
		System.out.println("함수 종료 : extension");
		return result;
	}
	
	private int updateUsedCount(HttpServletRequest request) {
		System.out.println("함수 시작 : updateUsedCount");
		int result = 0;
		
		int agencyNo = convert(request.getParameter("agencyNo"));
		result = agencyService.agencyUpdate(agencyNo);

		System.out.println("함수 종료 : updateUsedCount");
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
	    modelAndView.setViewName("agency.jsp");
	    return modelAndView;
	}
	
	
}
