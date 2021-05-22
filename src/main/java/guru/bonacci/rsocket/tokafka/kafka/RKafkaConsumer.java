package guru.bonacci.rsocket.tokafka.kafka;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class RKafkaConsumer {

    private final ReactiveKafkaConsumerTemplate<String, Foo> consumerTemplate;

    public Flux<Foo> receive() {
        return consumerTemplate
                .receiveAutoAck()
                .delayElements(Duration.ofMillis(2))
                .doOnNext(consumerRecord -> log.info("received value={} offset={}",
                        consumerRecord.value(),
                        consumerRecord.offset())
                )
                .map(ConsumerRecord::value)
                .doOnError(throwable -> log.error("something terrible happened while consuming : {}", throwable.getMessage()));
    }
}