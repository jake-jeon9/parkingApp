package regular.bean;

public class RegularDTO {
	private int regularNo;
	private int memberNo;
	private String plateNumOfCar;
	private String guestName;
	private String guestContact;
	private String issueOfDate;
	private String expireOfDate;
	private int countOfextend;
	private int paid;
	private int usedCount;
	private String reg_date;
	
	public RegularDTO() {
		super();
	}
	
	public RegularDTO(int regularNo, int memberNo, String plateNumOfCar, String guestName, String guestContact,
			String issueOfDate, String expireOfDate, int countOfextend, int paid, int usedCount, String reg_date) {
		super();
		this.regularNo = regularNo;
		this.memberNo = memberNo;
		this.plateNumOfCar = plateNumOfCar;
		this.guestName = guestName;
		this.guestContact = guestContact;
		this.issueOfDate = issueOfDate;
		this.expireOfDate = expireOfDate;
		this.countOfextend = countOfextend;
		this.paid = paid;
		this.usedCount = usedCount;
		this.reg_date = reg_date;
	}



	public int getRegularNo() {
		return regularNo;
	}

	public void setRegularNo(int regularNo) {
		this.regularNo = regularNo;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public String getPlateNumOfCar() {
		return plateNumOfCar;
	}

	public void setPlateNumOfCar(String plateNumOfCar) {
		this.plateNumOfCar = plateNumOfCar;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestContact() {
		return guestContact;
	}

	public void setGuestContact(String guestContact) {
		this.guestContact = guestContact;
	}

	public String getIssueOfDate() {
		return issueOfDate;
	}

	public void setIssueOfDate(String issueOfDate) {
		this.issueOfDate = issueOfDate;
	}

	public String getExpireOfDate() {
		return expireOfDate;
	}

	public void setExpireOfDate(String expireOfDate) {
		this.expireOfDate = expireOfDate;
	}

	public int getCountOfextend() {
		return countOfextend;
	}

	public void setCountOfextend(int countOfextend) {
		this.countOfextend = countOfextend;
	}

	public int getPaid() {
		return paid;
	}

	public void setPaid(int paid) {
		this.paid = paid;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
	
	
}
