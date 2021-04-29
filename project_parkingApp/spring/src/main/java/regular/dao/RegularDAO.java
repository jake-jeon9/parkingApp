package regular.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import agency.bean.AgencyDTO;
import regular.bean.RegularDTO;

@Repository
public class RegularDAO {


	@Autowired
	SqlSessionTemplate sqlSession;
	public int regularInsert(RegularDTO regularDTO) {
		return sqlSession.insert("mybatis.regularMapper.regularInsert", regularDTO);
	}
	public int regularModify(RegularDTO regularDTO) {
		return sqlSession.update("mybatis.regularMapper.regularModify", regularDTO);
	}
	public int regularDelete(int regularNo) {
		return sqlSession.delete("mybatis.regularMapper.regularDelete", regularNo);
	}
	public List<RegularDTO> regularSelect(int memberNo) {
		return sqlSession.selectList("mybatis.regularMapper.regularSelect", memberNo);
	}
	
}
