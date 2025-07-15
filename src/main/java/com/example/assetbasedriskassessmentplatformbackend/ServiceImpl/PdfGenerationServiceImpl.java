package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.PdfGenerationService;
import com.example.assetbasedriskassessmentplatformbackend.config.InventoryPdfGenerator;
import com.example.assetbasedriskassessmentplatformbackend.config.QuestionnairePdfGenerator;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    // Colors for styling
    private static final BaseColor LIGHT_BLUE = new BaseColor(225, 240, 255);
    private static final BaseColor LIGHT_GREEN = new BaseColor(225, 255, 240);
    private static final BaseColor LIGHT_YELLOW = new BaseColor(255, 255, 225);
    private static final BaseColor LIGHT_PURPLE = new BaseColor(240, 225, 255);
    private static final BaseColor TAG_GREEN = new BaseColor(50, 205, 50);
    private static final BaseColor TAG_RED = new BaseColor(255, 64, 64);
    private static final BaseColor TAG_RED_HEAVY = new BaseColor(205, 51, 51);
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
    private InventoryPdfGenerator inventoryPdfGenerator;

    @Autowired
    private QuestionnairePdfGenerator questionnairePdfGenerator;

    @Transactional
    public ResponseEntity<Map<String, Object>> generateEvidenceChainPdf(Integer assetId, String generatedBy) {
        Map<String, Object> response = new HashMap<>();
        try {
            AssetsBasicInfo asset = assetsBasicInfoRepository.findById((long)assetId)
                    .orElseThrow(() -> new RuntimeException("Asset not found"));
            long version = evidenceChainRepository.countByAsset_AssetId(asset.getAssetId())+1;

            List<RiskRelationship> risks = riskRelationshipRepository.findByAssetIdAndValid(assetId, 2);
            risks.sort(Comparator.comparing(risk -> risk.getRiskType().getTypeID()));

//            Document document = new Document();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            PdfWriter writer = PdfWriter.getInstance(document, baos);
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
//            writer.setPageEvent(null);

            document.open();

            // Add metadata
            document.addTitle(asset.getAssetName()+" Evidence Chain Version " + version);
            document.addAuthor(generatedBy);
            document.addCreator(generatedBy);

            // Add content
            System.out.println(111);
            addExecutiveSummary(document, writer, asset);
            System.out.println(222);
            addAssetInventory(document, writer, asset);
            System.out.println(333);
            addRiskAssessment(document, writer, asset, risks);
            System.out.println(444);
            addRiskTreatment(document, writer, assetId, risks);
            System.out.println(555);

            document.close();

            String filename = savePdfToStorage(baos.toByteArray(), assetId);
            createEvidenceChainRecord(asset, filename, generatedBy);

            response.put("success", true);
            response.put("message", asset.getAssetName()+" Evidence Chain " +version+" generated successfully");
            return ResponseEntity.ok(response);

        } catch (DocumentException | IOException e) {
            response.put("success", false);
            response.put("message", e);
            return ResponseEntity.badRequest().body(response);
        }
    }

    private void addExecutiveSummary(Document document, PdfWriter writer, AssetsBasicInfo asset) throws DocumentException {
//        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
//        Paragraph sectionTitle = new Paragraph("Executive Summary", sectionFont);
//        sectionTitle.setSpacingAfter(15);
//        document.add(sectionTitle);
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        // Create the title cell with background color
        PdfPCell titleCell = new PdfPCell(new Phrase("Executive Summary",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY)));
        titleCell.setBackgroundColor(LIGHT_BLUE);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPadding(10);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleTable.addCell(titleCell);

        document.add(titleTable);
        document.add(Chunk.NEWLINE);

        // Create a table for aligned content
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 7});

        addStyledTableRow(table, "Asset Name:", asset.getAssetName(), LIGHT_BLUE);
        addStyledTableRow(table, "Asset Type:", getAssetTypeName(asset.getAssetType()), LIGHT_BLUE);
        addStyledTableRow(table, "Owner:", asset.getAssetOwner().getAssetUserName(), LIGHT_BLUE);
        addStyledStatusRow(table, "Assessment Status:", getQStatusName(asset.getQStatus()),LIGHT_BLUE);
        addStyledStatusRow(table, "Risk Treatment Status:", getRTStatusName(asset.getRtStatus()),LIGHT_BLUE);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addAssetInventory(Document document, PdfWriter writer, AssetsBasicInfo asset) throws DocumentException {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        // Create the title cell with background color
        PdfPCell titleCell = new PdfPCell(new Phrase("Asset Inventory",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY)));
        titleCell.setBackgroundColor(LIGHT_YELLOW);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPadding(10);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleTable.addCell(titleCell);

        document.add(titleTable);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 7});

        addStyledStatusRow(table, "Asset Importance:", getImportanceName(asset.getImportance()),LIGHT_YELLOW);
        addStyledStatusRow(table, "Asset Status:", getStatusName(asset.getStatus()),LIGHT_YELLOW);

        // Add clickable link
        PdfPCell labelCell = new PdfPCell(new Phrase("Inventory Link:",
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(LIGHT_YELLOW);
        labelCell.setPadding(5);

        String linkText = asset.getAssetName();
        String inventoryPdfUrl ="";
        try {
            inventoryPdfUrl = inventoryPdfGenerator.generateInventoryPdf(asset);
        }catch (Exception e){
            System.out.println(e);
        }
        Anchor link = new Anchor(linkText,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLUE));
        link.setReference("http://localhost:8081/pdf-storage/inventory/"+inventoryPdfUrl);

        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(LIGHT_YELLOW);
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
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        // Create the title cell with background color
        PdfPCell titleCell = new PdfPCell(new Phrase("Risk Assessment",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY)));
        titleCell.setBackgroundColor(LIGHT_PURPLE);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPadding(10);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleTable.addCell(titleCell);

        document.add(titleTable);
        document.add(Chunk.NEWLINE);

        // 创建主表格（2列：标签和内容）
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{2, 7});

        PdfPCell labelCell = new PdfPCell(new Phrase("Questionnaire:",
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(LIGHT_PURPLE);
        labelCell.setPadding(5);

        String linkText = asset.getAssetName();
        String QuestionnairePdfUrl ="";
        try {
            QuestionnairePdfUrl = questionnairePdfGenerator.generateQuestionnairePdf(asset.getQuestionnaire());
        }catch (Exception e){
            System.out.println(e);
        }
        Anchor link = new Anchor(linkText,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLUE));
        link.setReference("http://localhost:8081/pdf-storage/questionnaires/"+QuestionnairePdfUrl);

        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(LIGHT_PURPLE);
        valueCell.setPadding(5);
        Paragraph linkParagraph = new Paragraph();
        linkParagraph.add(link);
        valueCell.addElement(linkParagraph);
        mainTable.addCell(labelCell);
        mainTable.addCell(valueCell);


        // 添加风险信息（合并所有风险到一个单元格）
        PdfPCell risksLabelCell = new PdfPCell(new Phrase("Identified Risks:",
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        risksLabelCell.setBorder(Rectangle.NO_BORDER);
        risksLabelCell.setBackgroundColor(LIGHT_PURPLE);
        risksLabelCell.setPadding(5);

        PdfPCell risksValueCell = new PdfPCell();
        risksValueCell.setBorder(Rectangle.NO_BORDER);
        risksValueCell.setBackgroundColor(LIGHT_PURPLE);
        risksValueCell.setPadding(5);

        // 创建风险内容列表
        Paragraph risksContent = new Paragraph();
        int index = 0;
        int subindex = 0;
        String warning ="";
        for (RiskRelationship risk : risks) {
            if(risk.getRiskType().getWarning().equals(warning)){
                risksContent.add(new Phrase("   Risk "+(++subindex)+":" + risk.getRiskType().getContent(),
                        FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
            }else{
                index+=1;
                subindex = 0;
                warning = risk.getRiskType().getWarning();
                if(index!=1) risksContent.add(new Phrase(" \n", FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK)));
                risksContent.add(new Phrase(index+". " + risk.getRiskType().getWarning() +"\n\n",
                        FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
                risksContent.add(new Phrase("   Risk "+(++subindex)+":" + risk.getRiskType().getContent(),
                        FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
            }
            risksContent.add(Chunk.NEWLINE); // 风险之间的间距
        }
        risksContent.add(Chunk.NEWLINE);
        risksContent.add(new Phrase(" \n", FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        risksValueCell.addElement(risksContent);
        mainTable.addCell(risksLabelCell);
        mainTable.addCell(risksValueCell);

        document.add(mainTable);
        document.add(Chunk.NEWLINE);
    }

    private void addRiskTreatment(Document document, PdfWriter writer, Integer assetId,List<RiskRelationship> risks) throws DocumentException {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);

        // Create the title cell with background color
        PdfPCell titleCell = new PdfPCell(new Phrase("Risk Treatment",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY)));
        titleCell.setBackgroundColor(LIGHT_GREEN);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setPadding(10);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        titleTable.addCell(titleCell);

        document.add(titleTable);
        document.add(Chunk.NEWLINE);

        // 创建风险内容列表
        int index = 0;
        int subindex = 0;
        String warning ="";
        for (RiskRelationship risk : risks) {
            if(!risk.getRiskType().getWarning().equals(warning)){
                index+=1;
                subindex = 0;
                warning = risk.getRiskType().getWarning();
                if(index==1){
                    PdfPTable mainTable = new PdfPTable(2);
                    mainTable.setWidthPercentage(100);
                    mainTable.setWidths(new float[]{2, 7});
                    PdfPCell risksLabelCell = new PdfPCell(new Phrase("Treatment Records:",
                            FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
                    risksLabelCell.setBorder(Rectangle.NO_BORDER);
                    risksLabelCell.setBackgroundColor(LIGHT_GREEN);
                    risksLabelCell.setPadding(5);

                    PdfPCell risksValueCell = new PdfPCell();
                    risksValueCell.setBorder(Rectangle.NO_BORDER);
                    risksValueCell.setBackgroundColor(LIGHT_GREEN);
                    risksValueCell.setPadding(5);

                    Paragraph risksContent = new Paragraph();
                    risksContent.add(new Phrase(index+". " + risk.getRiskType().getWarning() +"\n\n",
                            FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
                    risksValueCell.addElement(risksContent);
                    mainTable.addCell(risksLabelCell);
                    mainTable.addCell(risksValueCell);
                    document.add(mainTable);
                    document.add(Chunk.NEWLINE);
                }else{
                    PdfPTable mainTable = new PdfPTable(2);
                    mainTable.setWidthPercentage(100);
                    mainTable.setWidths(new float[]{2, 7});

                    PdfPCell risksLabelCell = new PdfPCell();
                    risksLabelCell.setBorder(Rectangle.NO_BORDER);
                    risksLabelCell.setBackgroundColor(LIGHT_GREEN);
                    risksLabelCell.setPadding(5);

                    PdfPCell risksValueCell = new PdfPCell();
                    risksValueCell.setBorder(Rectangle.NO_BORDER);
                    risksValueCell.setBackgroundColor(LIGHT_GREEN);
                    risksValueCell.setPadding(5);

                    Paragraph risksContent = new Paragraph();
                    risksContent.add(new Phrase("\n"+index+". " + risk.getRiskType().getWarning() +"\n\n",
                            FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
                    risksValueCell.addElement(risksContent);
                    mainTable.addCell(risksLabelCell);
                    mainTable.addCell(risksValueCell);
                    document.add(mainTable);
                    document.add(Chunk.NEWLINE);
                }
            }
            PdfPTable maiTable = new PdfPTable(2);
            maiTable.setWidthPercentage(100);
            maiTable.setWidths(new float[]{2, 7});
            PdfPCell risksLabelCell = new PdfPCell();
            risksLabelCell.setBorder(Rectangle.NO_BORDER);
            risksLabelCell.setBackgroundColor(LIGHT_GREEN);
            risksLabelCell.setPadding(5);

            PdfPCell risksValueCell = new PdfPCell();
            risksValueCell.setBorder(Rectangle.NO_BORDER);
            risksValueCell.setBackgroundColor(LIGHT_GREEN);
            risksValueCell.setPadding(5);

            Paragraph Content = new Paragraph();
            Content.add(new Phrase("   Risk "+(++subindex)+":" + risk.getRiskType().getContent(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
            Content.add(Chunk.NEWLINE); // 风险之间的间距
            risksValueCell.addElement(Content);

            maiTable.addCell(risksLabelCell);
            maiTable.addCell(risksValueCell);
            document.add(maiTable);
            document.add(Chunk.NEWLINE);

//开始列risk treatment history
            List<RiskRelationship> history = riskRelationshipRepository.findByAssetIdAndRiskTypeAndValidIn(
                    assetId, risk.getRiskType().getTypeID(), List.of(0, 2));
            // 创建横向表格（表头在最上，每行一个风险处理数据）
            PdfPTable treatmentTable = new PdfPTable(6); // 6列：序号、处理日期、处理人、风险等级、处理选项、证据和评论
            treatmentTable.setWidthPercentage(90);
            treatmentTable.setWidths(new float[]{1.5f,1f, 1f, 2f, 2f, 2f}); // 设置列宽比例

            // 设置表头样式
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.DARK_GRAY);
//            BaseColor headerBgColor = new BaseColor(70, 130, 180); // 钢蓝色背景

            // 添加表头
            addHeaderCell(treatmentTable, "Treatment Date", headerFont, TAG_GRAY);
            addHeaderCell(treatmentTable, "Treated By", headerFont, TAG_GRAY);
            addHeaderCell(treatmentTable, "Risk Level", headerFont, TAG_GRAY);
            addHeaderCell(treatmentTable, "Treatment Option", headerFont, TAG_GRAY);
            addHeaderCell(treatmentTable, "Evidence", headerFont, TAG_GRAY);
            addHeaderCell(treatmentTable, "Comments", headerFont, TAG_GRAY);

            boolean addhistory = false;
            for(RiskRelationship r : history){
                if(r.getRiskTreatment()!=null && r.getRiskTreatment().getValid()==1){
                    addhistory = true;
                    RiskTreatment treatment = r.getRiskTreatment();
                    // 处理日期
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String treatmentDate = treatment.getUpdateDate() != null ?
                            dateFormat.format(treatment.getUpdateDate()) : "N/A";
                    treatmentTable.addCell(createDataCell(treatmentDate, BaseColor.WHITE));

                    // 处理人
                    String treatedBy = r.getRiskOwner().getAssetUserName() != null ?
                            r.getRiskOwner().getAssetUserName() : "N/A";
                    treatmentTable.addCell(createDataCell(treatedBy, BaseColor.WHITE));

                    // 风险等级
                    treatmentTable.addCell(createDataCell(getRiskLevelName(treatment.getRiskLevel()), BaseColor.WHITE));

                    // 处理选项
                    treatmentTable.addCell(createDataCell(getTreatmentOptionName(treatment.getTreatmentOption()), BaseColor.WHITE));

                    // 证据
                    Paragraph linkParagraph = new Paragraph();
                    if(!treatment.getEvidenceFiles().isEmpty()){
                        for(Files f : treatment.getEvidenceFiles()){
                            String linkText = f.getOriginalName()+"\n";
                            Anchor link = new Anchor(linkText,
                                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLUE));
                            link.setReference(f.getFilePath());
                            linkParagraph.add(link);
                        }
                    }
                    PdfPCell cell = new PdfPCell();
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setBorder(Rectangle.BOX);
                    cell.setPadding(5);
                    cell.addElement(linkParagraph);
                    treatmentTable.addCell(cell);

                    //评论
                    treatmentTable.addCell(createDataCell(treatment.getComments(), BaseColor.WHITE));
                }
            }
            if(addhistory){
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100);
                mainTable.setWidths(new float[]{2, 7});

                PdfPCell LabelCell = new PdfPCell();
                LabelCell.setBorder(Rectangle.NO_BORDER);
                LabelCell.setBackgroundColor(LIGHT_GREEN);
                LabelCell.setPadding(5);

                PdfPCell ValueCell = new PdfPCell();
                ValueCell.setBorder(Rectangle.NO_BORDER);
                ValueCell.setBackgroundColor(LIGHT_GREEN);
                ValueCell.setPadding(5);

                Paragraph risksContent = new Paragraph();
                risksContent.add(treatmentTable);
                risksContent.add(Chunk.NEWLINE);
                ValueCell.addElement(risksContent);

                mainTable.addCell(LabelCell);
                mainTable.addCell(ValueCell);
                document.add(mainTable);
                document.add(Chunk.NEWLINE);
            }

        }
    }

    // 辅助方法：创建表头单元格
    private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    // 辅助方法：创建数据单元格
    private PdfPCell createDataCell(String text, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text,
                FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK)));
        cell.setBackgroundColor(bgColor);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        return cell;
    }

    private void addStyledTableRow(PdfPTable table, String label, String value, BaseColor bgColor) {
        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);

        // Value cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBackgroundColor(bgColor);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
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

    // Rest of the helper methods remain the same...
    private String savePdfToStorage(byte[] pdfContent, Integer assetId) throws IOException {
        Path storagePath = Paths.get(pdfStorageDir);
        if (!java.nio.file.Files.exists(storagePath)) {
            java.nio.file.Files.createDirectories(storagePath);
        }

        Path evidenceChainDir = storagePath.resolve("evidence_chain");
        if (!java.nio.file.Files.exists(evidenceChainDir)) {
            java.nio.file.Files.createDirectories(evidenceChainDir);
        }

        String filename = UUID.randomUUID() + ".pdf";
        Path filePath = evidenceChainDir.resolve(filename);
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

        evidenceChain.setSnapshot("http://localhost:8081/files/pdf-storage/evidence_chain/" + filename);

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