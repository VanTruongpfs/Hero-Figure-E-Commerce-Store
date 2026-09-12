package com.example.web.repository;

import com.example.web.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdAndStatusTrue(Long productId);
    Page<Review> findByProductIdAndStatusTrue(Long productId, Pageable pageable);
    boolean existsByUserIdAndProductIdAndOrderId(Long userId, Long productId, Long orderId);
}
