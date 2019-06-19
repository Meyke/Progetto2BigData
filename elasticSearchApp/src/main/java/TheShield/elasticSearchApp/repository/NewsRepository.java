package TheShield.elasticSearchApp.repository;

import TheShield.elasticSearchApp.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsRepository {
    void addNews(News news);
    List<News> getAllNews();
    List<News> findNewsByTitle(String title);
    List<News> findNewsByTitleOrContent(String title);
    List<News> findNewsById(String id);


}
