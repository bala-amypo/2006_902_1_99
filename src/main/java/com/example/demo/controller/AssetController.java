package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetRepository assetRepository;
    private final VendorRepository vendorRepository;
    private final DepreciationRuleRepository ruleRepository;

    public AssetController(
            AssetRepository assetRepository,
            VendorRepository vendorRepository,
            DepreciationRuleRepository ruleRepository) {
        this.assetRepository = assetRepository;
        this.vendorRepository = vendorRepository;
        this.ruleRepository = ruleRepository;
    }

    @PostMapping("/{vendorId}/{ruleId}")
    public ResponseEntity<Asset> createAsset(
            @PathVariable Long vendorId,
            @PathVariable Long ruleId,
            @Valid @RequestBody Asset asset) {

        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        DepreciationRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        asset.setVendor(vendor);
        asset.setDepreciationRule(rule);

        return ResponseEntity.ok(assetRepository.save(asset));
    }

    @GetMapping("/status/{status}")
    public List<Asset> getByStatus(@PathVariable String status) {
        return assetRepository.findByStatus(status);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Asset> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        asset.setStatus(status);
        return ResponseEntity.ok(assetRepository.save(asset));
    }
}
