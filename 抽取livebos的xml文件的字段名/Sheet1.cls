VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Sheet1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = True
Option Explicit

Public Sub test()
    'On Error GoTo ERR_HANDLE
    Dim db_name As String
    Dim UserName As String
    Dim Password As String
    
    Dim baseTableName As String
    baseTableName = Cells(4, 2).Value
    
    Dim tableName As String
    tableName = Cells(5, 2).Value
    
    db_name = "CIF2"
    UserName = "CIF2"
    Password = "abs"
    
    Dim cnn As Object, rst As Object
    Dim sql As String
    Set cnn = CreateObject("ADODB.Connection")
    Set rst = CreateObject("ADODB.Recordset")
    
    cnn.Open "Provider=OraOLEDB.Oracle;" & _
                       "Data Source=" & db_name & ";" & _
                       "User Id=" & UserName & ";" & _
                       "Password=" & Password & ";"
   
    sql = "SELECT * FROM " & baseTableName & " WHERE ROWNUM < 65530 AND UPPER(TABLENAME)=UPPER('" & tableName & "')"
    
    rst.Open sql, cnn
    Dim i As Integer
    i = 3
    Dim j As Integer
    Dim field
    Dim objSheet
    Set objSheet = createSheet(tableName, ActiveWorkbook.Name)
    
    While Not rst.EOF
        j = 3
        For Each field In rst.fields
            objSheet.Cells(2, j).Value = field.Name
            objSheet.Cells(i, j).Value = field.Value
            j = j + 1
        Next field
        i = i + 1
        rst.MoveNext
    Wend
    
    rst.Close
    cnn.Close
    Set cnn = Nothing
    Set rst = Nothing
    
    Call setStyle(objSheet, i, j, 2)
    objSheet.Rows("1:" & i).RowHeight = 14.25
    
    Exit Sub
    
ERR_HANDLE:
    rst.Close
    cnn.Close
    Set cnn = Nothing
    Set rst = Nothing
    MsgBox Err.Description
    
End Sub

Private Sub CommandButton1_Click()
    
    Application.ScreenUpdating = False
    Call test
    Application.ScreenUpdating = True
End Sub
