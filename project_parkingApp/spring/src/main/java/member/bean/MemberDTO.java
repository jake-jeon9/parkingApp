package member.bean;

public class MemberDTO {
    private int memberNo; 
    private String memberId;
    private String pw;
    private String email;
    private String nameOfParkingArea;
    private String phone;
    private String reg_date;
    private String device_token;
    
	public MemberDTO() {
		super();
	}
	
	public MemberDTO(int memberNo, String memberId, String pw, String email, String nameOfParkingArea, String phone,
			String reg_date, String device_token) {
		super();
		this.memberNo = memberNo;
		this.memberId = memberId;
		this.pw = pw;
		this.email = email;
		this.nameOfParkingArea = nameOfParkingArea;
		this.phone = phone;
		this.reg_date = reg_date;
		this.device_token = device_token;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNameOfParkingArea() {
		return nameOfParkingArea;
	}

	public void setNameOfParkingArea(String nameOfParkingArea) {
		this.nameOfParkingArea = nameOfParkingArea;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

    
}
