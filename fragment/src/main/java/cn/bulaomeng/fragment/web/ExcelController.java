package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.service.FragmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(description = "导出excel")
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private FragmentService fragmentService;
    @Resource
    private ResourceLoader resourceLoader;
    @ApiOperation(value = "excel下载")
    @GetMapping("/downloadExcel")
    public void  downloadExcel(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单,参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("碎片列表");

        //创建表头
        setTitle(workbook, sheet);
        List<Fragment> answers = fragmentService.getListAll();

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (Fragment answer:answers) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(3).setCellValue(answer.getDate().toString());
            row.createCell(0).setCellValue(answer.getName());
            row.createCell(1).setCellValue(answer.getExchange());
            row.createCell(2).setCellValue(answer.getHaveFragment());
            rowNum++;
        }
        //导出的文件名
        String fileName = "不老梦碎片.xls";
        //清空response
        response.reset();
        //设置response的Header
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" +
                URLEncoder.encode(fileName, "UTF-8") + "\"; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));
        //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
    }
    //设置表头
    private void setTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 45*256);
        sheet.setColumnWidth(1, 45*256);
        sheet.setColumnWidth(2, 45*256);
        sheet.setColumnWidth(3, 20*256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("想交换的碎片");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("已有碎片");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("时间");
        cell.setCellStyle(style);
    }

    @PostMapping("/import")
    @ApiOperation(value = "excel导入")
    public String upload(MultipartFile file) {
        if (file==null) {
            new Thread("文件内容为空");
        }
        List<Fragment> list = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            //有多少个sheet
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //获取多少行
                int rows = sheet.getPhysicalNumberOfRows();
                Fragment fragment = null;
                //遍历每一行，注意：第 0 行为标题
                for (int j = 1; j < rows; j++) {
                    fragment = new Fragment();
                    //获得第 j 行
                    HSSFRow row = sheet.getRow(j);
                    fragment.setName(row.getCell(1).getStringCellValue());
                    fragment.setHaveFragment(row.getCell(2).getStringCellValue());
                    fragment.setExchange(row.getCell(3).getStringCellValue());
                    fragment.setDate(Timestamp.valueOf(row.getCell(4).getStringCellValue()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation("下载模板")
    @GetMapping("/downloadModel")
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request) {
        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            String  fileName = "模板.xls";
            String  path = "download/model.xls";
            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:"+path);

            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    URLEncoder.encode(fileName, "UTF-8") + "\"; filename*=utf-8''" + URLEncoder.encode(fileName, "UTF-8"));

            inputStream = resource.getInputStream();
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                    servletOutputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                // java gc回收
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
