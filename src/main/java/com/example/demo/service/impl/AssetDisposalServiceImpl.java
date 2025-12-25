package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssetDisposalService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AssetDisposalServiceImpl implements AssetDisposalService {

    private final AssetDisposalRepository disposalRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    public AssetDisposalServiceImpl(
            AssetDisposalRepository disposalRepository,
            AssetRepository assetRepository,
            UserRepository userRepository) {
        this.disposalRepository = disposalRepository;
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AssetDisposal requestDisposal(Long assetId, AssetDisposal disposal) {
        Asset asset = assetRepository.findById(assetId).orElseThrow();
        disposal.setAsset(asset);
        return disposalRepository.save(disposal);
    }

    @Override
    public AssetDisposal approveDisposal(Long disposalId, Long adminId) {
        AssetDisposal d = disposalRepository.findById(disposalId).orElseThrow();
        d.setApprovedBy(userRepository.findById(adminId).orElseThrow());
        d.getAsset().setStatus("DISPOSED");
        return disposalRepository.save(d);
    }
}
