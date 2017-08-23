VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet3"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Option Explicit

Private Const MAX_LEN As Integer = 1000
Private Const START_LINE As Integer = 3
Private Const START_LINE_RESULT As Integer = 12

Private g_objFileSystem
Private g_intFileNum As Integer

Public Sub initVars()
    g_intFileNum = 12
    Set g_objFileSystem = CreateObject("Scripting.FileSystemObject")
    Call initGlobalVars
End Sub

Private Sub clear()
    Cells(11, 1).Value = "结果："
    Rows("13:65536").ClearContents
End Sub

Private Sub OptionFilter_name_Click()
    Cells(3, 2).Interior.ColorIndex = 15
    Cells(3, 2).Font.Strikethrough = True
    Cells(4, 2).Interior.ColorIndex = 0
    Cells(4, 2).Font.Strikethrough = False
End Sub

Private Sub OptionFilter_type_Click()
    Cells(3, 2).Interior.ColorIndex = 0
    Cells(3, 2).Font.Strikethrough = False
    Cells(4, 2).Interior.ColorIndex = 15
    Cells(4, 2).Font.Strikethrough = True
End Sub

Private Sub cmdSelect_Click()
    Dim selectFile As String
    selectFile = getFilePath(OptionForlder.Value)
    If Not "" = selectFile Then
        Cells(1, 2).Value = selectFile
    End If
End Sub

Private Sub cmdStart_Click()
    Call clear
    If OptionFilter_type.Value Then
        Call start(Cells(1, 2).Value, OptionForlder.Value, Cells(3, 2).Value, chkIncludeSubDir.Value)
    Else
        Call start2(Cells(1, 2).Value, Cells(4, 2).Value)
    End If
End Sub

Private Sub cmdClear_Click()
    Call clear
End Sub


Private Sub start(rootDir As String, isForder As Boolean, fileFilter As String, includeSubDir As Boolean)
    Application.ScreenUpdating = False
    
    Call initVars
    Call loadDir(rootDir, isForder, fileFilter, includeSubDir)
    Cells(START_LINE_RESULT - 1, 1).Value = "结果：共计" & g_intFileNum - START_LINE_RESULT & "个文件"
    
    Application.ScreenUpdating = True
    
    MsgBox "完成！共计" & g_intFileNum - START_LINE_RESULT & "个文件"
End Sub

Private Sub start2(rootDir As String, fileName As String)
    Application.ScreenUpdating = False
    
    Call initVars
    Call queryDir(rootDir, fileName)
    
    Cells(11, 1).Value = "结果：共计" & g_intFileNum - START_LINE_RESULT & "个文件"
        
    
    Application.ScreenUpdating = True
    
    MsgBox "完成！共计" & g_intFileNum - START_LINE_RESULT & "个文件"
End Sub

Private Sub LoadLivebosXml(xmlFileFullPath, workBookName As String)
    On Error GoTo ERR_HANDLE
    Dim xmlDoc As MSXML2.DOMDocument60
    Dim xmlNode1, xmlNode2, xmlNode3 As MSXML2.IXMLDOMNode
    
    Set xmlDoc = New MSXML2.DOMDocument60
    xmlDoc.async = False
    xmlDoc.load (xmlFileFullPath)
    
    '验证xml文件装载结果
    If xmlDoc.parseError.errorCode <> 0 Then
        Cells(g_intFileNum, 3).Value = "载入xml时发生异常：" & xmlDoc.parseError.reason & vbCrLf & xmlDoc.parseError.srcText
        Exit Sub
    End If
    
    Dim objSheet As Worksheet
    Dim xmlDocName As String
    xmlDocName = xmlDoc.selectSingleNode("//object").Attributes.getNamedItem("name").Text
    Set objSheet = createSheet(xmlDocName, workBookName)
    objSheet.Cells(1, 1).Value = xmlDocName
    objSheet.Name = xmlDocName
    objSheet.Cells(1, 2).Value = xmlDoc.selectSingleNode("//describe").Text
    
    Dim i As Integer
    i = START_LINE
    Dim j As Integer
    Dim startLine As Integer
    startLine = START_LINE
    For Each xmlNode1 In xmlDoc.selectSingleNode("//columns").selectNodes("column")
        j = 1
        For Each xmlNode2 In xmlNode1.childNodes
            If (startLine = i) Then
                objSheet.Cells(startLine - 1, j).Value = xmlNode2.nodeName
            End If
            objSheet.Cells(i, j).Value = subString(xmlNode2.Text, MAX_LEN)
            j = j + 1
        Next xmlNode2
        i = i + 1
    Next xmlNode1
    
    ' set style
    objSheet.Cells.EntireColumn.AutoFit
    objSheet.Cells.Interior.ColorIndex = 2
    
    Call setStyle(objSheet, i, j, startLine - 1)
    
    ' methods
    For Each xmlNode1 In xmlDoc.selectSingleNode("//methods").selectNodes("method")
        i = i + 2
        objSheet.Cells(i, 1).Value = xmlNode1.Attributes.getNamedItem("name").Text
        objSheet.Cells(i, 2).Value = xmlNode1.selectSingleNode("describe").Text
        i = i + 2
        startLine = i
        For Each xmlNode2 In xmlNode1.selectSingleNode("parameters").selectNodes("column")
            j = 1
            For Each xmlNode3 In xmlNode2.childNodes
                If (startLine = i) Then
                    objSheet.Cells(startLine - 1, j).Value = xmlNode3.nodeName
                End If
                objSheet.Cells(i, j).Value = subString(xmlNode3.Text, MAX_LEN)
                j = j + 1
            Next xmlNode3
            i = i + 1
        Next xmlNode2
        
        Call setStyle(objSheet, i, j, startLine - 1)
    Next xmlNode1
    
    objSheet.Rows("1:" & i).RowHeight = 14.25
    
    Exit Sub
ERR_HANDLE:
    Cells(g_intFileNum, 3).Value = "发生异常：" & Err.Description
End Sub


Private Sub loadDir(strDir, isForder As Boolean, fileFilter As String, includeSubDir As Boolean)
    'On Error GoTo ERR_HANDLE
    Dim strFile As String
    strFile = strDir
    
    ' 单个文件
    If Not isForder Then
        g_intFileNum = g_intFileNum + 1
        Cells(g_intFileNum, 2).Value = strDir
        Call LoadLivebosXml(strDir, g_strBaseWorkBookName)
        Exit Sub
    End If
    
    ' 文件夹
    strFile = Dir(strDir & Application.PathSeparator & fileFilter, vbNormal)
    While strFile <> ""
        g_intFileNum = g_intFileNum + 1
        Cells(g_intFileNum, 2).Value = strDir & Application.PathSeparator & strFile
        Call LoadLivebosXml(strDir & Application.PathSeparator & strFile, getParentDirName(strDir))
        strFile = Dir
    Wend
    
    Call closeWorkbook(getParentDirName(strDir), True)
    
    If Not includeSubDir Then
        Exit Sub
    End If
    
    Dim strSubDir
    Dim strSubDirs
    Dim folder
    Set folder = g_objFileSystem.GetFolder(strDir & Application.PathSeparator)
    Set strSubDirs = folder.SubFolders
    For Each strSubDir In strSubDirs
        If InStr(1, strSubDir, ".svn") <> Len(strSubDir) - 4 + 1 Then
            Call loadDir(strSubDir, isForder, fileFilter, includeSubDir)
       End If
    Next
    Exit Sub
ERR_HANDLE:
    MsgBox "发生异常：" & Err.Description
End Sub

Private Sub queryDir(strDir, fileName As String)
    On Error GoTo ERR_HANDLE
    Dim strFile As String
    
    ' 文件夹
    strFile = Dir(strDir & Application.PathSeparator, vbNormal)
    While strFile <> ""
        If strFile = fileName Or strFile = fileName & ".xml" Then
            g_intFileNum = g_intFileNum + 1
            Cells(g_intFileNum, 2).Value = strDir & Application.PathSeparator & strFile
            Call LoadLivebosXml(strDir & Application.PathSeparator & strFile, g_strBaseWorkBookName)
        End If
        strFile = Dir
    Wend
    
    Dim strSubDir
    Dim strSubDirs
    Dim folder
    Set folder = g_objFileSystem.GetFolder(strDir & Application.PathSeparator)
    Set strSubDirs = folder.SubFolders
    For Each strSubDir In strSubDirs
        If InStr(1, strSubDir, ".svn") <> Len(strSubDir) - 4 + 1 Then
            Call queryDir(strSubDir, fileName)
        End If
    Next
    Exit Sub
ERR_HANDLE:
    MsgBox "发生异常：" & Err.Description
End Sub

Private Sub Worksheet_Change(ByVal Target As Range)
    If 1 = Target.Row And Target.Column = 2 Then
        If InStr(1, Target.Value, ".xml", vbTextCompare) = Len(Target.Value) - 4 + 1 Then
            OptionFile.Value = True
        Else
            OptionForlder.Value = True
        End If
    End If
End Sub

Private Sub Worksheet_SelectionChange(ByVal Target As Range)

End Sub
