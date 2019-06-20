

package TheShield.elasticSearchApp.repository;


import TheShield.elasticSearchApp.model.News;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


@Repository
public class NewsRepositoryImpl implements NewsRepository{

    //private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${elasticsearch.index.name:news_idx}")
    private String indexName;

    @Value("${elasticsearch.user.type:news}")
    private String indexTypeName;

    @Autowired
    private ElasticsearchTemplate esTemplate; //fornitoci direttamente da Spring Data

    @Autowired
    private TransportClient client;


    /*
    @Override
    public List<News> getAllNews() {
        SearchQuery getAllQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery()).build();
        return esTemplate.queryForList(getAllQuery, News.class);
    }

     */

    @Override
    public List<News> getAllNews() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery()).build();

        SearchRequestBuilder builder = client
                .prepareSearch("news_idx")
                .setTypes("news")
                .setQuery(QueryBuilders.matchAllQuery())
                .setFrom(0).setSize(60).setExplain(true);

        SearchResponse response  = builder.execute().actionGet();

        return estraiRisultati(response);
    }

    @Override
    public void addNews(News news) {

        IndexQuery userQuery = new IndexQuery();
        userQuery.setIndexName(indexName);
        userQuery.setType(indexTypeName);
        userQuery.setObject(news);

        //LOG.info("News indexed: {}", esTemplate.index(userQuery));
        //LOG.info("nome indice {}", indexName);
        //LOG.info("nome tipo {}", indexTypeName);
        esTemplate.refresh(News.class);


    }


    @Override
    public List<News> findNewsByTitle(String title) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.matchQuery("title", title)).build();
        List<News> newsCollection = esTemplate.queryForList(searchQuery, News.class);
        return newsCollection;
        //PER PAGINAZIONE https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.query-methods.query-creation
        //nella parte 3.1. Filter Builder
        //Page<SampleEntity> sampleEntities = elasticsearchTemplate.queryForPage(searchQuery,SampleEntity.class);
    }

    @Override
    public List<News> findNewsByTitleOrContent(String title) {
        SearchRequestBuilder builder = client
                .prepareSearch("news_idx")
                .setTypes("news")
                .setQuery(QueryBuilders.multiMatchQuery(title, "title", "content"))
                .setFrom(0).setSize(10).setExplain(true);


        SearchResponse response  = builder.execute().actionGet();

        return estraiRisultati(response);
    }

    @Override
    public List<News> findNewsById(String id) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.matchQuery("id", id)).build();
        SearchRequestBuilder builder = client
                .prepareSearch("news_idx")
                .setTypes("news")
                .setQuery(QueryBuilders.matchQuery(id, "id"))
                .setFrom(0).setSize(60).setExplain(true);


        SearchResponse response  = builder.execute().actionGet();

        return estraiRisultati(response);
    }

    private List<News> estraiRisultati(SearchResponse response){
        Logger LOG = LoggerFactory.getLogger("il loggatore");
        List<News> content = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            ObjectMapper mapper = new ObjectMapper();
            News news = null;
            try {
                news = mapper.readValue(source, News.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            content.add(news);
            float documentScore = hit.getScore(); // <---- score di un hit
            //LOG.info("hitStampate: {}", hit.toString());
            //LOG.info("documentScore: {}", documentScore);
            news.setScore(documentScore);
            //LOG.info("setScore: {}", news.getId() + " " + news.getScore());
            //LOG.info("getMaxScore: {}", response.getHits().getMaxScore());

        }
        //Qualora occorresse paginare.
        //return new PageImpl<Website>(content, pageRequest, response.getHits().getTotalHits());
        return content;
    }
}
