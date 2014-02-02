package io.linger;

public class Contact {
	private String contactId;
	private String name;
	private String phoneNumber;
	private String emailAddress;
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String type; 
	
	public Contact(String contactId, String name, String phoneNumber,
			String emailAddress, String poBox, String street, 
			String city, String state, String postalCode, String country,
			String type)
	{
		this.contactId = contactId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.poBox = poBox;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.type = type;
	}
	
	public Contact(String contactId, String name, String phoneNumber, String emailAddress)
	{
		this.contactId = contactId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	// TODO
	public void postToDatabase()
	{
		
	}
	
	public String toString()
	{
		String complete = "\n";
		complete += "ContactId: \t" + contactId + "\n";
		complete += "Name: \t" + name + "\n";
		complete += "Phone number: \t" + phoneNumber + "\n";
		complete += "Email address: \t" + emailAddress + "\n";
		return complete;
	}
}
