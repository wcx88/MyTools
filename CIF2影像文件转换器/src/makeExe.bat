echo off

set path=C:\Python25;%path%

python C:\Python25\pyinstaller-1.3\MakeSpec.py CutImgFileHead.py
pause

python C:\Python25\pyinstaller-1.3\Build.py CutImgFileHead.spec

pause