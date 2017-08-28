

cnt = 0
errCnt = 0
Loop Files, *.* 
{
  if A_LoopFileAttrib contains H,R,S 
    continue 

  if A_LoopFileExt contains bat,ahk,exe,dump
    continue

  strDate = ""
  FormatTime, strDate , A_LoopFileTimeModified, yyyyMMdd

  strDir = %strDate%

  if A_LoopFileExt contains mov,avi,3gp,mp4
    strDir = %strDir%\视频

  FileCreateDir, %strDir%

  Progress, %A_index%, %A_LoopFileName%, 移动文件中..., 移动文件进度

  FileMove, %A_LoopFileName%, %strDir%
  errCnt += ErrorLevel
  cnt++
}

Progress, Off

okCnt := cnt - errCnt


MsgBox, "文件合计%cnt% 个，其中成功移动%okCnt% 个, 失败个%errCnt% 个"