package examples;

import io.github.tianduo4.scrapy.Elves;
import io.github.tianduo4.scrapy.config.Config;
import io.github.tianduo4.scrapy.pipeline.Pipeline;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 网易新闻示例
 *
 * @author xushipeng
 * @date 2018/3/12
 */
public class News163Example {

    @Slf4j
    static class News163Spider extends Spider {
        public News163Spider(String name) {
            super(name);
            this.startUrls(
                    "http://news.163.com/special/0001386F/rank_news.html",
                    "http://news.163.com/special/0001386F/rank_ent.html", // 娱乐
                    "http://news.163.com/special/0001386F/rank_sports.html", // 体育
                    "http://news.163.com/special/0001386F/rank_tech.html", // 科技
                    "http://news.163.com/special/0001386F/game_rank.html", //游戏
                    "http://news.163.com/special/0001386F/rank_book.html"); // 读书
        }

        @Override
        public void onStart(Config config) {
            this.addPipeline((Pipeline<List<String>>) (item, request) -> item.forEach(System.out::println));
            this.requests.forEach(request -> {
                request.contentType("text/html; charset=gb2312");
                request.charset("gb2312");
            });
        }

        @Override
        protected Result parse(Response response) {
            List<String> titles = response.body().css("div.areabg1 .area-half.left div.tabContents td a").stream()
                    .map(Element::text)
                    .collect(Collectors.toList());

            return new Result(titles);
        }
    }

    public static void main(String[] args) {
        Elves.me(new News163Spider("网易新闻")).start();
    }

}
