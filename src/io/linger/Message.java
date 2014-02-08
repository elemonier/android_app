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
	public Message(String threadId, String phoneNumber, String content, String dateSent)
	{
		this.threadId = threadId;
		this.content = content;
		this.phoneNumberAddress = phoneNumber;
		this.dateSent = dateSent;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("phone: " + phoneNumberAddress + ", ");
		sb.append("when_receive: " + dateSent + ", ");
		sb.append("content: " + content + ", ");
		sb.append("thread_id: " + threadId + ", ");
		return sb.toString();
	}
	
	
	public String getPhone() { return phoneNumberAddress; }
	public String getDateSent() { return dateSent; }
	public String getContent() { return content; }
	public String getThreadId() { return threadId; }
}
