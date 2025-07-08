package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface PdfGenerationService {
    ResponseEntity<Map<String, Object>> generateEvidenceChainPdf(Integer assetId, String generatedBy);

}
