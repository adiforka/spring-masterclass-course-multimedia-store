package com.adison.shop.payments;

import com.adison.shop.common.profiler.ExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;

import javax.transaction.Transactional;
import java.time.Instant;

@Log
@Transactional
@RequiredArgsConstructor
public class BasicPaymentService {

    private final PaymentIdGenerator paymentIdGenerator;
    private final PaymentRepository paymentRepository;
    //think this is the Observable in the Observer pattern, and it's publishEvent method is the notify method from the
    //pattern
    private final ApplicationEventPublisher eventPublisher;

    @ExecutionTime
    @LogPayments
    public Payment process(PaymentRequest paymentRequest)  {
        var payment = Payment.builder()
                .id(paymentIdGenerator.getNext())
                .money(paymentRequest.getMoney())
                .timestamp(Instant.now())
                .status(PaymentStatus.STARTED)
                .build();
        //the this argument refers to the current FakePaymentService instance. just saying this as remedy
        //for future brain freezes
        eventPublisher.publishEvent(new PaymentStatusChangedEvent(this, payment));
        return paymentRepository.save(payment);
    }
}