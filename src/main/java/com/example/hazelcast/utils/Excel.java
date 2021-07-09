package com.example.hazelcast.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author w97766
 * @date 2021/6/10
 */
public class Excel {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        String fileClassPath = "设备标准化数据字典表-TAF.xlsx";
        InputStream in = new ClassPathResource(fileClassPath).getInputStream();

        Workbook workbook = WorkbookFactory.create(in);
        Sheet sheet = workbook.getSheet("SENSOR_TEMPERATURE");
        List<String> list = new ArrayList<>();
        System.out.println(sheet.getLastRowNum());
        for (int i = 3; i < sheet.getLastRowNum(); i++) {
            if (i == 24) {
                System.out.println(i);
            }
            Row row = sheet.getRow(i);
            String gdd = row.getCell(6).getStringCellValue();
            String componentIdentifier = row.getCell(7).getStringCellValue();
            String cid = componentIdentifier+":"+gdd;
            if (!list.contains(cid)) {
                list.add(cid);
            }else {
                System.out.println(cid);
            }
        }
        System.out.println("done");
    }
}
