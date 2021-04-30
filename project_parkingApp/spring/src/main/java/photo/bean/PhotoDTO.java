package photo.bean;

public class PhotoDTO {
	private int photoId;
	private int usedNo;
	private String dir;
	private String fileName;
	private String originalName;
	private String fileType;
	private int fileSize;
	private String reg_date;
	
	
	public PhotoDTO() {
		super();
	}


	public PhotoDTO(int photoId, int usedNo, String dir, String fileName, String originalName, String fileType,
			int fileSize, String reg_date) {
		super();
		this.photoId = photoId;
		this.usedNo = usedNo;
		this.dir = dir;
		this.fileName = fileName;
		this.originalName = originalName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.reg_date = reg_date;
	}


	public int getPhotoId() {
		return photoId;
	}


	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}


	public int getUsedNo() {
		return usedNo;
	}


	public void setUsedNo(int usedNo) {
		this.usedNo = usedNo;
	}


	public String getDir() {
		return dir;
	}


	public void setDir(String dir) {
		this.dir = dir;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getOriginalName() {
		return originalName;
	}


	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public int getFileSize() {
		return fileSize;
	}


	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}


	public String getReg_date() {
		return reg_date;
	}


	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}


	
	
}
