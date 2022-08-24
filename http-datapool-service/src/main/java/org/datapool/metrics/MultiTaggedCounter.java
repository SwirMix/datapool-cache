package org.datapool.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTaggedCounter {
    private String name;
    private MeterRegistry registry;
    private Map<List<Tag>, Counter> counters = new HashMap<>();

    public MultiTaggedCounter(String name, MeterRegistry registry) {
        this.name = name;
        this.registry = registry;
    }

    public void incrementPost(String tagValue) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>();
            tags.add(new ImmutableTag("cacheName", tagValue));
            counter = Counter.builder("CACHE_COUNTER_POST").tags(tags).register(registry);
            counters.put(tags, counter);
        }
        counter.increment();
    }

    public void increment(String tagValue, String consumer) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>();
            tags.add(new ImmutableTag("cacheName", tagValue));
            tags.add(new ImmutableTag("consumer", consumer));
            counter = Counter.builder("CACHE_COUNTER_GET").tags(tags).register(registry);
            counters.put(tags, counter);
        }
        counter.increment();
    }

    public void incrementBatch(int size, String tagValue, String consumer) {
        Counter counter = counters.get(tagValue);
        if (counter == null) {
            List<Tag> tags = new ArrayList<>();
            tags.add(new ImmutableTag("cacheName", tagValue));
            tags.add(new ImmutableTag("consumer", consumer));
            counter = Counter.builder("CACHE_COUNTER_GET").tags(tags).register(registry);
            counters.put(tags, counter);
        }
        for (int i = 0; i < size; i++){
            counter.increment();
        }
    }
}
