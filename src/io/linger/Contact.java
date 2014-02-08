package io.linger;

public class Contact
{	

	private String name = "none";
	private String phoneNumber = "none";
	private String emailAddress = "none";

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getName() {
		return name; 
	};

	public void setName(String name) {
		this.name = name;
	}

	public Contact( String name, String phoneNumber, String emailAddress)
	{
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

}
