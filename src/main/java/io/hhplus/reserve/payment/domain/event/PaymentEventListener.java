package io.hhplus.reserve.payment.domain.event;

import io.hhplus.reserve.external.application.ExternalService;
import io.hhplus.reserve.waiting.domain.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final WaitingService waitingService;
    private final ExternalService externalService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRemoveToken(PaymentSuccessEvent event) {
        log.info("PaymentEventListener::: removeToken {}", event);
        waitingService.removeActiveToken(event.getToken());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotifyPayment(PaymentSuccessEvent event) {
        log.info("PaymentEventListener::: notify {}", event);
        externalService.notifyPaymentSuccess(event.getInfo());
    }
}