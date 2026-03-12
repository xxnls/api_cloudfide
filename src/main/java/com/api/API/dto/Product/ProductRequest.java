package com.api.API.dto.Product;

import java.util.Map;
import java.util.UUID;

public record ProductRequest(
        String name,
        UUID producerId,
        Map<String, String> attributes
) { }
