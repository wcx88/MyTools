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
    MsgBox, 64, ��Ϣ, %arrTemp1%
    i++
    continue
  }

  InputBox OutputVar, ������%i%, %arrTemp1%
  if (Trim(arrTemp2) = Trim(OutputVar))
  {
    MsgBox, 64, �ɹ�, Good! �ش���ȷ
    i++
  } else {
    MsgBox, 16, ʧ��, ���ԣ������룡 (*^_^*) `n��ʾ��Ϣ: %arrTemp3%
  }
}
