package com.yishang.yspay.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class ExportExcel {

    /**
     * @param alias     设置别名(strings[0]为字段,strings[1]为别名,string[2]列宽)
     * @param list      导出的数据
     * @param title     表内容的标题
     * @param excelName 表的文件名
     * @param response
     * @throws IOException
     */
    public void exportExcel(List<String[]> alias, List<?> list, String title, String excelName, HttpServletResponse response) throws IOException {
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        //设置别名以及列宽
        int i = 0;
        for (String[] strings : alias) {
            writer.addHeaderAlias(strings[0], strings[1]);
            if (strings.length < 3) {
                writer.setColumnWidth(i, 20);
            } else {
                writer.setColumnWidth(i, Integer.valueOf(strings[2]));
            }

            i++;
        }
        //设置标题
        writer.merge(alias.size() - 1, title, true);

        //只导设置了别名的字段
        writer.setOnlyAlias(true);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("utf-8"), "ISO-8859-1") + ".xls");
        //设置内容字体
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }
}
