package member.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import member.bean.MemberDTO;
import member.dao.MemberDAO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	MemberDAO memberDAO;

	@Override
	public int memberInsert(MemberDTO memberDTO) {
		return memberDAO.memberInsert(memberDTO);
	}

	@Override
	public int memberModify(MemberDTO memberDTO) {
		return memberDAO.memberModify(memberDTO);
	}

	@Override
	public int memberDelete(int memberNo) {
		return memberDAO.memberDelete(memberNo);
	}

	@Override
	public String memberSelect(String searchId) {
		return memberDAO.memberSelect(searchId);
	}

	@Override
	public MemberDTO memberLogin(MemberDTO memberDTO) {
		return memberDAO.memberLogin(memberDTO);
	}

}
