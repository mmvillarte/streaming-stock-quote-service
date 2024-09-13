package guru.springframework.streamingstockquoteservice.service;

import guru.springframework.streamingstockquoteservice.model.Quote;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
class QuoteGeneratorServiceImplTest {

    QuoteGeneratorService service;

    @BeforeEach
    void setUp() {
        service = new QuoteGeneratorServiceImpl();
    }

    @Test
    void fetchQuoteStream() throws InterruptedException {
        Flux<Quote> quoteFlux = service.fetchQuoteStream(Duration.ofMillis(100l));

        Consumer<Quote> quoteConsumer = quoteValue -> log.info(quoteValue.toString());

        Consumer<Throwable> throwableConsumer = e -> log.info("Error message: ", e.getMessage());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable done = () -> countDownLatch.countDown();

        quoteFlux.take(30).subscribe(quoteConsumer, throwableConsumer, done);

        countDownLatch.await();
    }
}