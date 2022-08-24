package org.hydra.samplers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.hydra.client.HttpDatapool;
import org.hydra.client.HttpPoolError;
import org.hydra.dto.ParametersResponse;
import org.hydra.dto.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class GetCacheRandomParameters extends AbstractJavaSamplerClient{

    private static final Logger logger = LoggerFactory.getLogger(GetCachesLoadTest.class);

    /**
     * Datapool parameters data
     */
    private String datapoolUrl;
    private String datapoolRemoteToken;

    /**
     * configuration parameters
     */
    private String staticCacheUUID = "";
    private String projectId = "";
    private LinkedTreeMap configParameters;
    /**
     *  common parameters
     */
    private String cacheName;
    private HttpDatapool client;
    private String consumer;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("datapoolUrl", "");
        params.addArgument("datapoolRemoteToken", "");
        params.addArgument("projectId", "");
        params.addArgument("staticCacheUUID", "");
        params.addArgument("consumer", "");
        return params;
    }

    public void setupTest(JavaSamplerContext context) {
        System.out.println("Test setup");
        datapoolUrl = context.getParameter("datapoolUrl");
        datapoolRemoteToken = context.getParameter("datapoolRemoteToken");
        staticCacheUUID = context.getParameter("staticCacheUUID");
        consumer = context.getParameter("consumer");
        projectId = context.getParameter("projectId");
        client = new HttpDatapool(datapoolRemoteToken, datapoolUrl);
        try {
            configParameters = (LinkedTreeMap) client.getStaticParameters(projectId, staticCacheUUID, consumer).getParams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (HttpPoolError e) {
            throw new RuntimeException(e);
        }

        cacheName = (String) ((LinkedTreeMap) configParameters.get("parameters")).get("cacheName");
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();

        try {
            result.sampleStart();
            ParametersResponse responseData = client.getParameters(Strategy.RANDOM, cacheName, 1, consumer);
            result.sampleEnd();
            logger.debug(new Gson().toJson(responseData));
            result.setSuccessful(true);
            result.setResponseData("Success", "UTF8");
            result.setResponseMessage(new Gson().toJson(responseData));
            result.setResponseCode(Integer.toString(200));
            result.setResponseData(new Gson().toJson(responseData), "UTF8");
            return result;
        } catch (IOException e) {
            result.setSuccessful(false);
            result.setResponseCode(Integer.toString(-1));
            result.setResponseMessage(e.getMessage());
            result.setResponseData(e.getMessage(), "UTF8");
            return result;
        } catch (HttpPoolError e) {
            result.setSuccessful(false);
            result.setResponseMessage(new Gson().toJson(e.getBody()));
            result.setResponseCode(Integer.toString(e.getResponseCode()));
            result.setResponseData(new Gson().toJson(e.getBody()), "UTF8");
            return result;
        }
    }

    public void teardownTest(JavaSamplerContext context) {
        System.out.println("Test cleanup");
    }
}
