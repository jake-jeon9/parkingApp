package agency.bean;
  
public class AgencyDTO {

	private int agencyNo;
	private int memberNo;
	private String nameOfAgency;
	private String contactName;
	private String contactPhone;
	private String issueOfDate;
	private String expireOfDate;
    private int countOfextend;
    private int paid;
    private int usedCount;
    private String reg_date;
    
	public AgencyDTO() {
		super();
	}
	public AgencyDTO(int agencyNo, int memberNo, String nameOfAgency, String contactName, String contactPhone,
			String issueOfDate, String expireOfDate, int countOfextend, int paid, int usedCount, String reg_date) {
		super();
		this.agencyNo = agencyNo;
		this.memberNo = memberNo;
		this.nameOfAgency = nameOfAgency;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.issueOfDate = issueOfDate;
		this.expireOfDate = expireOfDate;
		this.countOfextend = countOfextend;
		this.paid = paid;
		this.usedCount = usedCount;
		this.reg_date = reg_date;
	}
	public int getAgencyNo() {
		return agencyNo;
	}
	public void setAgencyNo(int agencyNo) {
		this.agencyNo = agencyNo;
	}
	public int getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}
	public String getNameOfAgency() {
		return nameOfAgency;
	}
	public void setNameOfAgency(String nameOfAgency) {
		this.nameOfAgency = nameOfAgency;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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
