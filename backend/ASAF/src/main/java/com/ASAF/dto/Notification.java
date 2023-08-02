package com.ASAF.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private String title;
    private String body;

    public Notification() {}

	public Notification(String title, String body, String image) {
		super();
		this.title = title;
		this.body = body;
	}

	@Override
	public String toString() {
		return "Notification{" +
				"title='" + title + '\'' +
				", body='" + body + '\'' +
				'}';
	}
}
