package com.lcs.util;

import com.lcs.exception.GlobleException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel解析工具类
 *
 * @author liuns 2017年5月8日上午8:49:59
 */
public class ExcelUtils<T> {

    /**
     * Excel03正则
     */
    private static final String REG_EXCEL_03 = "^.+\\.(?i)(xls)$";
    /**
     * Excel07正则
     */
    private static final String REG_EXCEL_07 = "^.+\\.(?i)(xlsx)$";

    /**
     * 根据文件名获取Excle类型
     *
     * @param fileName
     * @return
     */
    public static ExcelType getExcelType(String fileName) {
        if (StringUtils.isEmpty(fileName))
            return ExcelType.UNKNOWN;
        if (fileName.matches(REG_EXCEL_03))
            return ExcelType.EXCEL03;
        if (fileName.matches(REG_EXCEL_07))
            return ExcelType.EXCEL07;
        return ExcelType.UNKNOWN;
    }

    /**
     * 校验Excel 中Cell中的值是不是NULL
     *
     * @return
     */
    public static boolean checkCellValue(Cell cell) {
        if (null == cell)
            return true;
        if (cell.getCellType() == CellType.STRING)
            return StringUtils.isEmpty(cell.getStringCellValue().trim());
        return cell.getCellType() == CellType.BLANK;
    }

    /**
     * 根据模版生成对象集合 模版要求： 第一行为分类信息（可为空行，但必须有） 第二行为反射字段，与对象属性一一对应
     * 第三行为描述字段，带*号的就是必填字段
     *
     * @param multipartFile 模版
     * @param clazz         对象类型
     * @param sheetIndex    对象集合在模版中的sheet的Index
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<T> getDataByExcelModel(MultipartFile multipartFile, Class<T> clazz, int sheetIndex)
            throws IOException, IllegalAccessException, InstantiationException {
        if (null == multipartFile)
            throw new GlobleException("未上传文件");
        ExcelType excelType = getExcelType(multipartFile.getOriginalFilename());
        if (excelType == ExcelType.UNKNOWN)
            throw new GlobleException("未识别的Excel文件");
        if (multipartFile.getSize() == 0)
            throw new GlobleException("上传文件为空");
        Workbook workbook = null;
        if (excelType == ExcelType.EXCEL03)
            workbook = new HSSFWorkbook(multipartFile.getInputStream());
        else
            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        return getDataByExcelSheet(sheet, clazz);
    }

    /**
     * 根据模版生成对象集合 模版要求： 第一行为分类信息（可为空行，但必须有） 第二行为反射字段，与对象属性一一对应
     * 第三行为描述字段，带*号的就是必填字段
     *
     * @param multipartFile 模版
     * @param clazz         对象类型
     * @param sheetName     对象集合在模版中的sheet的名字
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public List<T> getDataByExcelModel(MultipartFile multipartFile, Class<T> clazz, String sheetName)
            throws IOException, IllegalAccessException, InstantiationException {
        if (null == multipartFile)
            throw new GlobleException("未上传文件");
        ExcelType excelType = getExcelType(multipartFile.getOriginalFilename());
        if (excelType == ExcelType.UNKNOWN)
            throw new GlobleException("未识别的Excel文件");
        if (multipartFile.getSize() == 0)
            throw new GlobleException("上传文件为空");
        Workbook workbook = null;
        if (excelType == ExcelType.EXCEL03)
            workbook = new HSSFWorkbook(multipartFile.getInputStream());
        else
            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheet(sheetName);
        return getDataByExcelSheet(sheet, clazz);
    }

    /**
     * 根据excel sheet 生成数据
     *
     */
    public List<T> getDataByExcelSheet(Sheet sheet, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        if (null == sheet)
            throw new GlobleException("未找到sheet");
        int rows = sheet.getPhysicalNumberOfRows();
        if (rows < 4)
            throw new GlobleException("未找到要导入的数据");
        // 获取反射行
        Row propertyRow = sheet.getRow(1);
        Map<Integer, String> propertyMap = new HashMap<Integer, String>();

        for (int i = 0; i < propertyRow.getPhysicalNumberOfCells(); i++) {
            if (ExcelUtils.checkCellValue(propertyRow.getCell(i)))
                throw new GlobleException(String.format("第%d列未找到反射字段", i));
            propertyMap.put(i, propertyRow.getCell(i).getStringCellValue());
        }

        Row titleRow = sheet.getRow(2);
        // 验证那一列的值不允许为空！判断条件就是标题列中带有“*”号就不允许为空
        Map<Integer, Boolean> allowNullMap = new HashMap<Integer, Boolean>();
        for (int i = 0; i < titleRow.getPhysicalNumberOfCells(); i++) {
            allowNullMap.put(i, !ExcelUtils.checkCellValue(titleRow.getCell(i))
                    && titleRow.getCell(i).getStringCellValue().indexOf("*") > 0);
        }
        List<T> reList = new ArrayList<>();
        for (int i = 3; i < rows; i++) {
            Row row = sheet.getRow(i);
            // 第一列必填！EXCEL空行
            if (null == row || checkExcelRowIsNull(row))
                continue;
            T value = clazz.newInstance();
            // 车辆
            for (int c = 0; c < propertyRow.getPhysicalNumberOfCells(); c++) {
                Cell cell = row.getCell(c);
                if (ExcelUtils.checkCellValue(cell)) {
                    if (allowNullMap.get(c))
                        throw new GlobleException(
                                String.format("导入异常:第%d行%s不能为空", i, titleRow.getCell(c).getStringCellValue()));
                } else {
                    String property = propertyMap.get(c);
                    Field field = FieldUtils.getField(clazz, property, true);
                    if (null == field)
                        throw new GlobleException("导入异常:未找到属性" + propertyMap.get(c));
                    if (field.getType().equals(Date.class)) {
                        FieldUtils.writeField(value, property, cell.getDateCellValue(), true);
                    } /*else if (field.getType().equals(Integer.class) || field.getType().equals(Short.class)
							|| field.getType().equals(Byte.class)) {
						FieldUtils.writeField(value, property,
								ConvertUtils.convert(cell.getNumericCellValue(), field.getType()), true);
					}*/ else {

                        cell.setCellType(CellType.STRING);
                        FieldUtils.writeField(value, property,
                                ConvertUtils.convert(cell.getStringCellValue(), field.getType()), true);
                    }
                }
            }
            reList.add(value);
        }
        return reList;
    }


    /**
     * 检查Excel行是不是所有列都是null,Excel空行比较难判断
     *
     * @param row
     * @return
     */
    public static boolean checkExcelRowIsNull(Row row) {
        for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
            if (!checkCellValue(row.getCell(c)))
                return false;
        }
        return true;
    }
}
