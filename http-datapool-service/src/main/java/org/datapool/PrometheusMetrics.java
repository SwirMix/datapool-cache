package org.datapool;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.datapool.metrics.MultiTaggedCounter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PrometheusMetrics {

    private final AtomicInteger testGauge;
    private final Counter testCounter;
    private static final Map<String, Counter> cacheMetrics = new HashMap<>();
    private MeterRegistry meterRegistry;
    private static MultiTaggedCounter cacheCounter;

    public void incrementGetCacheMetric(String cacheName, String consumer){
        cacheCounter.increment(cacheName, consumer);
    }

    public void incrementPutCacheMetric(String cacheName){
        cacheCounter.incrementPost(cacheName);
    }

    public void incrementGetBatchCacheMetric(String cacheName, String consumer, int size){
        cacheCounter.incrementBatch(size, cacheName, consumer);
    }

    public PrometheusMetrics(MeterRegistry meterRegistry) {
        testGauge = meterRegistry.gauge("custom_gauge", new AtomicInteger(0));
        testCounter = meterRegistry.counter("custom_counter");
        this.meterRegistry = meterRegistry;
        cacheCounter = new MultiTaggedCounter("cache_counter", meterRegistry);
    }

    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void schedulingTask() {
        testGauge.set(PrometheusMetrics.getRandomNumberInRange(0, 100));
        testCounter.increment();
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
