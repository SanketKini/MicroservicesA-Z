package com.awd.feature.toggle.configuration;

import com.awd.feature.toggle.model.FeatureEnvData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

import static com.awd.feature.toggle.constant.FeatureToggleConstants.*;

@Configuration
@ComponentScan("com.awd.feature.toggle")
public class FeatureToggleConfiguration {
    @Value("#{{${local-flags:}}}")
    Map<String, Boolean> localFeatureMap;

    @Value("#{'${features:}'.split(',')}")
    private List<String> features;

    @Value("${feature-toggle.service.url:}")
    private List<String> ftServiceUrl;

    @Value("${service.serviceId:}")
    private List<String> serviceName;

    @Value("${environment:}")
    private List<String> env;

    @Bean
    public FeatureEnvData featureEnvData() {
        return FeatureEnvData.builder().build();
    }

    @Bean(name = "featureToggleWebClient")
    public WebClient featureToggleWebClient() {
        final ObjectMapper mapper = new ObjectMapper()
                .findAndRegisterModules()
                .setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                    @Override
                    public Class<?> findPOJOBuilder(AnnotatedClass ac) {
                        final JsonDeserialize jsonDeserialize = ac.getAnnotation(JsonDeserialize.class);
                        return jsonDeserialize.builder();
                    }

                    @Override
                    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
                        if (ac.hasAnnotation(JsonPOJOBuilder.class)) {
                            return super.findPOJOBuilderConfig(ac);
                        }
                        return new JsonPOJOBuilder.Value(BUILD_METHOD_NAME, WITH_PREFIX);
                    }
                });
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper)))
                .build();
        return WebClient.builder()
                .baseUrl(ftServiceUrl.toString())
                .defaultHeaders(headers -> headers.addAll(defaultHeaders()))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    private MultiValueMap<String, String> defaultHeaders() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(USER_AGENT, USER_AGENT_VALUE);
        map.add(ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_VALUE);
        map.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        map.add(ACCEPT_ENCODING, ACCEPT_ENCODING_VALUE);
        return map;
    }

}
