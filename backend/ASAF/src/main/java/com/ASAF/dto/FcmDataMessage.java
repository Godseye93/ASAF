package com.ASAF.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmDataMessage {
    // validate_only가 true면 메시지를 발송하지 않습니다.
    private boolean validate_only;
    private Message message;

    public FcmDataMessage(boolean validate_only, Message message) {
		super();
		this.validate_only = validate_only;
		this.message = message;
	}
}
