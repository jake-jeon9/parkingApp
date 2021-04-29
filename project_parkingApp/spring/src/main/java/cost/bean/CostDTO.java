package cost.bean;

public class CostDTO {
	private int memberNo;
    private int additionalCost; 
    private int additionalTiem;
    private int baseCost;
    private int baseTime;
    private int maxcost;
    private int maxtime;
    private int maxArea;
    private String reg_date;
    
	public CostDTO() {
		super();
	}
	
	

	public CostDTO(int memberNo, int additionalCost, int additionalTiem, int baseCost, int baseTime, int maxcost,
			int maxtime, int maxArea, String reg_date) {
		super();
		this.memberNo = memberNo;
		this.additionalCost = additionalCost;
		this.additionalTiem = additionalTiem;
		this.baseCost = baseCost;
		this.baseTime = baseTime;
		this.maxcost = maxcost;
		this.maxtime = maxtime;
		this.maxArea = maxArea;
		this.reg_date = reg_date;
	}



	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public int getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(int additionalCost) {
		this.additionalCost = additionalCost;
	}

	public int getAdditionalTiem() {
		return additionalTiem;
	}

	public void setAdditionalTiem(int additionalTiem) {
		this.additionalTiem = additionalTiem;
	}

	public int getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(int baseCost) {
		this.baseCost = baseCost;
	}

	public int getBaseTime() {
		return baseTime;
	}

	public void setBaseTime(int baseTime) {
		this.baseTime = baseTime;
	}

	public int getMaxcost() {
		return maxcost;
	}

	public void setMaxcost(int maxcost) {
		this.maxcost = maxcost;
	}

	public int getMaxtime() {
		return maxtime;
	}

	public void setMaxtime(int maxtime) {
		this.maxtime = maxtime;
	}

	public int getMaxArea() {
		return maxArea;
	}

	public void setMaxArea(int maxArea) {
		this.maxArea = maxArea;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
    
}
