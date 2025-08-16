package com.cg.gold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.gold.entity.Payment;
import com.cg.gold.entity.Payment.PaymentMethod;
import com.cg.gold.entity.Payment.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

	List<Payment> findByUserUserId(Integer userId);

	List<Payment> findByPaymentStatus(PaymentStatus status);

	List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

	List<Payment> findByUserUserIdOrderByCreatedAtDesc(Integer userId);

}
