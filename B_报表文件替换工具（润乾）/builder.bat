@echo off
echo 设置环境变量 . . .
call env.bat
echo 开始编译 . . .
"%java_home%\bin\javac.exe" MyReportUtils.java
echo 编译完成 . . .
echo . . .
pause