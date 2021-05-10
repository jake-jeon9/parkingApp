package photo.dao;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import photo.bean.PhotoDTO;

@Repository
public class PhotoDAO {

	@Autowired
	SqlSessionTemplate sqlSession;
	

	public int photoWrite(PhotoDTO photoDTO) {
		return sqlSession.insert("mybatis.photoMapper.photoWrite", photoDTO);
	}

	public int photoDelete(int usedNo) {
		return sqlSession.delete("mybatis.photoMapper.photoDelete", usedNo);
	}
	public PhotoDTO photoSelect(String type, Object item) {
		HashMap<String,Object> map = new HashMap<>();
		if(type.equals("fileName")) {
			map.put("fileName",item);
		}else if(type.equals("photoId")) {
			map.put("photoId",item);
		}else if(type.equals("usedNo")) {
			map.put("usedNo",item);
		}
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectU", map);
	}
	/*
	 
	public PhotoDTO photoSelectByPlateNum(String plateNum) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectS", plateNum);
	}
	public PhotoDTO photoSelectByID(int photoId) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectI", photoId);
	}
	public PhotoDTO photoSelectByParkedId(int usedNo) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectU", usedNo);
	}
	 */
	
}
