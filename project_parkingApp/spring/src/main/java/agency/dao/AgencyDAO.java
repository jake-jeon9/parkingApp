package agency.dao;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import agency.bean.AgencyDTO;
@Repository
public class AgencyDAO {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int agencyInsert(AgencyDTO agencyDTO) {
		return sqlSession.insert("mybatis.agencyMapper.agencyInsert", agencyDTO);
	}
	public int agencyModify(AgencyDTO agencyDTO) {
		return sqlSession.update("mybatis.agencyMapper.agencyModify", agencyDTO);
	}
	public int agencyDelete(AgencyDTO agencyDTO) {
		return sqlSession.delete("mybatis.agencyMapper.agencyDelete", agencyDTO);
	}
	public int agencyUpdate(int agencyNo) {
		return sqlSession.update("mybatis.agencyMapper.agencyUpdate", agencyNo);
	}
	
	public int agencyExtension(int agencyNo,int addMonths,int cost) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("agencyNo",agencyNo);
		map.put("addMonths",addMonths);
		map.put("paid",cost);
		System.out.println(map.toString());
		return sqlSession.update("mybatis.agencyMapper.agencyExtension", map);
	}
	
	public List<AgencyDTO> agencySelect(int memberNo,int type) {
		HashMap<String,Integer> map = new HashMap<>();
		map.put("memberNo",memberNo);
		map.put("type",type);
		return sqlSession.selectList("mybatis.agencyMapper.agencySelect", map);
	}
	public String agencySelectSearchName(String nameOfAgency,int memberNo){
		HashMap<String,Object> map =new HashMap<>();
		map.put("nameOfAgency", nameOfAgency);
		map.put("memberNo", memberNo);
		return sqlSession.selectOne("mybatis.agencyMapper.agencySelectSearchName", map);
	}
	

}