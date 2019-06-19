package TheShield.elasticSearchApp.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableElasticsearchRepositories(basePackages = "TheShield.elasticSearchApp.repository")
@EnableWebMvc
@ComponentScan(basePackages = { "TheShield.elasticSearchApp" })
public class Config {

    @Value("${elasticsearch.cluster.name:meyke-cluster}")
    private String clusterName;

    @Bean
    public Client client() {
        //clusterName = "meyke-cluster";
        TransportClient client = null;
        try {
            final Settings elasticsearchSettings = Settings.builder()
                    .put("client.transport.sniff", false)
                    .put("cluster.name", clusterName).build();
            client = new PreBuiltTransportClient(elasticsearchSettings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    //@Bean
    //public ElasticsearchOperations elasticsearchTemplate() {
    //    return new ElasticsearchTemplate(client());
    //}
}
