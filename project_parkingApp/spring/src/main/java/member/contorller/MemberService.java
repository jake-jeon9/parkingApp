package member.contorller;

import member.bean.MemberDTO;

public interface MemberService {

	
	// 멤버 데이터
	public int memberInsert(MemberDTO memberDTO);
	public int memberModify(MemberDTO memberDTO);
	public int memberDelete(MemberDTO memberDTO);
	public MemberDTO memberSelect(String searchId);
	
}
