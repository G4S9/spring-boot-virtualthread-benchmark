package com.g4s9.virtualthreadbenchmark

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

import java.time.Duration
import java.time.Instant
import java.util.concurrent.Callable
import java.util.concurrent.Executors

record Durations(String d1, String d2, String dt) {}

@RestController
@SpringBootApplication
class VirtualThreadBenchmark {
    @GetMapping("/block/{seconds}")
    Durations delay(@PathVariable("seconds") int seconds) {
        def r = {
            def start = Instant.now()
            // blocks the thread for the specified duration
            Thread.currentThread().sleep(Duration.ofSeconds(seconds))
            getDuration(start)
        }
        try (def executor = Executors.newVirtualThreadPerTaskExecutor()) {
            def start = Instant.now()
            def (s1, s2) = executor.invokeAll([r, r])*.get()
            new Durations(s1, s2, getDuration(start))
        }
    }

    static String getDuration(Instant start) {
        return (Duration.between(start, Instant.now()).toMillis() / 1000.0).toString()
    }

    static void main(String[] args) {
        new SpringApplication().run(VirtualThreadBenchmark.class, args)
    }
}
