package cost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cost.bean.CostDTO;
import cost.dao.CostDAO;

@Service
public class CostServiceImpl implements CostService{

	
	@Autowired
	CostDAO costDAO;
	
	@Override
	public int costInsert(int memberNo) {
		return costDAO.costInsert(memberNo);
	}

	@Override
	public int costModify(CostDTO costDTO) {
		return costDAO.costModify(costDTO);
	}

	@Override
	public int costDelete(int memberNo) {
		return costDAO.costDelete(memberNo);
	}

	@Override
	public CostDTO costSelect(int memberNo) {
		return costDAO.costSelect(memberNo);
	}

}
