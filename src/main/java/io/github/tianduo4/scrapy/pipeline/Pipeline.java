package io.github.tianduo4.scrapy.pipeline;

import io.github.tianduo4.scrapy.request.Request;

/**
 * 数据处理接口
 *
 * @author xushipeng
 * @date 2018/3/12
 */
public interface Pipeline<T> {

    void process(T item, Request request);

}
