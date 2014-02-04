package io.linger;

public class Message {
	private static final String THREAD_ID = "thread_id";
	private static final String PHONE_NUMBER_ADDRESS = "address";
	private static final String CONTENT = "body";
	private static final String NAME = "person";
	private static final String DATE = "date";
	private static final String DATE_SENT = "date_sent";
	private static final String READ = "read"; // 0 or 1; 1 is read, 0 is unread
	
	private String thread_id;
	private String phoneNumberAddress;
	private String content;
	private String senderName;
	
	public Message()
	{
		
	}
	
	public Message(String name, String content, String phoneNumber)
	{
		this.senderName = name;
		this.content = content;
		this.phoneNumberAddress = phoneNumber;
	}
	
}
