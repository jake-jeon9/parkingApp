package cost.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cost.bean.CostDTO;


@Repository
public class CostDAO {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int costInsert(int memberNo) {
		return sqlSession.insert("mybatis.costMapper.costInsert", memberNo);
	}
	public int costModify(CostDTO costDTO) {
		return sqlSession.update("mybatis.costMapper.costModify", costDTO);
	}
	public int costDelete(int memberNo) {
		return sqlSession.delete("mybatis.costMapper.costDelete", memberNo);
	}
	public int costSelect(CostDTO costDTO) {
		return sqlSession.selectOne("mybatis.costMapper.costSelectOne", costDTO);
	}
	
}
