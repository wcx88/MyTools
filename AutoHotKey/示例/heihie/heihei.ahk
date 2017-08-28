MsgBox, "start"
arrQA := []
loop, read, conf.ini
{
  arrQA.Insert(A_LoopReadLine)
}
i := 2

loop
{ 
  if (i > arrQA.MaxIndex()) {
     break
  }
  sInput := arrQA[i]
  if ("" = Trim(sInput)) {
    i++
    continue
  }
  StringSplit, arrTemp, sInput, |
  
  if (1 = arrTemp0) {
    MsgBox, 64, 消息, %arrTemp1%
    i++
    continue
  }

  InputBox OutputVar, 请听题%i%, %arrTemp1%
  if (Trim(arrTemp2) = Trim(OutputVar))
  {
    MsgBox, 64, 成功, Good! 回答正确
    i++
  } else {
    MsgBox, 16, 失败, 不对！再想想！ (*^_^*) `n提示信息: %arrTemp3%
  }
}
