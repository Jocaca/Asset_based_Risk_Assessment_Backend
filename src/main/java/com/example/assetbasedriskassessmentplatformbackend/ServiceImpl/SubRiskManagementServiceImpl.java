package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.RiskService;
import com.example.assetbasedriskassessmentplatformbackend.Service.SubRiskManagementService;
import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import com.example.assetbasedriskassessmentplatformbackend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubRiskManagementServiceImpl implements SubRiskManagementService {

    @Autowired
    private RiskRelationshipRepository riskRelationshipRepo;

    @Autowired
    private RiskTreatmentRepository riskTreatmentRepo;

    @Autowired
    private AssetBasicInfoRepository assetRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RiskTypeRepository riskTypeRepo;

    @Override
    public ResponseEntity<Map<String, Object>> getRiskRelationshipsCount(Integer assetId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = riskRelationshipRepo.countByAsset(assetId);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to count risk relationships: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getRiskTypesWithContent(Integer assetId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Integer> riskTypeIds = riskRelationshipRepo.findRiskTypeIdsByAsset(assetId);
            List<RiskType> riskTypes = riskTypeRepo.findByTypeIdIn(riskTypeIds);

            List<Map<String, Object>> riskTypeData = riskTypes.stream()
                    .map(riskType -> {
                        Map<String, Object> typeMap = new HashMap<>();
                        typeMap.put("typeId", riskType.getTypeID());
                        typeMap.put("content", riskType.getContent());
                        return typeMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", riskTypeData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get risk types with content: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> saveRiskData(
            Integer assetId, Integer typeID, Boolean applicable,
            String riskOwnerUsername, String comments, String dueDateStr,
            Boolean isDone, Integer currentUserId) {

        Map<String, Object> response = new HashMap<>();

        //check
        System.out.println("--- 开始执行存储 ---  assetId:"
                + assetId + ", typeID:" + typeID + ", applicable:" + applicable + ", riskOwnerUsername:" + riskOwnerUsername + ", comments:"
                + comments + ", dueDateStr:" + dueDateStr + ", isDone:" + isDone + ", currentUserId:" + currentUserId );

        checkAndUpdateAssetStatus(assetId);

        try {
            System.out.println("--------------------------------------------------------");
            // 1. 验证当前用户是否是asset owner
            AssetsBasicInfo asset = assetRepo.findByAssetId(assetId)
                    .orElseThrow(() -> new RuntimeException("Asset not found"));
            //check
            System.out.println("assetID: "+ assetId);


            User currentUser = userRepo.findById(currentUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("当前的user: " + currentUser);

            if (!currentUser.getAssetUserId().equals(asset.getAssetOwner().getAssetUserId())) {
                response.put("success", false);
                response.put("message", "You are not the owner of this asset");
                System.out.println("注意！ 当前userTd与AssetOwnerId不匹配，无法进行存储 ");
                return ResponseEntity.status(403).body(response);
            }

            // 2. 获取risk owner
            User riskOwner = userRepo.findByAssetUserName(riskOwnerUsername)
                    .orElseThrow(() -> new RuntimeException("Risk owner not found"));
            System.out.println("该风险的Owner（riskOwner）: " + riskOwner);

            // 3. 获取risk type
            RiskType riskType = riskTypeRepo.findById(typeID)
                    .orElseThrow(() -> new RuntimeException("Risk type not found"));
            System.out.println(riskType);

            // 4. 处理保存逻辑
            if (isDone) {
                System.out.println("------  开始进行Done逻辑  --------");
                // 确定保存
                return handleDoneAction(assetId, typeID, applicable, riskOwner, comments, dueDateStr, asset, riskType);
            } else {
                System.out.println("------  开始进行save逻辑  --------");
                // 暂存
                return handleSaveAction(assetId, typeID, applicable, riskOwner, comments, dueDateStr, asset, riskType);
            }


        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private ResponseEntity<Map<String, Object>> handleDoneAction(
            Integer assetId, Integer typeID, Boolean applicable,
            User riskOwner, String comments, String dueDateStr,
            AssetsBasicInfo asset, RiskType riskType) {

        Map<String, Object> response = new HashMap<>();

        // 查找所有有效记录
        List<RiskRelationship> existingRecords = riskRelationshipRepo.findByAssetIdAndRiskType(assetId, typeID);

        // 检查是否有valid=2且treatmentStatus=0的记录
        // 则无法进行记录修改，返回给前端，当前任务正在进行中，暂时无法做出修改
        boolean hasInProgress = existingRecords.stream()
                .anyMatch(r -> r.getValid() == 2 && "0".equals(r.getTreatmentStatus()));

        if (hasInProgress) {
            System.out.println("检查得到valid=2且treatmentStatus=0的记录 --- 意味着有效task正在执行中，当前对记录无法做出更改");
            response.put("success", false);
            response.put("message", "Task is in progress, cannot modify");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查是否有valid=2且treatmentStatus=1的记录
        // 当前的所有 risk treatment task 已经完成  可以新增
        // 1. 筛选出所有需要更新的记录
        List<RiskRelationship> recordsToUpdate = existingRecords.stream()
                .filter(r -> r.getValid() == 2 && "1".equals(r.getTreatmentStatus()))
                .collect(Collectors.toList());

        System.out.println("找到 " + recordsToUpdate.size() + " 条需更新的记录");

        if (!recordsToUpdate.isEmpty()) {
            recordsToUpdate.forEach(record -> {
                record.setValid(0);
                riskRelationshipRepo.save(record); // 单条保存（适合少量数据）
            });
            System.out.println("已更新 " + recordsToUpdate.size() + " 条记录的 valid 为 0");
        }


        //valid = 1
        Optional<RiskRelationship> draftRecord = existingRecords.stream()
                .filter(r -> r.getValid() == 1)
                .findFirst();

        //如果目前已经有暂存记录，则直接覆盖； 如果没有暂存记录，就新建一条
        if (draftRecord.isPresent()) {
            System.out.println("检查得到valid=1 --- 意味着当前有暂存，直接覆盖暂存记录");
            // 更新现有暂时记录
            RiskRelationship record = draftRecord.get();
            updateRecord(record, applicable, riskOwner, comments, dueDateStr, true);
            riskRelationshipRepo.save(record);
        } else {
            //valid = 0 && valid =2 且 treatment——status = 1
            System.out.println("检查后发现没有valid=1 --- 意味着可以新建记录");
            // 创建新暂时记录
            RiskRelationship newRecord = createNewRecord(asset, riskType, applicable, riskOwner, comments, dueDateStr, true);
            riskRelationshipRepo.save(newRecord);
        }


        //check Treatment_Status
        checkAndUpdateAssetStatus(assetId);

        response.put("success", true);
        response.put("message", "Risk data saved successfully");
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> handleSaveAction(
            Integer assetId, Integer typeID, Boolean applicable,
            User riskOwner, String comments, String dueDateStr,
            AssetsBasicInfo asset, RiskType riskType) {

        Map<String, Object> response = new HashMap<>();

        System.out.println("--- 开始执行存储 ---  assetId:"
                + assetId + ", typeID:" + typeID + ", applicable:" + applicable + ", User" + riskOwner + ", comments:"
                + comments + ", dueDateStr:" + dueDateStr + ", AssetBasicInfo:" + asset + ", RiskType:" + riskType );

        // 查找所有有效记录
        try {
            List<RiskRelationship> existingRecords = riskRelationshipRepo.findByAssetIdAndRiskType(assetId, typeID);
            // ... 其他代码
            System.out.println("------ handleSaveAction： 列出所有有效记录 --------");
            System.out.println("assetId: " + assetId + " typeID: " + typeID);
            System.out.println("记录数: " + (existingRecords != null ? existingRecords.size() : "null"));
            System.out.println("------------");
            //-----

            // 检查是否有valid=2且treatmentStatus=0的记录
            // 目前生效的task进行中， 不可以修改
            boolean hasInProgress = existingRecords.stream()
                    .anyMatch(r -> (r.getValid() == 2 || r.getValid() == null)  && ("0".equals(r.getTreatmentStatus()) || r.getTreatmentStatus() == null));

            if (hasInProgress) {
                System.out.println("检查得到valid=2且treatmentStatus=0的记录--- 意味着有效task正在执行中，当前对记录无法做出更改");
                response.put("success", false);
                response.put("message", "Task is in progress, cannot save draft");
                return ResponseEntity.badRequest().body(response);
            }
            System.out.println("没有检查得到valid=2且treatmentStatus=0的记录");


            // 检查是否有valid=2且treatmentStatus=1的记录
            // 当前的所有 risk treatment task 已经完成  可以新增
            List<RiskRelationship> recordsToUpdate = existingRecords.stream()
                    .filter(r -> r.getValid() == 2 && "1".equals(r.getTreatmentStatus()))
                    .collect(Collectors.toList());

            System.out.println("找到 " + recordsToUpdate.size() + " 条需更新的记录");

            if (!recordsToUpdate.isEmpty()) {
                recordsToUpdate.forEach(record -> {
                    record.setValid(0);
                    riskRelationshipRepo.save(record); // 单条保存（适合少量数据）
                });
                System.out.println("已更新 " + recordsToUpdate.size() + " 条记录的 valid 为 0");
            }

            // 查找现有的暂时记录(valid=1)
            Optional<RiskRelationship> draftRecord = existingRecords.stream()
                    .filter(r -> r.getValid() == 1)
                    .findFirst();

            //如果目前已经有暂存记录，则直接覆盖； 如果没有暂存记录，就新建一条
            if (draftRecord.isPresent()) {
                System.out.println("检查得到valid=1 --- 意味着当前有暂存，直接覆盖暂存记录");
                // 更新现有暂时记录
                RiskRelationship record = draftRecord.get();
                updateRecord(record, applicable, riskOwner, comments, dueDateStr, false);
                riskRelationshipRepo.save(record);
            } else {
                System.out.println("检查后发现没有valid=1 --- 意味着可以新建记录");
                // 创建新暂时记录
                RiskRelationship newRecord = createNewRecord(asset, riskType, applicable, riskOwner, comments, dueDateStr, false);
                riskRelationshipRepo.save(newRecord);
            }
            //check Treatment_Status
            checkAndUpdateAssetStatus(assetId);

            response.put("success", true);
            response.put("message", "Draft saved successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(response);
        }

    }

    private RiskRelationship createNewRecord(AssetsBasicInfo asset, RiskType riskType, Boolean applicable,
                                             User riskOwner, String comments, String dueDateStr, boolean isFinal) {

        System.out.println("-----   开始执行 新建记录 ------");

        RiskRelationship record = new RiskRelationship();
        record.setAsset(asset);
        record.setRiskType(riskType);
        record.setApplicable(applicable);
        record.setRiskOwner(riskOwner);
        record.setComments(comments);

        System.out.println(record);

        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            try {
                // 解析 ISO 8601 格式的日期时间字符串
                Instant instant = Instant.parse(dueDateStr);
                // 转换为 java.sql.Date
                record.setDueDate(new java.sql.Date(instant.toEpochMilli()));
            } catch (Exception e) {
                System.err.println("日期解析错误: " + e.getMessage());
                record.setDueDate(null);
            }
        }


        //默认当前的treatmentstatus 为0
        record.setTreatmentStatus("0");
        //进入调用的时候 done是true ； save是false
        record.setValid(isFinal ? 2 : 1);
        //首次创建 需要写入createDate
        record.setCreateDate(new Date());


        System.out.println(record);
        return record;
    }

    private void updateRecord(RiskRelationship record, Boolean applicable,
                              User riskOwner, String comments, String dueDateStr, boolean isFinal) {

        System.out.println("-----   开始执行 覆盖记录 ------");

        //覆盖记录不会更改 CreateDate
        record.setApplicable(applicable);
        record.setRiskOwner(riskOwner);
        record.setComments(comments);
        //进入调用的时候 done是true ； save是false
        record.setValid(isFinal ? 2 : 1);

        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            try {
                // 解析 ISO 8601 格式的日期时间字符串
                Instant instant = Instant.parse(dueDateStr);
                // 转换为 java.sql.Date
                record.setDueDate(new java.sql.Date(instant.toEpochMilli()));
            } catch (Exception e) {
                System.err.println("日期解析错误: " + e.getMessage());
                record.setDueDate(null);
            }
        } else {
            record.setDueDate(null);
        }


    }

    @Override
    public ResponseEntity<Map<String, Object>> getRiskLogs(Integer assetId, Integer typeID) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("----------- getRiskLogs 开始 -------------");

        try {
            // 1. 检查并更新已完成但未标记的记录
            List<RiskRelationship> validRecords = riskRelationshipRepo.findByAssetIdAndRiskTypeAndValidIn(
                    assetId, typeID, List.of(2));

            for (RiskRelationship rr : validRecords) {
                if (rr.getValid() == 2 && "0".equals(rr.getTreatmentStatus())) {
                    riskTreatmentRepo.findByRiskRelationshipAndValid(rr, 1)
                            .ifPresent(treatment -> {
                                rr.setTreatmentStatus("1");
                                rr.setCreateDate(new Date());
                                riskRelationshipRepo.save(rr);
                            });
                }
            }

            // 2. 获取所有历史记录
            List<RiskRelationship> history = riskRelationshipRepo.findByAssetIdAndRiskTypeAndValidIn(
                    assetId, typeID, List.of(0, 2));

            // 3. 转换为前端格式
            List<Map<String, Object>> logData = history.stream().map(rr -> {
                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("dateTime", rr.getCreateDate());
                logEntry.put("action", "1".equals(rr.getTreatmentStatus()) ? "Treated" : "Assigned");
                logEntry.put("by", rr.getRiskOwner().getAssetUserName());
                logEntry.put("rid", rr.getRID());
                return logEntry;
            }).collect(Collectors.toList());

            response.put("success", true);
            response.put("data", logData);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get logs: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getTreatmentDetails(Integer rid) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("----------- getTreatmentDetails 开始 -------------");

        try {
            RiskRelationship relationship = riskRelationshipRepo.findById(rid)
                    .orElseThrow(() -> new RuntimeException("Risk relationship not found"));

            Optional<RiskTreatment> treatment = riskTreatmentRepo
                    .findByRiskRelationshipAndValid(relationship, 1);

            if (!treatment.isPresent()) {
                response.put("success", false);
                response.put("message", "No treatment details found");
                return ResponseEntity.badRequest().body(response);
            }

            RiskTreatment t = treatment.get();
            Map<String, Object> details = new HashMap<>();
            details.put("treatedTime", t.getUpdateDate());
            details.put("treatedBy", relationship.getRiskOwner().getAssetUserName());

            // Risk level mapping
            String riskLevel = switch (t.getRiskLevel()) {
                case 0 -> "Very Low";
                case 1 -> "Low";
                case 2 -> "Medium";
                case 3 -> "High";
                case 4 -> "Very High";
                default -> "Unknown";
            };
            System.out.println("当前的 risklevel" + riskLevel);
            details.put("riskLevel", riskLevel);

            // Treatment option mapping
            String treatmentOption = switch (t.getTreatmentOption()) {
                case 0 -> "Risk Avoidance";
                case 1 -> "Risk Modification";
                case 2 -> "Risk Retention";
                case 3 -> "Risk Sharing";
                default -> "Unknown";
            };
            System.out.println("当前的 treatment Option" + treatmentOption);
            details.put("treatmentOption", treatmentOption);

            // Get evidence files
            List<String> evidenceFiles = t.getEvidenceFiles().stream()
                    .map(Files::getStoredName)
                    .collect(Collectors.toList());
            details.put("evidence", evidenceFiles);

            details.put("comments", t.getComments());

            response.put("success", true);
            response.put("data", details);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get treatment details: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    //check treatment_status 如果都是1 的话 ， 修改assetBasicInfo表里的 rtStaturs =1
    private void checkAndUpdateAssetStatus(Integer assetId) {
        // 1. 查询当前 Asset 下所有有效的 risk_relationship 记录
        List<RiskRelationship> relationships = riskRelationshipRepo.findByAssetIdAndValid(assetId, 2);

        // 2. 检查是否所有记录的 treatment_status 均为 "1"
        boolean allFinished = relationships.stream()
                .allMatch(r -> "1".equals(r.getTreatmentStatus()));

        // 3. 如果全部完成，则更新 Asset 状态
        if (allFinished) {
            AssetsBasicInfo asset = assetRepo.findByAssetId(assetId)
                    .orElseThrow(() -> new RuntimeException("Asset not found"));
            asset.setRtStatus(1); // 1 表示 FINISH
            assetRepo.save(asset);
        }else{
            AssetsBasicInfo asset = assetRepo.findByAssetId(assetId)
                    .orElseThrow(() -> new RuntimeException("Asset not found"));
            asset.setRtStatus(0); // 1 表示 FINISH
            assetRepo.save(asset);
        }
    }

}