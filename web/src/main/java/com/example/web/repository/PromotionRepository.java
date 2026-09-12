package com.example.web.repository;

import com.example.web.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByCouponCodeAndStatusTrueAndStartDateBeforeAndEndDateAfter(
            String couponCode, LocalDateTime now1, LocalDateTime now2);
    Optional<Promotion> findByCouponCode(String couponCode);
    boolean existsByCouponCode(String couponCode);
}
