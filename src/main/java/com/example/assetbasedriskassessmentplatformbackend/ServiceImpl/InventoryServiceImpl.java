package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.InventoryService;
import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import com.example.assetbasedriskassessmentplatformbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    AssetBasicInfoRepository assetBasicInfoRepository;
    @Autowired
    AssetSoftwareRepository assetSoftwareRepository;
    @Autowired
    AssetPhysicalRepository assetPhysicalRepository;
    @Autowired
    AssetInformationRepository assetInformationRepository;
    @Autowired
    AssetPeopleRepository assetPeopleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionnaireRepository questionnaireRepository;
    @Autowired
    RiskRelationshipRepository riskRelationshipRepository;

    public ResponseEntity<Map<String, Object>> getAssetInfo (int id, String type){
        Object asset;
        System.out.println(id);
        System.out.println(type);
        switch (type) {
            case "Software":
                asset = assetSoftwareRepository.findById((long)id).orElse(null);
                break;
            case "Physical":
                asset = assetPhysicalRepository.findById((long)id).orElse(null);
                break;
            case "Information":
                asset = assetInformationRepository.findById((long)id).orElse(null);
                break;
            case "People":
                asset = assetPeopleRepository.findById((long)id).orElse(null);
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid asset type"
                ));
        }

        if (asset == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", asset
        ));
    }
    public ResponseEntity<Map<String,Object>> SaveInevntory(Map<String, Object> requestData) {
        try {
            Integer assetId = null;
            if (requestData.containsKey("id") && requestData.get("id") != null) {
                System.out.println(1);
                assetId = Integer.valueOf(requestData.get("id").toString());
            }
            System.out.println("assetId"+assetId);
            Integer newAssetType = (Integer) requestData.get("AssetType");
            Boolean newAsset = true;

            // 如果是有ID的修改操作，检查类型是否变化
            if (assetId != null && assetId > 0) {
                // 先检查资产是否存在以及当前类型
                Integer currentType = getCurrentAssetType(assetId);

                // 如果类型发生了变化
                if (currentType != null && !currentType.equals(newAssetType)) {
                    // 从原类型表中删除旧记录
                    deleteFromOldTable(assetId, currentType);
                    newAsset = false;
                }
            }

            switch (newAssetType) {
                case 0:
                    return saveSoftwareAsset(requestData,newAsset);
                case 1:
                    return savePhysicalAsset(requestData,newAsset);
                case 2:
                    return saveInformationAsset(requestData,newAsset);
                case 3:
                    return savePeopleAsset(requestData,newAsset);
                default:
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid asset type"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    private void setBasicFields(AssetsBasicInfo asset, Map<String, Object> data) {
        if (data.get("assetName") != "") asset.setAssetName((String) data.get("assetName"));
        if (data.get("AssetType") != "") asset.setAssetType((Integer) data.get("AssetType"));
        if (data.get("contact") != "") asset.setContact((String) data.get("contact"));
        if (data.get("description") != "") asset.setDescription((String) data.get("description"));
        if (data.get("swid") != "") asset.setSwid((String) data.get("swid"));
        if (data.get("status") != "") asset.setStatus((Integer) data.get("status"));
        if (data.get("importance") != "") asset.setImportance((Integer) data.get("importance"));
        if (data.get("associatedAssets") != "") asset.setAssociatedAssets((String) data.get("associatedAssets"));
        if (data.get("EmptyField") != "") asset.setEmptyFields((Integer) data.get("EmptyField"));

        asset.setUpdatedAt(new Date());
        asset.setAssetOwner(userRepository.findById((Integer) data.get("assetOwner")).get());
    }
    private Date parseDate(Object dateObj) {
        if (dateObj == null) return null;
        if (dateObj instanceof Date) return (Date) dateObj;
        if (dateObj instanceof String) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse((String) dateObj);
            } catch (ParseException e) {
                throw new RuntimeException("Invalid date format: " + dateObj);
            }
        }
        throw new RuntimeException("Unsupported date type: " + dateObj.getClass());
    }

    private ResponseEntity<Map<String, Object>> buildSuccessResponse(Integer assetId, boolean isUpdate) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "id", assetId,
                "message", isUpdate ? "Asset updated" : "Asset created"
        ));
    }
    private Integer getCurrentAssetType(Integer assetId) {
        System.out.println("getCurrentAssetType");
        // 检查资产存在于哪个表中并返回其类型
        if (assetSoftwareRepository.existsById(assetId.longValue())) return 0;
        if (assetPhysicalRepository.existsById(assetId.longValue())) return 1;
        if (assetInformationRepository.existsById(assetId.longValue())) return 2;
        if (assetPeopleRepository.existsById(assetId.longValue())) return 3;
        return null;
    }

    private void deleteFromOldTable(Integer assetId, Integer oldType) {
//        清空questionnaire
        AssetsBasicInfo assetsBasicInfo = assetBasicInfoRepository.findByAssetId(assetId).get();
//        把所有risk valid设置为0
        List<RiskRelationship> riskRelationships = riskRelationshipRepository.findByAssetIdAndValid(assetId,2);
        for (RiskRelationship riskRelationship : riskRelationships) {
            riskRelationship.setValid(0);
            riskRelationshipRepository.save(riskRelationship);
        }
//        删除所有暂存记录
        riskRelationshipRepository.deleteAll(riskRelationshipRepository.findByAssetIdAndValid(assetId,1));

        questionnaireRepository.delete(assetsBasicInfo.getQuestionnaire());
        switch (oldType) {
            case 0: assetSoftwareRepository.deleteById(assetId.longValue()); break;
            case 1: assetPhysicalRepository.deleteById(assetId.longValue()); break;
            case 2: assetInformationRepository.deleteById(assetId.longValue()); break;
            case 3: assetPeopleRepository.deleteById(assetId.longValue()); break;
        }
    }
    private ResponseEntity<Map<String, Object>> saveSoftwareAsset(Map<String, Object> data,Boolean newAsset) {
        System.out.println("save software");
        AssetsSoftware asset;
        Integer assetId = null;
        if (data.containsKey("id") && data.get("id") != null) {
            System.out.println(1);
            assetId = Integer.valueOf(data.get("id").toString());
        }
        if (assetId != null && assetId > 0 && newAsset) {
            asset = assetSoftwareRepository.findById((long) assetId)
                    .orElseThrow(() -> new RuntimeException("Software asset not found"));
            if (asset.getAssetType() != 0) asset.setQStatus(0);
        } else {
            asset = new AssetsSoftware();
            asset.setCreatedAt(new Date());
            asset.setQStatus(0);
            asset.setRtStatus(0);
        }
        System.out.println(asset);
        setBasicFields(asset, data);
        System.out.println(asset);

        // 设置Software专属字段（带空值检查）
        if (data.get("version") != "") asset.setVersion((String) data.get("version"));
        System.out.println("VERSION "+asset.getVersion());
        if (data.get("installDate") != "") asset.setInstallDate(parseDate(data.get("installDate")));
        System.out.println("installDate "+asset.getInstallDate());
        if (data.get("operatingSystems") != "") asset.setOperatingSystems((String) data.get("operatingSystems"));
        System.out.println("OS "+asset.getOperatingSystems());
        if (data.get("externalSuppliedService") != "") asset.setExternalSuppliedService((Integer) data.get("externalSuppliedService"));
        System.out.println("ESS "+asset.getExternalSuppliedService());
        if (data.get("manufacture") != "") asset.setManufacture((String) data.get("manufacture"));
        System.out.println("manufacture "+asset.getManufacture());
        if (data.get("serviceType") != "") asset.setServiceType((Integer) data.get("serviceType"));
        System.out.println("serviceType "+asset.getServiceType());
        if (data.get("licenseType") != "") asset.setLicenseType((Integer) data.get("licenseType"));
        System.out.println("licenseType "+asset.getLicenseType());
        if (data.get("licenseStartDate") != "") asset.setLicenseStartDate(parseDate(data.get("licenseStartDate")));
        System.out.println("licenseStartDate "+asset.getLicenseStartDate());
        if (data.get("licenseEndDate") != "") asset.setLicenseEndDate(parseDate(data.get("licenseEndDate")));
        System.out.println("licenseEndDate "+asset.getLicenseEndDate());
        if (data.get("licenseNumber") != "") asset.setLicenseNumber((String) data.get("licenseNumber"));
        System.out.println("licenseNumber "+asset.getLicenseNumber());
        if (data.get("relatedContractNumber") != "") asset.setRelatedContractNumber((String) data.get("relatedContractNumber"));
        System.out.println("relatedContractNumber "+asset.getRelatedContractNumber());
        System.out.println(asset);
        AssetsSoftware savedAsset = assetSoftwareRepository.save(asset);
        return buildSuccessResponse(savedAsset.getAssetId(), assetId != null);
    }
    private ResponseEntity<Map<String, Object>> savePhysicalAsset(Map<String, Object> data,Boolean newAsset) {
        AssetsPhysical asset;
        Integer assetId = null;
        if (data.containsKey("id") && data.get("id") != null) {
            System.out.println(1);
            assetId = Integer.valueOf(data.get("id").toString());
        }
        if (assetId != null && assetId > 0 &&newAsset) {
            asset = assetPhysicalRepository.findById(assetId.longValue())
                    .orElseThrow(() -> new RuntimeException("Physical asset not found"));
            if(asset.getAssetType() != 1) asset.setQStatus(0);
        } else {
            asset = new AssetsPhysical();
            asset.setCreatedAt(new Date());
            asset.setQStatus(0);
            asset.setRtStatus(0);
        }

        setBasicFields(asset, data);

        // Physical专属字段（带空值检查）
        if (data.get("physicalAssetType") != "") asset.setFixedPhysicalAsset((Integer) data.get("physicalAssetType"));
        if (data.get("epurchaseDate") != "") asset.setEpurchaseDate(parseDate(data.get("epurchaseDate")));
        if (data.get("depreciationPeriod") != "") asset.setDepreciationPeriod((Integer) data.get("depreciationPeriod"));
        if (data.get("maintenanceCycle") != "") asset.setMaintenanceCycle((Integer) data.get("maintenanceCycle"));

        System.out.println(asset.getFixedPhysicalAsset());
        if (asset.getFixedPhysicalAsset() != null && asset.getFixedPhysicalAsset() == 0) {
            if (data.get("assetCategory") != "") asset.setAssetCategory((Integer) data.get("assetCategory"));
            System.out.println("fixedAssetCategory "+asset.getAssetCategory());
            if (data.get("location") != "") asset.setLocation((String) data.get("location"));
            System.out.println("Location "+asset.getLocation());
        } else {
            if (data.get("assetCategory2") != "") asset.setAssetCategory2((Integer) data.get("assetCategory2"));
            if (data.get("currentHolder") != "") asset.setCurrentHolder((String) data.get("currentHolder"));
            if (data.get("checkoutDate") != "") asset.setCheckoutDate(parseDate(data.get("checkoutDate")));
            if (data.get("expectedReturnDate") != "") asset.setExpectedReturnDate(parseDate(data.get("expectedReturnDate")));
            if (data.get("conditions") != "") asset.setConditions((Integer) data.get("conditions"));
            if (data.get("dateEncryption") != "") asset.setDateEncryption((Integer) data.get("dateEncryption"));
            if (data.get("remoteWipeCapability") != "") asset.setRemoteWipeCapability((Integer) data.get("remoteWipeCapability"));
        }

        AssetsPhysical savedAsset = assetPhysicalRepository.save(asset);
        return buildSuccessResponse(savedAsset.getAssetId(), assetId != null);
    }
    private ResponseEntity<Map<String, Object>> saveInformationAsset(Map<String, Object> data ,Boolean newAsset) {
        AssetsInformation asset;
        Integer assetId = null;
        if (data.containsKey("id") && data.get("id") != null) {
            System.out.println(1);
            assetId = Integer.valueOf(data.get("id").toString());
        }
        if (assetId != null && assetId > 0 && newAsset) {
            asset = assetInformationRepository.findById(assetId.longValue())
                    .orElseThrow(() -> new RuntimeException("Information asset not found"));
            if(asset.getAssetType() != 2) asset.setQStatus(0);
        } else {
            asset = new AssetsInformation();
            asset.setCreatedAt(new Date());
            asset.setQStatus(0);
            asset.setRtStatus(0);
        }

        setBasicFields(asset, data);

        // Information专属字段
        if (data.get("informationAssetCategory") != "") asset.setAssetCategory((Integer) data.get("informationAssetCategory"));
        if (data.get("informationRetentionPolicy") != "") asset.setRetentionPolicy((Integer) data.get("informationRetentionPolicy"));
        if (data.get("informationStorageLocation") != "") asset.setStorageLocation((String) data.get("informationStorageLocation"));

        if (asset.getAssetCategory() != null) {
            switch (asset.getAssetCategory()) {
                case 0: // Database
                    if (data.get("databaseVersion") != "") asset.setAssetVersion((String) data.get("databaseVersion"));
                    if (data.get("DataSchema") != "") asset.setDataSchema((Integer) data.get("DataSchema"));
                    if (data.get("containsPII") != "") asset.setContainsPII((Integer) data.get("containsPII"));
                    if (data.get("backupFrequency") != "") asset.setBackupFrequency((Integer) data.get("backupFrequency"));
                    break;
                case 1: // Document
                    if (data.get("fileFormat") != "") asset.setFileFormat((Integer) data.get("fileFormat"));
                    if (data.get("confidentialityLevel") != "") asset.setConfidentialityLevel((Integer) data.get("confidentialityLevel"));
                    break;
                case 2: // Patent
                    if (data.get("registrationNumber") != "") asset.setRegistrationNumber((String) data.get("registrationNumber"));
                    if (data.get("expiryDate") != "") asset.setExpiryDate(parseDate(data.get("expiryDate")));
                    break;
            }
        }

        AssetsInformation savedAsset = assetInformationRepository.save(asset);
        return buildSuccessResponse(savedAsset.getAssetId(), assetId != null);
    }
    private ResponseEntity<Map<String, Object>> savePeopleAsset(Map<String, Object> data,Boolean newAsset) {
        AssetsPeople asset;
        Integer assetId = null;
        if (data.containsKey("id") && data.get("id") != null) {
            System.out.println(1);
            assetId = Integer.valueOf(data.get("id").toString());
        }
        if (assetId != null && assetId > 0 && newAsset) {
            asset = assetPeopleRepository.findById(assetId.longValue())
                    .orElseThrow(() -> new RuntimeException("People asset not found"));
            if(asset.getAssetType() != 3) asset.setQStatus(0);
        } else {
            asset = new AssetsPeople();
            asset.setCreatedAt(new Date());
            asset.setQStatus(0);
            asset.setRtStatus(0);
        }

        setBasicFields(asset, data);

        // People专属字段（带空值检查）
        if (data.get("department") != "") asset.setDepartment((String) data.get("department"));
        if (data.get("position") != "") asset.setPosition((String) data.get("position"));
        if (data.get("hireDate") != "") asset.setHireDate(parseDate(data.get("hireDate")));
        if (data.get("backgroundCheckStatus") != "") asset.setBackgroundCheckStatus((Integer) data.get("backgroundCheckStatus"));
        if (data.get("securityTrainingStatus") != "") asset.setSecurityTrainingStatus((Integer) data.get("securityTrainingStatus"));
        if (data.get("NDASigningDate") != "") asset.setNdaSigningDate(parseDate(data.get("NDASigningDate")));
        if (data.get("RemoteWorkAgreementStatus") != "") asset.setRemoteWorkAgreementStatus((Integer) data.get("RemoteWorkAgreementStatus"));
        if (data.get("securityIncidentRecords") != "") asset.setSecurityIncidentRecords((String) data.get("securityIncidentRecords"));
        if (data.get("LastAuditDate") != "") asset.setLastAuditDate(parseDate(data.get("LastAuditDate")));

        AssetsPeople savedAsset = assetPeopleRepository.save(asset);
        return buildSuccessResponse(savedAsset.getAssetId(), assetId != null);
    }
}
