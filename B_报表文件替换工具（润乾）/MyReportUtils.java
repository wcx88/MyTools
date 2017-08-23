
import com.runqian.report4.usermodel.Area;
import com.runqian.report4.usermodel.IByteMap;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.usermodel.IReport;
import com.runqian.report4.util.ReportUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: weicx
 * Date: 2012-3-29
 * Time: 14:08:30
 */
public class MyReportUtils {
    public static void main(String[] args) {
//        if (args.length >= 3 && !isEmpty(args[0]) && !isEmpty(args[1]) && !isEmpty(args[2])) {
//            replaceFile(args[0], args[1], args[2]);
//        } else {
//            showAndSaveMsg("�����������  ������\n" + joinArray(args));
//        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        Map<String, String> mapInput = new HashMap <String, String>();
        System.out.print("��ѡ����:\n");
        System.out.print("    1:�����ļ��滻\n");
        System.out.print("    2:ȥ������ɫ\n");
        String sInput;
        try {
            String sAction = br.readLine().trim();
            System.out.print("�����뱨��Ŀ¼���ļ���(Ĭ��Ϊ��ǰĿ¼�µ� rpt_file):");
            sInput = br.readLine();
            if (null == sInput || "".equals(sInput.trim())) {
                sInput = "rpt_file";
            }
            if ("1".equals(sAction)) {
                mapInput.put("FilePath", sInput);
                System.out.print("������Ҫ�滻��Դ�ַ���:");
                sInput = br.readLine();
                mapInput.put("OldStr", sInput);
                System.out.print("������Ҫ�滻��Ŀ���ַ���:");
                sInput = br.readLine();
                mapInput.put("NewStr", sInput);
                replaceFile(mapInput.get("FilePath"), mapInput.get("OldStr"), mapInput.get("NewStr"));
            } else  if ("2".equals(sAction)) {
                mapInput.put("FilePath", sInput);
                clearBackgroundColor(mapInput.get("FilePath"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeToLogFile();
    }

    private static String joinArray(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < arr.length; i++) {
            sb.append("args[").append(i).append("]=").append(arr[i]).append("\n");
        }
        return sb.toString();
    }
    private static boolean isEmpty(String str) {
        return (null == str) || "".equals(str);
    }
    private static String convNull(String str) {
        return ((null == str) ?  "" : str);
    }
    private static List<String> m_MsgList = new ArrayList<String>();
    private static final String m_sLogFilePath = "./log.txt";
    private static void writeToLogFile() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(m_sLogFilePath));
            for(String sMsg : m_MsgList) {
                bw.append(sMsg);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bw) try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void showAndSaveMsg(String sMsg) {
        System.out.println(sMsg);
        m_MsgList.add(sMsg);
    }
    /**
     * �滻�ļ�(�Զ�ʶ��Ŀ¼/�ļ�)
     * @param sFilePath     �ļ���
     * @param sOldStr       ���ַ���
     * @param sNewStr       ���ַ���
     */
    public static void replaceFile(String sFilePath, String sOldStr, String sNewStr) {
        File file = new File(sFilePath);
        sFilePath = file.getAbsolutePath();
        if (!file.exists()) {
            showAndSaveMsg("�ļ�[" + sFilePath + "]�����ڣ�");
            return;
        }
        if (file.isFile()) {
            // �滻�ļ�
            replaceFile(sFilePath, sFilePath.replaceFirst("\\.raq$", "_new.raq"), sOldStr, sNewStr);
        } else if (file.isDirectory()) {
            // ��Ŀ¼������
            int length = file.getAbsolutePath().length();
            // ��Ŀ¼��
            String sNewBaseDir = convNull(file.getParent()) + File.separator + file.getName() + "_new";
            // ȡ�������ļ�����ȫ·����
            List<String> list = new ArrayList<String>();
            dir(file, list);
            for(String sPath : list) {
                // ���ļ�����ȫ·����
                String sNewPath = sNewBaseDir + sPath.substring(length);
                // ��Ŀ¼����������򴴽�
                File newFile = new File(sNewPath);
                File newDir = new File(newFile.getParent());
                if (!newDir.exists()) {
                    if (!newDir.mkdirs()) {
                        showAndSaveMsg("����Ŀ¼[" + newDir.getAbsolutePath() + "]����");
                        continue;
                    }
                }
                // �滻�ļ�
                replaceFile(sPath, sNewPath, sOldStr, sNewStr);
            }
        }
    }
    /**
     * �滻�ļ�
     * @param sInFileName   �����ļ�
     * @param sOutFileName  ����ļ�
     * @param sOldStr       ���ַ���
     * @param sNewStr       ���ַ���
     * @return              true - �ɹ�, false - ʧ��
     */
    public static boolean replaceFile(String sInFileName, String sOutFileName, String sOldStr, String sNewStr) {
        showAndSaveMsg("---------------------------------------------------------------------------------");
        try {
            showAndSaveMsg("��ȡ�����ļ�[" + sInFileName + "]...");
            // ��ȡ�����ļ�
            IReport report = ReportUtils.read(sInFileName);

            // �滻����
            int iCount = replace(report, sOldStr, sNewStr);
            showAndSaveMsg("�ϼ��滻" + iCount + "��...");

            showAndSaveMsg("д�뵽���ļ�[" + sOutFileName + "]...");
            // д�뵽���ļ�
            ReportUtils.write(sOutFileName, report);
            return true;
        } catch (Exception e) {
            showAndSaveMsg(e.toString());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * �滻����
     * @param report        ����
     * @param sOldStr       ���ַ���
     * @param sNewStr       ���ַ���
     * @throws Exception    �쳣
     * @return              �滻����
     */
    public static int replace(IReport report, String sOldStr, String sNewStr) throws Exception {
        int iCount = 0;
        for (int i = 1; i <= report.getRowCount(); i++) {
            for (Short j = 1; j <= report.getColCount(); j++) {
                INormalCell cell = report.getCell(i, j);
                // ���ڱ��ϲ��ĵ�Ԫ��ֻҪ�����һ���Ϳ����ˣ���ֹ�ظ�����
                if (cell.isMerged()) {
                    Area area = cell.getMergedArea();
                    if (i != area.getBeginRow() || j != area.getBeginCol()) {
                        continue;
                    }
                }

                // ��ֵͨ
                Object value = cell.getValue();
                if (value instanceof String) {
                    // �滻��ֵͨ
                    if (((String) value).indexOf(sOldStr) >= 0) {
                        String sNewValue = ((String) value).replace(sOldStr, sNewStr);
                        showAndSaveMsg("�ڵ�Ԫ��[" + i +", " + j + "]�滻[" + value + "]Ϊ[" + sNewValue + "]");
                        cell.setValue(sNewValue);
                        iCount ++;
                    }
                }

                // �򵥱��ʽ
                IByteMap expMap = cell.getExpMap();
                if (null != expMap && !expMap.isEmpty()) {
                    for (Short index = 0; index < expMap.size(); index++) {
                        Object exp = expMap.getValue(index);
                        if (exp instanceof String) {
                            // �滻���ʽ
                            if (((String) exp).indexOf(sOldStr) >= 0) {
                                String sNewExp = ((String) exp).replace(sOldStr, sNewStr);
                                showAndSaveMsg("�ڵ�Ԫ��[" + i +", " + j + "]�滻[" + exp + "]Ϊ[" + sNewExp + "]");
                                expMap.setValue(index, sNewExp);
                                iCount ++;
                            }
                        }
                    }
                }
            }
        }
        return iCount;
    }
    /**
     * ȥ������ɫ(�Զ�ʶ��Ŀ¼/�ļ�)
     * @param sFilePath     �ļ���
     */
    public static void clearBackgroundColor(String sFilePath) {
        File file = new File(sFilePath);
        sFilePath = file.getAbsolutePath();
        if (!file.exists()) {
            showAndSaveMsg("�ļ�[" + sFilePath + "]�����ڣ�");
            return;
        }
        if (file.isFile()) {
            // ȥ������ɫ
            clearBackgroundColor(sFilePath, sFilePath.replaceFirst("\\.raq$", "_new.raq"));
        } else if (file.isDirectory()) {
            // ��Ŀ¼������
            int length = file.getAbsolutePath().length();
            // ��Ŀ¼��
            String sNewBaseDir = convNull(file.getParent()) + File.separator + file.getName() + "_new";
            // ȡ�������ļ�����ȫ·����
            List<String> list = new ArrayList<String>();
            dir(file, list);
            for(String sPath : list) {
                // ���ļ�����ȫ·����
                String sNewPath = sNewBaseDir + sPath.substring(length);
                // ��Ŀ¼����������򴴽�
                File newFile = new File(sNewPath);
                File newDir = new File(newFile.getParent());
                if (!newDir.exists()) {
                    if (!newDir.mkdirs()) {
                        showAndSaveMsg("����Ŀ¼[" + newDir.getAbsolutePath() + "]����");
                        continue;
                    }
                }
                // ȥ������ɫ
                clearBackgroundColor(sPath, sNewPath);
            }
        }
    }

    /**
     * ȥ������ɫ
     * @param sInFileName   �����ļ�
     * @param sOutFileName  ����ļ�
     * @return              true - �ɹ�, false - ʧ��
     */
    public static boolean clearBackgroundColor(String sInFileName, String sOutFileName) {
        showAndSaveMsg("---------------------------------------------------------------------------------");
        try {
            showAndSaveMsg("��ȡ�����ļ�[" + sInFileName + "]...");
            // ��ȡ�����ļ�
            IReport report = ReportUtils.read(sInFileName);

            // ȥ������ɫ
            int iCount = clearBackgroundColor(report);
            showAndSaveMsg("�ϼ�" + iCount + "��...");

            showAndSaveMsg("д�뵽���ļ�[" + sOutFileName + "]...");
            // д�뵽���ļ�
            ReportUtils.write(sOutFileName, report);
            return true;
        } catch (Exception e) {
            showAndSaveMsg(e.toString());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * ȥ������ɫ
     * @param report        ����
     * @throws Exception    �쳣
     * @return              ���ô���
     */
    public static int clearBackgroundColor(IReport report) throws Exception {
        int iCount = 0;
        // "53" ���뱳��ɫ
        byte byteBackColorIndex = Byte.parseByte("53");
        for (int i = 1; i <= report.getRowCount(); i++) {
            for (Short j = 1; j <= report.getColCount(); j++) {
                INormalCell cell = report.getCell(i, j);
                // ���ڱ��ϲ��ĵ�Ԫ��ֻҪ�����һ���Ϳ����ˣ���ֹ�ظ�����
                if (cell.isMerged()) {
                    Area area = cell.getMergedArea();
                    if (i != area.getBeginRow() || j != area.getBeginCol()) {
                        continue;
                    }
                }

                int iBackColor = cell.getBackColor();
                // "16777215" Ϊ��ɫ
                if (16777215 != iBackColor) {
                    showAndSaveMsg("�ڵ�Ԫ��[" + i +", " + j + "]ȥ������ɫ[" + iBackColor + "]");
                    cell.setBackColor(16777215);
                    iCount ++;
                }

                // �򵥱��ʽ
                IByteMap expMap = cell.getExpMap();
                if (null != expMap && !expMap.isEmpty()) {
                    // "53" �������ñ���ɫ�ı��ʽ
                    int index = expMap.getIndex(byteBackColorIndex);
                    if (index > 0) {
                        showAndSaveMsg("�ڵ�Ԫ��[" + i +", " + j + "]ȥ������ɫ���ʽ[" + expMap.getValue(index) + "]");
                        expMap.remove(expMap.getKey(index));
                        iCount ++;
                    }
                }
            }
        }
        return iCount;
    }

    /**
     * �����ļ�
     * @param dir   Ŀ¼����
     * @param list  �ļ�����ȫ·�����б�
     */
    private static void dir(File dir, List<String> list) {
        for(File file : dir.listFiles()) {
            if (file.isHidden()) continue;
            if (file.isFile()) {
                list.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                dir(file, list);
            }
        }
    }
}
