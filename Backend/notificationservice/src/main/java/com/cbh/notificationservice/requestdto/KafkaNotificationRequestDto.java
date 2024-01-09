package com.cbh.notificationservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaNotificationRequestDto {

	private Object message;
	private String topic;
	private String key;
	private String email;
	private String subject;

}
