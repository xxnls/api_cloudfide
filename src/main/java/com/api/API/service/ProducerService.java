package com.api.API.service;

import com.api.API.dto.Producer.ProducerRequest;
import com.api.API.dto.Producer.ProducerResponse;
import com.api.API.model.Producer;
import com.api.API.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;

    public List<ProducerResponse> getProducers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return producerRepository.findAll(pageable)
                .stream()
                .map(p -> new ProducerResponse(p.getId(), p.getName()))
                .toList();
    }

    public ProducerResponse getProducerById(UUID id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        return new ProducerResponse(producer.getId(), producer.getName());
    }

    @Transactional
    public ProducerResponse createProducer(ProducerRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        if (producerRepository.existsByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Producer already exists");
        }

        Producer producer = new Producer();
        producer.setName(request.name());
        Producer saved = producerRepository.save(producer);

        return new ProducerResponse(saved.getId(), saved.getName());
    }

    @Transactional
    public ProducerResponse updateProducer(UUID id, ProducerRequest request) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        producer.setName(request.name());
        Producer saved = producerRepository.save(producer);
        return new ProducerResponse(saved.getId(), saved.getName());
    }

    public void deleteProducer(UUID id) {
        if (!producerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found");
        }
        producerRepository.deleteById(id);
    }
}
