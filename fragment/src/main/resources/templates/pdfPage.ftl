<!DOCTYPE html>
<html>
<head lang="en">
    <title>Spring Boot Demo - PDF</title>
    <style>
        @page {
            size:  297mm 210mm; /*设置纸张大小:A4(210mm 297mm)、A3(297mm 420mm) 横向则反过来*/
            margin-top: 1.5in;
            padding: 1em;
            @bottom-center{
                content:"朝夕相伴·不老梦 © 版权所有";
                font-family: SimSun;
                font-size: 14px;
                color:red;
            };
            @top-center { content: element(header) };
            @bottom-right{
                content:"第" counter(page) "页  共 " counter(pages) "页";
                font-family: SimSun;
                font-size: 14px;
                color:#000;
            };
        }
        #table table {
            width: 100%;
            font-size: 12px;
            border: 1px solid #eee
        }

        #table {
            padding: 0 10px;
        }

        table thead th {
            background: #f5f5f5;
            padding: 10px;
            text-align: left;
        }

        table tbody td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #eee;
            border-right: 1px solid #eee;
        }

    </style>
</head>
<body style="font-family:SimSun  ">
<div style="">${title}</div>
<div>
    <table cellpadding="0" cellspacing="0" style="font-size: 14px;width: auto;height: auto"  align="center" valign="center" >
        <thead >
        <tr >
            <th>玩家</th>
            <th>拥有碎片</th>
            <th>想要碎片</th>
            <th>时间</th>
        </tr>
        </thead>
        <tbody>
          <#list listAll as student>
            <tr>
                  <td width="10%">${student.name!}</td>
                  <td width="40%">${student.haveFragment!}</td>
                  <td width="30%">${student.exchange!}</td>
                  <td width="20%">${(student.date?string("yyyy-MM-dd hh:mm:ss"))!}</td>
            </tr>
          </#list>
        </tbody>
    </table>
</div>
</body>
</html>