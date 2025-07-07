package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.PdfGenerationService;
import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import com.example.assetbasedriskassessmentplatformbackend.entity.Files;
import com.example.assetbasedriskassessmentplatformbackend.repository.AssetBasicInfoRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.EvidenceChainRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.RiskRelationshipRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    // Colors for styling
    private static final BaseColor LIGHT_BLUE = new BaseColor(225, 240, 255);
    private static final BaseColor LIGHT_GREEN = new BaseColor(225, 255, 240);
    private static final BaseColor LIGHT_YELLOW = new BaseColor(255, 255, 225);
    private static final BaseColor LIGHT_PURPLE = new BaseColor(240, 225, 255);
    private static final BaseColor TAG_GREEN = new BaseColor(50, 205, 50);
    private static final BaseColor TAG_RED = new BaseColor(220, 20, 60);
    private static final BaseColor TAG_BLUE = new BaseColor(30, 144, 255);
    private static final BaseColor TAG_ORANGE = new BaseColor(255, 165, 0);
    private static final BaseColor TAG_GRAY = new BaseColor(169, 169, 169);

    @Value("${pdf.storage-dir}")
    private String pdfStorageDir;

    @Autowired
    private AssetBasicInfoRepository assetsBasicInfoRepository;

    @Autowired
    private RiskRelationshipRepository riskRelationshipRepository;

    @Autowired
    private EvidenceChainRepository evidenceChainRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public byte[] generateEvidenceChainPdf(Integer assetId, String generatedBy) {
        try {
            AssetsBasicInfo asset = assetsBasicInfoRepository.findById((long)assetId)
                    .orElseThrow(() -> new RuntimeException("Asset not found"));

            List<RiskRelationship> risks = riskRelationshipRepository.findByAssetIdAndValid(assetId, 2);

            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Add metadata
            document.addTitle("Evidence Chain for " + asset.getAssetName());
            document.addAuthor("Risk Assessment Platform");
            document.addCreator("Risk Assessment Platform");

            // Add content
            System.out.println(111);
            addExecutiveSummary(document, writer, asset);
            System.out.println(222);
            addAssetInventory(document, writer, asset);
            System.out.println(333);
            addRiskAssessment(document, writer, asset, risks);
            System.out.println(444);
            addRiskTreatment(document, writer, risks);
            System.out.println(555);

            document.close();

            String filename = savePdfToStorage(baos.toByteArray(), assetId);
            createEvidenceChainRecord(asset, filename, generatedBy);

            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private void addExecutiveSummary(Document document, PdfWriter writer, AssetsBasicInfo asset) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph sectionTitle = new Paragraph("Executive Summary", sectionFont);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        // Create a table for aligned content
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addStyledTableRow(table, "Asset Name:", asset.getAssetName(), LIGHT_BLUE);
        addStyledTableRow(table, "Asset Type:", getAssetTypeName(asset.getAssetType()), LIGHT_BLUE);
        addStyledTableRow(table, "Owner:", asset.getAssetOwner().getAssetUserName(), LIGHT_BLUE);
        addStyledStatusRow(table, "Assessment Status:", getQStatusName(asset.getQStatus()),LIGHT_BLUE);
        addStyledStatusRow(table, "Risk Treatment Status:", getRTStatusName(asset.getRtStatus()),LIGHT_BLUE);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addAssetInventory(Document document, PdfWriter writer, AssetsBasicInfo asset) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph sectionTitle = new Paragraph("Asset Inventory", sectionFont);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3});

        addStyledStatusRow(table, "Asset Importance:", getImportanceName(asset.getImportance()),LIGHT_GREEN);
        addStyledStatusRow(table, "Asset Status:", getStatusName(asset.getStatus()),LIGHT_GREEN);

        // Add clickable link
        PdfPCell labelCell = new PdfPCell(new Phrase("Inventory Link:",
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(LIGHT_GREEN);
        labelCell.setPadding(5);

        String linkText = asset.getAssetName();
        Anchor link = new Anchor(linkText,
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLUE));
        link.setReference("/asset-detail/" + asset.getAssetId());

        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(LIGHT_GREEN);
        valueCell.setPadding(5);
        Paragraph linkParagraph = new Paragraph();
        linkParagraph.add(link);
        valueCell.addElement(linkParagraph);


        table.addCell(labelCell);
        table.addCell(valueCell);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addRiskAssessment(Document document, PdfWriter writer, AssetsBasicInfo asset, List<RiskRelationship> risks)
            throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph sectionTitle = new Paragraph("Risk Assessment", sectionFont);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        // 创建主表格（2列：标签和内容）
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{1, 3});

        // 添加问卷链接行
        addStyledTableRow(mainTable, "Questionnaire Link:", "", LIGHT_GREEN);
        PdfPCell linkCell = new PdfPCell();
        linkCell.setBorder(Rectangle.NO_BORDER);
        linkCell.setBackgroundColor(LIGHT_GREEN);
        linkCell.setPadding(5);

        Anchor questionnaireLink = new Anchor(asset.getAssetName(),
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLUE));
        questionnaireLink.setReference("/questionnaire/" + asset.getAssetId());

        Paragraph linkParagraph = new Paragraph();
        linkParagraph.add(questionnaireLink);
        linkCell.addElement(linkParagraph);
        mainTable.addCell(linkCell);

        // 添加风险信息（合并所有风险到一个单元格）
        PdfPCell risksLabelCell = new PdfPCell(new Phrase("Identified Risks:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));
        risksLabelCell.setBorder(Rectangle.NO_BORDER);
        risksLabelCell.setBackgroundColor(LIGHT_PURPLE);
        risksLabelCell.setPadding(5);

        PdfPCell risksValueCell = new PdfPCell();
        risksValueCell.setBorder(Rectangle.NO_BORDER);
        risksValueCell.setBackgroundColor(LIGHT_PURPLE);
        risksValueCell.setPadding(5);

        // 创建风险内容列表
        Paragraph risksContent = new Paragraph();
        for (RiskRelationship risk : risks) {
            // 添加风险类型
            risksContent.add(new Phrase("• " + risk.getRiskType().getContent() + "\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));

            // 添加警告（如果有）
            if (risk.getRiskType().getWarning() != null) {
                risksContent.add(new Phrase("   Warning: " + risk.getRiskType().getWarning() + "\n",
                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED)));
            }

//            // 添加评论（如果有）
//            if (risk.getComments() != null) {
//                risksContent.add(new Phrase("   Comments: " + risk.getComments() + "\n",
//                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY)));
//            }

            risksContent.add(Chunk.NEWLINE); // 风险之间的间距
        }

        risksValueCell.addElement(risksContent);
        mainTable.addCell(risksLabelCell);
        mainTable.addCell(risksValueCell);

        document.add(mainTable);
        document.add(Chunk.NEWLINE);
    }

    private void addRiskTreatment(Document document, PdfWriter writer, List<RiskRelationship> risks) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
        Paragraph sectionTitle = new Paragraph("4. Risk Treatment", sectionFont);
        sectionTitle.setSpacingAfter(15);
        document.add(sectionTitle);

        for (RiskRelationship risk : risks) {
            if (risk.getRiskTreatment() != null) {
                RiskTreatment treatment = risk.getRiskTreatment();

                PdfPTable treatmentTable = new PdfPTable(2);
                treatmentTable.setWidthPercentage(100);
                treatmentTable.setWidths(new float[]{1, 3});
                treatmentTable.setSpacingBefore(10);
                treatmentTable.setSpacingAfter(10);

                addStyledTableRow(treatmentTable, "Risk:", risk.getRiskType().getContent(), LIGHT_GREEN);
                addStyledStatusRow(treatmentTable, "Risk Level:", getRiskLevelName(treatment.getRiskLevel()),LIGHT_GREEN);
                addStyledStatusRow(treatmentTable, "Treatment Option:", getTreatmentOptionName(treatment.getTreatmentOption()),LIGHT_GREEN);

                if (treatment.getComments() != null) {
                    addStyledTableRow(treatmentTable, "Comments:", treatment.getComments(), LIGHT_GREEN);
                }

                // Add evidence files if any
                if (!treatment.getEvidenceFiles().isEmpty()) {
                    PdfPCell filesLabelCell = new PdfPCell(new Phrase("Evidence Files:",
                            FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                    filesLabelCell.setBorder(Rectangle.NO_BORDER);
                    filesLabelCell.setBackgroundColor(LIGHT_GREEN);
                    filesLabelCell.setPadding(5);

                    PdfPCell filesValueCell = new PdfPCell();
                    filesValueCell.setBorder(Rectangle.NO_BORDER);
                    filesValueCell.setBackgroundColor(LIGHT_GREEN);
                    filesValueCell.setPadding(5);

                    for (Files file : treatment.getEvidenceFiles()) {
                        Anchor fileLink = new Anchor(file.getOriginalName(),
                                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLUE));
                        fileLink.setReference(file.getFilePath());
                        filesValueCell.addElement(fileLink);
                        filesValueCell.addElement(new Phrase(" "));
                    }

                    treatmentTable.addCell(filesLabelCell);
                    treatmentTable.addCell(filesValueCell);
                }

                document.add(treatmentTable);
            }
        }
    }

    private void addStyledTableRow(PdfPTable table, String label, String value, BaseColor bgColor) {
        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);

        // Value cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value,
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(bgColor);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addStyledStatusRow(PdfPTable table, String label, String value, BaseColor bgColor) {
        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);

        // Value cell with tag styling
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(bgColor);
        valueCell.setPadding(5);

        Chunk tag = new Chunk(value,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE));
        tag.setBackground(getStatusColor(value), 5, 2, 5, 2);
        tag.setLineHeight(20);

        valueCell.addElement(new Phrase(tag));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private BaseColor getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "active":
            case "finished":
            case "high":
            case "very high":
            case "extremely high":
                return TAG_GREEN;
            case "decommissioned":
            case "low":
            case "very low":
                return TAG_RED;
            case "in-progress":
                return TAG_BLUE;
            case "medium":
                return TAG_ORANGE;
            default:
                return TAG_GRAY;
        }
    }

    // Rest of the helper methods remain the same...
    private String savePdfToStorage(byte[] pdfContent, Integer assetId) throws IOException {
        Path storagePath = Paths.get(pdfStorageDir);
        if (!java.nio.file.Files.exists(storagePath)) {
            java.nio.file.Files.createDirectories(storagePath);
        }

        String filename = "evidence_chain_" + assetId + "_" + System.currentTimeMillis() + ".pdf";
        Path filePath = storagePath.resolve(filename);
        java.nio.file.Files.write(filePath, pdfContent);

        return filename;
    }

    private void createEvidenceChainRecord(AssetsBasicInfo asset, String filename, String generatedBy) {
        long version = evidenceChainRepository.countByAsset_AssetId(asset.getAssetId()) + 1;
        EvidenceChain evidenceChain = new EvidenceChain();
        evidenceChain.setAsset(asset);
        evidenceChain.setGenerateDate(new Date());
        evidenceChain.setGenerateBy(generatedBy);
        evidenceChain.setName("Evidence Chain " + version);

        evidenceChain.setAssetName(asset.getAssetName());
        evidenceChain.setAssetType(getAssetTypeName(asset.getAssetType()));
        evidenceChain.setAssetOwner(asset.getAssetOwner().getAssetUserName());
        evidenceChain.setAssetStatus(getStatusName(asset.getStatus()));

        evidenceChain.setSnapshot("http://localhost:8081/api/pdf/download/" + filename);

        evidenceChainRepository.save(evidenceChain);
    }

    private String getAssetTypeName(Integer type) {
        switch (type) {
            case 0: return "Software";
            case 1: return "Physical";
            case 2: return "Information";
            case 3: return "People";
            default: return "Unknown";
        }
    }

    private String getQStatusName(Integer status) {
        switch (status) {
            case 0: return "In-progress";
            case 1: return "Finished";
            default: return "Unknown";
        }
    }

    private String getRTStatusName(Integer status) {
        switch (status) {
            case 0: return "In-progress";
            case 1: return "Finished";
            default: return "Unknown";
        }
    }

    private String getImportanceName(Integer importance) {
        switch (importance) {
            case 0: return "Low";
            case 1: return "Medium";
            case 2: return "High";
            case 3: return "Extremely High";
            default: return "Unknown";
        }
    }

    private String getStatusName(Integer status) {
        switch (status) {
            case 0: return "Active";
            case 1: return "Decommissioned";
            default: return "Unknown";
        }
    }

    private String getRiskLevelName(Integer level) {
        switch (level) {
            case 0: return "Very Low";
            case 1: return "Low";
            case 2: return "Medium";
            case 3: return "High";
            case 4: return "Very High";
            default: return "Unknown";
        }
    }

    private String getTreatmentOptionName(Integer option) {
        switch (option) {
            case 0: return "Risk Avoidance";
            case 1: return "Risk Modification";
            case 2: return "Risk Retention";
            case 3: return "Risk Sharing";
            default: return "Unknown";
        }
    }
}