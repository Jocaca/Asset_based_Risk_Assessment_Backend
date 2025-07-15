package com.example.assetbasedriskassessmentplatformbackend.config;

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
public class QuestionnairePdfGenerator {

    @Value("${pdf.storage-dir}")
    private String pdfStorageDir;

    // Colors for styling
    private static final BaseColor TAG_GREEN = new BaseColor(50, 205, 50);
    private static final BaseColor TAG_RED = new BaseColor(220, 20, 60);
    private static final BaseColor TAG_ORANGE = new BaseColor(255, 165, 0);
    private static final BaseColor TAG_GRAY = new BaseColor(169, 169, 169);

    public String generateQuestionnairePdf(Questionnaire questionnaire) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        addTitle(document, questionnaire);
//        addBasicInfo(document, questionnaire);
        addQuestionnaireContent(document, questionnaire);

        document.close();

        return savePdf(baos.toByteArray());
    }

    private void addTitle(Document document, Questionnaire questionnaire) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Questionnaire for " + questionnaire.getAsset().getAssetName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
    }

//    private void addBasicInfo(Document document, Questionnaire questionnaire) throws DocumentException {
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//        table.setWidths(new float[]{1, 3});
//
//        AssetsBasicInfo asset = questionnaire.getAsset();
//        addTableRow(table, "Asset Type:", Converters.assetType(asset.getAssetType()));
//        addTableRow(table, "Asset Owner:", asset.getAssetOwner() != null ? asset.getAssetOwner().getAssetUserName() : "");
//        addTableRow(table, "Asset ID:", asset.getSwid());
//        addStyledStatusRow(table, "Questionnaire Status:",
//                asset.getQStatus() == 0 ? "In Progress" : "Completed",
//                BaseColor.WHITE);
//
//        document.add(table);
//        document.add(Chunk.NEWLINE);
//    }

    private void addQuestionnaireContent(Document document, Questionnaire questionnaire) throws DocumentException {
        AssetsBasicInfo asset = questionnaire.getAsset();

        // Add type-specific questions section
//        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
//        Paragraph sectionTitle = new Paragraph("Questionnaire Responses", sectionFont);
//        sectionTitle.setSpacingAfter(10);
//        document.add(sectionTitle);

        // Handle different asset types
        switch (asset.getAssetType()) {
            case 0: // Software
                addSoftwareQuestions(document, questionnaire);
                break;
            case 1: // Physical
                addPhysicalQuestions(document, questionnaire);
                break;
            case 2: // Information
                addInformationQuestions(document, questionnaire);
                break;
            case 3: // People
                addPeopleQuestions(document, questionnaire);
                break;
        }
    }

    private void addSoftwareQuestions(Document document, Questionnaire questionnaire) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7, 3});

        // Software specific questions
        addQuestionRow(table, "1. Is the software authorized by the vendor?", questionnaire.getQ1());
        if ("No".equals(questionnaire.getQ1())) {
            addQuestionRow(table, "1.1 Is the software's status correctly updated in the inventory?", questionnaire.getQ2());
        }
        addQuestionRow(table, "2. Is the software explicitly whitelisted?", questionnaire.getQ3());
        addQuestionRow(table, "3. Is the software automatically scanned for vulnerabilities?", questionnaire.getQ4());
        addQuestionRow(table, "4. Is the software regularly scanned for vulnerabilities?", questionnaire.getQ5());
        addQuestionRow(table, "5. Is the software automatically updated?", questionnaire.getQ6());
        addQuestionRow(table, "6. Are vulnerability scan results compared?", questionnaire.getQ7());
        addQuestionRow(table, "7. Are security configuration standards maintained?", questionnaire.getQ8());
        addQuestionRow(table, "8. Are secure images or templates maintained?", questionnaire.getQ9());
        addQuestionRow(table, "9. Are master images stored securely?", questionnaire.getQ10());
        addQuestionRow(table, "10. Are system configuration tools deployed?", questionnaire.getQ11());
        addQuestionRow(table, "11. Is SCAP-compliant monitoring used?", questionnaire.getQ12());
        addQuestionRow(table, "12. Is the software an email or web browser?", questionnaire.getQ13());

        if ("Yes".equals(questionnaire.getQ13())) {
            addQuestionRow(table, "12.1 Are unauthorized plugins/add-ons disabled?", questionnaire.getQ14());
            addQuestionRow(table, "12.2 Are unauthorized scripting languages blocked?", questionnaire.getQ15());
        }

        addQuestionRow(table, "13. Is the software in-house developed?", questionnaire.getQ16());

        if ("Yes".equals(questionnaire.getQ16())) {
            addQuestionRow(table, "13.1 Are secure coding practices enforced?", questionnaire.getQ17());
            addQuestionRow(table, "13.2 Is explicit error checking performed?", questionnaire.getQ18());
            addQuestionRow(table, "13.3 Are trusted third-party components used?", questionnaire.getQ19());
            addQuestionRow(table, "13.4 Are standardized encryption algorithms used?", questionnaire.getQ20());
            addQuestionRow(table, "13.5 Do developers receive secure coding training?", questionnaire.getQ21());
            addQuestionRow(table, "13.6 Are SAST and DAST tools used?", questionnaire.getQ22());
            addQuestionRow(table, "13.7 Are production and non-production environments segregated?", questionnaire.getQ23());
        }

        addQuestionRow(table, "14. Are third-party components verified?", questionnaire.getQ24());
        addQuestionRow(table, "15. Is WAF or other firewall in place?", questionnaire.getQ25());

        document.add(table);
    }

    private void addPhysicalQuestions(Document document, Questionnaire questionnaire) throws DocumentException {
        AssetsBasicInfo asset = questionnaire.getAsset();
        if (!(asset instanceof AssetsPhysical)) return;

        AssetsPhysical physicalAsset = (AssetsPhysical) asset;
        boolean isFixedAsset = physicalAsset.getFixedPhysicalAsset() == 0;

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7, 3});

        if (isFixedAsset) {
            // Fixed asset specific questions
            addQuestionRow(table, "1. Are assets fully documented in the inventory system?", questionnaire.getQ1());
            if ("No".equals(questionnaire.getQ1())) {
                addQuestionRow(table, "1.1 Do the missing fields contain key information?", questionnaire.getQ2());
            }

            addQuestionRow(table, "2. Is the asset performing maintenance as planned?", questionnaire.getQ3());
            if ("No".equals(questionnaire.getQ3()) || "AVERAGE".equals(questionnaire.getQ3())) {
                addQuestionRow(table, "2.1 What are the reasons for overdue maintenance?", questionnaire.getQ4());
            }

            addQuestionRow(table, "3. Is the associated software/firmware licensed?", questionnaire.getQ5());
            if ("No".equals(questionnaire.getQ5())) {
                addQuestionRow(table, "3.1 Is there a corrective action plan in place?", questionnaire.getQ6());
            }

            addQuestionRow(table, "4. Does the current risk level match the actual one?", questionnaire.getQ7());

            addQuestionRow(table, "5. Is the physical storage location secure?", questionnaire.getQ8());
            if ("No".equals(questionnaire.getQ8())) {
                addQuestionRow(table, "5.1 Do you store sensitive data or high-value equipment?", questionnaire.getQ9());
            }
        } else {
            // Non-fixed asset specific questions
            addQuestionRow(table, "1. Is data encryption enabled on the device?", questionnaire.getQ1());
            if ("No".equals(questionnaire.getQ1())) {
                addQuestionRow(table, "1.1 Does the device store sensitive data?", questionnaire.getQ2());
            }

            addQuestionRow(table, "2. Does remote wipe support exist?", questionnaire.getQ3());
            if ("No".equals(questionnaire.getQ3())) {
                addQuestionRow(table, "2.1 Is the device type easy to lose?", questionnaire.getQ4());
            }

            addQuestionRow(table, "3. Is the current holder information accurate?", questionnaire.getQ5());
            if ("No".equals(questionnaire.getQ5())) {
                addQuestionRow(table, "3.1 Is it more than 30 days after the deadline?", questionnaire.getQ6());
            }

            addQuestionRow(table, "4. Is the device in good physical condition?", questionnaire.getQ7());
            if ("No".equals(questionnaire.getQ7()) || "Minor damage".equals(questionnaire.getQ7())) {
                addQuestionRow(table, "4.1 Does the damage affect functionality?", questionnaire.getQ8());
            }
        }

        document.add(table);
    }

    private void addInformationQuestions(Document document, Questionnaire questionnaire) throws DocumentException {
        AssetsInformation infoAsset = (AssetsInformation) questionnaire.getAsset();

        // Create a table with question and answer columns
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7, 3});

        // Handle different information asset categories
        switch (infoAsset.getAssetCategory()) {
            case 0: // Database
                addDatabaseQuestions(table, questionnaire);
                break;
            case 1: // Document
                addDocumentQuestions(table, questionnaire);
                break;
            case 2: // Patent
                addPatentQuestions(table, questionnaire);
                break;
        }

        document.add(table);
    }

    private void addDatabaseQuestions(PdfPTable table, Questionnaire questionnaire) {
        addQuestionRow(table, "1. Is data encryption (AES-256+) applied to sensitive fields?", questionnaire.getQ1());
        addQuestionRow(table, "2. Are database access logs retained for ≥90 days?", questionnaire.getQ2());
        addQuestionRow(table, "3. Is multi-factor authentication (MFA) required for admin access?", questionnaire.getQ3());
        addQuestionRow(table, "4. Are SQL injection protections implemented?", questionnaire.getQ4());
        addQuestionRow(table, "5. Is database patching automated with SLA?", questionnaire.getQ5());
        addQuestionRow(table, "6. Are backups tested annually for restore capability?", questionnaire.getQ6());
        addQuestionRow(table, "7. Is data anonymization used for PII in non-production environments?", questionnaire.getQ7());
        addQuestionRow(table, "8. Are database replicas geographically isolated?", questionnaire.getQ8());
        addQuestionRow(table, "9. Is vendor risk assessed for cloud databases?", questionnaire.getQ9());
        addQuestionRow(table, "10. Are database schemas documented with data lineage?", questionnaire.getQ10());
        addQuestionRow(table, "11. Is sensitive data masked in test environments?", questionnaire.getQ11());
        addQuestionRow(table, "12. Are failed login attempts locked after 5 tries?", questionnaire.getQ12());
    }

    private void addDocumentQuestions(PdfPTable table, Questionnaire questionnaire) {
        addQuestionRow(table, "1. Are documents watermarked with classification labels?", questionnaire.getQ1());
        addQuestionRow(table, "2. Is OCR disabled for sensitive scanned documents?", questionnaire.getQ2());
        addQuestionRow(table, "3. Do shared links expire after 7 days by default?", questionnaire.getQ3());
        addQuestionRow(table, "4. Is blockchain timestamping used for legal documents?", questionnaire.getQ4());
        addQuestionRow(table, "5. Are printed documents tracked via barcodes?", questionnaire.getQ5());
        addQuestionRow(table, "6. Are retention policies automated (e.g., auto-delete after 5 years)?", questionnaire.getQ6());
        addQuestionRow(table, "7. Are document access requests logged (who accessed/when)?", questionnaire.getQ7());
        addQuestionRow(table, "8. Are email attachments with sensitive docs password-protected?", questionnaire.getQ8());
        addQuestionRow(table, "9. Is version history preserved for all edits?", questionnaire.getQ9());
        addQuestionRow(table, "10. Are document templates stored in a centralized library?", questionnaire.getQ10());
        addQuestionRow(table, "11. Is staff trained annually on document handling?", questionnaire.getQ11());
    }

    private void addPatentQuestions(PdfPTable table, Questionnaire questionnaire) {
        addQuestionRow(table, "1. Are patent applications filed under PCT for international protection?", questionnaire.getQ1());
        addQuestionRow(table, "2. Is a patent troll monitoring service employed?", questionnaire.getQ2());
        addQuestionRow(table, "3. Are invention disclosures encrypted pre-filing?", questionnaire.getQ3());
        addQuestionRow(table, "4. Are patent maintenance fees tracked with reminders?", questionnaire.getQ4());
        addQuestionRow(table, "5. Are employees required to sign intellectual Property assignment agreements?", questionnaire.getQ5());
        addQuestionRow(table, "6. Are patent claims reviewed by external counsel?", questionnaire.getQ6());
        addQuestionRow(table, "7. Is there a process for reporting potential infringements?", questionnaire.getQ7());
        addQuestionRow(table, "8. Are patent filing dates validated for priority claims?", questionnaire.getQ8());
        addQuestionRow(table, "9. Are patent assets listed in an IP portfolio database?", questionnaire.getQ9());
        addQuestionRow(table, "10. Are joint ownership terms clearly defined?", questionnaire.getQ10());
        addQuestionRow(table, "11. Are foreign filing licenses obtained when required?", questionnaire.getQ11());
    }

    private void addPeopleQuestions(Document document, Questionnaire questionnaire) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{7, 3});

        // People asset specific questions
        addQuestionRow(table, "1. Is the employee's full name documented in the HR system?", questionnaire.getQ1());
        addQuestionRow(table, "2. Are employee unique identifiers (e.g., Employee ID) properly documented and linked to their role?", questionnaire.getQ2());
        addQuestionRow(table, "3. Is the employee's department recorded in the system?", questionnaire.getQ3());
        addQuestionRow(table, "4. Is the employee's position or role clearly defined in the system?", questionnaire.getQ4());
        addQuestionRow(table, "5. Is the employee's position linked to their access permissions and security clearance?", questionnaire.getQ5());
        addQuestionRow(table, "6. Has the employee undergone security training relevant to their role?", questionnaire.getQ6());
        addQuestionRow(table, "7. Is the employee's security clearance documented and up-to-date?", questionnaire.getQ7());
        addQuestionRow(table, "8. Is there a primary list of assets assigned to the employee?", questionnaire.getQ8());
        addQuestionRow(table, "9. Is the purpose of each asset assigned to the employee documented?", questionnaire.getQ9());
        addQuestionRow(table, "10. Is the employee's responsibility for the assets clearly defined in the system?", questionnaire.getQ10());
        addQuestionRow(table, "11. Is the employee's remote work agreement status documented and up-to-date?", questionnaire.getQ11());
        addQuestionRow(table, "12. Has the employee's access and asset data been audited in the last 12 months?", questionnaire.getQ12());
        addQuestionRow(table, "13. Is the employee's security incident record accurately maintained?", questionnaire.getQ13());
        addQuestionRow(table, "14. Are security incidents related to the employee regularly reviewed and analyzed?", questionnaire.getQ14());
        addQuestionRow(table, "15. Is the employee's device and information access regularly reviewed and adjusted as necessary?", questionnaire.getQ15());
        addQuestionRow(table, "16. Is an approver defined for granting or modifying the employee's system access permissions?", questionnaire.getQ16());
        addQuestionRow(table, "17. Does the employee regularly participate in security drills or simulated attack exercises?", questionnaire.getQ17());
        addQuestionRow(table, "18. Is the employee fully aware of potential social engineering attacks and have strategies to respond?", questionnaire.getQ18());
        addQuestionRow(table, "19. Has the employee's access and asset data been updated promptly following role changes?", questionnaire.getQ19());
        addQuestionRow(table, "20. Is the employee's workplace recorded and aligned with their role and access permissions?", questionnaire.getQ20());

        document.add(table);
    }

    private void addQuestionRow(PdfPTable table, String question, String answer) {
        PdfPCell questionCell = new PdfPCell(new Phrase(question,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        questionCell.setBorder(Rectangle.NO_BORDER);
        questionCell.setPadding(5);

        PdfPCell answerCell = new PdfPCell(new Phrase(answer != null ? answer : "Not answered",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        answerCell.setBorder(Rectangle.NO_BORDER);
        answerCell.setPadding(5);

        table.addCell(questionCell);
        table.addCell(answerCell);
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

    private void addStyledStatusRow(PdfPTable table, String label, String value, BaseColor bgColor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setBackgroundColor(bgColor);
        labelCell.setPadding(5);

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
            case "completed":
            case "finished":
                return TAG_GREEN;
            case "in progress":
                return TAG_ORANGE;
            case "decommissioned":
            default:
                return TAG_GRAY;
        }
    }

    private String savePdf(byte[] pdfContent) throws IOException {
        Path storagePath = Paths.get(pdfStorageDir);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        Path questionnaireDir = storagePath.resolve("questionnaires");
        if (!Files.exists(questionnaireDir)) {
            Files.createDirectories(questionnaireDir);
        }

        String filename = UUID.randomUUID() + ".pdf";
        Path filePath = questionnaireDir.resolve(filename);
        Files.write(filePath, pdfContent);

        return filename;
    }
}
