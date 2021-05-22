package guru.bonacci.rsocket.tokafka.kafka;

import static guru.bonacci.rsocket.tokafka.TheApp.TOPIC;

import java.util.Collections;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import reactor.kafka.receiver.ReceiverOptions;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ReceiverOptions<String, Foo> kafkaReceiverOptions(KafkaProperties kafkaProperties) {
		ReceiverOptions<String, Foo> basicReceiverOptions = ReceiverOptions
				.create(kafkaProperties.buildConsumerProperties());
		return basicReceiverOptions.subscription(Collections.singletonList(TOPIC));
	}

	@Bean
	public ReactiveKafkaConsumerTemplate<String, Foo> reactiveKafkaConsumerTemplate(
			ReceiverOptions<String, Foo> kafkaReceiverOptions) {
		return new ReactiveKafkaConsumerTemplate<String, Foo>(kafkaReceiverOptions);
	}
}