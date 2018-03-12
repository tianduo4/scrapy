package event;

import io.github.tianduo4.scrapy.Elves;
import io.github.tianduo4.scrapy.config.Config;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.spider.Spider;

/**
 * @author biezhi
 * @date 2018/1/12
 */
public class ElvesEventTest {

    public static void main(String[] args) {
        Elves.me(new Spider("测试爬虫") {
            @Override
            public Result<String> parse(Response response) {
                return new Result<>(response.body().toString());
            }
        }, Config.me()).onStart(config -> System.out.println("asasas")).start();
    }

}
