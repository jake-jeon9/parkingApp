package member.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import member.bean.MemberDTO;

@Repository
public class MemberDAO {

	@Autowired
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
	//이메일 조회
	public String memberSelect(String memberId) {
		return sqlSession.selectOne("mybatis.memberMapper.memberSelect", memberId);
	}
	
	//로그인
	public MemberDTO memberLogin(MemberDTO memberDTO) {
		return sqlSession.selectOne("mybatis.memberMapper.memberlogin", memberDTO);
	}
	
	//비밀번호찾기
//	
//	public String memberSetMyInfo(MemberDTO memberDTO) {
//		return sqlSession.selectOne("mybatis.memberMapper.memberSetMyInfo", memberDTO);
//	}
}
