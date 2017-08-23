Attribute VB_Name = "common"
Option Explicit
Private Const START_LINE As Integer = 3

Private g_objFileSystem
Private g_intFileNum As Integer

Public Sub start(rootDir As String, isForder As Boolean, fileFilter As String, includeSubDir As Boolean)
    Application.ScreenUpdating = False
    
    Call initGlobalVars
    Call loadDir(rootDir, isForder, fileFilter, includeSubDir)
    With Sheets("config")
        .Select
        .Cells(11, 1).Value = "结果：共计" & g_intFileNum - 12 & "个文件"
    End With
    
    Application.ScreenUpdating = True
    
    MsgBox "完成！共计" & g_intFileNum - 12 & "个文件"
End Sub


Private Sub initGlobalVars()
    Set g_objFileSystem = CreateObject("Scripting.FileSystemObject")
    g_intFileNum = 12
End Sub

Public Function getFilePath(isForder As Boolean) As String
    If isForder Then
        Dim fd As FileDialog
        Set fd = Application.FileDialog(msoFileDialogFolderPicker)
        If fd.Show = -1 Then
            getFilePath = fd.SelectedItems(1)
        End If
        Set fd = Nothing
    Else
        Dim selectFile
        selectFile = Application.GetOpenFilename("xml 文件 (*.xml),*.xml")
        If Not False = selectFile Then getFilePath = selectFile
    End If
End Function

Public Function sheetExists(sheetName As String) As Boolean
    '如果一个名称存在则返回true
    Dim objSheet As Worksheet
    sheetExists = False
    For Each objSheet In Sheets
        If UCase(objSheet.Name) = UCase(sheetName) Then
            sheetExists = True
            Exit Function
        End If
    Next objSheet
End Function

Public Function createSheet(sheetName As String)
    Set createSheet = Sheets.Add(, Sheets(Sheets.Count))
    If sheetExists(sheetName) Then
        Application.DisplayAlerts = False
        Sheets(sheetName).Delete
        Application.DisplayAlerts = True
    End If
    createSheet.Name = sheetName
End Function

Public Sub deleteSheet(exceptSheetNameList As String)
    Dim objSheet As Worksheet
    For Each objSheet In Sheets
        If InStr(1, "," & UCase(exceptSheetNameList) & ",", "," & UCase(objSheet.Name) & ",") <= 0 Then
        Application.DisplayAlerts = False
        Sheets(objSheet.Name).Delete
        Application.DisplayAlerts = True
        End If
    Next objSheet
End Sub


Public Sub LoadLivebosXml(xmlFileFullPath)
    On Error GoTo ERR_HANDLE
    Dim xmlDoc As MSXML2.DOMDocument60
    Dim xmlNode As MSXML2.IXMLDOMNode
    Dim xmlNode2 As MSXML2.IXMLDOMNode
    
    Set xmlDoc = New MSXML2.DOMDocument60
    xmlDoc.async = False
    xmlDoc.load (xmlFileFullPath)
    
    '验证xml文件装载结果
    If xmlDoc.parseError.errorCode <> 0 Then
        Sheets("config").Cells(g_intFileNum, 3).Value = "载入xml时发生异常：" & xmlDoc.parseError.reason & vbCrLf & xmlDoc.parseError.srcText
        Exit Sub
    End If
    Dim objSheet As Worksheet
    Dim xmlDocName As String
    xmlDocName = xmlDoc.selectSingleNode("//object").Attributes.getNamedItem("name").Text
    Set objSheet = createSheet(xmlDocName)
    objSheet.Cells(1, 1).Value = xmlDocName
    objSheet.Name = xmlDocName
    objSheet.Cells(1, 2).Value = xmlDoc.selectSingleNode("//describe").Text
    Dim i As Integer
    i = START_LINE
    Dim j As Integer
    For Each xmlNode In xmlDoc.selectSingleNode("//columns").childNodes
        j = 1
        For Each xmlNode2 In xmlNode.childNodes
            If (START_LINE = i) Then
                objSheet.Cells(START_LINE - 1, j).Value = xmlNode2.nodeName
            End If
            objSheet.Cells(i, j).Value = xmlNode2.Text
            j = j + 1
        Next xmlNode2
        i = i + 1
    Next xmlNode
    ' set style
    
    objSheet.Cells.EntireColumn.AutoFit
    objSheet.Cells.Interior.ColorIndex = 2
    objSheet.Range(objSheet.Cells(1, 1), objSheet.Cells(1, 2)).Interior.ColorIndex = 15
    objSheet.Range(objSheet.Cells(START_LINE - 1, 1), objSheet.Cells(START_LINE - 1, j - 1)).Interior.ColorIndex = 35
    With objSheet.Range(objSheet.Cells(1, 1), objSheet.Cells(1, 2)).Borders()
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    With objSheet.Range(objSheet.Cells(START_LINE - 1, 1), objSheet.Cells(i - 1, j - 1)).Borders()
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    
    Exit Sub
ERR_HANDLE:
    Sheets("config").Cells(g_intFileNum, 3).Value = "发生异常：" & Err.Description
End Sub

Public Sub loadDir(strDir, isForder As Boolean, fileFilter As String, includeSubDir As Boolean)
    'On Error Resume Next
    Dim strFile As String
    strFile = strDir
    
    ' 单个文件
    If Not isForder Then
        g_intFileNum = g_intFileNum + 1
        Sheets("config").Cells(g_intFileNum, 2).Value = strDir
        Call LoadLivebosXml(strDir)
        Exit Sub
    End If
    
    
    ' 文件夹
    strFile = Dir(strDir & Application.PathSeparator & fileFilter, vbNormal)
    While strFile <> ""
        g_intFileNum = g_intFileNum + 1
        Sheets("config").Cells(g_intFileNum, 2).Value = strDir & Application.PathSeparator & strFile
        Call LoadLivebosXml(strDir & Application.PathSeparator & strFile)
        strFile = Dir
    Wend
    
    If Not includeSubDir Then
        Exit Sub
    End If
    
    Dim strSubDir
    Dim strSubDirs
    Dim folder
    Set folder = g_objFileSystem.GetFolder(strDir & Application.PathSeparator)
    Set strSubDirs = folder.SubFolders
    For Each strSubDir In strSubDirs
       Call loadDir(strSubDir, isForder, fileFilter, includeSubDir)
    Next
End Sub



