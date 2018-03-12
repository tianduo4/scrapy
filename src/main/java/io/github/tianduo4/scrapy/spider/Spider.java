package io.github.tianduo4.scrapy.spider;

import io.github.tianduo4.scrapy.config.Config;
import io.github.tianduo4.scrapy.event.ElvesEvent;
import io.github.tianduo4.scrapy.event.EventManager;
import io.github.tianduo4.scrapy.pipeline.Pipeline;
import io.github.tianduo4.scrapy.request.Parser;
import io.github.tianduo4.scrapy.request.Request;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.response.Result;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * 爬虫基类
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Data
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String>   startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider(String name) {
        this.name = name;
        EventManager.registerEvent(ElvesEvent.SPIDER_STARTED, this::onStart);
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    /**
     * 爬虫启动前执行
     */
    public void onStart(Config config) {
    }

    /**
     * 添加 Pipeline 处理
     */
    protected <T> Spider addPipeline(Pipeline<T> pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * 构建一个Request
     */
    public <T> Request<T> makeRequest(String url) {
        return makeRequest(url, this::parse);
    }

    public <T> Request<T> makeRequest(String url, Parser<T> parser) {
        return new Request(this, url, parser);
    }

    /**
     * 解析 DOM
     */
    protected abstract <T> Result<T> parse(Response response);

    protected void resetRequest(Consumer<Request> requestConsumer) {
        this.resetRequest(this.requests, requestConsumer);
    }

    protected void resetRequest(List<Request> requests, Consumer<Request> requestConsumer) {
        requests.forEach(requestConsumer::accept);
    }

}
