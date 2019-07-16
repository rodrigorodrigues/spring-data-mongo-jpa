package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Slf4j
public class MultiThreadTests {
    Random random = new Random();

    private int getRandomValue() {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(100));
            int integer = random.nextInt(10);
            log.info("thread: {}\tinteger: {}\ttime: {}", Thread.currentThread().getName(), integer, ZonedDateTime
                .now());
            return integer;
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }

    @Test
    public void testAtomicInteger() { //slows as testFutureGet()
        AtomicInteger atomicInteger = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long time = System.currentTimeMillis();
        IntStream.range(0, 100)
                .forEach(i -> atomicInteger.updateAndGet(sum -> sum + getFuture(executorService).applyAsInt(i)));

        assertThat(atomicInteger.get()).isGreaterThanOrEqualTo(10);
        log.info("Time spent: {}", (System.currentTimeMillis() - time));
        executorService.shutdown();
    }

    @Test
    public void testFutureGet() { //Very slow using get method from Future(getFuture())
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long time = System.currentTimeMillis();
        Integer total = IntStream.range(0, 100)
            .boxed()
            .mapToInt(getFuture(executorService))
            .sum();

        assertThat(total).isGreaterThanOrEqualTo(10);
        log.info("Time spent: {}", (System.currentTimeMillis() - time));
        executorService.shutdown();
    }

    @Test
    public void testFutureStream() { //Better option using list of Futures with stream() - better to use this approach
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long time = System.currentTimeMillis();
        List<Future<Integer>> futures = IntStream.range(0, 100)
            .boxed()
            .map(i -> executorService.submit(this::getRandomValue))
            .collect(Collectors.toList());

        assertThat(futures.stream().mapToInt(getFuture()).sum()).isGreaterThanOrEqualTo(10);
        log.info("Time spent: {}", (System.currentTimeMillis() - time));
        executorService.shutdown();
    }

    @Test
    public void testFutureParallelStream() { //Better option using list of Futures with parallelStream()
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Long time = System.currentTimeMillis();
        List<Future<Integer>> futures = IntStream.range(0, 100)
            .boxed()
            .map(i -> executorService.submit(this::getRandomValue))
            .collect(Collectors.toList());

        assertThat(futures.parallelStream().mapToInt(getFuture()).sum()).isGreaterThanOrEqualTo(10);
        log.info("Time spent: {}", (System.currentTimeMillis() - time));
        executorService.shutdown();
    }

    @Test
    public void testCompletableFuture() throws ExecutionException, InterruptedException { //Faster option using CompletableFuture but more difficult to implement
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Long time = System.currentTimeMillis();
        List<CompletableFuture<Integer>> futures = IntStream.range(0, 100)
            .boxed()
            .map(i -> CompletableFuture.supplyAsync(this::getRandomValue, executorService))
            .collect(Collectors.toList());

        assertThat(
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[] {}))
                .thenApply(v -> futures.stream().mapToInt(CompletableFuture::join).sum())
                .get()
        ).isGreaterThanOrEqualTo(10);
        log.info("Time spent: {}", (System.currentTimeMillis() - time));
    }

    private ToIntFunction<Integer> getFuture(ExecutorService executorService) {
        return i -> {
            try {
                return executorService.submit(this::getRandomValue).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private ToIntFunction<Future<Integer>> getFuture() {
        return value -> {
            try {
                return value.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
