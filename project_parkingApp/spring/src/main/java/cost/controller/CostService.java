package cost.controller;

import cost.bean.CostDTO;
import cost.bean.CouponDTO;

public interface CostService {
	public int costInsert(int memberNo) ;
	public int costModify(CostDTO costDTO) ;
	public int costDelete(int memberNo) ;
	public CostDTO costSelect(int memberNo) ;
	public int couponInsert(CouponDTO couponDTO);
	public CouponDTO couponSelect(int usedNo);
	public int couponDelete(int usedNo);
}
