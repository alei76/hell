package ps.ml.stock.data;

import org.apache.commons.codec.EncoderException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by louis on 16-9-30.
 */
public class StockDataYesApi implements StockDataServer {
    
    public String url = "";
    
    public String stockId = "sh600000";
    
    public String start = "20150101";
    
    public String end = "20150809";
    
    private static final String ACCESS_TOKEN = "78011538d7f50045678628f17ff64da5b094e89194bf1715549c0ba26966ac76";
    
    private static CloseableHttpClient httpClient = createHttpsClient();
    
    @Override
    public void test() throws IOException, EncoderException {
        List<Object> obj = getData(this.stockId, this.start, this.end);
        int len = obj.size();
        for (int i = 0; i < len; i++) {
            System.out.println(obj.get(i));
        }
    }
    
    @Override
    public List<Object> getData(String stockId, String start, String end) throws IOException,
    EncoderException{
        //根据api store页面上实际的api url来发送get请求，获取数据
        //String url = "https://api.wmcloud.com/data/v1/api/market/getMktEqud
        //   .csv?field=&beginDate=20150101&endDate=&secID=&ticker=000001&tradeDate=";
        StringBuffer sb = new StringBuffer("https://api.wmcloud.com/data/v1/api/market/getQTDailyQuoteJY.json?field=&beginTime=2016-06-02T00:00:00&endTime=");
        //sb.append("?beginDate=");
        //sb.append(start);
        //sb.append("&endData=");
        //sb.append(end);
        HttpGet httpGet = new HttpGet(sb.toString());
        //在header里加入 Bearer {token}，添加认证的token，并执行get请求获取json数据
        httpGet.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println(body);
        return null;
    }
    
    public static CloseableHttpClient createHttpsClient() {
        X509TrustManager x509mgr = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }
            
            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }
            
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        //因为java客户端要进行安全证书的认证，这里我们设置ALLOW_ALL_HOSTNAME_VERIFIER来跳过认证，否则将报错
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509mgr}, null);
            sslsf = new SSLConnectionSocketFactory(
                sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
    
    public static void main(String[] args) throws IOException, EncoderException {
        StockDataServer server =new StockDataYesApi();
        server.test();
    }
}
