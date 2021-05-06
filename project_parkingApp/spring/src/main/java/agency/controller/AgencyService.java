package agency.controller;

import java.util.List;

import agency.bean.AgencyDTO;

public interface AgencyService {
	public int agencyInsert(AgencyDTO agencyDTO) ;
	public int agencyModify(AgencyDTO agencyDTO) ;
	public int agencyDelete(AgencyDTO agencyDTO);
	public int agencyUpdate(int agencyNo);
	public int agencyExtension(int agencyNo,int addMonths,int cost);
	public String agencySelectSearchName(String nameOfAgency, int memberNo);
	public List<AgencyDTO> agencySelect(int memberNo,int type);
}
