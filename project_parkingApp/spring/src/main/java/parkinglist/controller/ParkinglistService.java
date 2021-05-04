package parkinglist.controller;

import java.util.List;

import parkinglist.bean.ParkinglistDTO;

public interface ParkinglistService {

	public int parkinglistInsert(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistUpdate(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistModify(ParkinglistDTO parkinglistDTO);
	public int parkinglistDelete(int usedNo) ;
	public ParkinglistDTO getSpacificitem(int memberNo,String plateNumOfCar);
	public List<ParkinglistDTO> getTodayAll(int memberNo,int state,boolean coupon);
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate,String coupon,int startNum,int endNum) ;
}
