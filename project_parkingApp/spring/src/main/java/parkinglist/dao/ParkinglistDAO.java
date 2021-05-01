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
	
	//내주차장정보
	public List<ParkinglistDTO> parkinglistSelect(int memberNo, String startDate, String endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memberNo", memberNo);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return sqlSession.selectList("mybatis.parkinglistMapper.mparkinglistSelect", map);
	}

	
}
