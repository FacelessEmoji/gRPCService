package rut.miit.grpcservice;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DailyReportGenerator.class);

    public void generateReport(String description, String timestamp) {
        String companyInfo = "Welcome to RepairPro! We provide top-notch repair services for all your needs. "
                + "Our experienced masters are dedicated to delivering quality service and customer satisfaction.";
        String reportDate = "Report Date: " + LocalDate.now();

        String reportFileName = "DailyReport_" + LocalDate.now() + ".pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Daily Repair Service Report");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 730);
                contentStream.showText(reportDate);
                contentStream.endText();

                addTextWithWrap(contentStream, "About Us: " + companyInfo, PDType1Font.HELVETICA, 12, 50, 700, 500);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(50, 620);
                contentStream.showText("Daily Statistics:");
                contentStream.endText();

                String[] stats = formatStatistics(description);
                float textY = 600;
                for (String stat : stats) {
                    addTextWithWrap(contentStream, stat, PDType1Font.HELVETICA, 12, 50, textY, 500);
                    textY -= 20;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(50, 100);
                contentStream.showText("Generated on: " + timestamp);
                contentStream.endText();
            }

            document.save(reportFileName);
            logger.info("PDF Report Generated: {}", reportFileName);
        } catch (IOException e) {
            logger.error("Failed to generate PDF report: {}", e.getMessage(), e);
        }
    }

    private void addTextWithWrap(PDPageContentStream contentStream, String text, PDType1Font font, float fontSize, float startX, float startY, float maxWidth) throws IOException {
        float leading = fontSize + 2;
        List<String> lines = wrapText(text, font, fontSize, maxWidth);
        contentStream.setLeading(leading);
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, startY);

        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLine();
        }
        contentStream.endText();
    }

    private List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine + (currentLine.isEmpty() ? "" : " ") + word;
            float textWidth = (font.getStringWidth(testLine) / 1000) * fontSize;
            if (textWidth > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append((currentLine.isEmpty() ? "" : " ") + word);
            }
        }
        if (!currentLine.toString().isEmpty()) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    private String[] formatStatistics(String description) {
        String[] lines = description.split(", ");
        return new String[]{
                "The most successful master of the day: " + lines[0].replace("Best Master: ", ""),
                "The least successful master of the day: " + lines[1].replace("Worst Master: ", ""),
                "Total earnings for the day: " + lines[2].replace("Daily Turnover: ", ""),
                "Total completed orders: " + lines[3].replace("Completed Orders: ", ""),
                "Total parts used in repairs: " + lines[4].replace("Total Used Parts: ", ""),
                "Average cost per order: " + lines[5].replace("Average Order Cost: ", "")
        };
    }
}
