package com.cg.gold.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cg.gold.dto.VirtualGoldPurchasedEvent;

@Service
public class KafkaVirtualGoldConsumer {

	@KafkaListener(topics = "virtual-gold-purchased-events", groupId = "gold-group", containerFactory = "kafkaListenerContainerFactory")
	public void consume(VirtualGoldPurchasedEvent event) {
		System.out.println("✅ Received Virtual Gold Purchase Event from Kafka: " + event.toString());
	}
}
