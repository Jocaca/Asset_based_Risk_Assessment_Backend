package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface EvidenceChainService {
    ResponseEntity<Map<String, Object>> getall(int assetId);
    ResponseEntity<Map<String, Object>> getPdfUrl(int id);
    ResponseEntity<Map<String, Object>> addEvidenceToAuditproject(int projectId, int evidenceChainId);
}
