package com.express.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.express.model.InCash;

public class ExcelUtil {

	/** 
	 * POI : 导出数据，存放于Excel中 
	 * @param os 输出流 (action: OutputStream os = response.getOutputStream();) 
	 * @param employeeInfos 要导出的数据集合 
	 */  
	public static void exportEmployeeByPoi(OutputStream os, List<InCash> inCash) {  
	    try {  
	        //创建Excel工作薄  
	        HSSFWorkbook book = new HSSFWorkbook();
	        //在Excel工作薄中建一张工作表  
	        HSSFSheet sheet = book.createSheet("提现报表");  
	        //设置单元格格式(文本)  
	        //HSSFCellStyle cellStyle = book.createCellStyle();  
	        //第一行为标题行  
	        HSSFRow row = sheet.createRow(0);//创建第一行  
	        HSSFCell cell0 = row.createCell(0);  
	        HSSFCell cell1 = row.createCell(1);  
	        HSSFCell cell2 = row.createCell(2);  
	        HSSFCell cell3 = row.createCell(3);  
	        HSSFCell cell4 = row.createCell(4);  
	        HSSFCell cell5 = row.createCell(5);
	        //定义单元格为字符串类型  
	        cell0.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell3.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell4.setCellType(HSSFCell.CELL_TYPE_STRING);
	        cell5.setCellType(HSSFCell.CELL_TYPE_STRING);
	        //在单元格中输入数据  
	        cell0.setCellValue("姓名");  
	        cell1.setCellValue("手机号");  
	        cell2.setCellValue("提现金额");  
	        cell3.setCellValue("提现方式");  
	        cell4.setCellValue("账号");  
	        cell5.setCellValue("提现日期"); 
	        //循环导出数据到excel中  
	        for(int i = 0; i < inCash.size(); i++) {  
	        	InCash inCash1 = inCash.get(i);  
	            //创建第i行  
	            HSSFRow rowi = sheet.createRow(i + 1);  
	            //在第i行的相应列中加入相应的数据  
	            rowi.createCell(0).setCellValue(inCash1.getName());  
	            rowi.createCell(1).setCellValue(inCash1.getMobile());  
	            rowi.createCell(2).setCellValue(inCash1.getBalance());
	            if(inCash1.getInCashType().equals("WeiXin")){
	            	rowi.createCell(3).setCellValue("微信提现"); 
	            }else if(inCash1.getInCashType().equals("AliPay")){
	            	rowi.createCell(3).setCellValue("支付宝提现"); 
	            }else if(inCash1.getInCashType().equals("Bank")){
	            	rowi.createCell(3).setCellValue("银行卡提现"); 
	            }
	            rowi.createCell(4).setCellValue(inCash1.getAccount());  
	            //对日期的处理  
	            if(inCash1.getDate() != null && !"".equals(inCash1.getDate())){  
	                java.text.DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");  
	                rowi.createCell(5).setCellValue(format1.format(inCash1.getDate()));  
	            }  
	        }  
	        //写入数据  把相应的Excel 工作簿存盘  
	        book.write(os);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}  
}
