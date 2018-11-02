package com.ls.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpComponetTest {

    @Test
    public void get() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        try {
            HttpEntity entity = httpResponse.getEntity();
            System.out.println(EntityUtils.toString(entity));
        } finally {
            httpResponse.close();
        }
    }
    
    @Test
    public void post() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("loginId", "32019905@JS"));
        formParams.add(new BasicNameValuePair("j_password", "1234562"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
        
        HttpPost httpPost = new HttpPost("http://192.168.167.112:9072/MSS-PURCHASE/user/check.do");
        httpPost.setEntity(formEntity);
        
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            HttpEntity entity = httpResponse.getEntity();
            System.out.println(EntityUtils.toString(entity));
        } finally {
            httpResponse.close();
        }
    }
    
    @Test
    public void clientFormLogin() throws Exception{
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        
        try {
            HttpUriRequest cookieRequest = new HttpGet("http://192.168.167.112:9072/MSS-PURCHASE/user/login.do");
            CloseableHttpResponse cookieResponse = httpClient.execute(cookieRequest);
            try {
                HttpEntity entity = cookieResponse.getEntity();
                //System.out.println(EntityUtils.toString(entity));
                
                Header header = cookieResponse.getFirstHeader("Set-Cookie");
                System.out.println("cookieResponse:"+header.toString());
            } finally {
                cookieResponse.close();
            }
            
            Header cookieHead = null;
            List<Cookie> cookies = cookieStore.getCookies();
            if(!cookies.isEmpty()){
                String cookieStr = "";
                for(int i = 0; i < cookies.size(); i++){
                    cookieStr += cookies.get(i).getName() + "=" + cookies.get(i).getValue();
                    if(i > 1 && i < cookies.size()){
                        cookieStr += "; ";
                    }
                }
                System.out.println("cookieHeader:" + cookieStr.trim());
                cookieHead = new BasicHeader("Cookie", cookieStr);
            }
            
            HttpUriRequest login = RequestBuilder.post()//
                    .setUri("http://192.168.167.112:9072/MSS-PURCHASE/jspringsecuritycheck.do")//
                    //.setHeader(cookieHead)//
                    .addParameter("j_username", "32019905@JS")//
                    .addParameter("j_password", "111111")//
                    .build();
            CloseableHttpResponse loginResponse = httpClient.execute(login);
            try {
                HttpEntity entity = loginResponse.getEntity();
                if(entity != null){
                    System.out.println("loginResponseEntity:"+EntityUtils.toString(entity));
                }
                
                Header header = loginResponse.getFirstHeader("Set-Cookie");
                if(header != null){
                    System.out.println("loginResponse:"+header.toString());
                }
            } finally {
                loginResponse.close();
            }
            
            RequestBuilder requestBuilder = RequestBuilder.get();
            requestBuilder.setUri("http://192.168.167.112:9072/MSS-PURCHASE/framecont/listData.do");
            requestBuilder.addHeader(cookieHead);
            requestBuilder.addParameter("provinceCode", "04");
            HttpUriRequest httpGet = requestBuilder.build();
            CloseableHttpResponse response2 = httpClient.execute(httpGet);
            try {
                HttpEntity entity = response2.getEntity();
                System.out.println(EntityUtils.toString(entity));
                
            } finally {
                response2.close();
            }
            
        } finally{
            httpClient.close();
        }
    }
   
    @Test
    public void cookie() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://192.168.167.112:9072/MSS-PURCHASE/framecont/editFrameContract.do?id=5052099");
        httpGet.setHeader("Cookie", "JSESSIONID13=0000PzAGMgFy7nkeLwYZi9Gqje8:-1");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        try {
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            HttpEntity entity = httpResponse.getEntity();
//            System.out.println(EntityUtils.toString(entity));
        } finally {
            httpResponse.close();
        }
    }
    
    @Test
    public void cookie2() throws Exception {
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .build();
        RequestConfig localConfig = RequestConfig.copy(globalConfig)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();
        HttpGet httpGet = new HttpGet("/");
        httpGet.setConfig(localConfig);
    }
    
    @Test
    public void customPolicy(){
        PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();

        Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider>create()
                .register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider(publicSuffixMatcher))
                .register(CookieSpecs.STANDARD, new RFC6265CookieSpecProvider(publicSuffixMatcher))
                .register("easy", new EasySpecProvider())
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy")
                .build();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
    
    class EasySpecProvider implements CookieSpecProvider {

        @Override
        public CookieSpec create(HttpContext context) {
            
            return null;
        }
        
    }
    
    @Test
    public void customPolicy2(){
        String tmpdir = System.getProperty("java.io.tmpdir");
        System.out.println(tmpdir);
    }
}
