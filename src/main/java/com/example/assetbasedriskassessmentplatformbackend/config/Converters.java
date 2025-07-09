package com.example.assetbasedriskassessmentplatformbackend.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 基本类型转换
    public static String assetType(Integer type) {
        if (type == null) return "Unknown";
        switch (type) {
            case 0: return "Software";
            case 1: return "Physical";
            case 2: return "Information";
            case 3: return "People";
            default: return "Unknown";
        }
    }

    public static String assetStatus(Integer status) {
        if (status == null) return "Unknown";
        switch (status) {
            case 0: return "Active";
            case 1: return "Decommissioned";
            default: return "Unknown";
        }
    }

    public static String assetImportance(Integer importance) {
        if (importance == null) return "Unknown";
        switch (importance) {
            case 0: return "Low";
            case 1: return "Medium";
            case 2: return "High";
            case 3: return "Extremely High";
            default: return "Unknown";
        }
    }

    public static String booleanValue(Integer value) {
        if (value == null) return "N/A";
        return value == 1 ? "Yes" : "No";
    }

    public static String formatDate(Date date) {
        if (date == null) return "N/A";
        return DATE_FORMAT.format(date);
    }

    // 软件资产相关转换
    public static String softwareServiceType(Integer type) {
        if (type == null) return "Unknown";
        switch (type) {
            case 0: return "On-Premises";
            case 1: return "SaaS";
            case 2: return "Hybrid";
            case 3: return "Hosted";
            default: return "Unknown";
        }
    }

    public static String softwareLicenseType(Integer type) {
        if (type == null) return "Unknown";
        switch (type) {
            case 0: return "Permanent";
            case 1: return "Subscription";
            case 2: return "Trial";
            case 3: return "Volume Licensing";
            default: return "Unknown";
        }
    }

    // 物理资产相关转换
    public static String fixedAssetCategory(Integer category) {
        if (category == null) return "Unknown";
        switch (category) {
            case 0: return "Buildings & Structures";
            case 1: return "Production Equipment & Machinery";
            case 2: return "Office Equipment";
            case 3: return "Transportation Vehicles";
            case 4: return "Network & IT Infrastructure";
            case 5: return "Power & Utility Equipment";
            case 6: return "Security Equipment";
            default: return "Unknown";
        }
    }

    public static String nonFixedAssetCategory(Integer category) {
        if (category == null) return "Unknown";
        switch (category) {
            case 0: return "Mobile Electronic Devices";
            case 1: return "Removable Storage Media";
            case 2: return "Temporary Facilities and Tools";
            case 3: return "Transportation and Logistics Assets";
            case 4: return "Laboratory and Production Consumables";
            case 5: return "Security and Emergency Equipment";
            case 6: return "Other High-Mobility Assets";
            default: return "Unknown";
        }
    }

    public static String physicalCondition(Integer condition) {
        if (condition == null) return "Unknown";
        switch (condition) {
            case 0: return "Damaged";
            case 1: return "Good";
            default: return "Unknown";
        }
    }

    public static String maintenanceCycle(Integer cycle) {
        if (cycle == null) return "Unknown";
        switch (cycle) {
            case 0: return "Daily/Weekly/Every 72 Operating Hours";
            case 1: return "Monthly/Quarterly/Every 5000 km";
            case 2: return "Annually/Biannually/Every 5 Years";
            case 3: return "As Needed/After Event/On Failure";
            default: return "Unknown";
        }
    }

    // 信息资产相关转换
    public static String informationCategory(Integer category) {
        if (category == null) return "Unknown";
        switch (category) {
            case 0: return "Database";
            case 1: return "Document";
            case 2: return "Patent";
            default: return "Unknown";
        }
    }

    public static String retentionPolicy(Integer policy) {
        if (policy == null) return "Unknown";
        switch (policy) {
            case 0: return "Permanent";
            case 1: return "5 Years";
            default: return "Unknown";
        }
    }

    public static String dataSchema(Integer schema) {
        if (schema == null) return "Unknown";
        switch (schema) {
            case 0: return "MySQL";
            case 1: return "MongoDB";
            default: return "Unknown";
        }
    }

    public static String backupFrequency(Integer frequency) {
        if (frequency == null) return "Unknown";
        switch (frequency) {
            case 0: return "Daily";
            case 1: return "Weekly";
            case 2: return "Monthly";
            default: return "Unknown";
        }
    }

    public static String fileFormat(Integer format) {
        if (format == null) return "Unknown";
        switch (format) {
            case 0: return "PDF";
            case 1: return "DOCX";
            case 2: return "CAD";
            default: return "Unknown";
        }
    }

    public static String confidentialityLevel(Integer level) {
        if (level == null) return "Unknown";
        switch (level) {
            case 0: return "Public";
            case 1: return "Internal";
            case 2: return "Secret";
            default: return "Unknown";
        }
    }

    // 人员资产相关转换
    public static String backgroundCheckStatus(Integer status) {
        if (status == null) return "Unknown";
        switch (status) {
            case 0: return "Completed";
            case 1: return "Pending";
            case 2: return "Failed";
            case 3: return "Expired";
            case 4: return "Not Required";
            default: return "Unknown";
        }
    }

    public static String trainingStatus(Integer status) {
        if (status == null) return "Unknown";
        switch (status) {
            case 0: return "Completed";
            case 1: return "Pending";
            case 2: return "Failed";
            case 3: return "Expired";
            case 4: return "Not Required";
            default: return "Unknown";
        }
    }
}