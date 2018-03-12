package examples;

import io.github.tianduo4.scrapy.Elves;
import io.github.tianduo4.scrapy.config.Config;
import io.github.tianduo4.scrapy.pipeline.Pipeline;
import io.github.tianduo4.scrapy.request.Request;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 豆瓣电影示例
 *
 * @author biezhi
 * @date 2018/1/11
 */
public class DoubanExample {

    @Slf4j
    static class DoubanSpider extends Spider {

        public DoubanSpider(String name) {
            super(name);
            this.startUrls(
                    "https://movie.douban.com/tag/爱情",
                    "https://movie.douban.com/tag/喜剧",
                    "https://movie.douban.com/tag/动画",
                    "https://movie.douban.com/tag/动作",
                    "https://movie.douban.com/tag/史诗",
                    "https://movie.douban.com/tag/犯罪");
        }

        @Override
        public void onStart(Config config) {
            this.addPipeline((Pipeline<List<String>>) (item, request) -> log.info("保存到文件: {}", item));
        }

        public Result parse(Response response) {
            Result<List<String>> result   = new Result<>();
            Elements             elements = response.body().css("#content table .pl2 a");

            List<String> titles = elements.stream().map(Element::text).collect(Collectors.toList());
            result.setItem(titles);

            // 获取下一页 URL
            Elements nextEl = response.body().css("#content > div > div.article > div.paginator > span.next > a");
            if (null != nextEl && nextEl.size() > 0) {
                String  nextPageUrl = nextEl.get(0).attr("href");
                Request nextReq     = this.makeRequest(nextPageUrl, this::parse);
                result.addRequest(nextReq);
            }
            return result;
        }

    }

    public static void main(String[] args) {
        DoubanSpider doubanSpider = new DoubanSpider("豆瓣电影");
        Elves.me(doubanSpider, Config.me()).start();
    }

}
