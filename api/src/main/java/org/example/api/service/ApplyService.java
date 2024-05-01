package org.example.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.domain.Coupon;
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
    @Transactional
    public void apply(Long userId) {
        long count = couponCountRepository.increment();
        log.info("count ={}", count);

        if (count > 100) {
            return;
        }
        Coupon save = couponRepository.save(new Coupon(userId));
       log.info("coupon ={}",save.getId());
    }
}
