package com.example.demo.service;

import com.example.demo.entity.AssetDisposal;

public interface AssetDisposalService {
    AssetDisposal requestDisposal(Long assetId, AssetDisposal disposal);
    AssetDisposal approveDisposal(Long disposalId, Long adminId);
}

8. AssetLifecycleEventService.java
package com.example.demo.service;

import com.example.demo.entity.AssetLifecycleEvent;
import java.util.List;

public interface AssetLifecycleEventService {
    AssetLifecycleEvent logEvent(Long assetId, AssetLifecycleEvent event);
    List<AssetLifecycleEvent> getEventsForAsset(Long assetId);
}