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
	
	public ParkinglistDTO getSpacificitem(int memberNo,String plateNumOfCar) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("startDate", plateNumOfCar);
		return sqlSession.selectOne("mybatis.parkinglistMapper.getSpacificitem", map);
	}
	
	public List<ParkinglistDTO> getTodayAll(int memberNo,int state,String coupon) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("state", state);
		map.put("coupon", coupon);
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

	
}
