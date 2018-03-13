package io.github.tianduo4.scrapy.response;

import io.github.tianduo4.scrapy.request.Request;
import lombok.Getter;

import java.io.InputStream;

/**
 * 响应对象
 *
 * @author xushipeng
 * @date 2018/3/12
 */
public class Response {

    @Getter
    private Request request;
    private Body    body;

    public Response(Request request, InputStream inputStream) {
        this.request = request;
        this.body = new Body(inputStream, request.charset());
    }

    public Body body() {
        return body;
    }

}
