package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "*")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @PostMapping("/generate/{assetId}")
    public ResponseEntity<Map<String, Object>> generateEvidenceChain(
            @PathVariable Integer assetId,
            @RequestHeader("X-Generated-By") String generatedBy) {
        return pdfGenerationService.generateEvidenceChainPdf(assetId,generatedBy);
//        try {
//            byte[] pdfContent = pdfGenerationService.generateEvidenceChainPdf(assetId, generatedBy);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("filename", "evidence_chain_" + assetId + ".pdf");
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//
//            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
    }

//    @GetMapping("/download/{filename:.+}")
//    public ResponseEntity<byte[]> downloadPdf(@PathVariable String filename) {
//        try {
//            Path filePath = Paths.get(pdfGenerationService.getPdfStorageDir(), filename);
//            byte[] fileContent = Files.readAllBytes(filePath);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("filename", filename);
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//
//            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
