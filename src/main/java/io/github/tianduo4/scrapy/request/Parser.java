package io.github.tianduo4.scrapy.request;

import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.response.Response;

/**
 * 解析器接口
 *
 * @author xushipeng
 * @date 2018/3/12
 */
public interface Parser<T> {

    Result<T> parse(Response response);

}
