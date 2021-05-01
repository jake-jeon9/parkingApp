package member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import member.bean.MemberDTO;

@Repository
public class MemberDAO {

	SqlSessionTemplate sqlSession;
	
	public int memberInsert(MemberDTO memberDTO) {
		return sqlSession.insert("mybatis.memberMapper.memberInsert", memberDTO);
	}
	public int memberModify(MemberDTO memberDTO) {
		return sqlSession.update("mybatis.memberMapper.memberModify", memberDTO);
	}
	public int memberDelete(int memberNo) {
		return sqlSession.delete("mybatis.memberMapper.memberDelete", memberNo);
	}
	public MemberDTO memberSelect(String searchId) {
		return sqlSession.selectOne("mybatis.memberMapper.memberSelect", searchId);
	}
	
}
