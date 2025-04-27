package com.danosoftware.spring_boot_logging.masking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(mapper.getSerializerFactory()
                .withSerializerModifier(new SensitiveSerializer.SensitiveBeanSerializerModifier()));
        return mapper;
    }
}