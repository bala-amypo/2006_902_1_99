package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssetService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
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
        if (asset.getPurchaseCost() < 0) {
            throw new IllegalArgumentException("Invalid cost");
        }

        Vendor v = vendorRepository.findById(vendorId).orElseThrow();
        DepreciationRule r = ruleRepository.findById(ruleId).orElseThrow();

        asset.setVendor(v);
        asset.setDepreciationRule(r);
        asset.setStatus("ACTIVE");

        return assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAsset(Long id) {
        return assetRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Asset> getAssetsByStatus(String status) {
        return assetRepository.findByStatus(status);
    }
}
