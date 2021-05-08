package cost.bean;

public class CouponDTO {
	private int usedNo;
    private int memberNo;
    private int targetNo;
    private String target_type;
    
	public CouponDTO() {
		super();
	}

	public CouponDTO(int usedNo, int memberNo, int targetNo, String target_type) {
		super();
		this.usedNo = usedNo;
		this.memberNo = memberNo;
		this.targetNo = targetNo;
		this.target_type = target_type;
	}

	public int getUsedNo() {
		return usedNo;
	}

	public void setUsedNo(int usedNo) {
		this.usedNo = usedNo;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	public int getTargetNo() {
		return targetNo;
	}

	public void setTargetNo(int targetNo) {
		this.targetNo = targetNo;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}    
	
    
}
