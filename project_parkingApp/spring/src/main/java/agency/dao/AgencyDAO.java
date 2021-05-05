package agency.dao;
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
	public int agencyUpdate(int memberNo) {
		return sqlSession.update("mybatis.agencyMapper.agencyUpdate", memberNo);
	}
	
	public List<AgencyDTO> agencySelect(int memberNo) {
		return sqlSession.selectList("mybatis.agencyMapper.agencySelect", memberNo);
	}
	

}
