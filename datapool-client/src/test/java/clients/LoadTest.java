package clients;

import org.apache.jmeter.config.Arguments;
import org.hydra.samplers.GetCacheRandomParameters;
import org.testng.annotations.Test;
import java.io.IOException;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class LoadTest {

    @Test
    public void test() throws IOException {
        Arguments params = new Arguments();
        params.addArgument("datapoolUrl", "http://localhost:8086");
        params.addArgument("datapoolRemoteToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJwcm9qZWN0cyI6WyIyMGM3OTRmNC00NmQyLTQ2YTEtYWYxOS01ZWZiZDFhZGRmNWQiLCJjODYwMTU2Zi1jMDhmLTRmN2MtOTM1ZC05YjRhNmI4NjM0MDIiLCI4MzhjNmQxNS0zZjcyLTRkOWEtYjQ5MC01ZDQ3N2VhMjlkM2IiXSwibG9naW4iOiJwb25jaGljayIsInR5cGUiOiJkYXRhcG9vbCIsInZhbGlkRGF0ZUVuZCI6MCwidXNlcklkIjoxLCJlbWFpbCI6InBvbmNoaWNrQGdtYWlsLmNvbSIsImNyZWF0ZURhdGUiOjE2NjA4NTc4NTQwOTF9.KZjq68gT3albF1EBQWUwnqV8CssQmucN8yJjsb4OF3MVtecfzEADSgdLgFxt2JTgZSYCSjGKTMCq0HHC0fjHLg");
        params.addArgument("cacheName", "20c794f4-46d2-46a1-af19-5efbd1addf5d_users_new16.csv");
        params.addArgument("consumer", "test-for-test");

    }
}
