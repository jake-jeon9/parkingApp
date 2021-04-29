package regular.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import regular.bean.RegularDTO;
import regular.dao.RegularDAO;

@Service
public class RegularServiceImpl implements RegularService {

	@Autowired
	RegularDAO regularDAO;
	
	@Override
	public int regularInsert(RegularDTO regularDTO) {
		return regularDAO.regularInsert(regularDTO);
	}

	@Override
	public int regularModify(RegularDTO regularDTO) {
		return regularDAO.regularModify(regularDTO);
	}

	@Override
	public int regularDelete(int regularNo) {
		return regularDAO.regularDelete(regularNo);
	}

	@Override
	public List<RegularDTO> regularSelect(int memberNo) {
		return regularDAO.regularSelect(memberNo);
	}

}
