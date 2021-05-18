package cost.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cost.bean.CostDTO;
import cost.bean.CouponDTO;


@Repository
public class CostDAO {

	@Autowired
	SqlSessionTemplate sqlSession;
	
	public int costInsert(int memberNo) {
		//System.out.println("memberno?"+memberNo);
		return sqlSession.insert("mybatis.costMapper.costInsert", memberNo);
	}
	public int costModify(CostDTO costDTO) {
		return sqlSession.update("mybatis.costMapper.costModify", costDTO);
	}
	public int costDelete(int memberNo) {
		return sqlSession.delete("mybatis.costMapper.costDelete", memberNo);
	}
	public CostDTO costSelect(int memberNo) {
		return sqlSession.selectOne("mybatis.costMapper.costSelect", memberNo);
	}
	
	public int couponInsert(CouponDTO couponDTO) {
		return sqlSession.insert("mybatis.costMapper.couponInsert", couponDTO);
	}
	public CouponDTO couponSelect(int usedNo) {
		return sqlSession.selectOne("mybatis.costMapper.couponSelect", usedNo);
	}
	public int couponDelete(int usedNo) {
		return sqlSession.delete("mybatis.costMapper.couponDelete", usedNo);
	}
	
	
}
