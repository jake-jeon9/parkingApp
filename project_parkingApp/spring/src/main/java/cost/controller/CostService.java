package cost.controller;

import cost.bean.CostDTO;

public interface CostService {
	public int costInsert(CostDTO costDTO) ;
	public int costModify(CostDTO costDTO) ;
	public int costDelete(int memberNo) ;
	public int costSelect(CostDTO costDTO) ;
}
