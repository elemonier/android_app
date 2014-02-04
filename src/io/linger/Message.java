package io.linger;

import java.util.HashMap;

public class Message {
	public static final String THREAD_ID = "thread_id";
	public static final String PHONE_NUMBER_ADDRESS = "address";
	public static final String CONTENT = "body";
	public static final String NAME = "person";
	public static final String DATE = "date";
	public static final String DATE_SENT = "date_sent";
	public static final String READ = "read"; // 0 or 1; 1 is read, 0 is unread
	
	private String threadId;
	private String phoneNumberAddress;
	private String content;
	private String senderName;
	private String dateSent;
	
	public Message()
	{}
	
	/**
	 * Sets the variables for a message object.
	 * @param name
	 * @param content
	 * @param phoneNumber
	 * @param dateSent
	 */
	public Message(String threadId, String phoneNumber, String content, 
			String name, String dateSent)
	{
		this.threadId = threadId;
		this.senderName = name;
		this.content = content;
		this.phoneNumberAddress = phoneNumber;
		this.dateSent = dateSent;
	}
	
	/**
	 * Returns a HashMap of all the values
	 * @return
	 */
	public HashMap<String, String> getMap()
	{
		HashMap<String, String> messageInfo = new HashMap<String, String>();
        messageInfo.put(THREAD_ID, threadId);
        messageInfo.put(NAME, senderName);
        messageInfo.put(CONTENT, content);
        messageInfo.put(PHONE_NUMBER_ADDRESS, phoneNumberAddress);
        return messageInfo;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Sender name: " + senderName + ", ");
		sb.append("Message content: " + content + ", ");
		sb.append("Phone number or address: " + phoneNumberAddress + ", ");
		sb.append("Date sent: " + dateSent);
		return sb.toString();
	}
}
