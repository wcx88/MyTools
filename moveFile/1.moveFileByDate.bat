@echo off
setlocal enabledelayedexpansion
set pic_dir=".\"
for /f %%i in ('dir /a-d-s /b %pic_dir%') do (
	set "str_date=%%~ti"
	set "str_path=%%~nxi"
	set "str_ext=%%~xi"
	rem echo !str_date!    !str_path!
	if not "!str_date!" == "" (
	if not "!str_ext!" == ".bat" (
		set str_dir=!str_date:~0,10!
		set str_dir=!str_dir:/=!
		
		if "!str_ext!" == ".MOV" (
			set str_dir=!str_dir!\视频
			)
		if not exist !str_dir! (
			md !str_dir!
			)

		
		echo !str_dir!    !str_path!
		rem copy !str_path! !str_dir!\
		move "!str_path!" "!str_dir!\"

		)

	)
	)


pause