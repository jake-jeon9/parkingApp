package parkinglist.controller;

import java.util.List;

import parkinglist.bean.ParkinglistDTO;

public interface ParkinglistService {

	public int parkinglistInsert(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistUpdate(ParkinglistDTO parkinglistDTO) ;
	public int parkinglistModify(ParkinglistDTO parkinglistDTO);
	public int parkinglistDelete(int usedNo) ;
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate);
}
