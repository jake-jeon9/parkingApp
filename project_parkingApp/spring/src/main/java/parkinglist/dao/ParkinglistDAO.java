package parkinglist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import parkinglist.bean.ParkinglistDTO;



@Repository
public class ParkinglistDAO {

	@Autowired
	SqlSessionTemplate sqlSession;

	public int parkinglistInsert(ParkinglistDTO parkinglistDTO) {
		return sqlSession.insert("mybatis.parkinglistMapper.parkinglistInsert", parkinglistDTO);
	}
	public int parkinglistUpdate(ParkinglistDTO parkinglistDTO) {
		return sqlSession.update("mybatis.parkinglistMapper.parkinglistUpdate", parkinglistDTO);
	}
	
	public int parkinglistModify(ParkinglistDTO parkinglistDTO) {
		return sqlSession.update("mybatis.parkinglistMapper.parkinglistModify", parkinglistDTO);
	}
	public int parkinglistDelete(int usedNo) {
		return sqlSession.delete("mybatis.parkinglistMapper.parkinglistDelete", usedNo);
	}
	public String parkinglistGetPhotoLink(int usedNo) {
		return sqlSession.selectOne("mybatis.parkinglistMapper.parkinglistGetPhotoLink", usedNo);
	}
	
	public int parkinglistUpdateLink(int usedNo,String photo_link) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("usedNo",usedNo);
		map.put("photo_link",photo_link);
		return sqlSession.delete("mybatis.parkinglistMapper.parkinglistupdateLink", map);
	}
	
	public ParkinglistDTO getSpacificitem(int memberNo,String plateNumOfCar,String state) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("plateNumOfCar", plateNumOfCar);
		map.put("state", state);
		return sqlSession.selectOne("mybatis.parkinglistMapper.getSpacificitem", map);
	}
	
	public List<ParkinglistDTO> getTodayAll(int memberNo,String targetState,String coupon, int startNum, int endNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("state", targetState);
		map.put("coupon", coupon);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.parkinglistMapper.getTodayAll", map);
	}
	
	//특정 기간 조회 startDate 가 null로 오면 전체 기간 조회
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate,String coupon,int startNum,int endNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("coupon", coupon);
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		return sqlSession.selectList("mybatis.parkinglistMapper.parkinglistSelect", map);
	}
	
	//서버로 부터 시간얻기
	public String getTimeFromServer() {
		return sqlSession.selectOne("mybatis.parkinglistMapper.getTimeFromServer");
	}

	
}
