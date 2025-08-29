package com.bajaj.finserv.model;

import lombok.Data;

@Data
public class WebhookResponse {
    private String webhook;
    private String accessToken;
    private boolean success;
    private String message;
}