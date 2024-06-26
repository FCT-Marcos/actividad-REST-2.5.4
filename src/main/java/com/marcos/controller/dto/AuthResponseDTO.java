package com.marcos.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "username", "message", "token" })
public record AuthResponseDTO(String username, String message, String token) {
}