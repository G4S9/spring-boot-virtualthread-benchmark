# TL;DR

Benchmarking SpringBoot/Tomcat with JDK21 virtual threads enabled/disabled.
Tomcat restricted to 10 threads.


# Intro

Inspired by the [blogpost](https://www.danvega.dev/blog/virtual-threads-spring-boot) by Dan Vega.

The benchmark is a rather optimistic PoC version to demonstrate the benefits of virtual threads.

For a more realistic view about the short term challanges to realize those benefits to their full extent (and possibly a roadmap), see the very informative [blogpost](https://medium.com/@phil_3582/java-virtual-threads-some-early-gotchas-to-look-out-for-f65df1bad0db) by Phil Boutros.

Also a bit off-topic, but highly recommended [video](https://www.youtube.com/watch?v=zluKcazgkV4) comparing Kotlin coroutines and Java virtual therads by Roman Elizarov.

.. and so we get to the .. 

# Benchmark results

## Results with spring boot virtual threads enabled

```properties
...
server.tomcat.threads.max=10
spring.threads.virtual.enabled=true
```

```
$ docker run -it apoutchika/oha -n 1000 -c 100 http://172.17.0.1:8080/block/3 
Summary:
  Success rate: 1.0000
  Total:        31.2952 secs
  Slowest:      3.5205 secs
  Fastest:      3.0029 secs
  Average:      3.0993 secs
  Requests/sec: 31.9538

  Total data:   38.64 KiB
  Size/request: 39 B
  Size/sec:     1.23 KiB

Response time histogram:
  0.047 [480] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.094 [202] |■■■■■■■■■■■■■
  0.141 [82]  |■■■■■
  0.188 [53]  |■■■
  0.235 [51]  |■■■
  0.282 [40]  |■■
  0.329 [21]  |■
  0.376 [20]  |■
  0.424 [22]  |■
  0.471 [18]  |■
  0.518 [11]  |

Latency distribution:
  10% in 3.0076 secs
  25% in 3.0158 secs
  50% in 3.0538 secs
  75% in 3.1287 secs
  90% in 3.2742 secs
  95% in 3.3800 secs
  99% in 3.4864 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0388 secs, 0.0003 secs, 0.0779 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0009 secs

Status code distribution:
  [200] 1000 responses
```

### Demo (another run with the same parameters)
[demo.webm](https://github.com/G4S9/spring-boot-virtualthread-benchmark/assets/96652361/f39badce-5ebc-4f61-a36b-7e89168ddedb)



## Results with spring boot virtual threads disabled

```properties
...
server.tomcat.threads.max=10
spring.threads.virtual.enabled=false
```

```
$ docker run -it apoutchika/oha -n 1000 -c 100 http://172.17.0.1:8080/block/3 
Summary:
  Success rate: 1.0000
  Total:        301.7644 secs
  Slowest:      39.0807 secs
  Fastest:      3.0032 secs
  Average:      28.8162 secs
  Requests/sec: 3.3138

  Total data:   38.20 KiB
  Size/request: 39 B
  Size/sec:     129 B

Response time histogram:
  3.280 [84]  |■■■■■■
  6.560 [10]  |
  9.839 [7]   |
  13.119 [18]  |■
  16.399 [7]   |
  19.679 [9]   |
  22.958 [8]   |
  26.238 [8]   |
  29.518 [399] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  32.798 [287] |■■■■■■■■■■■■■■■■■■■■■■■
  36.078 [163] |■■■■■■■■■■■■■

Latency distribution:
  10% in 9.9309 secs
  25% in 30.0668 secs
  50% in 30.0938 secs
  75% in 33.1328 secs
  90% in 36.0974 secs
  95% in 36.1349 secs
  99% in 36.1905 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0300 secs, 0.0002 secs, 0.0929 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0005 secs

Status code distribution:
  [200] 1000 responses
```
