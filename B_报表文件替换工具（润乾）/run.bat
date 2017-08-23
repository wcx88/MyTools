@echo off
call env.bat

"%java_home%\bin\java" MyReportUtils
echo . . .
echo 处理完成，处理结果请查看文件[log.txt] ！
echo ...
pause