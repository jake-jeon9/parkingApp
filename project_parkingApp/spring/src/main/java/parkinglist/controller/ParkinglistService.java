package parkinglist.controller;

import java.util.List;

import parkinglist.bean.ParkinglistDTO;

public interface ParkinglistService {

	public int parkinglistInsert(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistUpdate(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistModify(ParkinglistDTO parkinglistDTO);
	public int parkinglistDelete(int usedNo) ;
	public String parkinglistGetPhotoLink(int usedNo);
	public int parkinglistUpdateLink(int usedNo,String photo_link);
	public ParkinglistDTO getSpacificitem(int memberNo,String plateNumOfCar,String state);
	public List<ParkinglistDTO> getTodayAll(int memberNo,String targetState,String coupon,int startNum,int endNum);
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate,String coupon,int startNum,int endNum) ;
	public String getTimeFromServer();
}
