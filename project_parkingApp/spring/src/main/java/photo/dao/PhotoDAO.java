package photo.dao;

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

	public int photoDelete(int photoId) {
		return sqlSession.delete("mybatis.photoMapper.photoDelete", photoId);
	}
	public PhotoDTO photoSelectByPlateNum(String plateNum) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectS", plateNum);
	}
	public PhotoDTO photoSelectByID(int photoId) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectI", photoId);
	}
	public PhotoDTO photoSelectByParkedId(int usedNo) {
		return sqlSession.selectOne("mybatis.photoMapper.photoSelectU", usedNo);
	}
	
	
}
