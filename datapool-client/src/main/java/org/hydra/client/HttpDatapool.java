package org.hydra.client;

import com.google.gson.internal.LinkedTreeMap;
import jdk.jfr.MetadataDefinition;
import okhttp3.OkHttpClient;
import org.hydra.dto.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class HttpDatapool{
    private String remoteToken;
    private String url;
    private Retrofit retrofit;
    private HttpDatapoolClient client;

    public HttpDatapool(String remoteToken, String url){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        this.remoteToken = remoteToken;
        this.url = url;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        this.client = retrofit.create(HttpDatapoolClient.class);
    }

    public ParametersResponse getParameters(Strategy strategy, String cacheName, Integer size, String consumer) throws IOException, HttpPoolError {
        Call<ParametersResponse> param = client.getParameters(remoteToken, strategy, cacheName, size, consumer);
        Response<ParametersResponse> response = param.execute();
        return (ParametersResponse) getResponse(response);
    }

    public String[] getStrategy() throws IOException, HttpPoolError {
        Call<String[]> response = client.getStrategy();
        return (String[]) getResponse(response.execute());
    }

    public List<LinkedTreeMap> getCaches(String project) throws IOException, HttpPoolError {
        Call response = client.getCaches(project);
        return (List<LinkedTreeMap>) getResponse(response.execute());
    }

    public MetadataResponse createCache(CreateRuntimeCacheRequest request) throws IOException, HttpPoolError {
        Call response = client.createCache(remoteToken, request);
        return (MetadataResponse) getResponse(response.execute());
    }

    public ParametersResponse postParameterToCache(PostParameters parameters) throws IOException, HttpPoolError {
        Call response = client.postParameters(remoteToken, parameters);
        return (ParametersResponse) getResponse(response.execute());
    }

    public ParametersResponse getStaticParameters(String projectId, String cacheUUID, String consumer) throws IOException, HttpPoolError {
        Call response = client.getStaticParameters(remoteToken, cacheUUID, projectId, consumer);
        return (ParametersResponse) getResponse(response.execute());
    }

    private Object getResponse(Response response) throws HttpPoolError {
        if (response.code()==200 | response.code()==201) {
            return response.body();
        } else throw new HttpPoolError(response, response.code());
    }
}
