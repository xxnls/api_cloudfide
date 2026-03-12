package com.api.API.dto.Producer;

import java.util.UUID;

public record ProducerResponse(
        UUID id,
        String name
) { }
