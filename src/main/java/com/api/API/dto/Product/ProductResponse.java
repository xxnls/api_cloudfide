package com.api.API.dto.Product;

import com.api.API.dto.Producer.ProducerResponse;

import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        ProducerResponse producer,
        Map<String, String> attributes
) { }
