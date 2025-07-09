package com.example.assetbasedriskassessmentplatformbackend.config;

import com.example.assetbasedriskassessmentplatformbackend.ServiceImpl.PdfGenerationServiceImpl;
import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class InventoryPdfGenerator {

    // Colors for styling
    private static final BaseColor TAG_GREEN = new BaseColor(50, 205, 50);
    private static final BaseColor TAG_RED = new BaseColor(220, 20, 60);
    private static final BaseColor TAG_BLUE = new BaseColor(30, 144, 255);
    private static final BaseColor TAG_ORANGE = new BaseColor(255, 165, 0);
    private static final BaseColor TAG_GRAY = new BaseColor(169, 169, 169);
    private static final BaseColor TAG_RED_HEAVY = new BaseColor(205, 51, 51);

    @Value("${pdf.storage-dir}")
    private String pdfStorageDir;

    public String generateInventoryPdf(AssetsBasicInfo asset) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        addTitle(document, asset);
        addBasicInfo(document, asset);
        addTypeSpecificInfo(document, asset);

        document.close();

        return savePdf(baos.toByteArray());
    }

    private void addTitle(Document document, AssetsBasicInfo asset) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph(asset.getAssetName() + " Inventory", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

    private void addBasicInfo(Document document, AssetsBasicInfo asset) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addTableRow(table, "Asset Type:", Converters.assetType(asset.getAssetType()));
        addTableRow(table, "Asset Owner:", asset.getAssetOwner() != null ? asset.getAssetOwner().getAssetUserName() : "");
        addTableRow(table, "Contact:", asset.getContact());
        addTableRow(table, "Description:", asset.getDescription());
        addTableRow(table, "Asset ID:", asset.getSwid());
        addStyledStatusRow(table, "Status:",Converters.assetStatus(asset.getStatus()),BaseColor.WHITE);
        addStyledStatusRow(table, "Importance:",Converters.assetImportance(asset.getImportance()),BaseColor.WHITE);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addTypeSpecificInfo(Document document, AssetsBasicInfo asset) throws DocumentException {
        if (asset == null) return;

        switch (asset.getAssetType()) {
            case 0:
                if (asset instanceof AssetsSoftware) {
                    addSoftwareInfo(document, (AssetsSoftware) asset);
                }
                break;
            case 1:
                if (asset instanceof AssetsPhysical) {
                    addPhysicalInfo(document, (AssetsPhysical) asset);
                }
                break;
            case 2:
                if (asset instanceof AssetsInformation) {
                    addInformationInfo(document, (AssetsInformation) asset);
                }
                break;
            case 3:
                if (asset instanceof AssetsPeople) {
                    addPeopleInfo(document, (AssetsPeople) asset);
                }
                break;
        }
    }

    private void addSoftwareInfo(Document document, AssetsSoftware software) throws DocumentException {
        if (software == null) return;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addTableRow(table, "Version:", software.getVersion());
        addTableRow(table, "Install Date:", software.getInstallDate() != null ? Converters.formatDate(software.getInstallDate()) : "");
        addTableRow(table, "Operating Systems:", software.getOperatingSystems());
        addTableRow(table, "External Supplied Service?", software.getExternalSuppliedService() != null ?
                (software.getExternalSuppliedService() == 0 ? "Yes" : "No") : "");

        if (software.getExternalSuppliedService() != null && software.getExternalSuppliedService() == 0) {
            addTableRow(table, "Manufacturer:", software.getManufacture());
            addTableRow(table, "Service Type:", software.getServiceType() != null ?
                    Converters.softwareServiceType(software.getServiceType()) : "");
            addTableRow(table, "License Type:", software.getLicenseType() != null ?
                    Converters.softwareLicenseType(software.getLicenseType()) : "");

                addTableRow(table, "License Start Date:", software.getLicenseStartDate() != null ?
                        Converters.formatDate(software.getLicenseStartDate()) : "");
                addTableRow(table, "License End Date:", software.getLicenseEndDate() != null ?
                        Converters.formatDate(software.getLicenseEndDate()) : "");

                addTableRow(table, "License Number:", software.getLicenseNumber());

            addTableRow(table, "Related Contract Number:", software.getRelatedContractNumber());
        }

        document.add(table);
    }

    private void addPhysicalInfo(Document document, AssetsPhysical physical) throws DocumentException {
        if (physical == null) return;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addTableRow(table, " Fixed or Non-fixed physical asset?", physical.getFixedPhysicalAsset() != null ?
                (physical.getFixedPhysicalAsset() == 0 ? "Yes" : "No") : "");
        if (physical.getFixedPhysicalAsset() != null && physical.getFixedPhysicalAsset() == 0) {
            addTableRow(table, "Asset Category:", physical.getAssetCategory() != null ?
                    Converters.fixedAssetCategory(physical.getAssetCategory()) : "");
            addTableRow(table, "Location:", physical.getLocation());
        } else {
            addTableRow(table, "Asset Category:", physical.getAssetCategory2() != null ?
                    Converters.nonFixedAssetCategory(physical.getAssetCategory2()) : "");
            addTableRow(table, "Current Holder:", physical.getCurrentHolder());
            addTableRow(table, "Checkout Date:", physical.getCheckoutDate() != null ?
                    Converters.formatDate(physical.getCheckoutDate()) : "");
            addTableRow(table, "Expected Return Date:", physical.getExpectedReturnDate() != null ?
                    Converters.formatDate(physical.getExpectedReturnDate()) : "");
            addTableRow(table, "Condition:", physical.getConditions() != null ?
                    Converters.physicalCondition(physical.getConditions()) : "");
            addTableRow(table, "Data Encryption:", physical.getDateEncryption() != null ?
                    Converters.booleanValue(physical.getDateEncryption()) : "");
            addTableRow(table, "Remote Wipe Capability:", physical.getRemoteWipeCapability() != null ?
                    Converters.booleanValue(physical.getRemoteWipeCapability()) : "");
        }

        addTableRow(table, "Purchase Date:", physical.getEpurchaseDate() != null ?
                Converters.formatDate(physical.getEpurchaseDate()) : "");
        addTableRow(table, "Depreciation Period:", physical.getDepreciationPeriod() != null ?
                physical.getDepreciationPeriod() + " years" : "");
        addTableRow(table, "Maintenance Cycle:", physical.getMaintenanceCycle() != null ?
                Converters.maintenanceCycle(physical.getMaintenanceCycle()) : "");

        document.add(table);
    }

    private void addInformationInfo(Document document, AssetsInformation information) throws DocumentException {
        if (information == null) return;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addTableRow(table, "Asset Category:", information.getAssetCategory() != null ?
                Converters.informationCategory(information.getAssetCategory()) : "");
        addTableRow(table, "Retention Policy:", information.getRetentionPolicy() != null ?
                Converters.retentionPolicy(information.getRetentionPolicy()) : "");
        addTableRow(table, "Storage Location:", information.getStorageLocation());

        if (information.getAssetCategory() != null) {
            switch (information.getAssetCategory()) {
                case 0:
                    addTableRow(table, "Data Schema:", information.getDataSchema() != null ?
                            Converters.dataSchema(information.getDataSchema()) : "");
                    addTableRow(table, "Version:", information.getAssetVersion());
                    addTableRow(table, "Contains PII:", information.getContainsPII() != null ?
                            Converters.booleanValue(information.getContainsPII()) : "");
                    addTableRow(table, "Backup Frequency:", information.getBackupFrequency() != null ?
                            Converters.backupFrequency(information.getBackupFrequency()) : "");
                    break;
                case 1:
                    addTableRow(table, "File Format:", information.getFileFormat() != null ?
                            Converters.fileFormat(information.getFileFormat()) : "");
                    addTableRow(table, "Confidentiality Level:", information.getConfidentialityLevel() != null ?
                            Converters.confidentialityLevel(information.getConfidentialityLevel()) : "");
                    break;
                case 2:
                    addTableRow(table, "Registration Number:", information.getRegistrationNumber());
                    addTableRow(table, "Expiry Date:", information.getExpiryDate() != null ?
                            Converters.formatDate(information.getExpiryDate()) : "");
                    break;
            }
        }

        document.add(table);
    }

    private void addPeopleInfo(Document document, AssetsPeople people) throws DocumentException {
        if (people == null) return;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addTableRow(table, "Department:", people.getDepartment());
        addTableRow(table, "Position:", people.getPosition());
        addTableRow(table, "Hire Date:", people.getHireDate() != null ?
                Converters.formatDate(people.getHireDate()) : "");
        addTableRow(table, "Background Check Status:", people.getBackgroundCheckStatus() != null ?
                Converters.backgroundCheckStatus(people.getBackgroundCheckStatus()) : "");
        addTableRow(table, "Security Training Status:", people.getSecurityTrainingStatus() != null ?
                Converters.trainingStatus(people.getSecurityTrainingStatus()) : "");
        addTableRow(table, "NDA Signing Date:", people.getNdaSigningDate() != null ?
                Converters.formatDate(people.getNdaSigningDate()) : "");
        addTableRow(table, "Remote Work Agreement:", people.getRemoteWorkAgreementStatus() != null ?
                Converters.booleanValue(people.getRemoteWorkAgreementStatus()) : "");
        addTableRow(table, "Security Incident Records:", people.getSecurityIncidentRecords());
        addTableRow(table, "Last Audit Date:", people.getLastAuditDate() != null ?
                Converters.formatDate(people.getLastAuditDate()) : "");

        document.add(table);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String savePdf(byte[] pdfContent) throws IOException {
        Path storagePath = Paths.get(pdfStorageDir);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        Path inventoryDir = storagePath.resolve("inventory");
        if (!Files.exists(inventoryDir)) {
            Files.createDirectories(inventoryDir);
        }

        String filename = UUID.randomUUID() + ".pdf";
        Path filePath = inventoryDir.resolve(filename);
        Files.write(filePath, pdfContent);

        return filename;
    }

    private void addStyledStatusRow(PdfPTable table, String label, String value, BaseColor bgColor) {
        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);

        // Value cell with tag styling
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(bgColor);
        valueCell.setPadding(5);

        Chunk tag = new Chunk(value,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE));
        tag.setBackground(getStatusColor(value), 5, 2, 5, 2);
        tag.setLineHeight(15);

        valueCell.addElement(new Phrase(tag));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private BaseColor getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "active":
            case "finished":
            case "low":
                return TAG_GREEN;
            case "very low":
                return new BaseColor(202, 255, 112);
            case "very high":
            case "extremely high":
                return TAG_RED_HEAVY;
            case "high":
                return TAG_RED;
            case "in-progress":
            case "medium":
                return TAG_ORANGE;
            case "decommissioned":
            default:
                return TAG_GRAY;
        }
    }
}