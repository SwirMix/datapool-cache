package org.datapool.core.datapool;

import java.util.List;
import java.util.Map;

public interface Datapool {
    public Map<String, Object> getRow(String cacheName);
    public List<Map<String, Object>> getBatch(int size, String cacheName);
}
