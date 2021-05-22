package guru.bonacci.rsocket.tokafka.kafka;

import static guru.bonacci.rsocket.tokafka.TheApp.TOPIC;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class RKafkaProducer {

	private final ReactiveKafkaProducerTemplate<String, Foo> producerTemplate;


	public Mono<SenderResult<Void>> send(Foo message) {
		log.info("send to topic={}, {}={},", TOPIC, Foo.class.getSimpleName(), message);
		return producerTemplate.send(TOPIC, message)
				.doOnSuccess(senderResult -> log.info("sent {} offset : {}", message, senderResult.recordMetadata().offset()));
	}
}
