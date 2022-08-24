package org.datapool.core.datapool;

import org.apache.ignite.Ignite;
import org.datapool.core.Strategy;
import org.datapool.core.cache.CacheMetadata;

public class ServiceFactory {
    public static Datapool getDatapool(CacheMetadata cacheMetadata, Strategy strategy, Ignite ignite){
        switch (strategy){
            case UNIQUE:
                Datapool service = ignite.services().serviceProxy(UniqueCountParamService.class.getName(), Datapool.class, /*not-sticky*/false);
                return service;
            default:
                return null;
        }
    }

    public static Datapool getDatapool(Strategy strategy, Ignite ignite){
        switch (strategy){
            case UNIQUE:
                Datapool service = ignite.services().serviceProxy(UniqueCountParamService.class.getName(), Datapool.class, /*not-sticky*/false);
                return service;
            case RANDOM:
                return ignite.services().serviceProxy(RandomParamService.class.getName(), Datapool.class, false);
            default:
                return null;
        }
    }
}
