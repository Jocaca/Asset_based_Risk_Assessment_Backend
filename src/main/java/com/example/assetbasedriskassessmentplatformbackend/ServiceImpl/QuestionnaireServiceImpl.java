package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.QuestionnaireService;
import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import com.example.assetbasedriskassessmentplatformbackend.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.type.TypeHelper.deepCopy;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    @Autowired
    private AssetSoftwareRepository assetSoftwareRepository;

    @Autowired
    private AssetPeopleRepository assetPeopleRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private RiskRelationshipRepository riskRelationshipRepository;

    @Autowired
    private RiskTypeRepository riskTypeRepository;

    public ResponseEntity<Map<String, Object>> loadSoftware(int id){
        Map<String, Object> response = new HashMap<>();
        AssetsSoftware asset = assetSoftwareRepository.findById((long)id).get();
        if(asset.getQuestionnaire() == null){
            response.put("isload", false);
            response.put("success", true);
        }else{
            Questionnaire questionnaire = asset.getQuestionnaire();
            Map<String,Object> map = new HashMap<>();
            map.put("Q1Status",questionnaire.getQ1());
            map.put("Q1_1Status",questionnaire.getQ2());
            map.put("Q2Status",questionnaire.getQ3());
            map.put("Q3Status",questionnaire.getQ4());
            map.put("Q4Status",questionnaire.getQ5());
            map.put("Q5Status",questionnaire.getQ6());
            map.put("Q6Status",questionnaire.getQ7());
            map.put("Q7Status",questionnaire.getQ8());
            map.put("Q8Status",questionnaire.getQ9());
            map.put("Q9Status",questionnaire.getQ10());
            map.put("Q10Status",questionnaire.getQ11());
            map.put("Q11Status",questionnaire.getQ12());
            map.put("Q12Status",questionnaire.getQ13());
            map.put("Q12_1Status",questionnaire.getQ14());
            map.put("Q12_2Status",questionnaire.getQ15());
            map.put("Q13Status",questionnaire.getQ16());
            map.put("Q13_1Status",questionnaire.getQ17());
            map.put("Q13_2Status",questionnaire.getQ18());
            map.put("Q13_3Status",questionnaire.getQ19());
            map.put("Q13_4Status",questionnaire.getQ20());
            map.put("Q13_5Status",questionnaire.getQ21());
            map.put("Q13_6Status",questionnaire.getQ22());
            map.put("Q13_7Status",questionnaire.getQ23());
            map.put("Q14Status",questionnaire.getQ24());
            map.put("Q15Status",questionnaire.getQ25());
            response.put("isload", true);
            response.put("success", true);
            response.put("status",map);
        }
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<Map<String, Object>> loadPeople(int id) {
        Map<String, Object> response = new HashMap<>();
        // Assuming you have a People repository similar to AssetsSoftware
        AssetsPeople person = assetPeopleRepository.findById((long)id).get();
        if(person.getQuestionnaire() == null) {
            response.put("isload", false);
            response.put("success", true);
        } else {
            Questionnaire questionnaire = person.getQuestionnaire();
            Map<String,Object> map = new HashMap<>();
            map.put("Q1Status",questionnaire.getQ1());
            map.put("Q2Status",questionnaire.getQ2());
            map.put("Q3Status",questionnaire.getQ3());
            map.put("Q4Status",questionnaire.getQ4());
            map.put("Q5Status",questionnaire.getQ5());
            map.put("Q6Status",questionnaire.getQ6());
            map.put("Q7Status",questionnaire.getQ7());
            map.put("Q8Status",questionnaire.getQ8());
            map.put("Q9Status",questionnaire.getQ9());
            map.put("Q10Status",questionnaire.getQ10());
            map.put("Q11Status",questionnaire.getQ11());
            map.put("Q12Status",questionnaire.getQ12());
            map.put("Q13Status",questionnaire.getQ13());
            map.put("Q14Status",questionnaire.getQ14());
            map.put("Q15Status",questionnaire.getQ15());
            map.put("Q16Status",questionnaire.getQ16());
            map.put("Q17Status",questionnaire.getQ17());
            map.put("Q18Status",questionnaire.getQ18());
            map.put("Q19Status",questionnaire.getQ19());
            map.put("Q20Status",questionnaire.getQ20());
            response.put("isload", true);
            response.put("success", true);
            response.put("status",map);
        }
        return ResponseEntity.ok(response);
    }
    // 统一处理函数
    private int handleQuestion(String answer, String positiveAnswer,
                                List<Integer> riskTypes,Integer id,AssetsBasicInfo asset) {
        int count = 0;
        // 获取valid=2和valid=1的记录
        List<RiskRelationship> valid2Records = riskRelationshipRepository.findByAssetIdAndRiskTypeInAndValid(id, riskTypes, 2);
        List<RiskRelationship> valid1Records = riskRelationshipRepository.findByAssetIdAndRiskTypeInAndValid(id, riskTypes, 1);

        // 情况1: 有记录且答案为肯定或空
        if ((!valid2Records.isEmpty() || !valid1Records.isEmpty()) &&
                (answer == "" || answer.equals(positiveAnswer))) {
            // 将valid=2的记录设为valid=0
            for (RiskRelationship rr : valid2Records) {
                rr.setValid(0);
                riskRelationshipRepository.save(rr);
            }
            // 删除valid=1的暂存记录
            riskRelationshipRepository.deleteAll(valid1Records);
        }

        // 情况2: 无记录且答案为否定
        if ((valid2Records.isEmpty() && valid1Records.isEmpty()) &&
                (answer != "" && !answer.equals(positiveAnswer))) {
            // 创建新的valid=2的记录
            for (Integer type : riskTypes) {
                count++;
                RiskRelationship rr = new RiskRelationship();
                rr.setValid(2);
                rr.setRiskType(riskTypeRepository.getReferenceById(type));
                rr.setAsset(asset);
                riskRelationshipRepository.save(rr);
            }
        }
        return  count;
    }

    public  ResponseEntity<Map<String, Object>> submitSoftware(Integer id, Integer status, Map<String,Object> answer){
        Map<String, Object> response = new HashMap<>();

        AssetsSoftware asset = assetSoftwareRepository.findById((long)id).get();
        System.out.println("--------------------"+asset.getQuestionnaire()==null+"--------------------------");
        Questionnaire questionnaire;
        if(asset.getQuestionnaire() == null){
            questionnaire = new Questionnaire();
        }else{
            questionnaire = asset.getQuestionnaire();
        }
        System.out.println("--------------------"+questionnaire+"--------------------------");

        questionnaire.setQ1(answer.get("Q1Status").toString());
        questionnaire.setQ2(answer.get("Q1_1Status").toString());
        questionnaire.setQ3(answer.get("Q2Status").toString());
        questionnaire.setQ4(answer.get("Q3Status").toString());
        questionnaire.setQ5(answer.get("Q4Status").toString());
        questionnaire.setQ6(answer.get("Q5Status").toString());
        questionnaire.setQ7(answer.get("Q6Status").toString());
        questionnaire.setQ8(answer.get("Q7Status").toString());
        questionnaire.setQ9(answer.get("Q8Status").toString());
        questionnaire.setQ10(answer.get("Q9Status").toString());
        questionnaire.setQ11(answer.get("Q10Status").toString());
        questionnaire.setQ12(answer.get("Q11Status").toString());
        questionnaire.setQ13(answer.get("Q12Status").toString());
        questionnaire.setQ14(answer.get("Q12_1Status").toString());
        questionnaire.setQ15(answer.get("Q12_2Status").toString());
        questionnaire.setQ16(answer.get("Q13Status").toString());
        questionnaire.setQ17(answer.get("Q13_1Status").toString());
        questionnaire.setQ18(answer.get("Q13_2Status").toString());
        questionnaire.setQ19(answer.get("Q13_3Status").toString());
        questionnaire.setQ20(answer.get("Q13_4Status").toString());
        questionnaire.setQ21(answer.get("Q13_5Status").toString());
        questionnaire.setQ22(answer.get("Q13_6Status").toString());
        questionnaire.setQ23(answer.get("Q13_7Status").toString());
        questionnaire.setQ24(answer.get("Q14Status").toString());
        questionnaire.setQ25(answer.get("Q15Status").toString());
        questionnaire.setAsset(asset);
        asset.setQStatus(status);
        asset.setQuestionnaire(questionnaire);
        System.out.println("--------------------"+questionnaire+"--------------------------");
        questionnaireRepository.save(questionnaire);
        assetSoftwareRepository.save(asset);
        if(status == 1){
            int count =0;
//            System.out.println(tempQuestionnaire.getQ2());
//            Q1_1
            // Q1_1
            count+=handleQuestion(questionnaire.getQ2(), "Yes", List.of(1, 2, 3), id,asset);
// Q2
            count+=handleQuestion(questionnaire.getQ3(), "Yes, whitelisted with version control", List.of(4,5,6), id, asset);

// Q3
            count+=handleQuestion(questionnaire.getQ4(), "Yes, scanned weekly or more frequently", List.of(7,8), id, asset);

// Q4
            count+=handleQuestion(questionnaire.getQ5(), "Yes", List.of(9,10,11), id, asset);

// Q5
            count+=handleQuestion(questionnaire.getQ6(), "Yes, fully automated", List.of(12,13), id, asset);

// Q6
            count+=handleQuestion(questionnaire.getQ7(), "Yes, with documented tracking (e.g., Jira tickets, automated dashboards)",
                    List.of(14,15), id, asset);

// Q7
            count+=handleQuestion(questionnaire.getQ8(), "Yes, fully documented and enforced", List.of(16,17,18,19), id, asset);

// Q8
            count+=handleQuestion(questionnaire.getQ9(), "Yes, always", List.of(20,21,22,23), id, asset);

// Q9
            count+=handleQuestion(questionnaire.getQ10(), "Yes, fully secured & monitored", List.of(24), id, asset);

// Q10
            count+=handleQuestion(questionnaire.getQ11(), "Yes, fully automated", List.of(25,26,27), id, asset);

// Q11
            count+=handleQuestion(questionnaire.getQ12(), "Yes, fully implemented (automated scans + real-time alerts)",
                    List.of(28,29,30,31), id, asset);

// Q12_1
            count+=handleQuestion(questionnaire.getQ14(), "Yes, actively managed (automated blocking + allowlists)",
                    List.of(32), id, asset);

// Q12_2
            count+=handleQuestion(questionnaire.getQ15(), "Yes, strictly enforced", List.of(33,34), id, asset);

// Q13_1
            count+=handleQuestion(questionnaire.getQ17(), "Yes, strictly enforced (via code reviews, SAST/DAST tools, and training)",
                    List.of(35,36,37), id, asset);

// Q13_2
            count+=handleQuestion(questionnaire.getQ18(), "Yes, rigorously enforced", List.of(38,39,40,41), id, asset);

// Q13_3
            count+=handleQuestion(questionnaire.getQ19(), "Yes, strictly enforced (automated scanning + approved vendor lists)",
                    List.of(42,43), id, asset);

// Q13_4
            count+=handleQuestion(questionnaire.getQ20(), "Yes, rigorously enforced", List.of(44,45,46,47), id, asset);

// Q13_5
            count+=handleQuestion(questionnaire.getQ21(), "Yes, mandatory & role-specific", List.of(48,49), id, asset);

// Q13_6
            count+=handleQuestion(questionnaire.getQ22(), "Yes, integrated into CI/CD (automated blocking of insecure code)",
                    List.of(50,51,52,53), id, asset);

// Q13_7
            count+=handleQuestion(questionnaire.getQ23(), "Yes, fully enforced (separate networks, RBAC, and audit logs)",
                    List.of(54,55,56,57), id, asset);

// Q14
            count+=handleQuestion(questionnaire.getQ24(), "Yes, rigorously tracked and updated (automated monitoring + patching)",
                    List.of(58,59,60), id, asset);

// Q15
            count+=handleQuestion(questionnaire.getQ25(), "Yes", List.of(61,62,63,64), id, asset);
            response.put("risk",count);
        }
        response.put("success",true);
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<Map<String, Object>> submitPeople(Integer id, Integer status, Map<String,Object> answer) {
        Map<String, Object> response = new HashMap<>();

        AssetsPeople asset = assetPeopleRepository.findById((long)id).get();
        Questionnaire questionnaire;
        if(asset.getQuestionnaire() == null) {
            questionnaire = new Questionnaire();
        } else {
            questionnaire = asset.getQuestionnaire();
        }

        questionnaire.setQ1(answer.get("Q1Status").toString());
        questionnaire.setQ2(answer.get("Q2Status").toString());
        questionnaire.setQ3(answer.get("Q3Status").toString());
        questionnaire.setQ4(answer.get("Q4Status").toString());
        questionnaire.setQ5(answer.get("Q5Status").toString());
        questionnaire.setQ6(answer.get("Q6Status").toString());
        questionnaire.setQ7(answer.get("Q7Status").toString());
        questionnaire.setQ8(answer.get("Q8Status").toString());
        questionnaire.setQ9(answer.get("Q9Status").toString());
        questionnaire.setQ10(answer.get("Q10Status").toString());
        questionnaire.setQ11(answer.get("Q11Status").toString());
        questionnaire.setQ12(answer.get("Q12Status").toString());
        questionnaire.setQ13(answer.get("Q13Status").toString());
        questionnaire.setQ14(answer.get("Q14Status").toString());
        questionnaire.setQ15(answer.get("Q15Status").toString());
        questionnaire.setQ16(answer.get("Q16Status").toString());
        questionnaire.setQ17(answer.get("Q17Status").toString());
        questionnaire.setQ18(answer.get("Q18Status").toString());
        questionnaire.setQ19(answer.get("Q19Status").toString());
        questionnaire.setQ20(answer.get("Q20Status").toString());
        questionnaire.setAsset(asset);
        asset.setQStatus(status);
        asset.setQuestionnaire(questionnaire);

        questionnaireRepository.save(questionnaire);
        assetPeopleRepository.save(asset);

        if(status == 1) {
            int count = 0;
            // Define risk type mappings for each question starting from 124
            // Q1 - Employee name documentation
            count += handleQuestion(questionnaire.getQ1(), "Yes", List.of(124), id, asset);
            // Q2 - Employee ID documentation
            count += handleQuestion(questionnaire.getQ2(), "Yes", List.of(125), id, asset);
            // Q3 - Department recording
            count += handleQuestion(questionnaire.getQ3(), "Yes", List.of(126), id, asset);
            // Q4 - Role definition
            count += handleQuestion(questionnaire.getQ4(), "Yes", List.of(127), id, asset);
            // Q5 - Access permissions linkage
            count += handleQuestion(questionnaire.getQ5(), "Yes", List.of(128), id, asset);
            // Q6 - Security training
            count += handleQuestion(questionnaire.getQ6(), "Yes", List.of(129), id, asset);
            // Q7 - Security clearance
            count += handleQuestion(questionnaire.getQ7(), "Yes", List.of(130), id, asset);
            // Q8 - Asset list
            count += handleQuestion(questionnaire.getQ8(), "Yes", List.of(131), id, asset);
            // Q9 - Asset purpose documentation
            count += handleQuestion(questionnaire.getQ9(), "Yes", List.of(132), id, asset);
            // Q10 - Asset responsibility
            count += handleQuestion(questionnaire.getQ10(), "Yes", List.of(133), id, asset);
            // Q11 - Remote work agreement
            count += handleQuestion(questionnaire.getQ11(), "Yes", List.of(134), id, asset);
            // Q12 - Access audit
            count += handleQuestion(questionnaire.getQ12(), "Yes", List.of(135), id, asset);
            // Q13 - Incident record
            count += handleQuestion(questionnaire.getQ13(), "Yes", List.of(136), id, asset);
            // Q14 - Incident review
            count += handleQuestion(questionnaire.getQ14(), "Yes", List.of(137), id, asset);
            // Q15 - Access review
            count += handleQuestion(questionnaire.getQ15(), "Yes", List.of(138), id, asset);
            // Q16 - Access approver
            count += handleQuestion(questionnaire.getQ16(), "Yes", List.of(139), id, asset);
            // Q17 - Security drills
            count += handleQuestion(questionnaire.getQ17(), "Yes", List.of(140), id, asset);
            // Q18 - Social engineering awareness
            count += handleQuestion(questionnaire.getQ18(), "Yes", List.of(141), id, asset);
            // Q19 - Role change updates
            count += handleQuestion(questionnaire.getQ19(), "Yes", List.of(142), id, asset);
            // Q20 - Workplace recording
            count += handleQuestion(questionnaire.getQ20(), "Yes", List.of(143), id, asset);

            response.put("risk", count);
        }
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
