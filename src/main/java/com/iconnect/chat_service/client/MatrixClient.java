package com.iconnect.chat_service.client;

import com.iconnect.chat_service.controller.dto.ChatMessageResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.UUID;


@Component
public class MatrixClient {


    private final WebClient adminWebClient;
    private final WebClient clientWebClient;

    public MatrixClient(
            @Qualifier("matrixAdminWebClient") WebClient adminWebClient,
            @Qualifier("matrixClientWebClient") WebClient clientWebClient
    ) {
        this.adminWebClient = adminWebClient;
        this.clientWebClient = clientWebClient;
    }



    /* ---------- ADMIN APIs ---------- */

    public String createUser(String iconnectUserId) {

        String matrixUserId = "@" + iconnectUserId + ":iconnect.chat";

        try {
            adminWebClient.put()
                    .uri("/users/{userId}", matrixUserId)
                    .bodyValue(Map.of(
                            "password", UUID.randomUUID().toString(),
                            "admin", false,
                            "deactivated", false
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        } catch (WebClientResponseException.Conflict ex) {
            // 409 → user already exists → idempotent behavior
        }
        return matrixUserId;
    }

    public String createDmRoom(String user1, String user2) {

        // NOTE:
        // user1, user2 are iconnect userIds
        // Room creator is the service token user

        Map<String, Object> requestBody = Map.of(
                "preset", "trusted_private_chat",
                "is_direct", true
        );

        @Nullable Map response =
                clientWebClient.post()
                        .uri("/createRoom")
                        .bodyValue(requestBody)
                        .retrieve()
                        .onStatus(
                                status -> status.is4xxClientError(),
                                resp -> resp.bodyToMono(String.class)
                                        .map(body -> new RuntimeException(
                                                "Matrix createRoom failed: " + body))
                        )
                        .bodyToMono(Map.class)
                        .block();

        return (String) response.get("room_id");
    }

    public void inviteUser(String roomId, String matrixUserId) {

        clientWebClient.post()
                .uri("/rooms/{roomId}/invite", roomId)
                .bodyValue(Map.of(
                        "user_id", matrixUserId
                ))
                .retrieve()
                .toBodilessEntity()
                .block();
    }



    public void sendMessage(
            String matrixRoomId,
            String accessToken,
            String message) {

        String txnId = UUID.randomUUID().toString();

        clientWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/_matrix/client/v3/rooms/{roomId}/send/m.room.message/{txnId}")
                        .build(
                                UriUtils.encodePathSegment(matrixRoomId, StandardCharsets.UTF_8),
                                txnId
                        ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "msgtype", "m.text",
                        "body", message
                ))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(err -> new RuntimeException("Matrix send failed: " + err))
                )
                .toBodilessEntity()
                .block();
    }



    public List<ChatMessageResponse> fetchMessages(
            String matrixRoomId,
            int limit) {

        @Nullable Map response =
                clientWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/rooms/{roomId}/messages")
                                .queryParam("dir", "b")
                                .queryParam("limit", limit)
                                .build(matrixRoomId)
                        )
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

        List<Map<String, Object>> chunk =
                (List<Map<String, Object>>) response.get("chunk");

        List<ChatMessageResponse> messages = new ArrayList<>();

        for (Map<String, Object> event : chunk) {

            if (!"m.room.message".equals(event.get("type"))) {
                continue;
            }

            Map<String, Object> content =
                    (Map<String, Object>) event.get("content");

            messages.add(
                    new ChatMessageResponse(
                            (String) event.get("sender"),
                            (String) content.get("body"),
                            Instant.parse(
                                    (String) event.get("origin_server_ts")
                            ).toEpochMilli()
                    )
            );
        }

        return messages;
    }


    public void sendTyping(
            String matrixRoomId,
            String matrixUserId,
            boolean typing) {

        clientWebClient.put()
                .uri("/rooms/{roomId}/typing/{userId}",
                        matrixRoomId, matrixUserId)
                .bodyValue(Map.of(
                        "typing", typing,
                        "timeout", 30000
                ))
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    public void sendReadReceipt(
            String matrixRoomId,
            String matrixUserId,
            String eventId) {

        clientWebClient.post()
                .uri("/rooms/{roomId}/receipt/m.read/{eventId}",
                        matrixRoomId, eventId)
                .retrieve()
                .toBodilessEntity()
                .block();
    }



}