

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
    strDir = %strDir%\��Ƶ

  FileCreateDir, %strDir%

  Progress, %A_index%, %A_LoopFileName%, �ƶ��ļ���..., �ƶ��ļ�����

  FileMove, %A_LoopFileName%, %strDir%
  errCnt += ErrorLevel
  cnt++
}

Progress, Off

okCnt := cnt - errCnt


MsgBox, "�ļ��ϼ�%cnt% �������гɹ��ƶ�%okCnt% ��, ʧ�ܸ�%errCnt% ��"