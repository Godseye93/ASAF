package com.ASAF.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private Notification notification;
//    private String token;
    private List<String> token;
    private Map<String, String> data;

	public Message() {}

	public Message(Notification notification, List<String> token) {
		super();
		this.notification = notification;
		this.token = token;
	}

	public Message(Notification notification, List<String> token, Map<String, String> data) {
		super();
		this.notification = notification;
		this.token = token;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Message{" +
				"notification=" + notification +
				", token='" + token + '\'' +
				", data=" + data +
				'}';
	}
}
