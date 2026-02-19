package com.cabottesting.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelUtils {

    public static List<String> readPrompts(String path) throws Exception {
        List<String> prompts = new ArrayList<>();
        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null) prompts.add(cell.getStringCellValue());
        }

        workbook.close();
        return prompts;
    }
}
