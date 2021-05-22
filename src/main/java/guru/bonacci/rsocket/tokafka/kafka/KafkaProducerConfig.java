package guru.bonacci.rsocket.tokafka.kafka;

import static guru.bonacci.rsocket.tokafka.TheApp.TOPIC;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public ReactiveKafkaProducerTemplate<String, Foo> reactiveKafkaProducerTemplate(KafkaProperties properties) {
		Map<String, Object> props = properties.buildProducerProperties();
		return new ReactiveKafkaProducerTemplate<String, Foo>(SenderOptions.create(props));
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name(TOPIC).build();
	}
}
