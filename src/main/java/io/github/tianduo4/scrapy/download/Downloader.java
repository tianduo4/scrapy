package io.github.tianduo4.scrapy.download;

import io.github.tianduo4.scrapy.request.Request;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 下载器线程
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
public class Downloader implements Runnable {

    private final Scheduler scheduler;
    private final Request request;

    public Downloader(Scheduler scheduler, Request request) {
        this.scheduler = scheduler;
        this.request = request;
    }

    @Override
    public void run() {
        log.debug("[{}] 开始请求", request.getUrl());
        io.github.biezhi.request.Request httpReq = null;
        if ("get".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.get(request.getUrl());
        }
        if ("post".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.post(request.getUrl());
        }

        InputStream result = httpReq.contentType(request.contentType()).headers(request.getHeaders())
                .connectTimeout(request.getSpider().getConfig().timeout())
                .readTimeout(request.getSpider().getConfig().timeout())
                .stream();

        log.debug("[{}] 下载完毕", request.getUrl());
        Response response = new Response(request, result);
        scheduler.addResponse(response);
    }

}
