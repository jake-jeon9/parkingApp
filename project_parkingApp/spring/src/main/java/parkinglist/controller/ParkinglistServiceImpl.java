package parkinglist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import parkinglist.bean.ParkinglistDTO;
import parkinglist.dao.ParkinglistDAO;

@Service
public class ParkinglistServiceImpl implements ParkinglistService {

	@Autowired
	ParkinglistDAO parkinglistDAO;
	
	@Override
	public int parkinglistInsert(ParkinglistDTO parkinglistDTO) {
		return parkinglistDAO.parkinglistInsert(parkinglistDTO);
	}
	@Override
	public int parkinglistUpdate(ParkinglistDTO parkinglistDTO) {
		return parkinglistDAO.parkinglistUpdate(parkinglistDTO);
	}
	@Override
	public int parkinglistModify(ParkinglistDTO parkinglistDTO) {
		return parkinglistDAO.parkinglistModify(parkinglistDTO);
	}

	@Override
	public int parkinglistDelete(int usedNo) {
		return parkinglistDAO.parkinglistDelete(usedNo);
	}
	@Override
	public ParkinglistDTO getSpacificitem(int memberNo, String plateNumOfCar,String state) {
		return parkinglistDAO.getSpacificitem(memberNo, plateNumOfCar,state);
	}
	@Override
	public List<ParkinglistDTO> getTodayAll(int memberNo, String targetState, String coupon,int startNum,int endNum) {
		return parkinglistDAO.getTodayAll(memberNo, targetState, coupon,startNum,endNum);
	}
	@Override
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate, String coupon,
			int startNum, int endNum) {
		return parkinglistDAO.parkinglistSelect(memberNo, startDate, endDate, coupon, startNum, endNum);
	}
	@Override
	public String getTimeFromServer() {
		return parkinglistDAO.getTimeFromServer();
	}
	@Override
	public int parkinglistUpdateLink(int usedNo, String photo_link) {
		return parkinglistDAO.parkinglistUpdateLink(usedNo, photo_link);
	}
	@Override
	public String parkinglistGetPhotoLink(int usedNo) {
		return parkinglistDAO.parkinglistGetPhotoLink(usedNo);
	}

	
	
	

	

}
