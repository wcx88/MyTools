Attribute VB_Name = "common"
Option Explicit

Public g_strBaseWorkBookName As String
Private g_strBaseWorkBookDir As String


Public Sub initGlobalVars()
    g_strBaseWorkBookName = ActiveWorkbook.Name
    g_strBaseWorkBookDir = ActiveWorkbook.path
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

Public Function workBookExists(workBookName) As Boolean
    '如果一个名称存在则返回true
    Dim objWorkBook As Workbook
    workBookExists = False
    For Each objWorkBook In Workbooks
        If UCase(objWorkBook.Name) = UCase(workBookName) Then
            workBookExists = True
            Exit Function
        End If
    Next objWorkBook
End Function

Public Sub closeWorkbook(workBookName, needSave)
    If (workBookExists(workBookName)) Then Workbooks(workBookName).Close savechanges:=needSave
End Sub

Public Function createWorkbook(workBookName)
    Workbooks.Add
    ActiveWorkbook.SaveAs fileName:= _
        g_strBaseWorkBookDir & Application.PathSeparator & workBookName, FileFormat:=xlNormal, _
        Password:="", WriteResPassword:="", ReadOnlyRecommended:=False, _
        CreateBackup:=False
    Workbooks(g_strBaseWorkBookName).Activate
End Function

Public Function createSheet(sheetName As String, workBookName As String)
    If Not workBookExists(workBookName) Then Call createWorkbook(workBookName)

    Set createSheet = Workbooks(workBookName).Sheets.Add(, Sheets(Sheets.Count))
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

Function subString(txt, maxLen)
    If Len(txt) > maxLen Then
        subString = Mid(txt, 1, maxLen)
    Else
        subString = txt
    End If
End Function


Public Function getParentDirName(path)
    Dim p1 As Integer
    p1 = InStrRev(path, Application.PathSeparator)
    getParentDirName = Mid(path, p1 + 1) & ".xls"
End Function


Public Sub setStyle(objSheet, i, j, titleLine)
    ' head
    objSheet.Range(objSheet.Cells(titleLine - 1, 1), objSheet.Cells(titleLine - 1, 2)).Interior.ColorIndex = 37
    With objSheet.Range(objSheet.Cells(titleLine - 1, 1), objSheet.Cells(titleLine - 1, 2)).Borders()
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
    ' title
    objSheet.Range(objSheet.Cells(titleLine, 1), objSheet.Cells(titleLine, j - 1)).Interior.ColorIndex = 34
    With objSheet.Range(objSheet.Cells(titleLine, 1), objSheet.Cells(i - 1, j - 1)).Borders()
        .LineStyle = xlContinuous
        .Weight = xlThin
        .ColorIndex = xlAutomatic
    End With
End Sub




