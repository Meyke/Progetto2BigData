

package TheShield.elasticSearchApp.service;


import TheShield.elasticSearchApp.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<News> findNewsByTitle(String title);

    List<News> findNewsByTitleOrContent(String title);

    List<News> findNewsById(String id);

    List<News> getAllNews();

    void addNews(News news);
}
