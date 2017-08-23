@echo off


rem set java_home=C:\Java\jdk1.6.0_13


set CLASSPATH=.
for %%i in (".\lib\*.jar") do call ".\cpappend.bat" %%i
@echo %java_home%



