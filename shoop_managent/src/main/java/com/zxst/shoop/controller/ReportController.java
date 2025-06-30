package com.zxst.shoop.controller;

import com.zxst.shoop.service.ReportService;
import com.zxst.shoop.util.JsonResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    //平台会员统计折线图
    @RequestMapping("/getCustomerReport")
    public JsonResult getCustomerReport() {
        return  reportService.getCustomerReport();
    }

    //统计销售商品数量饼状图
    @RequestMapping("/getOrderReport")
    public JsonResult getOrderReport() {
        return  reportService.getOrderReport();
    }

    //统计运营数据
    @RequestMapping("/getBusinessReport")
    public JsonResult getBusinessReport() {
        return  reportService.getBusinessReport();
    }

    //导出运营数据到excel中
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletResponse response) {
        //获取表格中的运营数据
        Map<String,Object> data = (Map<String,Object>)reportService.getBusinessReport().getData();
        String  path = "E:\\report_template.xlsx";
        try {
            File  file = new File(path);
            //通过文件构建了一个XSSFWorkbook文档对象
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //通过文档对象获取 sheet页对象
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row0 = sheet.getRow(2);
            XSSFCell cell0 = row0.getCell(5);
            cell0.setCellValue(data.get("reportDate").toString());


            XSSFRow row1 = sheet.getRow(4);
            XSSFCell cell1 = row1.getCell(5);
            XSSFCell cell2 = row1.getCell(7);
            cell1.setCellValue(data.get("todayNewMember").toString());
            cell2.setCellValue(data.get("totalMember").toString());

            XSSFRow row2 = sheet.getRow(5);
            XSSFCell cell3 = row2.getCell(5);
            XSSFCell cell4 = row2.getCell(7);
            cell3.setCellValue(data.get("thisWeekNewMember").toString());
            cell4.setCellValue(data.get("thisMonthNewMember").toString());


            XSSFRow row3 = sheet.getRow(7);
            XSSFCell cell5 = row3.getCell(5);
            XSSFCell cell6 = row3.getCell(7);
            cell5.setCellValue(data.get("todayOrderNumber").toString());
            cell6.setCellValue(data.get("todayVisitsNumber").toString());

            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>)data.get("hotSetmeal");
            int mark = 10;
            for (Map<String, Object> stringObjectMap : hotSetmeal) {
                XSSFRow row4 = sheet.getRow(mark);
                XSSFCell cell7 = row4.getCell(4);
                cell7.setCellValue(stringObjectMap.get("name").toString());
                XSSFCell cell8 = row4.getCell(5);
                cell8.setCellValue(stringObjectMap.get("setmeal_count").toString());
                XSSFCell cell9 = row4.getCell(6);
                cell9.setCellValue(stringObjectMap.get("proportion").toString());
                XSSFCell cell10 = row4.getCell(7);
                cell10.setCellValue(stringObjectMap.get("sell_point").toString());
                mark ++;
            }
            //响应输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //设定响应数据的头信息 响应的数据格式是excel文件
            response.setContentType("application/vnd.ms-excel");
            //响应头中设定输出文件名称
            response.setHeader("Content-Disposition", "attachment; filename=business.xlsx");
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
