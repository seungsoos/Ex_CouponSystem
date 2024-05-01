package org.example.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.domain.Coupon;
import org.example.api.producer.CouponCreateProducer;
import org.example.api.repository.AppliedUserRepository;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    /**
     * 동시성 이슈 발생
     */
//    public void apply(Long userId) {
//        long count = couponRepository.count();
//
//        if (count >= 100) {
//            return;
//        }
//        couponRepository.save(new Coupon(userId));
//    }

    /**
     * redis incr 사용
     */
//    @Transactional
//    public void apply(Long userId) {
//        long count = couponCountRepository.increment();
//        log.info("count ={}", count);
//
//        if (count > 100) {
//            return;
//        }
//        Coupon save = couponRepository.save(new Coupon(userId));
//       log.info("coupon ={}",save.getId());
//    }

    /**
     * Kafka 사용
     */
//        public void apply(Long userId) {
//        long count = couponCountRepository.increment();
//
//        if (count > 100) {
//            return;
//        }
//        couponCreateProducer.create(userId);
//    }

    /**
     * 요구사항 변경으로 1인 1쿠폰 발급을 제한
     */
    public void apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);

        log.info("userId ={}, apply ={}", userId, apply);
        if (apply != 1) {
            return;
        }

        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }
        couponCreateProducer.create(userId);
    }
}
