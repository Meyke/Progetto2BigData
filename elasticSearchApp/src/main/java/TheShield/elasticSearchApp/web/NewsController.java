package TheShield.elasticSearchApp.web;


import TheShield.elasticSearchApp.model.News;
import TheShield.elasticSearchApp.repository.NewsRepository;
import TheShield.elasticSearchApp.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/theshield")
public class NewsController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private NewsService service;

    @RequestMapping("/all")
    public List<News> getAllNews() {
        return service.getAllNews();
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public void addNews(@RequestBody News news) {
        LOG.info("Adding user : {}", news.getTitle());
        service.addNews(news);
        LOG.info("Added user : {}", news.getTitle());

    }


    @RequestMapping(value = {"/findNewsByTitle/{title}"}, method = RequestMethod.GET)
    @ResponseBody
    public List<News> findSOQandAByTitle(@PathVariable("title") String title) {
        return service.findNewsByTitle(title);
        // Solo se voglio pagination
        // return RestPreconditions.checkFound( service.findNewsByTitle( title, PageRequest.of(content.isPresent() ? content.get() : 0, 12) ));
    }

    @RequestMapping(value = {"/findNewsByTitleOrContent/{title}", "/findNewsTitleOrContent/{title}/{content}"}, method = RequestMethod.GET)
    @ResponseBody
    public List<News> findSOQandAByTitleOrAnswer(@PathVariable("title") String title) {
        return service.findNewsByTitleOrContent(title);
        }

    @RequestMapping(value = "/findNewsById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<News> findNewsById(@PathVariable("id") String id) {
        return service.findNewsById(id);
    }


}
