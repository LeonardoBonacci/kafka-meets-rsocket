package guru.bonacci.rsocket.tokafka;

import java.util.function.Consumer;

import org.springframework.context.annotation.Configuration;

import guru.bonacci.rsocket.tokafka.dto.Foo;
import guru.bonacci.rsocket.tokafka.utils.RandomUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class FooRequester {

    private final Sinks.Many<Foo> sink = Sinks.many().unicast().onBackpressureBuffer();
    private Foo foo = new Foo("foo and bar");
    private int attempts = 0;

    public Flux<Foo> start(){
        return this.sink.asFlux();
    }

    public void send(){
        this.emit();
    }

    public Consumer<Foo> receives(){
        return this::processResponse;
    }

    private void processResponse(Foo response){
        attempts++;
        System.out.println(attempts + " : " + response.getBar());

        if(attempts > 10) {
            System.out.println("that's it for today...");
            this.sink.tryEmitComplete();
            return;
        }
        
        foo = RandomUtils.randomReplaceFirstChar(response);
        this.emit();
    }

    private void emit(){
        this.sink.tryEmitNext(foo);
    }
}
