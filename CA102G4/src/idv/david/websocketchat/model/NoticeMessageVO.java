package idv.david.websocketchat.model;

public class NoticeMessageVO {
	private String title;
	private String sender;
	private String receiver;
	private String message;
	
	public NoticeMessageVO(String id,String sender,String receiver,String message) {
		// TODO Auto-generated constructor stub
		this.title=id;
		this.receiver=receiver;
		this.sender=sender;
		this.message=message;
	}
	
	public String getId() {
		return title;
	}
	public void setId(String id) {
		this.title = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	

	
}
