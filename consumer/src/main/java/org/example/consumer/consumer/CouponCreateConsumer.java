package org.example.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.consumer.domain.Coupon;
import org.example.consumer.domain.FailedEvent;
import org.example.consumer.repository.CouponRepository;
import org.example.consumer.repository.FailedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponCreateConsumer {

    private final CouponRepository couponRepository;
    private final FailedEventRepository failedEventRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1", containerFactory = "listenerContainerFactory")
    public void lister(Long userId) {
        try {
            log.info("consumer :: userId = {}", userId);
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            log.error("consumer error :: userId = {}", userId);
            failedEventRepository.save(new FailedEvent(userId));
        }
    }

}
