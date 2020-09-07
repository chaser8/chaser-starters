package top.chaser.reactor.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Slf4j
public class FluxTest {
    public static void main(String[] args) throws InterruptedException {
//        Flux.just("Hello", "World").subscribe(System.out::println);
//        Flux.fromArray(new Integer[] {1, 2, 3}).subscribe(System.out::println);
//        Flux.empty().subscribe(System.out::println);
//        Flux.range(1, 10).subscribe(System.out::println);
//        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);



//        Flux.generate(sink -> {
//            sink.next("Hello");
//            sink.complete();
//        }).subscribe(System.out::println);
//
//        final Random random = new Random();
//        Flux.generate(HashMap::new, (list, sink) -> {
//            int value = random.nextInt(100);
//            list.put(value, "1");
//            sink.next(list);
//            if (list.size() == 10) {
//                sink.complete();
//            }
//            return list;
//        }).subscribe(o -> {
//            System.out.println(o);
//        });
//        Mono<String> stringMono = Mono.fromSupplier(() -> "Hello");
//        String block = stringMono.block();
//        System.out.println(block);
//        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
//        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
//        Mono.create(sink -> sink.success("Hello")).doOnNext(System.out::println).subscribe(System.out::println);
//        Flux.range(1, 3).next().doOnNext(integer -> {
//            System.out.println("next"+integer);
//        }).subscribe(integer -> {
//            System.out.println(integer);
//        });

//        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
//        Flux.interval(Duration.ofMillis(1000)).buffer(Duration.ofMillis(1001)).take(2).toStream().forEach(System.out::println);
//        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
//        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);


        Flux<byte[]> fluxBody = Flux.just("21321313131313131333333333333333".getBytes());
//        fluxBody.subscribe(bytes -> {
//            String s = new String(bytes);
//            System.out.println("subscribe"+s);
//        });
//        fluxBody.subscribe(bytes -> {
//            String s = new String(bytes);
//            System.out.println("subscribe"+s);
//        });


        fluxBody.map(bytes -> {
            String s = new String(bytes);
            System.out.println("111"+s);
            return s;
        }).subscribe(s -> {
//            String s = new String(bytes);
            System.out.println("subscribe1"+s);
        });
        fluxBody.map(bytes -> {
            String s = new String(bytes);
            System.out.println(s);
            return s;
        }).subscribe(s -> {
//            String s = new String(bytes);
            System.out.println("subscribe"+s);
        });;
        Thread.sleep(10000);

    }
}
