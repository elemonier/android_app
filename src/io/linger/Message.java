package io.linger;

public class Message {
	
	private String threadId = "none";
	private String phoneNumberAddress = "none";
	private String content = "none";
	private String dateSent = "none";
	
	public Message()
	{}
	
	/**
	 * Sets the variables for a message object.
	 * @param name
	 * @param content
	 * @param phoneNumber
	 * @param dateSent
	 */
	public Message(String threadId, String phoneNumber, String content, String dateSent)
	{
		this.setThreadId(threadId);
		this.setContent(content);
		this.setPhoneNumberAddress(phoneNumber);
		this.setDateSent(dateSent);
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getPhoneNumberAddress() {
		return phoneNumberAddress;
	}

	public void setPhoneNumberAddress(String phoneNumberAddress) {
		this.phoneNumberAddress = phoneNumberAddress;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateSent() {
		return dateSent;
	}

	public void setDateSent(String dateSent) {
		this.dateSent = dateSent;
	}

}
