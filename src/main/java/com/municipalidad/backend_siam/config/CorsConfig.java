package com.municipalidad.backend_siam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // 🔥 IMPORTANTE
        config.setAllowCredentials(true);

        // 🔥 Permite cualquier IP (localhost, red, etc.)
        config.setAllowedOriginPatterns(List.of("*"));

        // 🔥 Headers y métodos abiertos
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));

        // 🔥 (OPCIONAL PERO RECOMENDADO)
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}