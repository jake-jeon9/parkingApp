package regular.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import regular.bean.RegularDTO;

@Controller
public class RegularController {

	@Autowired
	RegularService regularService;

	@RequestMapping(value = "/regular/regular_insert.do")
	public ModelAndView regularInsert(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : regular_insert.do --");
		request.setCharacterEncoding("UTF-8");

		String regularRT = "FAIL";
		boolean check = checkAgency(request);
		if(check) {
			regularRT = getResult(insertregular(request));
		}else {
			regularRT = "이미 등록 됨";
		}
		JSONObject json = new JSONObject();
		json.put("regularRT", regularRT);

		System.out.println("-- 함수 종료 : regular_insert.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/regular/regular_modify.do")
	public ModelAndView regularModify(HttpServletRequest request, MultipartFile[] photo) throws Exception {
		System.out.println("-- 함수 실행 : regular_modify.do --");
		request.setCharacterEncoding("UTF-8");

		String regularRT = getResult(modifyRegular(request));
		JSONObject json = new JSONObject();
		json.put("regularRT", regularRT);

		System.out.println("-- 함수 종료 : regular_modify.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/regular/regular_delete.do")
	public ModelAndView regularDelete(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : regular_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String regularRT = "FAIL";

		regularRT = getResult(deleterRegular(request));
		JSONObject json = new JSONObject();
		json.put("regularRT", regularRT);
		System.out.println("-- 함수 종료 : regular_delete.do --\n");
		return modelAndView(json);
	}

	@RequestMapping(value = "/regular/regular_Select.do")
	public ModelAndView searchKeyword(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : regular_Select.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		int type = convert(request.getParameter("type"));
		
		List<RegularDTO> list = regularService.regularSelect(memberNo,type);
		JSONObject json = new JSONObject();
		
		if(list != null) {
			RT = "OK";
			JSONArray regularList = new JSONArray();
			int size = list.size();
			for(RegularDTO regularDTO : list) {
				JSONObject temp = new JSONObject();
				temp.put("regularNo", regularDTO.getRegularNo());
				temp.put("plateNumOfCar", regularDTO.getPlateNumOfCar());
				temp.put("guestName", regularDTO.getGuestName());
				temp.put("guestContect", regularDTO.getGuestContact());
				temp.put("issueOfDate", regularDTO.getIssueOfDate());
				temp.put("expireOfDate", regularDTO.getExpireOfDate());
				temp.put("countOfextend", regularDTO.getCountOfextend());
				temp.put("paid", regularDTO.getPaid());
				temp.put("usedCount", regularDTO.getUsedCount());
				temp.put("reg_date", regularDTO.getReg_date());
				regularList.put(temp);
			}
			json.put("regularList", regularList);
			json.put("size", size);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : regular_Select.do --");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/regular/regular_updateUsedcount.do")
	public ModelAndView regular_updateUsedcount(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : regular_updateUsedcount.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(updateUsedCount(request));
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : regular_updateUsedcount.do --\n");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/regular/regular_extension.do")
	public ModelAndView regularExtension(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : regular_extension.do --");
		request.setCharacterEncoding("UTF-8");
		
		String agencyRT = getResult(extension(request));
		
		JSONObject json = new JSONObject();
	    json.put("agencyRT", agencyRT);
	      
		System.out.println("-- 함수 종료 : regular_extension.do --\n");
		return modelAndView(json);
	}
	

	private boolean checkAgency(HttpServletRequest request) {
		System.out.println("함수 실행 : checkAgency");
		boolean result = false;
		String targetName = request.getParameter("plateNumOfCar");
		int memberNo = convert(request.getParameter("memberNo"));
		RegularDTO regularDTO = regularService.regularSelectSearchName(targetName,memberNo);
		if(regularDTO == null ) result = true;
		
		System.out.println("함수 종료 : checkAgency");
		return result;
	}

	private int extension(HttpServletRequest request) {
		System.out.println("함수 시작 : extension");
		int result = 0;
		
		int regularNo = convert(request.getParameter("regularNo"));
		int addMonths = convert(request.getParameter("addMonths"));
		int cost =  convert(request.getParameter("cost"));
		
		result =  regularService.regularExtension(regularNo, addMonths, cost);
		
		System.out.println("함수 종료 : extension");
		return result;
	}
	
	private int updateUsedCount(HttpServletRequest request) {
		System.out.println("함수 시작 : updateUsedCount");
		int result = 0;
		
		int regularNo = convert(request.getParameter("regularNo"));
		result = regularService.regularUpdate(regularNo);

		System.out.println("함수 종료 : updateUsedCount");
		return result;
	}
		
	public int insertregular(HttpServletRequest request) {
		System.out.println("함수 실행 : insertBoard");
		// 기본 정보
		int memberNo = convert(request.getParameter("memberNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String guestName = request.getParameter("guestName");
		String guestContact = request.getParameter("guestContact");
		String expireOfDate = request.getParameter("expireOfDate");
		int paid = convert(request.getParameter("paid"));
		
		// 보드 작성
		RegularDTO regularDTO = new RegularDTO();
		regularDTO.setMemberNo(memberNo);
		regularDTO.setPlateNumOfCar(plateNumOfCar);
		regularDTO.setGuestName(guestName);
		regularDTO.setGuestContact(guestContact);
		regularDTO.setExpireOfDate(expireOfDate);
		regularDTO.setPaid(paid);
		
		int result = regularService.regularInsert(regularDTO);
		System.out.println("함수 종료 : insertBoard");
		return result;
	}
	
	public int modifyRegular(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyRegular");
		int regularNo =  convert(request.getParameter("regularNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String guestName = request.getParameter("guestName");
		String guestContact = request.getParameter("guestContact");
		String expireOfDate = request.getParameter("expireOfDate");
		int paid = convert(request.getParameter("paid"));
		String issueOfDate = request.getParameter("issueOfDate");
		int countOfextend = convert(request.getParameter("countOfextend"));
	    int usedCount= convert(request.getParameter("usedCount"));
		
		// 보드 작성
		RegularDTO regularDTO = new RegularDTO();
		regularDTO.setRegularNo(regularNo);
		regularDTO.setPlateNumOfCar(plateNumOfCar);
		regularDTO.setGuestName(guestName);
		regularDTO.setGuestContact(guestContact);
		regularDTO.setIssueOfDate(issueOfDate);
		regularDTO.setExpireOfDate(expireOfDate);
		regularDTO.setPaid(paid);
		regularDTO.setUsedCount(usedCount);
		regularDTO.setCountOfextend(countOfextend);
		int result = regularService.regularModify(regularDTO);
	    System.out.println("함수 종료 : modifyRegular");
		return result;
	}
	
	public int deleterRegular(HttpServletRequest request) {
		System.out.println("함수 실행 : deleterRegular");
		int regualrNo = convert(request.getParameter("regularNo"));
		int result = 0;
		
		result = regularService.regularDelete(regualrNo);
		System.out.println("함수 종료 : deleterRegular");
		return result;
	}

	public int convert(String id) {
		System.out.println("함수 실행 : convert");
		if (id == null || id.equals("")) {
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
		modelAndView.setViewName("regular.jsp");
		return modelAndView;
	}

}
