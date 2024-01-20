package kreadcn.homework.utils;

import com.alibaba.fastjson2.JSONObject;
import kreadcn.homework.model.NameValuePair;
import lombok.Data;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {
    public static String GET = "GET";
    public static String POST = "POST";
    private static HttpClient httpClient;

    static {
        httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
    }

    public static Response request(Request request) {
        try {
            return requestWithException(request);
        } catch (IOException e) {
            return new Response();
        }
    }

    public static Response requestWithException(Request request) throws IOException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(request.getUrl())).timeout(Duration.ofSeconds(10));

        for (NameValuePair<String, String> header : request.getHeaders()) {
            builder.header(header.getName(), header.getValue());
        }
        if (GET.equals(request.getMethod())) {
            builder.GET();
        } else if (POST.equals(request.getMethod())) {
            String body = "";
            if (request.getPostData() != null) {
                body = request.getPostData().toString();
            }
            builder.POST(HttpRequest.BodyPublishers.ofString(body));
        } else {
            throw new RuntimeException("Unsupported method: " + request.getMethod());
        }

        Response response = new Response();
        try {
            HttpResponse<String> httpResponse = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());

            response.setContent(httpResponse.body());
            response.setHeaders(httpResponse.headers());

        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }

        return response;
    }


    public static Request withRequest() {
        return new Request().method(GET);
    }

    @Data
    public static class Request {
        private String reqCharset = "utf-8";
        private String url;
        private String method;
        private String contentType;
        private StringBuilder postData;
        private List<NameValuePair<String, String>> headers = new ArrayList<>();

        public Request requestCharset(String charset) {
            this.reqCharset = charset;
            return this;
        }

        public Request method(String method) {
            this.method = method;
            return this;
        }

        public Request header(String name, String value) {
            headers.add(new NameValuePair<>(name, value));
            return this;
        }

        public Request setHeader(String name, String value) {
            NameValuePair<String, String> header = headers.stream().filter(item -> name.equalsIgnoreCase(item.getName())).findAny().orElse(null);
            if (header == null) {
                headers.add(new NameValuePair<>(name, value));
            } else {
                header.setValue(value);
            }

            return this;
        }

        public Request url(String url) {
            this.url = url;
            return this;
        }

        public Request postString(String data) {
            Assert.isTrue(POST.equals(method), "POST method must be specified");
            if (StringUtils.hasText(data)) {
                this.postData = new StringBuilder(data);
            } else {
                this.postData = null;
            }

            return this;
        }

        public Request postForm(String name, String value) {
            Assert.isTrue(POST.equals(method), "POST method must be specified");
            this.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + reqCharset);
            if (postData == null) {
                postData = new StringBuilder();
            }

            if (postData.length() == 0) {
                postData.append('&');
            }
            try {
                postData.append(URLEncoder.encode(name, reqCharset));
            } catch (Exception ex) {
                postData.append(name);
            }
            postData.append('=');
            try {
                postData.append(URLEncoder.encode(value, reqCharset));
            } catch (Exception ex) {
                postData.append(value);
            }
            return this;
        }
    }

    @Data
    public static class Response {
        private String content;
        private HttpHeaders headers;

        public JSONObject asJSONObject() {
            if (content == null) {
                return null;
            }

            return JSONObject.parseObject(content);
        }

        public JXDocument asXML() {
            return null;
        }

        public String asString() {
            return content;
        }

        public List<String> getHeaders(String name) {
            return headers.allValues(name);
        }

        public String getHeader(String name) {
            return headers.firstValue(name).get();
        }
    }
}
