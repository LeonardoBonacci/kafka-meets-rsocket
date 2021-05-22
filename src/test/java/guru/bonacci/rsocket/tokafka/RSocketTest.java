package guru.bonacci.rsocket.tokafka;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import io.rsocket.transport.netty.client.TcpClientTransport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RSocketTest {

	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup(){
		this.requester = this.builder
				.transport(TcpClientTransport.create("localhost", 7000));
	}

	@Test
	public void fireAndForget(){
		Mono<Void> mono = this.requester.route("fire.and.forget")
				.data(new Foo("some foo"))
				.send();

		StepVerifier.create(mono)
				.verifyComplete();
	}

	@Test
	public void requestResponse(){
		Mono<Foo> mono = this.requester.route("request.and.response")
				.data(new Foo("some foo"))
				.retrieveMono(Foo.class)
				.doOnNext(System.out::println);

		StepVerifier.create(mono)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test @Disabled("embedded kafka required for execution")
	public void requestStream(){
		Flux<Foo> mono = this.requester.route("request.stream")
				.data(new Foo("some foo"))
				.retrieveFlux(Foo.class)
				.doOnNext(System.out::println);

		StepVerifier.create(mono)
				.expectNextCount(10)
				.verifyComplete();
	}

    @Test
    public void requestChannel() {
        FooRequester fooStreamer = new FooRequester();

        Mono<Void> mono = this.requester.route("request.channel")
                .data(fooStreamer.start().delayElements(Duration.ofMillis(1)))
                .retrieveFlux(Foo.class)
                .doOnNext(fooStreamer.receives())
                .doFirst(fooStreamer::send)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}