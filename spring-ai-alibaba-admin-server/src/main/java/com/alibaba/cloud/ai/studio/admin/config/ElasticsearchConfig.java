package com.alibaba.cloud.ai.studio.admin.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    @Bean
    public RestClient restClient(ElasticsearchProperties properties) {
        try {
            URL url = new URL(properties.getUrl());
            
            return RestClient.builder(
                    new HttpHost(url.getHost(), url.getPort(), url.getProtocol()))
                .setRequestConfigCallback(requestConfigBuilder -> 
                    requestConfigBuilder
                        .setConnectTimeout(properties.getConnectTimeout())
                        .setSocketTimeout(properties.getSocketTimeout()))
                    .setHttpClientConfigCallback(httpClientBuilder -> {
                        httpClientBuilder
                                .setMaxConnTotal(properties.getConnectionPool().getMaxConnections())
                                .setMaxConnPerRoute(properties.getConnectionPool().getMaxIdleConnections());
                        // 如果配置了用户名和密码，添加认证
                        if (!properties.getUsername().isEmpty() && properties.getPassword() != null) {
                            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                            credentialsProvider.setCredentials(
                                    AuthScope.ANY,
                                    new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword())
                            );
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        }

                        return httpClientBuilder;
                    })
                .build();
        } catch (Exception e) {
            throw new RuntimeException("创建RestClient失败", e);
        }
    }

    @Bean
    public RestClientTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClientTransport transport) {
        return new ElasticsearchClient(transport);
    }
}