package agency.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import agency.bean.AgencyDTO;
import agency.dao.AgencyDAO;

@Service
public class AgencyServiceImpl implements AgencyService{

	@Autowired
	AgencyDAO agencyDAO;
	
	@Override
	public int agencyInsert(AgencyDTO agencyDTO) {
		return agencyDAO.agencyInsert(agencyDTO);
	}

	@Override
	public int agencyModify(AgencyDTO agencyDTO) {
		return agencyDAO.agencyModify(agencyDTO);
	}

	@Override
	public int agencyDelete(AgencyDTO agencyDTO) {
		return agencyDAO.agencyDelete(agencyDTO);
	}

	@Override
	public List<AgencyDTO> agencySelect(int memberNo) {
		return agencyDAO.agencySelect(memberNo);
	}

	@Override
	public int agencyUpdate(int memberNo) {
		return agencyDAO.agencyUpdate(memberNo);
	}

}
