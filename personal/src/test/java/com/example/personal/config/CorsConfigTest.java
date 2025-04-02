//package com.example.personal.config;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import static org.junit.Assert.*;
//
//import java.util.List;
//
//public class CorsConfigTest {
//    private CorsConfig corsConfig;
//
//    @Before
//    public void setUp() {
//        corsConfig = new CorsConfig();
//    }
//
//    @Test
//    public void testCorsFilterConfiguration() {
//        CorsFilter corsFilter = corsConfig.corsFilter();
//        assertNotNull(corsFilter);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:5173"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        source.registerCorsConfiguration("/**", config);
//
//        CorsFilter expectedFilter = new CorsFilter(source);
//        assertNotNull(expectedFilter);
//    }
//}
