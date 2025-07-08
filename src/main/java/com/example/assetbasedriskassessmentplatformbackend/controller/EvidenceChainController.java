package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.EvidenceChainService;
import com.example.assetbasedriskassessmentplatformbackend.entity.EvidenceChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/evidence_chain")
@CrossOrigin(origins = "*")
public class EvidenceChainController {
    @Autowired
    private EvidenceChainService evidenceChainService;

    @GetMapping("/getall")
    public ResponseEntity<Map<String, Object>> getall(@RequestParam int assetId) {
        System.out.println("evidence chain: getall");
        return evidenceChainService.getall(assetId);
    }

    @GetMapping("/get_pdf_url")
    public ResponseEntity<Map<String, Object>> getPdfUrl(@RequestParam int id) {
        System.out.println("evidence chain: get_pdf_url");
        return evidenceChainService.getPdfUrl(id);
    }

    @PostMapping("/add_evidence_to_auditproject")
    public ResponseEntity<Map<String, Object>> addEvidenceToAuditproject(@RequestParam int projectId,
                                                                         @RequestParam int evidenceChainId) {
        System.out.println("evidence chain: add_evidence_to_auditproject");
        return evidenceChainService.addEvidenceToAuditproject(projectId,evidenceChainId);
    }
}
