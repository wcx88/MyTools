import com.apex.crm.wsclient.*;

import javax.xml.namespace.QName;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;

public class ClientDemo {
    public static void main(String[] args) {
        ClientDemo client = new ClientDemo();
        System.out.println("-----------------------------cif通用查询接口案例-----------------------");
        client.cifQuery();
        System.out.println("-----------------------------cif查询非现场开户申请接口案例-----------------------");
        client.cifCXFXCKHSQ();
        System.out.println("-----------------------------公民身份验证_发送查询接口案例-----------------------");
        client.cifwsGMSFYZ_FSCX();
    }

    private LBEBusinessService serviePort;
    private String sessionId;
    private static final QName SERVICE_NAME = new QName("http://ws.livebos.apex.com/", "LBEBusinessWebService");

    public LBEBusinessService getServiePort() {
        if (serviePort == null) {
            LBEBusinessWebService service;
            URL wsdlURL = null;
            try {
                //wsdlURL = new URL("http://cif.crm.apexsoft.com.cn/service/LBEBusiness?wsdl");
                wsdlURL = new URL("http://192.168.10.40:8820/service/LBEBusiness?wsdl");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            service = new LBEBusinessWebService(wsdlURL, SERVICE_NAME);
            serviePort = service.getLBEBusinessServiceImplPort();
        }
        return serviePort;
    }

    protected void printQueryResult(QueryResult qrs) {
        System.out.println("result message: " + qrs.getMessage());
        System.out.println("count:" + qrs.getCount() + " hasMore:" + qrs.isHasMore());
        System.out.println("size:" + qrs.getRecords().size());
        for (Iterator<ColInfo> it = qrs.getMetaData().getColInfo().iterator(); it.hasNext();) {
            ColInfo colInfo = it.next();
            System.out.print(colInfo.getLabel() + "]\t");
        }
        System.out.println("\n====================================data-start===========================================");
        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {
            LbRecord record = it.next();
            for (Iterator<Object> itVal = record.getValues().iterator(); itVal.hasNext();) {
                System.out.print(itVal.next() + "\t");
            }
            System.out.println("");
        }
        System.out.println("=====================================data-end==========================================");
    }

    public LoginResult login(LBEBusinessService client) {
        // WebService 用户:webuser/111111
        LoginResult result = client.login("webuser", "111111", "", "plain", "");

        System.out.println("result login message: " + result.getMessage());
        System.out.println("result sessionId: " + result.getSessionId());
        return result;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        if (sessionId == null) {
            LoginResult result = login(getServiePort());
            sessionId = result.getSessionId();
        }
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public static LbParameter createLbParameter(String name, String value) {
        LbParameter param = new LbParameter();
        param.setName(name);
        param.setValue(value);
        return param;
    }
    // cif通用查询
    public void cifQuery() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("DXID","102")); // 对象ID  参看"表结构"页中定义的对象ID
        params.add(createLbParameter("CXTJ","ROWNUM < 3")); // 查询条件  如：Field1= '1' and Field2 = '2002'
        QueryResult qrs = client.query(getSessionId(), "cifQuery", params, null, queryOption);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        List<ColInfo> listCols = qrs.getMetaData().getColInfo();
        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {
            LbRecord record = it.next();
            List listValues = record.getValues();
            Map<String, Object> mapData = new HashMap<String, Object>();
            for (int j = 0; j < listCols.size(); j++) {
                mapData.put(listCols.get(j).getLabel(), listValues.get(j));
            }
            listData.add(mapData);
        }
        
        printQueryResult(qrs); // 控制抬打印全部结果集 (根据传参""对象ID""不同，返回的结果集也不同)
    }

    // cif查询风险测评题目
    public void cifCXFXCPTM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("WJBM","值")); // 问卷编码  取开户默认属性表(TKH_MRSX)中的问卷编码(WJBM)
        params.add(createLbParameter("JGBZ","值")); // 机构标志  0-个人,1-机构
        QueryResult qrs = client.query(getSessionId(), "cifCXFXCPTM", params, null, queryOption);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        List<ColInfo> listCols = qrs.getMetaData().getColInfo();
        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {
            LbRecord record = it.next();
            List listValues = record.getValues();
            Map<String, Object> mapData = new HashMap<String, Object>();
            for (int j = 0; j < listCols.size(); j++) {
                mapData.put(listCols.get(j).getLabel(), listValues.get(j));
            }
            listData.add(mapData);
        }
        for(Map<String, Object> mapData : listData) {
            System.out.print("WJBM = " + mapData.get("WJBM") + "\t "); // 问卷编码  
            System.out.print("WJMC = " + mapData.get("WJMC") + "\t "); // 文件名称  
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 机构标志  0-个人,1-机构,-1表示不限
            System.out.print("WJID = " + mapData.get("WJID") + "\t "); // 问卷ID  
            System.out.print("QID = " + mapData.get("QID") + "\t "); // 题目ID  
            System.out.print("QTYPE = " + mapData.get("QTYPE") + "\t "); // 题目类型  0|单选题;1|多选题;2|判断题;3|填空题;4|简答题;5|论述题
            System.out.print("QDESCRIBE = " + mapData.get("QDESCRIBE") + "\t "); // 题目描述  
            System.out.print("SANSWER = " + mapData.get("SANSWER") + "\t "); // 备选答案  本题的答案列表，以分号分割不同的答案。示例：A|A.两年之内（短期）;B|B.2－5年（中期）;C|C.6年以上（中长期或长期）
            System.out.print("ANSWER = " + mapData.get("ANSWER") + "\t "); // 答案  本题的指定答案：如选C
            System.out.print("SCORE = " + mapData.get("SCORE") + "\t "); // 得分  本题指定答案对应得分：如8分

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // 客户风险测评_风险测评
    public void cifwsKHFXCP_FXCP() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("CPGY","值")); // 测评柜员  给客户进行评测的柜员ID
        params.add(createLbParameter("CZZD","值")); // 操作站点  
        params.add(createLbParameter("KHH","值")); // 客户号  
        params.add(createLbParameter("WJID","值")); // 问卷ID  本调查问卷的ID号
        params.add(createLbParameter("ZJLB","值")); // 证件类别  
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("KHXM","值")); // 客户姓名  
        params.add(createLbParameter("FXCSNL","值")); // 风险承受能力  
        params.add(createLbParameter("TMDAC","值")); // 题目答案串  题目与答案间以"|"分割，多个题目间以";"号分割。示例:25|A;26|B;27|A;28|C;29|B;30|C;31|A
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsKHFXCP_FXCP", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // 返回代码  >0成功,<=0失败
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // 返回说明  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // 返回流水号  
        }
    }

    // 公民身份验证_发送查询
    public void cifwsGMSFYZ_FSCX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
		params.add(createLbParameter("GYID","1")); // 柜员ID  登录柜员的ID
		params.add(createLbParameter("ZJLB","0")); // 证件类别
		params.add(createLbParameter("ZJBH","350521198406188556")); // 证件编号
		params.add(createLbParameter("KHXM","郑海鹏")); // 客户姓名
		params.add(createLbParameter("CXLX","0")); // 查询类型  强制送"1"

        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsGMSFYZ_FSCX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // 返回代码  >0成功,<=0失败
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // 返回说明  
            System.out.println("O_FLAG = " + mapOutputVar.get("O_FLAG")); // 标志  1-从TCIF_GMZJCXJG表返回数据2-从TGMSFCXSQ表返回数据
            System.out.println("O_ID = " + mapOutputVar.get("O_ID")); // 输出ID值  
            System.out.println("O_YZRQ = " + mapOutputVar.get("O_YZRQ")); // 验证日期  
            System.out.println("O_SFZH = " + mapOutputVar.get("O_SFZH")); // 身份证号  
            System.out.println("O_CSRQ = " + mapOutputVar.get("O_CSRQ")); // 出生日期  
            System.out.println("O_XM = " + mapOutputVar.get("O_XM")); // 姓名  
            System.out.println("O_NATION = " + mapOutputVar.get("O_NATION")); // 国籍  
            System.out.println("O_XB = " + mapOutputVar.get("O_XB")); // 性别  
        }
    }

    // 中登验证_中登账户查询
    public void cifwsZDYZ_ZDZHCX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GYID","值")); // 柜员ID  登录柜员的ID
        params.add(createLbParameter("ZJLB","值")); // 证件类别  
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("KHXM","值")); // 客户姓名  
        params.add(createLbParameter("GJDM","值")); // 国籍代码  强制送"156"
        params.add(createLbParameter("SCDM","值")); // 市场代码  强制送空
        params.add(createLbParameter("CFBZ","值")); // 重发标志  0-表示存在申请不发送,1-强制重发.默认送"0"
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsZDYZ_ZDZHCX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // 返回代码  >0成功,<=0失败
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // 返回说明  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // 返回流水号  
        }
    }

    // 影像管理_上传影像
    public void cifwsMedia_Upload() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("BASE64STR","值")); // 图片BASE64编码串  
        params.add(createLbParameter("FILENAME","值")); // 文件名  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsMedia_Upload", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_FILEPATH = " + mapOutputVar.get("O_FILEPATH")); // 返回影像路径加密串  
        }
    }

    // 影像管理_下载影像
    public void cifwsMedia_Download() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FILEPATH","值")); // 影像路径加密串  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsMedia_Download", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_BASE64STR = " + mapOutputVar.get("O_BASE64STR")); // 返回图片BASE64编码串  
        }
    }

    // 非现场开户_开户申请
    public void cifwsFXCKH_KHSQ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","值")); // 开户方式  1|临柜开户;2|见证开户;3|网上开户. 当前填写"2"
        params.add(createLbParameter("JGBZ","值")); // 客户类型  0|个人;1|机构
        params.add(createLbParameter("YYB","值")); // 开户营业部  
        params.add(createLbParameter("KHLY","值")); // 客户来源  TXTDM.FLDM=GT_YYKHLY(1-网站预约开户;2-经纪人预开户;3-银行预约开户;8-新客户;9-老客户),当前填写"8"
        params.add(createLbParameter("KHXM","值")); // 客户姓名  
        params.add(createLbParameter("KHQC","值")); // 客户全称  
        params.add(createLbParameter("JZR","值")); // 见证人  见证开户时必填，其他开户方式时不填
        params.add(createLbParameter("KHXZR","值")); // 开户协助人  见证开户时必填，其他开户方式时不填
        params.add(createLbParameter("ZJLB","值")); // 证件类别  
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("ZJYXQ","值")); // 证件有效期  
        params.add(createLbParameter("ZJDZ","值")); // 证件地址  
        params.add(createLbParameter("ZJDZYB","值")); // 证件地址邮编  
        params.add(createLbParameter("ZJFZJG","值")); // 发证机关  
        params.add(createLbParameter("ZJZP","值")); // 证件照片  影像上传成功后返回的字符串
        params.add(createLbParameter("ZJYZLY","值")); // 证件验证来源  TXTDM.FLDM=KH_ZJYZLY(1-二代证读卡器;2-公安部联网)
        params.add(createLbParameter("EDZ","值")); // 二代证校验  
        params.add(createLbParameter("DZ","值")); // 通讯地址  
        params.add(createLbParameter("YZBM","值")); // 邮编  
        params.add(createLbParameter("SJ","值")); // 手机  
        params.add(createLbParameter("DH","值")); // 电话  
        params.add(createLbParameter("CZ","值")); // 传真  
        params.add(createLbParameter("Email","值")); // EMAIL  
        params.add(createLbParameter("GJ","值")); // 国家  
        params.add(createLbParameter("TBSM","值")); // 特别说明  
        params.add(createLbParameter("FXQHYLB","值")); // 反洗钱行业类别  
        params.add(createLbParameter("XQFXDJ","值")); // 洗钱风险等级  
        params.add(createLbParameter("BZ","值")); // 币种  默认填"1"
        params.add(createLbParameter("KHQZ","值")); // 客户群组  
        params.add(createLbParameter("KHKH","值")); // 客户卡号  默认填""
        params.add(createLbParameter("GTKHH","值")); // 客户号  默认填""
        params.add(createLbParameter("WTFS","值")); // 委托方式  
        params.add(createLbParameter("FWXM","值")); // 服务项目  
        params.add(createLbParameter("FXJB","值")); // 风险级别  默认填""
        params.add(createLbParameter("FXCSNL","值")); // 基金风险承受能力  
        params.add(createLbParameter("GPFXCSNL","值")); // 股票风险承受能力  默认填""
        params.add(createLbParameter("SFTB","值")); // 是否同步  默认填""
        params.add(createLbParameter("JYMM","值")); // 交易密码  默认填""
        params.add(createLbParameter("ZJMM","值")); // 资金密码  默认填""
        params.add(createLbParameter("FWMM","值")); // 服务密码  默认填""
        params.add(createLbParameter("SQJJZH","值")); // 申请基金账号  
        params.add(createLbParameter("GDKH_SH","值")); // 沪A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SH","值")); // 登记沪A股东号  填入沪A股东账户
        params.add(createLbParameter("GDKH_HB","值")); // 沪B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_HB","值")); // 登记沪B股东号  默认填""
        params.add(createLbParameter("GDKH_SZ","值")); // 深A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SZ","值")); // 登记深A股东号  填入深A股东账户
        params.add(createLbParameter("GDKH_SB","值")); // 深B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_SB","值")); // 登记深B股东号  默认填""
        params.add(createLbParameter("GDKH_TA","值")); // 三板A股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_TA","值")); // 登记三板A股东号  默认填""
        params.add(createLbParameter("GDKH_TU","值")); // 三板B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_TU","值")); // 登记三板B股东号  默认填""
        params.add(createLbParameter("GDJYQX_SH","值")); // 沪A股东交易权限  
        params.add(createLbParameter("GDJYQX_HB","值")); // 沪B股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_SZ","值")); // 深A股东交易权限  
        params.add(createLbParameter("GDJYQX_SB","值")); // 深B股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_TA","值")); // 三板A股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_TU","值")); // 三板B股东交易权限  默认填""
        params.add(createLbParameter("SHZDJY","值")); // 沪A指定  默认填""
        params.add(createLbParameter("HBZDJY","值")); // 沪B指定  默认填""
        params.add(createLbParameter("SFWLFW","值")); // 是否需要网络服务  默认填""
        params.add(createLbParameter("WLFWMM","值")); // 网络服务密码  默认填""
        params.add(createLbParameter("CGYH","值")); // 存管银行  
        params.add(createLbParameter("CGYHZH","值")); // 存管银行账户  默认填""
        params.add(createLbParameter("CGYHMM","值")); // 存管银行密码  默认填""
        params.add(createLbParameter("YHDM_USD","值")); // 美元转账银行  默认填""
        params.add(createLbParameter("DJFS_USD","值")); // 美元登记方式  默认填""
        params.add(createLbParameter("YHZH_USD","值")); // 美元银行账号  默认填""
        params.add(createLbParameter("YHMM_USD","值")); // 美元银行密码  默认填""
        params.add(createLbParameter("YHDM_HKD","值")); // 港币转账银行  默认填""
        params.add(createLbParameter("DJFS_HKD","值")); // 港币登记方式  默认填""
        params.add(createLbParameter("YHZH_HKD","值")); // 港币银行账号  默认填""
        params.add(createLbParameter("YHMM_HKD","值")); // 港币银行密码  默认填""
        params.add(createLbParameter("KHZP","值")); // 客户照片  影像上传成功后返回的字符串
        params.add(createLbParameter("KHSP","值")); // 客户视频  视频上传成功后返回的字符串
        params.add(createLbParameter("DJR","值")); // 操作人  
        params.add(createLbParameter("CZZD","值")); // 操作站点  
        params.add(createLbParameter("IBY1","值")); // IBY1  默认填""
        params.add(createLbParameter("IBY2","值")); // IBY2  默认填""
        params.add(createLbParameter("IBY3","值")); // IBY3  默认填""
        params.add(createLbParameter("CBY1","值")); // CBY1  默认填""
        params.add(createLbParameter("CBY2","值")); // CBY2  默认填""
        params.add(createLbParameter("CBY3","值")); // CBY3  默认填""
        params.add(createLbParameter("LXRXXSTR","值")); // 联系人信息串  示例：[{"LXRXM":"姚明明", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"领导"}, {"LXRXM":"李小四", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"下属"}]
        params.add(createLbParameter("DLRSTR","值")); // 代理人串  示例：[{"DLRXM":"张一三", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"长期",  "ZJFZJG":"福州市鼓楼公安局", "CSRQ":"19840101", "ZJDZ":"福州市湖东路165号", "DZ":"福州市五四路150号",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
        params.add(createLbParameter("YXSTR","值")); // 影像串  示例：[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        params.add(createLbParameter("JGKGGDSTR","值")); // 机构控股股东串  默认填""
        params.add(createLbParameter("DJJJZHSTR","值")); // 登记基金账号  默认填""
        params.add(createLbParameter("FJSXSTR","值")); // 附加属性串  默认填""
        params.add(createLbParameter("WJID","值")); // 问卷ID  本调查问卷的ID号
        params.add(createLbParameter("TMDAC","值")); // 题目答案串  题目与答案间以"|"分割，多个题目间以";"号分割。示例:25|A;26|B;27|A;28|C;29|B;30|C;31|A
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_KHSQ", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // 返回代码  >0成功,<=0失败
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // 返回说明  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // 返回流水号  
        }
    }

    // cif查询非现场开户申请
    public void cifCXFXCKHSQ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","1")); // 查询标志  1-按申请ID查询,2-按发起人查询(见证开户),3-按手机号查询(网上开户)
        params.add(createLbParameter("SQID","1")); // 申请ID  FLAG=1时必填
        params.add(createLbParameter("GYID","")); // 柜员ID  FLAG=2时必填
        params.add(createLbParameter("SJ","")); // 手机  FLAG=3时必填
        params.add(createLbParameter("KSRQ","")); // 开始日期  yyyymmdd格式,非必填
        params.add(createLbParameter("JSRQ","")); // 结束日期  yyyymmdd格式,非必填
        params.add(createLbParameter("KHFS","")); // 开户方式  1|临柜开户;2|见证开户;3|网上开户
        params.add(createLbParameter("CXNR","")); // 查询内容  支持对客户姓名、身份证号进行模糊查询
        QueryResult qrs = client.query(getSessionId(), "cifCXFXCKHSQ", params, null, queryOption);
        List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
        List<ColInfo> listCols = qrs.getMetaData().getColInfo();
        for (Iterator<LbRecord> it = qrs.getRecords().iterator(); it.hasNext();) {
            LbRecord record = it.next();
            List listValues = record.getValues();
            Map<String, Object> mapData = new HashMap<String, Object>();
            for (int j = 0; j < listCols.size(); j++) {
                mapData.put(listCols.get(j).getLabel(), listValues.get(j));
            }
            listData.add(mapData);
        }
        for(Map<String, Object> mapData : listData) {
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // 开户方式  1|临柜开户;2|见证开户;3|网上开户. 当前填写"2"
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 客户类型  0|个人;1|机构
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // 开户营业部  
            System.out.print("KHLY = " + mapData.get("KHLY") + "\t "); // 客户来源  TXTDM.FLDM=GT_YYKHLY(1-网站预约开户;2-经纪人预开户;3-银行预约开户;8-新客户;9-老客户),当前填写"8"
            System.out.print("KHXM = " + mapData.get("KHXM") + "\t "); // 客户姓名  
            System.out.print("KHQC = " + mapData.get("KHQC") + "\t "); // 客户全称  
            System.out.print("JZR = " + mapData.get("JZR") + "\t "); // 见证人  见证开户时必填，其他开户方式时不填
            System.out.print("KHXZR = " + mapData.get("KHXZR") + "\t "); // 开户协助人  见证开户时必填，其他开户方式时不填
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // 证件类别  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // 证件编号  
            System.out.print("ZJYXQ = " + mapData.get("ZJYXQ") + "\t "); // 证件有效期  
            System.out.print("ZJDZ = " + mapData.get("ZJDZ") + "\t "); // 证件地址  
            System.out.print("ZJDZYB = " + mapData.get("ZJDZYB") + "\t "); // 证件地址邮编  
            System.out.print("ZJFZJG = " + mapData.get("ZJFZJG") + "\t "); // 发证机关  
            System.out.print("ZJZP = " + mapData.get("ZJZP") + "\t "); // 证件照片  影像上传成功后返回的字符串
            System.out.print("ZJYZLY = " + mapData.get("ZJYZLY") + "\t "); // 证件验证来源  TXTDM.FLDM=KH_ZJYZLY(1-二代证读卡器;2-公安部联网)
            System.out.print("EDZ = " + mapData.get("EDZ") + "\t "); // 二代证校验  
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // 通讯地址  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // 邮编  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // 手机  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // 电话  
            System.out.print("CZ = " + mapData.get("CZ") + "\t "); // 传真  
            System.out.print("Email = " + mapData.get("Email") + "\t "); // EMAIL  
            System.out.print("GJ = " + mapData.get("GJ") + "\t "); // 国家  
            System.out.print("TBSM = " + mapData.get("TBSM") + "\t "); // 特别说明  
            System.out.print("FXQHYLB = " + mapData.get("FXQHYLB") + "\t "); // 反洗钱行业类别  
            System.out.print("XQFXDJ = " + mapData.get("XQFXDJ") + "\t "); // 洗钱风险等级  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // 币种  默认填"1"
            System.out.print("KHQZ = " + mapData.get("KHQZ") + "\t "); // 客户群组  
            System.out.print("KHKH = " + mapData.get("KHKH") + "\t "); // 客户卡号  默认填""
            System.out.print("GTKHH = " + mapData.get("GTKHH") + "\t "); // 客户号  默认填""
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // 委托方式  
            System.out.print("FWXM = " + mapData.get("FWXM") + "\t "); // 服务项目  
            System.out.print("FXJB = " + mapData.get("FXJB") + "\t "); // 风险级别  默认填""
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // 基金风险承受能力  
            System.out.print("GPFXCSNL = " + mapData.get("GPFXCSNL") + "\t "); // 股票风险承受能力  默认填""
            System.out.print("SFTB = " + mapData.get("SFTB") + "\t "); // 是否同步  默认填""
            System.out.print("JYMM = " + mapData.get("JYMM") + "\t "); // 交易密码  默认填""
            System.out.print("ZJMM = " + mapData.get("ZJMM") + "\t "); // 资金密码  默认填""
            System.out.print("FWMM = " + mapData.get("FWMM") + "\t "); // 服务密码  默认填""
            System.out.print("SQJJZH = " + mapData.get("SQJJZH") + "\t "); // 申请基金账号  
            System.out.print("GDKH_SH = " + mapData.get("GDKH_SH") + "\t "); // 沪A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
            System.out.print("GDDJ_SH = " + mapData.get("GDDJ_SH") + "\t "); // 登记沪A股东号  填入沪A股东账户
            System.out.print("GDKH_HB = " + mapData.get("GDKH_HB") + "\t "); // 沪B股东开户  默认填"0"
            System.out.print("GDDJ_HB = " + mapData.get("GDDJ_HB") + "\t "); // 登记沪B股东号  默认填""
            System.out.print("GDKH_SZ = " + mapData.get("GDKH_SZ") + "\t "); // 深A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
            System.out.print("GDDJ_SZ = " + mapData.get("GDDJ_SZ") + "\t "); // 登记深A股东号  填入深A股东账户
            System.out.print("GDKH_SB = " + mapData.get("GDKH_SB") + "\t "); // 深B股东开户  默认填"0"
            System.out.print("GDDJ_SB = " + mapData.get("GDDJ_SB") + "\t "); // 登记深B股东号  默认填""
            System.out.print("GDKH_TA = " + mapData.get("GDKH_TA") + "\t "); // 三板A股东开户  默认填"0"
            System.out.print("GDDJ_TA = " + mapData.get("GDDJ_TA") + "\t "); // 登记三板A股东号  默认填""
            System.out.print("GDKH_TU = " + mapData.get("GDKH_TU") + "\t "); // 三板B股东开户  默认填"0"
            System.out.print("GDDJ_TU = " + mapData.get("GDDJ_TU") + "\t "); // 登记三板B股东号  默认填""
            System.out.print("GDJYQX_SH = " + mapData.get("GDJYQX_SH") + "\t "); // 沪A股东交易权限  
            System.out.print("GDJYQX_HB = " + mapData.get("GDJYQX_HB") + "\t "); // 沪B股东交易权限  默认填""
            System.out.print("GDJYQX_SZ = " + mapData.get("GDJYQX_SZ") + "\t "); // 深A股东交易权限  
            System.out.print("GDJYQX_SB = " + mapData.get("GDJYQX_SB") + "\t "); // 深B股东交易权限  默认填""
            System.out.print("GDJYQX_TA = " + mapData.get("GDJYQX_TA") + "\t "); // 三板A股东交易权限  默认填""
            System.out.print("GDJYQX_TU = " + mapData.get("GDJYQX_TU") + "\t "); // 三板B股东交易权限  默认填""
            System.out.print("SHZDJY = " + mapData.get("SHZDJY") + "\t "); // 沪A指定  默认填""
            System.out.print("HBZDJY = " + mapData.get("HBZDJY") + "\t "); // 沪B指定  默认填""
            System.out.print("SFWLFW = " + mapData.get("SFWLFW") + "\t "); // 是否需要网络服务  默认填""
            System.out.print("WLFWMM = " + mapData.get("WLFWMM") + "\t "); // 网络服务密码  默认填""
            System.out.print("CGYH = " + mapData.get("CGYH") + "\t "); // 存管银行  
            System.out.print("CGYHZH = " + mapData.get("CGYHZH") + "\t "); // 存管银行账户  默认填""
            System.out.print("CGYHMM = " + mapData.get("CGYHMM") + "\t "); // 存管银行密码  默认填""
            System.out.print("YHDM_USD = " + mapData.get("YHDM_USD") + "\t "); // 美元转账银行  默认填""
            System.out.print("DJFS_USD = " + mapData.get("DJFS_USD") + "\t "); // 美元登记方式  默认填""
            System.out.print("YHZH_USD = " + mapData.get("YHZH_USD") + "\t "); // 美元银行账号  默认填""
            System.out.print("YHMM_USD = " + mapData.get("YHMM_USD") + "\t "); // 美元银行密码  默认填""
            System.out.print("YHDM_HKD = " + mapData.get("YHDM_HKD") + "\t "); // 港币转账银行  默认填""
            System.out.print("DJFS_HKD = " + mapData.get("DJFS_HKD") + "\t "); // 港币登记方式  默认填""
            System.out.print("YHZH_HKD = " + mapData.get("YHZH_HKD") + "\t "); // 港币银行账号  默认填""
            System.out.print("YHMM_HKD = " + mapData.get("YHMM_HKD") + "\t "); // 港币银行密码  默认填""
            System.out.print("KHZP = " + mapData.get("KHZP") + "\t "); // 客户照片  影像上传成功后返回的字符串
            System.out.print("KHSP = " + mapData.get("KHSP") + "\t "); // 客户视频  视频上传成功后返回的字符串
            System.out.print("DJR = " + mapData.get("DJR") + "\t "); // 操作人  
            System.out.print("CZZD = " + mapData.get("CZZD") + "\t "); // 操作站点  
            System.out.print("IBY1 = " + mapData.get("IBY1") + "\t "); // IBY1  默认填""
            System.out.print("IBY2 = " + mapData.get("IBY2") + "\t "); // IBY2  默认填""
            System.out.print("IBY3 = " + mapData.get("IBY3") + "\t "); // IBY3  默认填""
            System.out.print("CBY1 = " + mapData.get("CBY1") + "\t "); // CBY1  默认填""
            System.out.print("CBY2 = " + mapData.get("CBY2") + "\t "); // CBY2  默认填""
            System.out.print("CBY3 = " + mapData.get("CBY3") + "\t "); // CBY3  默认填""
            System.out.print("LXRXXSTR = " + mapData.get("LXRXXSTR") + "\t "); // 联系人信息串  示例：[{"LXRXM":"姚明明", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"领导"}, {"LXRXM":"李小四", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"下属"}]
            System.out.print("DLRSTR = " + mapData.get("DLRSTR") + "\t "); // 代理人串  示例：[{"DLRXM":"张一三", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"长期",  "ZJFZJG":"福州市鼓楼公安局", "CSRQ":"19840101", "ZJDZ":"福州市湖东路165号", "DZ":"福州市五四路150号",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
            System.out.print("YXSTR = " + mapData.get("YXSTR") + "\t "); // 影像串  示例：[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
            System.out.print("JGKGGDSTR = " + mapData.get("JGKGGDSTR") + "\t "); // 机构控股股东串  默认填""
            System.out.print("DJJJZHSTR = " + mapData.get("DJJJZHSTR") + "\t "); // 登记基金账号  默认填""
            System.out.print("FJSXSTR = " + mapData.get("FJSXSTR") + "\t "); // 附加属性串  默认填""
            System.out.print("CPSCORE = " + mapData.get("CPSCORE") + "\t "); // 测评得分  
            System.out.print("CPFXCSNL = " + mapData.get("CPFXCSNL") + "\t "); // 测评风险承受能力  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // 上海首次交易日查询
    public void cifwsSHSCJYR_CX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GYID","值")); // 柜员ID  登录柜员的ID
        params.add(createLbParameter("ZQZH","值")); // 证券账号  
        params.add(createLbParameter("JYS","值")); // 交易所  送1
        params.add(createLbParameter("CFBZ","值")); // 重发标志  0-表示存在申请不发送,1-强制重发.默认送"0"
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsSHSCJYR_CX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // 所有出参
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("出参:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // 出参
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // 返回代码  >0成功,<=0失败
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // 返回说明  
        }
    }

}
