package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssetLifecycleEventService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AssetLifecycleEventServiceImpl implements AssetLifecycleEventService {

    private final AssetLifecycleEventRepository repo;
    private final AssetRepository assetRepository;

    public AssetLifecycleEventServiceImpl(AssetLifecycleEventRepository repo,
                                          AssetRepository assetRepository) {
        this.repo = repo;
        this.assetRepository = assetRepository;
    }

    @Override
    public AssetLifecycleEvent logEvent(Long assetId, AssetLifecycleEvent event) {
        if (event.getEventDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Future date");
        }

        Asset asset = assetRepository.findById(assetId).orElseThrow();
        event.setAsset(asset);
        return repo.save(event);
    }

    @Override
    public List<AssetLifecycleEvent> getEventsForAsset(Long assetId) {
        return repo.findByAssetIdOrderByEventDateDesc(assetId);
    }
}
