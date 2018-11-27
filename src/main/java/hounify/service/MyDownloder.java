package hounify.service;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.geccocrawler.gecco.annotation.Downloader;
import com.geccocrawler.gecco.downloader.AbstractDownloader;
import com.geccocrawler.gecco.downloader.DownloadException;
import com.geccocrawler.gecco.downloader.UserAgent;
import com.geccocrawler.gecco.downloader.proxy.Proxys;
import com.geccocrawler.gecco.downloader.proxy.ProxysContext;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;
import com.geccocrawler.gecco.spider.SpiderThreadLocal;
import com.geccocrawler.gecco.utils.UrlUtils;
import org.apache.http.HttpHost;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 *自定义下载器
 */

@Downloader("myDownloder")
public abstract class MyDownloder extends AbstractDownloader {
    private WebClient webClient;
    public MyDownloder() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.webClient.getOptions().setRedirectEnabled(false);
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.setJavaScriptTimeout(10000L);
        this. webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    public HttpResponse download(HttpRequest request, int timeout) throws DownloadException {
        try {
            URL ex = new URL(request.getUrl());
            WebRequest webRequest = new WebRequest(ex);
            webRequest.setHttpMethod(HttpMethod.GET);
            if (request instanceof HttpPostRequest) {
                HttpPostRequest isMobile = (HttpPostRequest) request;
                webRequest.setHttpMethod(HttpMethod.POST);
                ArrayList proxy = new ArrayList();
                Iterator page = isMobile.getFields().entrySet().iterator();

                while (page.hasNext()) {
                    Map.Entry resp = (Map.Entry) page.next();
                    NameValuePair webResponse = new NameValuePair((String) resp.getKey(), resp.getValue().toString());
                    proxy.add(webResponse);
                }
                webRequest.setRequestParameters(proxy);
            }

            boolean isMobile1 = SpiderThreadLocal.get().getEngine().isMobile();
            webRequest.setAdditionalHeader("User-Agent", UserAgent.getUserAgent(isMobile1));
            webRequest.setAdditionalHeaders(request.getHeaders());
            Proxys proxys = ProxysContext.get();
            HttpHost proxy1 = proxys.getProxy();
            if (proxy1 != null) {
                webRequest.setProxyHost(proxy1.getHostName());
                webRequest.setProxyPort(proxy1.getPort());
            }

            this.webClient.getOptions().setTimeout(timeout);
            this.webClient.getPage(webRequest);
            HtmlPage page1 = (HtmlPage) this.webClient.getPage(request.getUrl());
            HttpResponse resp1 = new HttpResponse();
            WebResponse webResponse1 = page1.getWebResponse();
            int status = webResponse1.getStatusCode();
            resp1.setStatus(status);
            String content;
            if (status != 302 && status != 301) {
                if (status != 200) {
                    throw new DownloadException("ERROR : " + status);
                }

                content = page1.asXml();
                resp1.setContent(content);

                ByteArrayInputStream inByte = new ByteArrayInputStream(input2byte(webResponse1.getContentAsStream()));
                resp1.setRaw(inByte);
                String contentType = webResponse1.getContentType();
                resp1.setContentType(contentType);
                String charset = this.getCharset(request.getCharset(), contentType);
                resp1.setCharset(charset);
            } else {
                content = webResponse1.getResponseHeaderValue("Location");
                resp1.setContent(UrlUtils.relative2Absolute(request.getUrl(), content));
            }

            return resp1;
        } catch (Exception var14) {
            throw new DownloadException(var14);
        }
    }

    public void shutdown() {
        this.webClient.close();
    }

/**
 * 把InputStream 转换成byte[]
 * @param InputStream
 * @return byte[]
 * @throws IOException
 */
    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
}
