package com.example.demo.service.impl;

import com.example.demo.entity.Asset;
import com.example.demo.entity.DepreciationRule;
import com.example.demo.entity.Vendor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.DepreciationRuleRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.AssetService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final VendorRepository vendorRepository;
    private final DepreciationRuleRepository ruleRepository;

    public AssetServiceImpl(AssetRepository assetRepository,
                            VendorRepository vendorRepository,
                            DepreciationRuleRepository ruleRepository) {
        this.assetRepository = assetRepository;
        this.vendorRepository = vendorRepository;
        this.ruleRepository = ruleRepository;
    }

    @Override
    public Asset createAsset(Long vendorId, Long ruleId, Asset asset) {

        if (asset.getAssetTag() == null || asset.getAssetTag().isBlank()) {
            throw new IllegalArgumentException("Asset tag is required");
        }

        if (asset.getAssetName() == null || asset.getAssetName().isBlank()) {
            throw new IllegalArgumentException("Asset name is required");
        }

        if (asset.getPurchaseCost() == null || asset.getPurchaseCost() <= 0) {
            throw new IllegalArgumentException("Purchase cost must be greater than 0");
        }

        if (assetRepository.existsByAssetTag(asset.getAssetTag())) {
            throw new IllegalArgumentException("Asset tag already exists");
        }

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        DepreciationRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Depreciation rule not found"));

        asset.setVendor(vendor);
        asset.setDepreciationRule(rule);
        asset.setStatus("ACTIVE");
        asset.setCreatedAt(LocalDateTime.now());

        return assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAssetsByStatus(String status) {
        return assetRepository.findByStatus(status);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAsset(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }
}