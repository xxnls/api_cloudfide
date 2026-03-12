package com.api.API.controller;

import com.api.API.dto.Producer.ProducerRequest;
import com.api.API.dto.Producer.ProducerResponse;
import com.api.API.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/producers")
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;

    @GetMapping("/{id}")
    public ProducerResponse getById(@PathVariable UUID id) {
        return producerService.getProducerById(id);
    }

    @GetMapping
    public List<ProducerResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return producerService.getProducers(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProducerResponse create(@RequestBody ProducerRequest request) {
        return producerService.createProducer(request);
    }

    @PutMapping("/{id}")
    public ProducerResponse update(@PathVariable UUID id, @RequestBody ProducerRequest request) {
        return producerService.updateProducer(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        producerService.deleteProducer(id);
    }
}
