package top.dooper.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import top.dooper.common.vo.RankVo;
import top.dooper.sys.entity.Rank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    public static ResponseEntity<byte[]> createExcelFile(List<RankVo> dataList) {
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建一个工作表
            Sheet sheet = workbook.createSheet("Sheet1");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "作品ID", "学号", "姓名", "分数", "排名"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // 写入数据
            int rowNum = 1;
            for (RankVo entity : dataList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(entity.getId());
                row.createCell(1).setCellValue(entity.getWork_id());
                row.createCell(2).setCellValue(entity.getSid());
                row.createCell(3).setCellValue(entity.getUsername());
                row.createCell(4).setCellValue(entity.getScore());
                row.createCell(5).setCellValue(entity.getRanking());
            }

            // 将 Excel 数据写入 ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            // 准备 ResponseEntity
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "成绩排名.xlsx"); // 设置下载文件的文件名

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (IOException e) {
            // 异常处理
            e.printStackTrace();
            // 返回空的 ResponseEntity 或者其他错误信息
            return ResponseEntity.badRequest().build();
        }
    }
}
