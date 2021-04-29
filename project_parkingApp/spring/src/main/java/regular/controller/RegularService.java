package regular.controller;

import java.util.List;

import regular.bean.RegularDTO;

public interface RegularService {
	public int regularInsert(RegularDTO regularDTO);
	public int regularModify(RegularDTO regularDTO) ;
	public int regularDelete(int regularNo) ;
	public List<RegularDTO> regularSelect(int memberNo);
}
