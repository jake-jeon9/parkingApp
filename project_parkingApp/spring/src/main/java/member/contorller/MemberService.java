package member.contorller;

import member.bean.MemberDTO;

public interface MemberService {
	// 멤버 데이터
	public int memberInsert(MemberDTO memberDTO);
	public int memberModify(MemberDTO memberDTO);
	public int memberDelete(int memberNo);
	public String memberSelect(String searchId);//아이디 찾기
	public MemberDTO memberLogin(MemberDTO memberDTO);
	
}
