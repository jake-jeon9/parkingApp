package photo.controller;

import photo.bean.PhotoDTO;

public interface PhotoService {
	
	public int photoWrite(PhotoDTO photoDTO);
	public int photoDelete(int photoId);
	public PhotoDTO photoSelect(String type, Object item);
//	public PhotoDTO photoSelectByPlateNum(String plateNum);
//	public PhotoDTO photoSelectByID(int photoId);
//	public PhotoDTO photoSelectByParkedId(int usedNo);
	
}
