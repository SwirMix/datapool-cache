package org.hydra.samplers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.hydra.client.HttpDatapool;
import org.hydra.client.HttpPoolError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GetCachesLoadTest extends AbstractJavaSamplerClient {

    private static final Logger logger = LoggerFactory.getLogger(GetCachesLoadTest.class);
    /**
     * Datapool parameters data
     */
    private String datapoolUrl;
    private String datapoolRemoteToken;
    private String projectId;
    private HttpDatapool client;

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("datapoolUrl", "");
        params.addArgument("datapoolRemoteToken", "");
        params.addArgument("projectId", "");
        return params;
    }

    public void setupTest(JavaSamplerContext context) {
        System.out.println("Test setup");
        datapoolUrl = context.getParameter("datapoolUrl");
        datapoolRemoteToken = context.getParameter("datapoolRemoteToken");
        projectId = context.getParameter("projectId");
        client = new HttpDatapool(datapoolRemoteToken, datapoolUrl);
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();

        try {
            result.sampleStart();
            List<LinkedTreeMap> responseData = client.getCaches(projectId);
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
