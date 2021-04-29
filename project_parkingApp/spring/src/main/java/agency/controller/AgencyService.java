package agency.controller;

import java.util.List;

import agency.bean.AgencyDTO;

public interface AgencyService {
	public int agencyInsert(AgencyDTO agencyDTO) ;
	public int agencyModify(AgencyDTO agencyDTO) ;
	public int agencyDelete(AgencyDTO agencyDTO);
	public List<AgencyDTO> agencySelect(int memberNo);
}
