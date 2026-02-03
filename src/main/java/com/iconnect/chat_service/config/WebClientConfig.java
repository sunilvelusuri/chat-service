package com.iconnect.chat_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {



    /* =========================================================
       ADMIN WEB CLIENT  (Synapse Admin APIs)
       Used ONLY for:
       - Create users
       ========================================================= */
    @Bean
    @Qualifier("matrixAdminWebClient")
    public WebClient matrixAdminWebClient() {

        String adminToken = "syt_YWRtaW4_UWaQsxdhfNcSDrTCaIKJ_0sH5MJ";

        if (adminToken == null || adminToken.isBlank()) {
            throw new IllegalStateException(
                    "❌ MATRIX ADMIN TOKEN NOT CONFIGURED"
            );
        }

        adminToken = adminToken.trim();

        return WebClient.builder()
                .baseUrl("http://localhost:8008/_synapse/admin/v2")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

                // 🔍 TEMP DEBUG FILTER (REMOVE AFTER ISSUE IS FIXED)
                .filter((request, next) -> {
                    System.out.println("=== MATRIX ADMIN REQUEST ===");
                    System.out.println("URL  : " + request.url());
                    System.out.println("AUTH : " +
                            request.headers().getFirst(HttpHeaders.AUTHORIZATION));
                    return next.exchange(request);
                })

                .build();
    }

    /* =========================================================
       CLIENT WEB CLIENT  (Matrix Client APIs)
       Used for:
       - Create rooms
       - Invite users
       - Send messages
       ========================================================= */
    @Bean
    @Qualifier("matrixClientWebClient")
    public WebClient matrixClientWebClient() {

        String clientToken = "syt_Y2hhdC1zZXJ2aWNl_HjTVlAdCSWeIrpZSVAxn_2XfGIQ";

        if (clientToken == null || clientToken.isBlank()) {
            throw new IllegalStateException(
                    "❌ MATRIX SERVICE TOKEN NOT CONFIGURED"
            );
        }

        clientToken = clientToken.trim();

        return WebClient.builder()
                .baseUrl("http://localhost:8008/_matrix/client/v3")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + clientToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}




