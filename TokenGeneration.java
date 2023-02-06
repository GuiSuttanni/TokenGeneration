import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

public class MutalTLSMain {
    private static final String KEY_STORE_PATH = "/tmp/certificado.jks";
    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String PRIVATE_KEY_PASSWORD = "123456";

    public static void main(String[] args) throws Exception {
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(new File(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray(), PRIVATE_KEY_PASSWORD.toCharArray())
                .loadTrustMaterial(new File(KEY_STORE_PATH), KEY_STORE_PASSWORD.toCharArray())
                .build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, null, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        HttpGet get = new HttpGet("");
        get.addHeader("accept", "application/json");

        CloseableHttpResponse response = httpClient.execute(get);

        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println();

        Header[] headers = response.getAllHeaders();
        if (headers != null) {
            for (Header header : headers) {
                System.out.println(header.getName() + ": " + header.getValue());
            }
        }
    }
}
