package kreadcn.homework;

import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpClientTests {
    private static HttpClient httpClient;

    static {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {

            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20))
                    .sslContext(sc)
                    .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 8087)))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test(String ck) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.POST(HttpRequest.BodyPublishers.ofString("INFO=00040AFE0000210100000000000000A001000000000000000&%40_1_6_T=T_XX&_1_6_T=%E6%AD%B2%E8%BE%9B%E4%B8%91%E5%90%8C%E4%BA%BA&%40_1_9_M=M_DA&_1_9_M_1=&_1_9_M_2=&_IMG_%E7%B0%A1%E6%98%93%E6%9F%A5%E8%A9%A2.x=0&_IMG_%E7%B0%A1%E6%98%93%E6%9F%A5%E8%A9%A2.y=0&INFO=00040AFE0000210100000000000000A001000000000000000"));
        builder.uri(URI.create("http://lib.cqvip.com/Search/SearchList?"));
        builder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.183");
        builder.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        builder.header("Accept", "text/html, */*; q=0.01");
        builder.header("Origin", "http://lib.cqvip.com");
        builder.header("Accept-Encoding", "gzip, deflate");
        builder.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        builder.header("X-Requested-With", "XMLHttpRequest");
        builder.header("Cookie", ck);
        builder.header("Referer", "http://lib.cqvip.com/Qikan/Search/Advance?from=index");
        builder.POST(HttpRequest.BodyPublishers.ofString("searchParamModel=%7B%22ObjectType%22%3A1%2C%22SearchKeyList%22%3A%5B%7B%22FieldIdentifier%22%3A%22M%22%2C%22SearchKey%22%3A%22java%22%2C%22PreLogicalOperator%22%3A%22%22%2C%22IsExact%22%3A%220%22%7D%5D%2C%22SearchExpression%22%3A%22%22%2C%22BeginYear%22%3A%22%22%2C%22EndYear%22%3A%22%22%2C%22JournalRange%22%3A%22%22%2C%22DomainRange%22%3A%22%22%2C%22PageSize%22%3A%220%22%2C%22PageNum%22%3A%221%22%2C%22Sort%22%3A%220%22%2C%22ClusterFilter%22%3A%22%22%2C%22SType%22%3A%22%22%2C%22StrIds%22%3A%22%22%2C%22UpdateTimeType%22%3A%22%22%2C%22ClusterUseType%22%3A%22Article%22%2C%22IsNoteHistory%22%3A1%2C%22AdvShowTitle%22%3A%22%E9%A2%98%E5%90%8D%E6%88%96%E5%85%B3%E9%94%AE%E8%AF%8D%3Djava%22%2C%22ObjectId%22%3A%22%22%2C%22ObjectSearchType%22%3A%220%22%2C%22ChineseEnglishExtend%22%3A%220%22%2C%22SynonymExtend%22%3A%220%22%2C%22ShowTotalCount%22%3A%220%22%2C%22AdvTabGuid%22%3A%22cdb49f50-c3f5-b2ce-cc8f-6fd444658d44%22%7D"));
        CompletableFuture<HttpResponse<String>> httpResponse = httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());

        httpResponse.thenAccept((response) -> {
            System.out.println(response.body());
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        Thread.sleep(5000);
        System.out.println();
    }

    @Test
    public void testCnki() throws Exception {
        test("GW1gelwM5YZuS=60f59CEZjOhd7PVoBJPXLGJmgSGsCh7lDJiaaq30ObtI4JsMXDU7_ZQOCwgYmfuubiHS3lUWd1BBQ4LBfprb8Uya; ae51635ca5836b4864=9412c3c5350c84c17f2255979cf15e34; ASP.NET_SessionId=xmicn2m511k0gs5whqiropp5; f6324025fe=075acf59953577e481a10d15d668f262; Hm_lvt_fee827c3dc795c5122daf5ee854c1683=1690541918; Hm_lvt_17262dc62ce874a510e9c97140f381d6=1690541918; Hm_lvt_540085501ec41c08ad1c432c82ab13d7=1690541918; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221899c268969aec-08e93e93e93e94-7c546c7d-2073600-1899c26896a18e1%22%2C%22first_id%22%3A%22%22%2C%22props%22%3A%7B%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg5OWMyNjg5NjlhZWMtMDhlOTNlOTNlOTNlOTQtN2M1NDZjN2QtMjA3MzYwMC0xODk5YzI2ODk2YTE4ZTEifQ%3D%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%22%2C%22value%22%3A%22%22%7D%2C%22%24device_id%22%3A%221899c268969aec-08e93e93e93e94-7c546c7d-2073600-1899c26896a18e1%22%7D; sajssdk_2015_cross_new_user=1; __bid_n=1899a18c8849104cb5b351; search_isEnable=1; Hm_lpvt_540085501ec41c08ad1c432c82ab13d7=1690543876; Hm_lpvt_fee827c3dc795c5122daf5ee854c1683=1690543876; Hm_lpvt_17262dc62ce874a510e9c97140f381d6=1690543876; GW1gelwM5YZuT=0rGnp4hprD7OUByuhUUo0ImNsmLe3L1PFQ.OrpnMCqBxA58LaehLstCjDEqU7GOJAPa_6115qqchNJ4aYoP9kcR3KDLkYh1OsJ.ov_xcJr4JRjKvoUQC4k35SPmKjB31MTqDdoglg4UcATt.mn.HlDc7OhEi3NRUh_MGFStWTiJG3gZ9mMg2tx3t9_Sp2QQYzc4QfdkQ5QVh4Exr.ySkqAx8Nc2P6e05lgiJVtQmAO7nBBkMYr4mU8r787xqoaH8xRnFI9dQ5qpDG0vk2S3is6F_Xh0Vw.yofikvMux3kremqA.W_uwXdY1JgET1zjcAzOEB1KPIr2eN_etk7SUMDRCq3.0fzS4pxncDLW__LR9t3x8aKqA8ZZlRkcFOwsROqmDXPLrkR78blBaqk3lmJaYzf3efbv35UoTKr_EKHIe50KfGIJiq8szFPJrkfWyyo");
    }
}
