package org.hydra.client;

import com.google.gson.internal.LinkedTreeMap;
import org.hydra.dto.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface HttpDatapoolClient {
    @GET("/api/v1/datapool/parameters")
    public Call<ParametersResponse> getParameters(
            @Header("remoteToken") String token,
            @Query("strategy") Strategy strategy,
            @Query("cacheName") String cacheName,
            @Query("size") Integer size,
            @Query("consumer") String consumer
    );

    @POST("/api/v1/datapool/parameters/single")
    public Call<ParametersResponse> postParameters(
            @Header("remoteToken") String token,
            @Body PostParameters parameters
    );

    @POST("/api/v1/datapool/cache/runtime/create")
    public Call<MetadataResponse> createCache(
            @Header("token") String token,
            @Body CreateRuntimeCacheRequest parameters
    );

    @GET("/api/v1/caches")
    public Call<List<LinkedTreeMap>> getCaches(
            @Query("project") String project
    );

    @GET("/api/v1/strategy")
    public Call<String[]> getStrategy();
}
