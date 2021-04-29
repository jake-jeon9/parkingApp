package parkinglist.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import parkinglist.bean.ParkinglistDTO;
import photo.bean.PhotoDTO;
import photo.controller.PhotoService;

@Controller
public class ParkinglistController {

	@Autowired
	ParkinglistService parkinglistService;
	
	@Autowired
	PhotoService photoService;
	
	@RequestMapping(value = "/parkinglist/parkinglist_insert.do")
	public ModelAndView parkinglistInsert(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_insert.do --");
		request.setCharacterEncoding("UTF-8");
		
		String parkingListRT = "FAIL"; 
		String photoRT = "FAIL";
		
	    // 회원 등록 후 회원 아이디 가져오기
		int usedNo = insertParkingList(request);
		if( usedNo !=0) {
			parkingListRT ="OK";
			photoRT = getResult(insertParkingPhoto(usedNo, request, photo));	
		}
		 
	    JSONObject json = new JSONObject();
	    json.put("parkingListRT", parkingListRT);
	    json.put("photoRT", photoRT);
	      
	    System.out.println("-- 함수 종료 : parkinglist_insert.do --\n");
	    return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_upload.do")
	public ModelAndView memberModify(HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_upload.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String parkingListRT = "FAIL"; 
		
		parkingListRT = getResult(uploadParkingList(request));
		 
	    JSONObject json = new JSONObject();
	    json.put("parkingListRT", parkingListRT);
	      
	    System.out.println("-- 함수 종료 : parkinglist_upload.do --\n");
	    return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_delete.do")
	public ModelAndView parkingListDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		memberRT = getResult(deleteParkingList(request));
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : parkinglist_delete.do --\n");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_Select.do")
	public ModelAndView searchKeyword(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_Select.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		List<ParkinglistDTO> list = parkinglistService.parkinglistSelect(memberNo,startDate,endDate);
		JSONObject json = new JSONObject();
		
		if(list != null) {
			RT = "OK";
			JSONArray parkingList = new JSONArray();
			int size = list.size();
			for(ParkinglistDTO parkinglistDTO : list) {
				parkingList.put(parkinglistDTO);
			}
			json.put("parkingList", parkingList);
			json.put("size", size);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : parkinglist_Select.do --");
		return modelAndView(json);
	}
	
	public int insertParkingList(HttpServletRequest request) {
		System.out.println("함수 실행 : insertParkingList");
		// 기본 정보
		int memberNo = convert(request.getParameter("memberNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String currentOfState = request.getParameter("currentOfState");
		
		String timeOfParked = request.getParameter("timeOfParked");

		// 보드 작성
		ParkinglistDTO parkinglistDTO = new ParkinglistDTO();
		parkinglistDTO.setMemberNo(memberNo);
		parkinglistDTO.setCurrentOfState(currentOfState);
		parkinglistDTO.setPlateNumOfCar(plateNumOfCar);
		parkinglistDTO.setTimeOfParked(timeOfParked);
		
//		parkinglistDTO.setTimeOfOut(timeOfOut);
//		parkinglistDTO.setTimeOfused(timeOfused);
//		parkinglistDTO.setPaid(paid);
//		parkinglistDTO.setUsedNo(usedNo);
//		parkinglistDTO.setCoupon(coupon);
		
		int result = parkinglistService.parkinglistInsert(parkinglistDTO);
		if(result ==1) {
			
		}
		System.out.println("함수 종료 : insertParkingList");
		return memberNo;
	}
	
	public int uploadParkingList(HttpServletRequest request) {
		System.out.println("함수 실행 : uploadParkingList");
		// 기본 정보
		int usedNo  = convert(request.getParameter("usedNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String currentOfState = request.getParameter("currentOfState");
		String timeOfParked = request.getParameter("timeOfParked");
		String timeOfOut  = request.getParameter("timeOfOut");
		int timeOfused  = convert(request.getParameter("timeOfused"));
		int paid  = convert(request.getParameter("paid"));
		String coupon  = request.getParameter("coupon");
		
		// 보드 작성
		ParkinglistDTO parkinglistDTO = new ParkinglistDTO();
		parkinglistDTO.setMemberNo(usedNo);
		parkinglistDTO.setCurrentOfState(currentOfState);
		parkinglistDTO.setPlateNumOfCar(plateNumOfCar);
		parkinglistDTO.setTimeOfParked(timeOfParked);
		parkinglistDTO.setTimeOfOut(timeOfOut);
		parkinglistDTO.setTimeOfused(timeOfused);
		parkinglistDTO.setPaid(paid);
		parkinglistDTO.setUsedNo(usedNo);
		parkinglistDTO.setCoupon(coupon);
		
		int result = parkinglistService.parkinglistModify(parkinglistDTO);
	    System.out.println("함수 종료 : uploadParkingList");
		return result;
	}
	
	public int deleteParkingList(HttpServletRequest request) {
		System.out.println("함수 실행 : deleterRegular");
		int usedNo = convert(request.getParameter("usedNo"));
		int result = 0;
		
		result = parkinglistService.parkinglistDelete(usedNo);
		System.out.println("함수 종료 : deleterRegular");
		return result;
	}
	private int insertParkingPhoto(int usedNo, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : insertParkingPhoto");
		int result = 0;
		if(photo != null) {
			result = parkedCarPhotoWrite(usedNo, request, photo);
		}
		System.out.println("함수 종료 : insertParkingPhoto");
		return result;
	}
	
	public int parkedCarPhotoWrite(int usedId, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : parkedCarPhotoWrite");
		int result = 0;
		String dir = request.getSession().getServletContext().getRealPath("/storage");
		String originname = photo.getOriginalFilename();	
		String filename = photo.getOriginalFilename();
		int lastIndex = originname.lastIndexOf(".");
        String filetype = originname.substring(lastIndex + 1);
        int filesize = (int)photo.getSize();
        File file = new File(dir, filename);
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
        
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setDir(dir);
        photoDTO.setOriginalName(originname);
        photoDTO.setFileName(filename);
        photoDTO.setFileType(filetype);
        photoDTO.setFileSize(filesize);
        photoDTO.setUsedId(usedId);
        
        result = photoService.foretPhotoWrite(photoDTO);
        System.out.println("함수 종료 : parkedCarPhotoWrite");
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
		modelAndView.setViewName("board.jsp");
		return modelAndView;
	}
	

	
}
