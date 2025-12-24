package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/events")
public class AssetLifecycleEventController {

    private final AssetRepository assetRepository;
    private final AssetLifecycleEventRepository eventRepository;

    public AssetLifecycleEventController(
            AssetRepository assetRepository,
            AssetLifecycleEventRepository eventRepository) {
        this.assetRepository = assetRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/{assetId}")
    public ResponseEntity<?> addEvent(
            @PathVariable Long assetId,
            @Valid @RequestBody AssetLifecycleEvent event) {

        if (event.getEventDate().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        event.setAsset(asset);
        return ResponseEntity.ok(eventRepository.save(event));
    }
}
