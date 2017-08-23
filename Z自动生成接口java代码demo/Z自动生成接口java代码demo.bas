Attribute VB_Name = "Z自动生成接口java代码demo"
Option Explicit


Function getInParamStr(strParamName As String, strComment As String) As String
    getInParamStr = "        params.add(createLbParameter(""" & strParamName & """,""" & "" & "值"")); // " & Replace(strComment, Chr(10), "") & vbNewLine
End Function
Function getOutParamStr(strParamName As String, strComment As String, strCallFunc As String) As String
    If strCallFunc = "query" Then
        getOutParamStr = "            System.out.print(""" & strParamName & " = "" + mapData.get(""" & strParamName & """) + ""\t ""); // " & Replace(strComment, Chr(10), "") & vbNewLine
    ElseIf strCallFunc = "execBizProcess" Then
        getOutParamStr = "            System.out.println(""" & strParamName & " = "" + mapOutputVar.get(""" & strParamName & """)); // " & Replace(strComment, Chr(10), "") & vbNewLine
    End If
End Function

Function getHeadStr(strWsName As String, strCallFunc As String, strComment As String) As String
    If strCallFunc = "query" Then
        getHeadStr = "    // " & Replace(strComment, Chr(10), "") & vbNewLine & _
                "    public void " & strWsName & "() {" & vbNewLine & _
                "        LBEBusinessService client = getServiePort();" & vbNewLine & _
                "        QueryOption queryOption = new QueryOption();" & vbNewLine & _
                "        queryOption.setBatchNo(1);" & vbNewLine & _
                "        queryOption.setBatchSize(100);" & vbNewLine & _
                "        queryOption.setQueryCount(true);" & vbNewLine & _
                "        List<LbParameter> params = new ArrayList<LbParameter>();" & vbNewLine
    ElseIf strCallFunc = "execBizProcess" Then
        getHeadStr = "    // " & Replace(strComment, Chr(10), "") & vbNewLine & _
                "    public void " & strWsName & "() {" & vbNewLine & _
                "        LBEBusinessService client = getServiePort();" & vbNewLine & _
                "        List<LbParameter> params = new ArrayList<LbParameter>();" & vbNewLine
    
    End If
End Function

Function getFootStr(strWsName As String, strCallFunc As String, arrCodeStr_OUT) As String
    If strCallFunc = "query" Then
        If (strWsName = "cifQuery") Then
        getFootStr = "        QueryResult qrs = client.query(getSessionId(), """ & strWsName & """, params, null, queryOption);" & vbNewLine & _
                    "        printQueryResult(qrs); // 控制抬打印全部结果集 (根据传参""对象ID""不同，返回的结果集也不同)" & vbNewLine & _
                    "    }" & vbNewLine & vbNewLine

        Else
        getFootStr = "        QueryResult qrs = client.query(getSessionId(), """ & strWsName & """, params, null, queryOption);" & vbNewLine & _
                    "        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();" & vbNewLine & _
                    "        List<ColInfo> listCols = qrs.getMetaData().getColInfo();" & vbNewLine & _
                    "        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {" & vbNewLine & _
                    "            LbRecord record = it.next();" & vbNewLine & _
                    "            List listValues = record.getValues();" & vbNewLine & _
                    "            Map<String, Object> mapData = new HashMap<String, Object>();" & vbNewLine & _
                    "            for (int j = 0; j < listCols.size(); j++) {" & vbNewLine & _
                    "                mapData.put(listCols.get(j).getLabel(), listValues.get(j));" & vbNewLine & _
                    "            }" & vbNewLine & _
                    "            listData.add(mapData);" & vbNewLine & _
                    "        }" & vbNewLine & _
                    "        for(Map<String, Object> mapData : listData) {" & vbNewLine & _
                                Join(arrCodeStr_OUT, "") & vbNewLine & _
                    "           System.out.println("""");" & vbNewLine & _
                    "        }" & vbNewLine & _
                    "        //printQueryResult(qrs); // 控制抬打印全部结果集" & vbNewLine & _
                    "    }" & vbNewLine & vbNewLine
        End If
    ElseIf strCallFunc = "execBizProcess" Then
        getFootStr = "        BizProcessResult result = client.execBizProcess(getSessionId(), """ & strWsName & """, """", params, null);" & vbNewLine & _
                    "        System.out.println(""result message: "" + result.getMessage());" & vbNewLine & _
                    "        System.out.println(""result code: "" + result.getResult());" & vbNewLine & _
                    "        if (result.getOutputVariables() != null) {" & vbNewLine & _
                    "            Map<String, String> mapOutputVar = new HashMap<String, String>();" & vbNewLine & _
                    "            // 所有出参" & vbNewLine & _
                    "            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {" & vbNewLine & _
                    "                LbParameter var = it.next();" & vbNewLine & _
                    "                // System.out.println(""出参:  "" + var.getName() + ""="" + var.getValue());" & vbNewLine & _
                    "                mapOutputVar.put(var.getName(), var.getValue());" & vbNewLine & _
                    "            }" & vbNewLine & _
                    "            // 出参" & vbNewLine & _
                                Join(arrCodeStr_OUT, "") & _
                    "        }" & vbNewLine & _
                    "    }" & vbNewLine & vbNewLine
    End If
End Function

Function pushInToArray(ByRef arr, element)
    Dim idx As Integer
    idx = UBound(arr) + 1
    ReDim Preserve arr(idx)
    arr(idx) = element
    pushInToArray = idx
End Function

Sub CreateJavaCode()
    Dim objIndexSheet As Worksheet
    Dim objDeatilSheet As Worksheet
    Set objIndexSheet = Sheets("第三方接口目录")
    Set objDeatilSheet = Sheets("第三方接口")
    
    Dim objCell
    
    ReDim arrCodeStr(0)
    ReDim arrCodeStr_IN(0)
    ReDim arrCodeStr_OUT(0)
    Dim idx As Integer, idx_IN As Integer, idx_OUT As Integer
    
    idx = pushInToArray(arrCodeStr, getCommonStr())
    
    Dim i As Integer, j As Integer
    Dim strWsName As String   ' 对象名
    Dim strCallFunc As String ' 调用平台方法
    For i = 1 To objDeatilSheet.UsedRange.Rows.Count
        If Trim(objDeatilSheet.Cells(i, 2).Value) = "" Then GoTo LOOP1_NEXT
        
        If Trim(objDeatilSheet.Cells(i, 1).Value) = "对象名" Then
            
            If strWsName <> "" Then
                idx = pushInToArray(arrCodeStr, Join(arrCodeStr_IN, ""))
                idx = pushInToArray(arrCodeStr, getFootStr(strWsName, strCallFunc, arrCodeStr_OUT))
            End If
            
            strWsName = Trim(objDeatilSheet.Cells(i, 2).Value)
            
            Set objCell = objIndexSheet.Range("C:C").Find(What:=strWsName, lookAt:=xlWhole)
            If objCell Is Nothing Then
                'Err.Raise 513, "", "对象名[" & strWsName & "]在sheet[第三方接口目录]没有找到，请检查对象名拼写是否有误"
                MsgBox ("对象名[" & strWsName & "]在sheet[第三方接口目录]没有找到，请检查对象名拼写是否有误")
                GoTo ERR_HANDLE
            End If
            strCallFunc = Trim(objCell.Offset(0, -1).Value)
            
            idx = pushInToArray(arrCodeStr, getHeadStr(strWsName, strCallFunc, Trim(objDeatilSheet.Cells(i, 4).Value)))
            
            idx_IN = 0
            idx_OUT = 0
            ReDim arrCodeStr_IN(0)
            ReDim arrCodeStr_OUT(0)
            GoTo LOOP1_NEXT
        End If
        
        '参数类型为入参
        If (Trim(objDeatilSheet.Cells(i, 2).Value) = "IN") Then
            idx_IN = pushInToArray(arrCodeStr_IN, getInParamStr(objDeatilSheet.Cells(i, 3).Value, objDeatilSheet.Cells(i, 4).Value & "  " & objDeatilSheet.Cells(i, 6).Value))
            GoTo LOOP1_NEXT
        End If
        '参数类型为出参
        If (Trim(objDeatilSheet.Cells(i, 2).Value) = "OUT") Then
            idx_OUT = pushInToArray(arrCodeStr_OUT, getOutParamStr(objDeatilSheet.Cells(i, 3).Value, objDeatilSheet.Cells(i, 4).Value & "  " & objDeatilSheet.Cells(i, 6).Value, strCallFunc))
            GoTo LOOP1_NEXT
        End If
        
LOOP1_NEXT:
    Next i
LOOP1_END:
        
        
    idx = pushInToArray(arrCodeStr, Join(arrCodeStr_IN, ""))
    idx = pushInToArray(arrCodeStr, getFootStr(strWsName, strCallFunc, arrCodeStr_OUT))
    idx = pushInToArray(arrCodeStr, "}" & vbNewLine)
    If WriteFile(arrCodeStr) Then
        
    End If
    'Debug.Print Join(arrCodeStr, vbNewLine)
    Exit Sub
    
ERR_HANDLE:
    'MsgBox "发生异常：" & Err.Description
End Sub


Private Function WriteFile(arrData)
    On Error GoTo ERR_HANDLE
    Dim objFileSystem As Object
    Dim objOutFile As Object

    Set objFileSystem = CreateObject("Scripting.FileSystemObject")
    
    
    Set objOutFile = objFileSystem.OpenTextFile(ThisWorkbook.Path & "\ClientDemo.java", 2, True)
    
    objOutFile.Write (Join(arrData, ""))
    
    objOutFile.Close

    MsgBox ("成功生成[" & ThisWorkbook.Path & "\ClientDemo.java]")
    
    WriteFile = True
    
    Exit Function
ERR_HANDLE:
    MsgBox "发生异常：" & Err.Description
    objOutFile.Close
    WriteFile = False
    
End Function

Function getCommonStr()
    Dim arrTemp(85)
    arrTemp(1) = "import com.apex.crm.wsclient.*;" & vbNewLine
    arrTemp(2) = "" & vbNewLine
    arrTemp(3) = "import javax.xml.namespace.QName;" & vbNewLine
    arrTemp(4) = "import java.net.URL;" & vbNewLine
    arrTemp(5) = "import java.net.MalformedURLException;" & vbNewLine
    arrTemp(6) = "import java.util.*;" & vbNewLine
    arrTemp(7) = "" & vbNewLine
    arrTemp(8) = "public class ClientDemo {" & vbNewLine
    arrTemp(9) = "    public static void main(String[] args) {" & vbNewLine
    arrTemp(10) = "         ClientDemo client = new ClientDemo();" & vbNewLine
    arrTemp(11) = "        //client.cifQuery();" & vbNewLine
    arrTemp(12) = "        client.cifCXFXCKHSQ();" & vbNewLine
    arrTemp(13) = "        //client.cifwsGMSFYZ_FSCX();" & vbNewLine
    arrTemp(14) = "    }" & vbNewLine & vbNewLine
    arrTemp(15) = "    private LBEBusinessService serviePort;" & vbNewLine
    arrTemp(16) = "    private String sessionId;" & vbNewLine
    arrTemp(17) = "    private static final QName SERVICE_NAME = new QName(""http://ws.livebos.apex.com/"", ""LBEBusinessWebService"");" & vbNewLine
    arrTemp(18) = "" & vbNewLine
    arrTemp(19) = "    public static void main(String[] args) {" & vbNewLine
    arrTemp(19) = "        Demo testDemo = new Demo();" & vbNewLine
    arrTemp(19) = "        testDemo.cifQuery();" & vbNewLine
    arrTemp(19) = "        testDemo.cifwsKHFXCP_FXCP();" & vbNewLine
    arrTemp(19) = "    }" & vbNewLine
    arrTemp(19) = "    " & vbNewLine
    arrTemp(19) = "    public LBEBusinessService getServiePort() {" & vbNewLine
    arrTemp(20) = "        if (serviePort == null) {" & vbNewLine
    arrTemp(21) = "            LBEBusinessWebService service;" & vbNewLine
    arrTemp(22) = "            URL wsdlURL = null;" & vbNewLine
    arrTemp(23) = "            try {" & vbNewLine
    arrTemp(24) = "                //wsdlURL = new URL(""http://cif.crm.apexsoft.com.cn/service/LBEBusiness?wsdl"");" & vbNewLine
    arrTemp(25) = "                wsdlURL = new URL(""http://192.168.10.40:8820/service/LBEBusiness?wsdl"");" & vbNewLine
    arrTemp(26) = "            } catch (MalformedURLException e) {" & vbNewLine
    arrTemp(27) = "                e.printStackTrace();" & vbNewLine
    arrTemp(28) = "            }" & vbNewLine
    arrTemp(29) = "            service = new LBEBusinessWebService(wsdlURL, SERVICE_NAME);" & vbNewLine
    arrTemp(30) = "            serviePort = service.getLBEBusinessServiceImplPort();" & vbNewLine
    arrTemp(31) = "        }" & vbNewLine
    arrTemp(32) = "        return serviePort;" & vbNewLine
    arrTemp(33) = "    }" & vbNewLine
    arrTemp(34) = "" & vbNewLine
    arrTemp(35) = "    protected void printQueryResult(QueryResult qrs) {" & vbNewLine
    arrTemp(36) = "        System.out.println(""result message: "" + qrs.getMessage());" & vbNewLine
    arrTemp(37) = "        System.out.println(""count:"" + qrs.getCount() + "" hasMore:"" + qrs.isHasMore());" & vbNewLine
    arrTemp(38) = "        System.out.println(""size:"" + qrs.getRecords().size());" & vbNewLine
    arrTemp(39) = "        for (Iterator<ColInfo> it = qrs.getMetaData().getColInfo().iterator(); it.hasNext();) {" & vbNewLine
    arrTemp(40) = "            ColInfo colInfo = it.next();" & vbNewLine
    arrTemp(41) = "            System.out.print(colInfo.getLabel() + ""]\t"");" & vbNewLine
    arrTemp(42) = "        }" & vbNewLine
    arrTemp(43) = "        System.out.println(""\n====================================data-start==========================================="");" & vbNewLine
    arrTemp(44) = "        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {" & vbNewLine
    arrTemp(45) = "            LbRecord record = it.next();" & vbNewLine
    arrTemp(46) = "            for (Iterator<Object> itVal = record.getValues().iterator(); itVal.hasNext();) {" & vbNewLine
    arrTemp(47) = "                System.out.print(itVal.next() + ""\t"");" & vbNewLine
    arrTemp(48) = "            }" & vbNewLine
    arrTemp(49) = "            System.out.println("""");" & vbNewLine
    arrTemp(50) = "        }" & vbNewLine
    arrTemp(51) = "        System.out.println(""=====================================data-end=========================================="");" & vbNewLine
    arrTemp(52) = "    }" & vbNewLine
    arrTemp(53) = "" & vbNewLine
    arrTemp(54) = "    public LoginResult login(LBEBusinessService client) {" & vbNewLine
    arrTemp(55) = "        // WebService 用户:webuser/111111" & vbNewLine
    arrTemp(56) = "        LoginResult result = client.login(""webuser"", ""111111"", """", ""plain"", """");" & vbNewLine
    arrTemp(57) = "" & vbNewLine
    arrTemp(58) = "        System.out.println(""result login message: "" + result.getMessage());" & vbNewLine
    arrTemp(59) = "        System.out.println(""result sessionId: "" + result.getSessionId());" & vbNewLine
    arrTemp(60) = "        return result;" & vbNewLine
    arrTemp(61) = "    }" & vbNewLine
    arrTemp(62) = "" & vbNewLine
    arrTemp(63) = "    /**" & vbNewLine
    arrTemp(64) = "     * @return the sessionId" & vbNewLine
    arrTemp(65) = "     */" & vbNewLine
    arrTemp(66) = "    public String getSessionId() {" & vbNewLine
    arrTemp(67) = "        if (sessionId == null) {" & vbNewLine
    arrTemp(68) = "            LoginResult result = login(getServiePort());" & vbNewLine
    arrTemp(69) = "            sessionId = result.getSessionId();" & vbNewLine
    arrTemp(70) = "        }" & vbNewLine
    arrTemp(71) = "        return sessionId;" & vbNewLine
    arrTemp(72) = "    }" & vbNewLine
    arrTemp(73) = "" & vbNewLine
    arrTemp(74) = "    /**" & vbNewLine
    arrTemp(75) = "     * @param sessionId the sessionId to set" & vbNewLine
    arrTemp(76) = "     */" & vbNewLine
    arrTemp(77) = "    public void setSessionId(String sessionId) {" & vbNewLine
    arrTemp(78) = "        this.sessionId = sessionId;" & vbNewLine
    arrTemp(79) = "    }" & vbNewLine
    arrTemp(80) = "    public static LbParameter createLbParameter(String name, String value) {" & vbNewLine
    arrTemp(81) = "        LbParameter param = new LbParameter();" & vbNewLine
    arrTemp(82) = "        param.setName(name);" & vbNewLine
    arrTemp(83) = "        param.setValue(value);" & vbNewLine
    arrTemp(84) = "        return param;" & vbNewLine
    arrTemp(85) = "    }" & vbNewLine

    getCommonStr = Join(arrTemp, "")
End Function
