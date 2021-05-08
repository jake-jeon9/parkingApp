package parkinglist.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cost.bean.CostDTO;
import cost.controller.CostService;
import parkinglist.bean.ParkinglistDTO;
import photo.bean.PhotoDTO;
import photo.controller.PhotoService;

@Controller
public class ParkinglistController {

	@Autowired
	ParkinglistService parkinglistService;
	
	@Autowired
	PhotoService photoService;
	
	@Autowired
	CostService costService;
	
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
	public ModelAndView parkingUpdate(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_upload.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String parkingListRT = "FAIL"; 
		
		parkingListRT = getResult(uploadParkingList(request));
		 
	    JSONObject json = new JSONObject();
	    json.put("parkingListRT", parkingListRT);
	      
	    System.out.println("-- 함수 종료 : parkinglist_upload.do --\n");
	    return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_modify.do")
	public ModelAndView parkingModify(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_modify.do --");
		request.setCharacterEncoding("UTF-8");
	    
		String parkingListRT = "FAIL"; 
		
		parkingListRT = getResult(modifyParkingList(request));
		 
	    JSONObject json = new JSONObject();
	    json.put("parkingListRT", parkingListRT);
	      
	    System.out.println("-- 함수 종료 : parkinglist_modify.do --\n");
	    return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_delete.do")
	public ModelAndView parkingListDelete(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_delete.do --");
		request.setCharacterEncoding("UTF-8");
		String memberRT = "FAIL";
		String photoRT = "FAIL";
		
		memberRT = getResult(deleteParkingList(request));
		
		if(memberRT.equals("OK")) {
			photoRT = getResult(deletePhoto(request));
		}
		JSONObject json = new JSONObject();
	    json.put("memberRT", memberRT);
	    System.out.println("-- 함수 종료 : parkinglist_delete.do --\n");
		return modelAndView(json);
	}
	

	@RequestMapping(value = "/parkinglist/parkinglist_searchSpacifictItem.do")
	public ModelAndView searchSpacifictItem(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : searchSpacifictItem.do --");
		request.setCharacterEncoding("UTF-8");
		String itemRT = "FAIL";
		String photoRT = "FAIL";
		
		int memberNo = convert(request.getParameter("memberNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String state = null;
		if(request.getParameter("state") != null) state =request.getParameter("state");
		
		ParkinglistDTO parkinglistDTO = parkinglistService.getSpacificitem(memberNo,plateNumOfCar,state);
		JSONObject json = new JSONObject();
		
		if(parkinglistDTO != null) {
			itemRT = "OK";
			int usedNo = parkinglistDTO.getUsedNo();
			PhotoDTO photoDTO = photoService.photoSelect("usedNo", usedNo);
			String fileName = "/"+photoDTO.getFileName();
			String date = "/"+parkinglistDTO.getTimeOfParked().substring(0,11);
			String url = "http://192.168.219.102:8081/parker/storage/"+memberNo+date+fileName;

			
			json.put("parkinglistDTO", parkinglistDTO);
		}
		json.put("RT", itemRT);
		System.out.println("-- 함수 종료 : searchSpacifictItem.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_searchTodayitems.do")
	public ModelAndView searchTodayList(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_searchTodayitems.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		String targetState = request.getParameter("state"); // all(전체), in(주차중), out(정산한)
		String coupon =request.getParameter("coupon");
		
		//페이징
		int page = convert(request.getParameter("page"));
		int count = 10;
		int endNum = page * count; 
		int startNum = endNum - (count - 1);
		
		List<ParkinglistDTO> list = parkinglistService.getTodayAll(memberNo, targetState, coupon,startNum,endNum);
		JSONObject json = new JSONObject();
		
		if(list != null) {
			RT = "OK";
			JSONArray parkingList = new JSONArray();
			int size = list.size();
			for(ParkinglistDTO parkinglistDTO : list) {
				JSONObject temp = new JSONObject();
				temp.put("usedNo", parkinglistDTO.getUsedNo());
				temp.put("plateNumOfCar", parkinglistDTO.getPlateNumOfCar());
				temp.put("currentOfState", parkinglistDTO.getCurrentOfState());
				temp.put("timeOfused", parkinglistDTO.getTimeOfused());
				temp.put("timeOfParked", parkinglistDTO.getTimeOfParked());
				temp.put("timeOfOut", parkinglistDTO.getTimeOfOut());
				temp.put("paid", parkinglistDTO.getPaid());
				temp.put("coupon", parkinglistDTO.getCoupon());
				
				parkingList.put(temp);
			}
			json.put("parkingList", parkingList);
			json.put("size", size);
		}
		json.put("RT", RT);
		System.out.println("-- 함수 종료 : parkinglist_searchTodayitems.do --");
		return modelAndView(json);
	}
	
	@RequestMapping(value = "/parkinglist/parkinglist_Select.do")
	public ModelAndView searchKeyword(HttpServletRequest request) throws Exception {
		System.out.println("-- 함수 실행 : parkinglist_Select.do --");
		request.setCharacterEncoding("UTF-8");
		String RT = "FAIL";
		int memberNo = convert(request.getParameter("memberNo"));
		String coupon = request.getParameter("coupon");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		//페이징
		int page = convert(request.getParameter("page"));
		int count = 10;
		int endNum = page * count; 
		int startNum = endNum - (count - 1);
		
		List<ParkinglistDTO> list = parkinglistService.parkinglistSelect(memberNo,startDate,endDate,coupon,startNum,endNum);
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
		
		parkinglistService.parkinglistInsert(parkinglistDTO);
		int usedNo = parkinglistDTO.getUsedNo();
		
		if(Objects.isNull(usedNo)) usedNo = 0;
		System.out.println("함수 종료 : insertParkingList");
		return usedNo;
	}
	
	public int uploadParkingList(HttpServletRequest request) {
		System.out.println("함수 실행 : uploadParkingList");
		// 기본 정보
		String currentOfState = request.getParameter("currentOfState");
		//현재 상태가 out이면 업데이트 불가 이미 출차상태.
		if(currentOfState.equals("out")) return 0;
		
		int usedNo  = convert(request.getParameter("usedNo"));
		int memberNo  = convert(request.getParameter("memberNo"));

		String timeOfParked = request.getParameter("timeOfParked");
		String coupon  = request.getParameter("coupon");
		//여기서 false or memb or a%
		
		int timeOfused =0;
		int paid = 0;
		char type = coupon.charAt(0);
		
		String timeOfOut  = parkinglistService.getTimeFromServer();
		
		//요금계산
		CostDTO costDTO = costService.costSelect(memberNo);
		int[] hh = new int[2];
		int[] mm = new int[2];
		
		hh[0] = Integer.parseInt(timeOfParked.substring(11,13));
		mm[0] = Integer.parseInt(timeOfParked.substring(14,16));

		hh[1] = Integer.parseInt(timeOfOut.substring(11,13));
		mm[1] = Integer.parseInt(timeOfOut.substring(14,16));
		
		System.out.println("in time :"+hh[0]+"/"+mm[0]);
		System.out.println("out time :"+hh[1]+"/"+mm[1]);
		int tm = ((hh[1] - hh[0])*60) +(mm[1]-mm[0]);
		timeOfused = tm;
		
		//맥시멈 시간 초과 
		if(tm>=(costDTO.getMaxtime()*60)) {
			paid = costDTO.getMaxcost();
		}else {
			int baseCost = costDTO.getBaseCost();
			int baseTime = costDTO.getBaseTime();
			int addCost = costDTO.getAdditionalCost();
			int addTime = costDTO.getAdditionalTiem();
			//사용시간이 할인 시간시간 이내일경우 0원.
			System.out.println("baseT :" + baseTime);
			System.out.println("baseC :" + baseCost);
			System.out.println("addT :" + addTime);
			System.out.println("addC :" + addCost);
			int discount = 0;
			if(type == 'a') {
				discount = Integer.parseInt(coupon.substring(1));
				//agency count up
			}
			if(tm<=discount) {
				paid = 0;
			}else {
				tm -=baseTime;
				paid += baseCost;
				if(tm>0) {
					paid += (Math.ceil(((tm-discount)*1.0/addTime)) * addCost);	
				}
				if(paid<0) {
					paid = 0;
				}
			}
		}
		if(type == 'm') {
			paid = 0;
			//regular count up
		}
		
		System.out.println("type : "+ type);
		System.out.println("tm : "+ tm);
		System.out.println("paid : "+ paid);
		System.out.println("timeOfused : "+ timeOfused);
		
		// 보드 작성
		ParkinglistDTO parkinglistDTO = new ParkinglistDTO();
		parkinglistDTO.setUsedNo(usedNo);
		parkinglistDTO.setTimeOfOut(timeOfOut);
		parkinglistDTO.setTimeOfused(timeOfused);
		parkinglistDTO.setPaid(paid);
		parkinglistDTO.setCoupon(coupon);
		
		int result = parkinglistService.parkinglistUpdate(parkinglistDTO);
	    System.out.println("함수 종료 : uploadParkingList");
		return result;
	}
	

	public int modifyParkingList(HttpServletRequest request) {
		System.out.println("함수 실행 : modifyParkingList");
		// 기본 정보
		String currentOfState = request.getParameter("currentOfState");
		int usedNo  = convert(request.getParameter("usedNo"));
		String plateNumOfCar = request.getParameter("plateNumOfCar");
		String timeOfParked = request.getParameter("timeOfParked");
		String coupon  = request.getParameter("coupon");
		int timeOfused = convert(request.getParameter("timeOfused"));
		int paid =  convert(request.getParameter("paid"));
		String timeOfOut  = request.getParameter("timeOfOut");
		
		// 보드 작성
		ParkinglistDTO parkinglistDTO = new ParkinglistDTO();
		parkinglistDTO.setUsedNo(usedNo);
		parkinglistDTO.setTimeOfOut(timeOfOut);
		parkinglistDTO.setTimeOfused(timeOfused);
		parkinglistDTO.setPaid(paid);
		parkinglistDTO.setCoupon(coupon);
		parkinglistDTO.setTimeOfParked(timeOfParked);
		parkinglistDTO.setCurrentOfState(currentOfState);
		parkinglistDTO.setPlateNumOfCar(plateNumOfCar);
		
		int result = parkinglistService.parkinglistModify(parkinglistDTO);
		
	    System.out.println("함수 종료 : modifyParkingList");
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
	
	public int parkedCarPhotoWrite(int usedNo, HttpServletRequest request, MultipartFile photo) throws Exception {
		System.out.println("함수 실행 : parkedCarPhotoWrite");
		int result = 0;
		if( photo == null) return 0;
		
		String memberFold = "/"+request.getParameter("memberNo");
		String date = "/"+parkinglistService.getTimeFromServer().substring(0,10);
		
		
		String dir = request.getSession().getServletContext().getRealPath("/resources/storage"+memberFold+date);
		String originname = photo.getOriginalFilename();	
		String filename = photo.getOriginalFilename();
		int lastIndex = originname.lastIndexOf(".");
        String filetype = originname.substring(lastIndex + 1);
        int filesize = (int)photo.getSize();
        
        File path = new File(dir);
        if(!path.exists())  Files.createDirectories(Paths.get(dir));
        File file = new File(dir+"/"+filename);
        
        FileCopyUtils.copy(photo.getInputStream(), new FileOutputStream(file));
        
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setUsedNo(usedNo);
        photoDTO.setDir(dir);
        photoDTO.setFileName(filename);
        photoDTO.setOriginalName(originname);
        photoDTO.setFileType(filetype);
        photoDTO.setFileSize(filesize);
        
        System.out.println("--사진업로드 시도중--");
        result = photoService.photoWrite(photoDTO);
        if(result >0) {
        	System.out.println("사진업로드 성공 ");
        	result = parkinglistService.parkinglistUpdateLink(usedNo, dir+"/"+filename);
        }else {
        	System.out.println("사진업로드 실패");
        }
        
        System.out.println("함수 종료 : parkedCarPhotoWrite");
		return result;
	}
	
	private int deletePhoto(HttpServletRequest request) {
		System.out.println("함수 실행 : deletePhoto");
		int usedNo = convert(request.getParameter("usedNo"));
		String dir = parkinglistService.parkinglistGetPhotoLink(usedNo);
		
		int result = photoService.photoDelete(usedNo);
		
//		실제 폴더에서 삭제
		if(result== 0) return 0;
		
		File file = new File(dir);
		if(file.exists()) {
			file.delete();
		}
		
		System.out.println("함수 종료 : deletePhoto");
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
		modelAndView.setViewName("parkinglist.jsp");
		return modelAndView;
	}
	

	
}
