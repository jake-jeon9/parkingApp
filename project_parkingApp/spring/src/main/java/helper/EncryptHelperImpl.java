package helper;

//import org.mindrot.jbcrypt.BCrypt;

public class EncryptHelperImpl implements EncryptHelper {

	@Override
	public String encrypt(String password) {
		String pw = "";
		//pw = BCrypt.hashpw(password, BCrypt.gensalt());
		return pw;
	}

	@Override
	public boolean isMatch(String password, String hashed) {
		boolean result =false;
//		if (BCrypt.checkpw(password, hashed)) result = true;
		return result;
	}
	
}
