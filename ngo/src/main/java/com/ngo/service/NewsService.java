package com.ngo.service;

import com.ngo.common.ApiResponse;
import com.ngo.common.message.SuccessMessage;
import com.ngo.model.TodayNews;
import com.ngo.repository.TodayNewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class NewsService
{
    private final TodayNewsRepository todayNewsRepository;
    private final WebClient webClient;

    public NewsService(TodayNewsRepository todayNewsRepository)
    {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
                .build();
        this.webClient = WebClient.builder().exchangeStrategies(strategies).build();
        this.todayNewsRepository = todayNewsRepository;

    }

    public ApiResponse<List<TodayNews>> getTodayNews(String level)
    {
        List<TodayNews> todayNewsList = todayNewsRepository.findAllByLevel(level);
        return ApiResponse.success(SuccessMessage.GET_TODAY_NEWS_SUCCESS, todayNewsList);
    }
    public ApiResponse<Void> postTodayNews()
    {
        Map todayNewsData;
        try {
            todayNewsData = webClient.get()
                    .uri("http://localhost:8003/selectNews")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch(Exception e) {
            throw new IllegalStateException("뉴스 생성 과정에서 오류가 발생했습니다");
        }

        saveTodayNews(todayNewsData);
        return ApiResponse.success(SuccessMessage.POST_TODAY_NEWS_SUCCESS);
    }

    private void saveTodayNews(Map<String, List> todayNewsData)
    {
        if (todayNewsData != null && !todayNewsData.isEmpty())
        {
            for (String key : todayNewsData.keySet())
            {
                List<Map<String, String>> highLevelNews = (List<Map<String, String>>) todayNewsData.get(key);
                for (Map<String, String> newsMap : highLevelNews)
                {
                    TodayNews todayNews = TodayNews.builder()
                            .title(newsMap.get("title"))
                            .media(newsMap.get("media"))
                            .editor(newsMap.get("editor"))
                            .thumbnail(newsMap.get("thumbnail"))
                            .summary(newsMap.get("summary"))
                            .contents(newsMap.get("contents"))
                            .level(key)
                            .build();

                    todayNewsRepository.save(todayNews);
                }
            }
        }
    }
}