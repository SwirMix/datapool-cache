package clients;

import com.google.gson.internal.LinkedTreeMap;
import org.hydra.client.HttpDatapool;
import org.hydra.client.HttpPoolError;
import org.hydra.dto.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.*;

public class RestApiTest {

    private String url = "http://localhost:8086";
    private String remoteToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJwcm9qZWN0cyI6WyIyMGM3OTRmNC00NmQyLTQ2YTEtYWYxOS01ZWZiZDFhZGRmNWQiLCJjODYwMTU2Zi1jMDhmLTRmN2MtOTM1ZC05YjRhNmI4NjM0MDIiLCI4MzhjNmQxNS0zZjcyLTRkOWEtYjQ5MC01ZDQ3N2VhMjlkM2IiLCJjODc0MTM0OS0yYThkLTQ2MWYtYWFkNy00ZDE0YTEzODVmOGQiLCJmNjM0MzMzMi03MTI2LTQxNmUtYjk4NC02ZTEwOGM0NTBlNWEiLCJkNmE2YWNjMC0wODYwLTRhZjUtYTJhYS0zYTk5NDg3NGU4M2EiXSwibG9naW4iOiJwb25jaGljayIsInR5cGUiOiJkYXRhcG9vbCIsInZhbGlkRGF0ZUVuZCI6MCwidXNlcklkIjoxLCJlbWFpbCI6InBvbmNoaWNrQGdtYWlsLmNvbSIsImNyZWF0ZURhdGUiOjE2NjA0Njc2NjU3Mjd9.2pHVh68-jxhu6la5CpOn-8WWq6vUicjMj1-ZtDtpmnTlHEC5gIepZuXEK3Mkpo5Lmm5VUUZmCRUbXiUYFfoP3w";
    private String cacheName = "d6a6acc0-0860-4af5-a2aa-3a994874e83a_users_new17.csv";
    private String projectId = "20c794f4-46d2-46a1-af19-5efbd1addf5d";
    private String staticCacheUUID = "ba33b45a-cd80-4356-9a80-223952e58bce";
    private HttpDatapool client;

    @BeforeTest
    public void init(){
        client = new HttpDatapool(remoteToken, url);
    }

    @Test
    public void getParamTest() throws IOException, HttpPoolError {
        ParametersResponse response = client.getParameters(Strategy.UNIQUE, cacheName, 1, "test-3");
        System.out.println(response.toString());
        response = client.getParameters(Strategy.RANDOM, cacheName, 1, "test-3");
        System.out.println(response.toString());
        response = client.getParameters(Strategy.BATCH_RANDOM, cacheName, 5, "test-3");
        System.out.println(response.toString());
        response = client.getParameters(Strategy.BATCH_UNIQUE, cacheName, 5, "test-3");
        System.out.println(response.toString());
        List<LinkedTreeMap> caches = client.getCaches(projectId);
        System.out.println(caches);
    }

    @Test
    public void testRuntimeCache() throws HttpPoolError, IOException {
        CreateRuntimeCacheRequest request = new CreateRuntimeCacheRequest();
        request.setProjectId(projectId);
        request.setCacheName("runtime_cache");
        request.setColumns(new String[]{"id","name","token"});
        MetadataResponse response = client.createCache(request);

        Map<String, Object> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", UUID.randomUUID().toString());
        params.put("token", UUID.randomUUID().toString());
        PostParameters parameters = new PostParameters();
        parameters.setCacheName(response.getCacheMetadata().getCacheName());
        parameters.setParameters(params);
        ParametersResponse parametersResponse = client.postParameterToCache(parameters);
        parametersResponse = client.getParameters(Strategy.BATCH_RANDOM, response.getCacheMetadata().getCacheName(), 1, "test-3");
        System.out.println(parametersResponse);
    }

    @Test
    public void postGetCache() throws HttpPoolError, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", UUID.randomUUID().toString());
        params.put("token", UUID.randomUUID().toString());
        PostParameters parameters = new PostParameters();
        parameters.setCacheName("20c794f4-46d2-46a1-af19-5efbd1addf5d_runtime_cache");
        parameters.setParameters(params);
        ParametersResponse parametersResponse = client.postParameterToCache(parameters);
        parametersResponse = client.getParameters(Strategy.UNIQUE, "20c794f4-46d2-46a1-af19-5efbd1addf5d_runtime_cache", 10, "test-3");
        System.out.println(parametersResponse);
    }

    @Test
    public void getStaticCache() throws HttpPoolError, IOException {
        ParametersResponse response = client.getStaticParameters(projectId, staticCacheUUID, "consumer");
        System.out.println(response.getParams());
    }
}
