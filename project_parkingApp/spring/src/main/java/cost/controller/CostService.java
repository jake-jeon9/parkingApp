package cost.controller;

import cost.bean.CostDTO;

public interface CostService {
	public int costInsert(int memberNo) ;
	public int costModify(CostDTO costDTO) ;
	public int costDelete(int memberNo) ;
	public CostDTO costSelect(int memberNo) ;
}
