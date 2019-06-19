

package TheShield.elasticSearchApp.service;

import TheShield.elasticSearchApp.model.News;
import TheShield.elasticSearchApp.repository.NewsRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepositoryImpl newsRepository;

    @Override
    public List<News> findNewsByTitle(String title) {
        return newsRepository.findNewsByTitle(title);
    }

    @Override
    public List<News> findNewsByTitleOrContent(String title) {
        return newsRepository.findNewsByTitleOrContent(title);
    }

    @Override
    public List<News> findNewsById(String id) {
        return newsRepository.findNewsById(id);
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.getAllNews();
    }

    @Override
    public void addNews(News news) {
        newsRepository.addNews(news);

    }

}
