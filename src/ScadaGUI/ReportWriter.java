package ScadaGUI;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class ReportWriter {

//    public static void writeReportToFile(ArrayList report) throws IOException {
//        FileWriter fileReport = new FileWriter("votes.csv");
//        fileReport.write("\"idVote\";\"vote\";\"date\";\"time\"\n");
//        for (int i = 0; i < report.size(); i++) {
//            fileReport.write(report.get(i).toString() + "\n");
//        }
//
//        fileReport.close();
//
//    }

//    public static void writeVotesByDate(ArrayList reportByDate) throws IOException {
//        FileWriter fileReport = new FileWriter("C:\\votesByDate.csv");
//        fileReport.write("\"Дата\";\"Позитивных нажатий\";\"Грустных нажатий\"\n");
//        for (int i = 0; i < reportByDate.size(); i++) {
//            fileReport.write(reportByDate.get(i).toString() + "\n");
//        }
//        fileReport.close();
//
//    }

    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static void writeReportToXLS() throws IOException, InterruptedException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Отчет по работе оборудования");
        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("№");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Наименование");
        cell.setCellStyle(style);
// Salary
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Дата начала сессии");
        cell.setCellStyle(style);
        // Salary
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Время");
        cell.setCellStyle(style);
        // Длительность
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Длительность");
        cell.setCellStyle(style);
        // Data
        Iterator<MachineSessions> iter = FXMLDocumentController.tableFills.iterator();
        int ID = 0;
        while (iter.hasNext()) {
            rownum++;
            MachineSessions mach = iter.next();
            row = sheet.createRow(rownum);

            // ID (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(++ID);
            // MachName (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(mach.getName());
            // Start Date (C)
            cell = row.createCell(2, CellType.NUMERIC);
            cell.setCellValue(mach.getDateStart());
            // Start Time (D)
            cell = row.createCell(3, CellType.NUMERIC);
            cell.setCellValue(mach.getTimeStart());
            // UpTime0 (E)
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(mach.getSessionLong());

        }
        File file = new File(".\\report.xls");
        file.getParentFile().mkdirs();

        FileOutputStream outFile = new FileOutputStream(file);
        workbook.write(outFile);
        System.out.println("Created file: " + file.getAbsolutePath());
        outFile.close();
        execOutput();
    }

    public static void writeLOGToFile(String status) throws IOException {
        FileWriter fileReport = new FileWriter("workflow.log");
        fileReport.append(status);

        fileReport.close();

    }

    public static void execOutput() throws IOException {
        Desktop.getDesktop().open(new File(".\\report.xls"));
    }

    public static void appendLogToFile(String status) throws IOException {
        String filePath = "myfile.txt";
        String text = status;

        try {
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(text);
            bufferWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
