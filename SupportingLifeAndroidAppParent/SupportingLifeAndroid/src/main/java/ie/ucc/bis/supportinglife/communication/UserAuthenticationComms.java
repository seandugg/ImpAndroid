package ie.ucc.bis.supportinglife.communication;

import java.io.Serializable;


/** 
 * @author timothyosullivan
 */

public class UserAuthenticationComms implements Serializable {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 299542531889979099L;
	
	// User Details
	private String hsaUserId;
	private String password;

	public UserAuthenticationComms() {}

	public UserAuthenticationComms(String hsaUserId, String password) {
		setHsaUserId(hsaUserId);
		setPassword(password);
	}

	public String getHsaUserId() {
		return hsaUserId;
	}

	public void setHsaUserId(String hsaUserId) {
		this.hsaUserId = hsaUserId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
