package io.github.tianduo4.scrapy.response;

import io.github.tianduo4.scrapy.request.Request;
import io.github.tianduo4.scrapy.utils.ElvesUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应结果封装
 * <p>
 * 存储 Item 数据和新添加的 Request 列表
 *
 * @author xushipeng
 * @date 2018/3/12
 */
@Data
@NoArgsConstructor
public class Result<T> {

    private List<Request> requests = new ArrayList<>();
    private T item;

    public Result(T item) {
        this.item = item;
    }

    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }

    public Result addRequests(List<Request> requests) {
        if (!ElvesUtils.isEmpty(requests)) {
            this.requests.addAll(requests);
        }
        return this;
    }

}
