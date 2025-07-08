package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.EvidenceChainService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AuditProject;
import com.example.assetbasedriskassessmentplatformbackend.entity.EvidenceChain;
import com.example.assetbasedriskassessmentplatformbackend.repository.AuditProjectRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.EvidenceChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EvidenceChainServiceImpl implements EvidenceChainService {
    @Autowired
    EvidenceChainRepository evidenceChainRepository;

    @Autowired
    AuditProjectRepository projectRepository;

    public ResponseEntity<Map<String, Object>> getall(int assetId){
        Map<String, Object> response = new HashMap<>();
        List<EvidenceChain> evidenceChains = evidenceChainRepository.findByAsset_AssetId(assetId);
        evidenceChains.sort(Comparator.comparing(evidenceChain -> evidenceChain.getGenerateDate()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String,Object>> evidenceChainList = new ArrayList<>();
        for(EvidenceChain evidenceChain : evidenceChains){
            Map<String,Object> evidenceChainMap = new HashMap<>();
            evidenceChainMap.put("id",evidenceChain.getId());
            evidenceChainMap.put("generateDate",dateFormat.format(evidenceChain.getGenerateDate()));
            evidenceChainMap.put("name",evidenceChain.getName());
            evidenceChainMap.put("generateby",evidenceChain.getGenerateBy());
            evidenceChainMap.put("isAdded",evidenceChain.getAuditProject()!=null);
//            evidenceChainMap.put("url",evidenceChain.getSnapshot());
            evidenceChainList.add(evidenceChainMap);
        }
        response.put("data",evidenceChainList);
        response.put("success",true);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getPdfUrl(int id){
        Map<String, Object> response = new HashMap<>();
//        System.out.println(evidenceChainRepository.findById(id));
        EvidenceChain evidenceChain = evidenceChainRepository.findById(id).get();
        response.put("url",evidenceChain.getSnapshot());
        response.put("success",true);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> addEvidenceToAuditproject(int projectId, int evidenceChainId){
        Map<String, Object> response = new HashMap<>();
        EvidenceChain evidenceChain = evidenceChainRepository.findById(evidenceChainId).get();
        if(evidenceChain.getAuditProject()!=null){
            response.put("success",false);
            response.put("message","Evidence chain had been added to audit project");
            return ResponseEntity.ok(response);
        }else {
            AuditProject auditProject = projectRepository.findById(projectId).get();

            evidenceChain.setAuditProject(auditProject);
            evidenceChainRepository.save(evidenceChain);
            response.put("success", true);
            return ResponseEntity.ok(response);
        }
    }
}
