package com.cbh.bankservice.requestdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaNotificationRequestDto {
	
	private Object message;
	private String topic;
	private String key;
	private String email;
	private String subject;

}
