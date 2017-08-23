
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
//            showAndSaveMsg("输入参数有误！  参数：\n" + joinArray(args));
//        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


        Map<String, String> mapInput = new HashMap <String, String>();
        System.out.print("请选择功能:\n");
        System.out.print("    1:报表文件替换\n");
        System.out.print("    2:去除背景色\n");
        String sInput;
        try {
            String sAction = br.readLine().trim();
            System.out.print("请输入报表目录或文件名(默认为当前目录下的 rpt_file):");
            sInput = br.readLine();
            if (null == sInput || "".equals(sInput.trim())) {
                sInput = "rpt_file";
            }
            if ("1".equals(sAction)) {
                mapInput.put("FilePath", sInput);
                System.out.print("请输入要替换的源字符串:");
                sInput = br.readLine();
                mapInput.put("OldStr", sInput);
                System.out.print("请输入要替换的目标字符串:");
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
     * 替换文件(自动识别目录/文件)
     * @param sFilePath     文件名
     * @param sOldStr       旧字符串
     * @param sNewStr       新字符串
     */
    public static void replaceFile(String sFilePath, String sOldStr, String sNewStr) {
        File file = new File(sFilePath);
        sFilePath = file.getAbsolutePath();
        if (!file.exists()) {
            showAndSaveMsg("文件[" + sFilePath + "]不存在！");
            return;
        }
        if (file.isFile()) {
            // 替换文件
            replaceFile(sFilePath, sFilePath.replaceFirst("\\.raq$", "_new.raq"), sOldStr, sNewStr);
        } else if (file.isDirectory()) {
            // 旧目录名长度
            int length = file.getAbsolutePath().length();
            // 新目录名
            String sNewBaseDir = convNull(file.getParent()) + File.separator + file.getName() + "_new";
            // 取到所有文件名（全路径）
            List<String> list = new ArrayList<String>();
            dir(file, list);
            for(String sPath : list) {
                // 新文件名（全路径）
                String sNewPath = sNewBaseDir + sPath.substring(length);
                // 父目录如果不存在则创建
                File newFile = new File(sNewPath);
                File newDir = new File(newFile.getParent());
                if (!newDir.exists()) {
                    if (!newDir.mkdirs()) {
                        showAndSaveMsg("创建目录[" + newDir.getAbsolutePath() + "]出错！");
                        continue;
                    }
                }
                // 替换文件
                replaceFile(sPath, sNewPath, sOldStr, sNewStr);
            }
        }
    }
    /**
     * 替换文件
     * @param sInFileName   输入文件
     * @param sOutFileName  输出文件
     * @param sOldStr       旧字符串
     * @param sNewStr       新字符串
     * @return              true - 成功, false - 失败
     */
    public static boolean replaceFile(String sInFileName, String sOutFileName, String sOldStr, String sNewStr) {
        showAndSaveMsg("---------------------------------------------------------------------------------");
        try {
            showAndSaveMsg("读取报表文件[" + sInFileName + "]...");
            // 读取报表文件
            IReport report = ReportUtils.read(sInFileName);

            // 替换报表
            int iCount = replace(report, sOldStr, sNewStr);
            showAndSaveMsg("合计替换" + iCount + "次...");

            showAndSaveMsg("写入到新文件[" + sOutFileName + "]...");
            // 写入到新文件
            ReportUtils.write(sOutFileName, report);
            return true;
        } catch (Exception e) {
            showAndSaveMsg(e.toString());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 替换报表
     * @param report        报表
     * @param sOldStr       旧字符串
     * @param sNewStr       新字符串
     * @throws Exception    异常
     * @return              替换次数
     */
    public static int replace(IReport report, String sOldStr, String sNewStr) throws Exception {
        int iCount = 0;
        for (int i = 1; i <= report.getRowCount(); i++) {
            for (Short j = 1; j <= report.getColCount(); j++) {
                INormalCell cell = report.getCell(i, j);
                // 对于被合并的单元格，只要处理第一个就可以了，防止重复处理
                if (cell.isMerged()) {
                    Area area = cell.getMergedArea();
                    if (i != area.getBeginRow() || j != area.getBeginCol()) {
                        continue;
                    }
                }

                // 普通值
                Object value = cell.getValue();
                if (value instanceof String) {
                    // 替换普通值
                    if (((String) value).indexOf(sOldStr) >= 0) {
                        String sNewValue = ((String) value).replace(sOldStr, sNewStr);
                        showAndSaveMsg("在单元格[" + i +", " + j + "]替换[" + value + "]为[" + sNewValue + "]");
                        cell.setValue(sNewValue);
                        iCount ++;
                    }
                }

                // 简单表达式
                IByteMap expMap = cell.getExpMap();
                if (null != expMap && !expMap.isEmpty()) {
                    for (Short index = 0; index < expMap.size(); index++) {
                        Object exp = expMap.getValue(index);
                        if (exp instanceof String) {
                            // 替换表达式
                            if (((String) exp).indexOf(sOldStr) >= 0) {
                                String sNewExp = ((String) exp).replace(sOldStr, sNewStr);
                                showAndSaveMsg("在单元格[" + i +", " + j + "]替换[" + exp + "]为[" + sNewExp + "]");
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
     * 去除背景色(自动识别目录/文件)
     * @param sFilePath     文件名
     */
    public static void clearBackgroundColor(String sFilePath) {
        File file = new File(sFilePath);
        sFilePath = file.getAbsolutePath();
        if (!file.exists()) {
            showAndSaveMsg("文件[" + sFilePath + "]不存在！");
            return;
        }
        if (file.isFile()) {
            // 去除背景色
            clearBackgroundColor(sFilePath, sFilePath.replaceFirst("\\.raq$", "_new.raq"));
        } else if (file.isDirectory()) {
            // 旧目录名长度
            int length = file.getAbsolutePath().length();
            // 新目录名
            String sNewBaseDir = convNull(file.getParent()) + File.separator + file.getName() + "_new";
            // 取到所有文件名（全路径）
            List<String> list = new ArrayList<String>();
            dir(file, list);
            for(String sPath : list) {
                // 新文件名（全路径）
                String sNewPath = sNewBaseDir + sPath.substring(length);
                // 父目录如果不存在则创建
                File newFile = new File(sNewPath);
                File newDir = new File(newFile.getParent());
                if (!newDir.exists()) {
                    if (!newDir.mkdirs()) {
                        showAndSaveMsg("创建目录[" + newDir.getAbsolutePath() + "]出错！");
                        continue;
                    }
                }
                // 去除背景色
                clearBackgroundColor(sPath, sNewPath);
            }
        }
    }

    /**
     * 去除背景色
     * @param sInFileName   输入文件
     * @param sOutFileName  输出文件
     * @return              true - 成功, false - 失败
     */
    public static boolean clearBackgroundColor(String sInFileName, String sOutFileName) {
        showAndSaveMsg("---------------------------------------------------------------------------------");
        try {
            showAndSaveMsg("读取报表文件[" + sInFileName + "]...");
            // 读取报表文件
            IReport report = ReportUtils.read(sInFileName);

            // 去除背景色
            int iCount = clearBackgroundColor(report);
            showAndSaveMsg("合计" + iCount + "次...");

            showAndSaveMsg("写入到新文件[" + sOutFileName + "]...");
            // 写入到新文件
            ReportUtils.write(sOutFileName, report);
            return true;
        } catch (Exception e) {
            showAndSaveMsg(e.toString());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 去除背景色
     * @param report        报表
     * @throws Exception    异常
     * @return              设置次数
     */
    public static int clearBackgroundColor(IReport report) throws Exception {
        int iCount = 0;
        // "53" 代码背景色
        byte byteBackColorIndex = Byte.parseByte("53");
        for (int i = 1; i <= report.getRowCount(); i++) {
            for (Short j = 1; j <= report.getColCount(); j++) {
                INormalCell cell = report.getCell(i, j);
                // 对于被合并的单元格，只要处理第一个就可以了，防止重复处理
                if (cell.isMerged()) {
                    Area area = cell.getMergedArea();
                    if (i != area.getBeginRow() || j != area.getBeginCol()) {
                        continue;
                    }
                }

                int iBackColor = cell.getBackColor();
                // "16777215" 为无色
                if (16777215 != iBackColor) {
                    showAndSaveMsg("在单元格[" + i +", " + j + "]去除背景色[" + iBackColor + "]");
                    cell.setBackColor(16777215);
                    iCount ++;
                }

                // 简单表达式
                IByteMap expMap = cell.getExpMap();
                if (null != expMap && !expMap.isEmpty()) {
                    // "53" 代表设置背景色的表达式
                    int index = expMap.getIndex(byteBackColorIndex);
                    if (index > 0) {
                        showAndSaveMsg("在单元格[" + i +", " + j + "]去除背景色表达式[" + expMap.getValue(index) + "]");
                        expMap.remove(expMap.getKey(index));
                        iCount ++;
                    }
                }
            }
        }
        return iCount;
    }

    /**
     * 遍历文件
     * @param dir   目录对象
     * @param list  文件名（全路径）列表
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
