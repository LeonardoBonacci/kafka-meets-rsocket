package guru.bonacci.rsocket.tokafka;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import guru.bonacci.rsocket.tokafka.kafka.RKafkaConsumer;
import guru.bonacci.rsocket.tokafka.kafka.RKafkaProducer;
import guru.bonacci.rsocket.tokafka.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import reactor.util.function.Tuple2;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FooController {

	private final RKafkaProducer producer;
	private final RKafkaConsumer consumer;

	@MessageMapping("fire.and.forget")
	Mono<Void> fireAndForget(Foo request) {
		log.info("ff: " + request);
		return Flux.range(1, 10).flatMap(i -> producer.send(request)).then();
	}
	
	@MessageMapping("request.and.response")
	Mono<Foo> requestResponse(Foo request) {
		return producer.send(request).thenReturn(new Foo("well done"));
	}
	
	@MessageMapping("request.stream")
	Flux<Foo> requestStream(Foo request) {
		return Flux.range(1, 10).flatMap(i -> producer.send(request)).thenMany(consumer.receive());
	}

	@MessageMapping("request.channel")
	Flux<Foo> requestStream(Flux<Foo> requestFlux) {
		Flux<Foo> consumption = consumer.receive();
		Flux<SenderResult<Void>> production = requestFlux.flatMap(request -> producer.send(request));
		return Flux.zip(production, consumption)
					.map(Tuple2::getT2)
					.map(RandomUtils::randomReplaceLastChar);
	}

}