package com.example.assetbasedriskassessmentplatformbackend.Service;

public interface PdfGenerationService {
    byte[] generateEvidenceChainPdf(Integer assetId, String generatedBy);


}
