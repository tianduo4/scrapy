package io.github.tianduo4.scrapy.request;

import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.response.Response;

/**
 * 解析器接口
 *
 * @author biezhi
 * @date 2018/1/12
 */
public interface Parser<T> {

    Result<T> parse(Response response);

}
