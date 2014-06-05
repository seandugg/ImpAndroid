package ie.ucc.bis.supportinglife.communication;

import java.io.Serializable;
import java.util.Locale;


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
	private Boolean authenticated;

	public UserAuthenticationComms() {}

	public UserAuthenticationComms(String hsaUserId, String password) {
		// convert user id to lowercase to avoid case-sensitivity 
		// however password is case sensitive 
		setHsaUserId(hsaUserId.toLowerCase(Locale.UK));
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

	public Boolean getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(Boolean authenticated) {
		this.authenticated = authenticated;
	}
}
