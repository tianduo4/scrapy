package io.github.tianduo4.scrapy.request;

import io.github.tianduo4.scrapy.spider.Spider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Request 请求
 *
 * @author xushipeng
 * @date 2018/3/12
 */
@Getter
public class Request<T> {

    private Spider spider;
    private String url;
    private String              method  = "GET";
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    private String contentType = "text/html; charset=UTF-8";
    private String charset = "UTF-8";
    private Parser<T> parser;

    public Request(Spider spider, String url, Parser<T> parser) {
        this.spider = spider;
        this.url = url;
        this.parser = parser;
        this.header("User-Agent", spider.getConfig().userAgent());
    }

    public Request header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Request cookie(String key, String value) {
        this.cookies.put(key, value);
        return this;
    }

    public String header(String key) {
        return this.headers.get(key);
    }

    public String cookie(String key) {
        return this.cookies.get(key);
    }

    public void setParser(Parser<T> parser) {
        this.parser = parser;
    }

    public String contentType() {
        return contentType;
    }

    public Request contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String charset() {
        return charset;
    }

    public Request charset(String charset) {
        this.charset = charset;
        return this;
    }

    public Request method(String method) {
        this.method = method;
        return this;
    }

    public String method() {
        return this.method;
    }
}
