package regular.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public int regularUpdate(int regularNo) {
		return sqlSession.update("mybatis.regularMapper.regularUpdate", regularNo);
	}
	public List<RegularDTO> regularSelect(int memberNo,int type) {
		HashMap<String,Integer> map = new HashMap<>();
		map.put("memberNo",memberNo);
		map.put("type",type);
		return sqlSession.selectList("mybatis.regularMapper.regularSelect", map);
	}
	public RegularDTO regularSelectSearchName(String plateNumOfCar,int memberNo){
		HashMap<String,Object> map =new HashMap<>();
		map.put("plateNumOfCar", plateNumOfCar);
		map.put("memberNo", memberNo);
		return sqlSession.selectOne("mybatis.regularMapper.regularSelectSearchName", map);
	}
	public int regularExtension(int regularNo,int addMonths,int cost) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("regularNo",regularNo);
		map.put("addMonths",addMonths);
		map.put("paid",cost);
		return sqlSession.update("mybatis.regularMapper.regularExtension", map);
	}
	
}
