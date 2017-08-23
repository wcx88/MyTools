@echo off
setlocal enabledelayedexpansion
set pic_dir=".\"
for /f %%i in ('dir /s /b %pic_dir%') do (
	set str_date=%%~ti
	set str_path=%%~nxi
	if not "!str_path!" == "1.moveFileByDate.bat" (
		set str_date=!str_date:~0,10!
		set str_date=!str_date:/=_!
		rem echo !str_date!
		if not exist !str_date! (
			md !str_date!
			)

		echo !str_path!
		
		rem copy !str_path! !str_date!\
		move !str_path! !str_date!\

		)

	)

pause