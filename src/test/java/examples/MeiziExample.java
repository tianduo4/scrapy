package examples;

import io.github.tianduo4.scrapy.Elves;
import io.github.tianduo4.scrapy.config.Config;
import io.github.tianduo4.scrapy.config.UserAgent;
import io.github.tianduo4.scrapy.pipeline.Pipeline;
import io.github.tianduo4.scrapy.request.Parser;
import io.github.tianduo4.scrapy.request.Request;
import io.github.tianduo4.scrapy.response.Response;
import io.github.tianduo4.scrapy.response.Result;
import io.github.tianduo4.scrapy.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 妹子图示例
 *
 * @author biezhi
 * @date 2018/1/12
 */
public class MeiziExample {

    @Slf4j
    static class MeiziSpider extends Spider {

        private String storageDir = "E:/meizi";

        public MeiziSpider(String name) {
            super(name);
            this.startUrls(
                    "http://www.meizitu.com/a/pure.html",
                    "http://www.meizitu.com/a/cute.html",
                    "http://www.meizitu.com/a/sexy.html",
                    "http://www.meizitu.com/a/fuli.html",
                    "http://www.meizitu.com/a/legs.html");
        }

        @Override
        public void onStart(Config config) {
            this.addPipeline((Pipeline<List<String>>) (item, request) -> {
                item.forEach(imgUrl -> {
                    log.info("开始下载: {}", imgUrl);
                    io.github.biezhi.request.Request.get(imgUrl)
                            .header("Referer", request.getUrl())
                            .header("User-Agent", UserAgent.CHROME_FOR_MAC)
                            .connectTimeout(20_000)
                            .readTimeout(20_000)
                            .receive(new File(storageDir, System.currentTimeMillis() + ".jpg"));
                });

                log.info("[{}] 图片下载 OJ8K.", request.getUrl());
            });

            this.requests.forEach(this::resetRequest);
        }

        private Request resetRequest(Request request) {
            request.contentType("text/html; charset=gb2312");
            request.charset("gb2312");
            return request;
        }

        @Override
        protected Result parse(Response response) {
            Result   result   = new Result<>();
            Elements elements = response.body().css("#maincontent > div.inWrap > ul > li:nth-child(1) > div > div > a");
            log.info("elements size: {}", elements.size());

            List<Request> requests = elements.stream()
                    .map(element -> element.attr("href"))
                    .map(href -> MeiziSpider.this.makeRequest(href, new MeiziSpider.PictureParser()))
                    .map(this::resetRequest)
                    .collect(Collectors.toList());
            result.addRequests(requests);

            // 获取下一页 URL
            Optional<Element> nextEl = response.body().css("#wp_page_numbers > ul > li > a").stream().filter(element -> "下一页".equals(element.text())).findFirst();
            if (nextEl.isPresent()) {
                String          nextPageUrl = "http://www.meizitu.com/a/" + nextEl.get().attr("href");
                Request<String> nextReq     = MeiziSpider.this.makeRequest(nextPageUrl, this::parse);
                result.addRequest(this.resetRequest(nextReq));
            }
            return result;
        }

        static class PictureParser implements Parser<List<String>> {
            @Override
            public Result<List<String>> parse(Response response) {
                Elements     elements = response.body().css("#picture > p > img");
                List<String> src      = elements.stream().map(element -> element.attr("src")).collect(Collectors.toList());
                return new Result<>(src);
            }
        }

    }


    public static void main(String[] args) {
        MeiziSpider meiziSpider = new MeiziSpider("妹子图");
        Elves.me(meiziSpider, Config.me().delay(3000)).start();
    }

}
