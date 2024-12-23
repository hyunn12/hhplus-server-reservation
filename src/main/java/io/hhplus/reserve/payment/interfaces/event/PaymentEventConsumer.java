package io.hhplus.reserve.payment.interfaces.event;

import io.hhplus.reserve.common.kafka.KafkaConstant;
import io.hhplus.reserve.common.util.JsonUtil;
import io.hhplus.reserve.external.application.ExternalService;
import io.hhplus.reserve.outbox.domain.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OutboxService outboxService;
    private final ExternalService externalService;

    @KafkaListener(topics = KafkaConstant.PAYMENT_TOPIC, groupId = "payment-outbox")
    public void outboxPublished(ConsumerRecord<String, String> consumerRecord){
        log.info("# [PaymentEventConsumer] outboxPublished ::: {}", consumerRecord.key());
        outboxService.publishOutbox(consumerRecord.key());
    }

    @KafkaListener(topics = KafkaConstant.PAYMENT_TOPIC, groupId = "payment-notify")
    public void successPayment(ConsumerRecord<String, String> consumerRecord){
        log.info("# [PaymentEventConsumer] successPayment ::: {}", consumerRecord.value());
        Long paymentId = JsonUtil.jsonStringToObject(consumerRecord.value(), Long.class);
        externalService.notifyPaymentSuccess(paymentId);
    }
}
