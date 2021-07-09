package com.example.hazelcast.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author w97766
 * @date 2021/6/10
 */
public class ExcelUtil {
    public static void main(String[] args) throws IOException {
        String fileClassPath = "设备标准化数据字典表-TAF.xlsx";
        InputStream in = new ClassPathResource(fileClassPath).getInputStream();
        List<String> sheetNameList = Arrays.asList("SENSOR_TEMPERATURE", "POWER_DISTRIBUTOR", "POWER_RAIL", "SENSOR_MOTION");
        XSSFWorkbook workbook = new XSSFWorkbook(in);

        for (String name : sheetNameList) {
            XSSFSheet sheet = workbook.getSheet(name);
            JSONArray jsonArray = new JSONArray();
            String sheetName = sheet.getSheetName().replace("-", "_");

            for (Row row : sheet) {
                try {
                    if (row.getRowNum() > 2 && row.getCell(6) != null && !row.getCell(6).getStringCellValue().equals("")) {
                        Integer signalId = new Double(row.getCell(0).getNumericCellValue()).intValue();
                        String signalType = row.getCell(1).getStringCellValue();
                        String programmaticName = row.getCell(6).getStringCellValue();
                        String componentIdentifier = row.getCell(7).getStringCellValue();

                        if (null != programmaticName && !"".equals(programmaticName)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("deviceCategory", sheetName);
                            jsonObject.put("signalType", signalType);
                            jsonObject.put("signalId", String.valueOf(signalId));
                            jsonObject.put("programmaticName", programmaticName);
                            jsonObject.put("componentIdentifier", componentIdentifier);
                            jsonArray.add(jsonObject);
                        }
                    }
                }catch (Exception e) {
                    System.out.println(e);
                }

            }
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(sheetName + ".json"));
                out.write(jsonArray.toJSONString());
                out.close();
            } catch (IOException ignored) {
            }
        }
        workbook.close();
    }
}
