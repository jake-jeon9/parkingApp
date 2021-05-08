package regular.controller;

import java.util.List;

import regular.bean.RegularDTO;

public interface RegularService {
	public int regularInsert(RegularDTO regularDTO);
	public int regularModify(RegularDTO regularDTO) ;
	public int regularDelete(int regularNo) ;
	public int regularUpdate(int regularNo) ;
	public List<RegularDTO> regularSelect(int memberNo,int type);
	public RegularDTO regularSelectSearchName(String plateNumOfCar,int memberNo);
	public int regularExtension(int regularNo,int addMonths,int cost);
}
