﻿#Persistent
SetTimer, CheckResponseText, 6000
return

CheckResponseText:
url:="http://3030.ij120.zoenet.cn/reservation/DocInfo?staffNo=%E9%99%88%E6%B0%B4%E4%BB%99%2F2096&deptCode=50&isArmyMan=0&deptName=%E4%BA%A7%E7%A7%91%E5%A4%8D%E8%AF%8A%E5%8F%B7&orgCode=FJSFY"
; 示例: 下载文本到变量:
whr := ComObjCreate("WinHttp.WinHttpRequest.5.1")
whr.Open("GET", url, true)
whr.Send()
; 使用 'true'(上面) 和调用下面的函数, 允许脚本保持响应.
whr.WaitForResponse()
responseText := whr.ResponseText
;MsgBox % responseText

str = "allowPrecontractFlag":1
FoundPos := InStr(responseText, str) 

if (FoundPos > 0)
	MsgBox "可以预约了" 
if (FoundPos <= 0)
	MsgBox "不可预约了" 
	
return
	