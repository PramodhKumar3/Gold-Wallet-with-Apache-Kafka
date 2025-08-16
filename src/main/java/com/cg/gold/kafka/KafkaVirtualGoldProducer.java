package com.cg.gold.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cg.gold.dto.VirtualGoldPurchasedEvent;
import com.cg.gold.entity.VirtualGoldHolding;

@Service
public class KafkaVirtualGoldProducer {

	private static final String TOPIC = "virtual-gold-purchased-events";

	@Autowired
	private KafkaTemplate<String, VirtualGoldPurchasedEvent> kafkaTemplate;

	public void sendEvent(VirtualGoldHolding holding) {
		VirtualGoldPurchasedEvent event = new VirtualGoldPurchasedEvent();
		event.setHoldingId(holding.getHoldingId());
		event.setUserId(holding.getUser().getUserId());
		event.setBranchId(holding.getBranch().getBranchId());
		event.setQuantity(holding.getQuantity());
		event.setCreatedAt(holding.getCreatedAt());
		event.setUserEmail(holding.getUser().getEmail());
		event.setBranchLocation(holding.getBranch().getAddress().getCity());

		kafkaTemplate.send(TOPIC, event);
	}
}
