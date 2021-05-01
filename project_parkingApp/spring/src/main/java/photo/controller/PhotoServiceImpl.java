package photo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import photo.bean.PhotoDTO;
import photo.dao.PhotoDAO;

@Service
public class PhotoServiceImpl implements PhotoService{

	@Autowired
	PhotoDAO photoDAO;

	@Override
	public int photoWrite(PhotoDTO photoDTO) {
		return photoDAO.photoWrite(photoDTO);
	}

	@Override
	public int photoDelete(int photoId) {
		return photoDAO.photoDelete(photoId);
	}

	@Override
	public PhotoDTO photoSelectByPlateNum(String plateNum) {
		return photoDAO.photoSelectByPlateNum(plateNum);
	}

	@Override
	public PhotoDTO photoSelectByID(int photoId) {
		return photoDAO.photoSelectByID(photoId);
	}

	@Override
	public PhotoDTO photoSelectByParkedId(int usedNo) {
		return photoDAO.photoSelectByParkedId(usedNo);
	}

	

}
