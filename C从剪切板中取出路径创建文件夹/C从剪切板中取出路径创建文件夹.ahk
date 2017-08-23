
newDir = %clipboard%

MsgBox, 4, , 确定要创建目录[%newDir%]吗?
IfMsgBox, No
    Exit

; 获取盘符:
SplitPath, newDir, name, dir, ext, name_no_ext, drive
; 没有盘符是相对路径
if(drive == "") {
	newDir = .\%newDir%
}

IfNotExist, %newDir% 
{
	FileCreateDir, %newDir%
	IfExist, %newDir%
	{
		MsgBox, 创建目录[%newDir%]成功
	} else {
		MsgBox, 创建目录[%newDir%]失败
	}
} else {
	MsgBox, 目录[%newDir%]已经存在
}