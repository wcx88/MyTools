import com.apex.crm.wsclient.*;

import javax.xml.namespace.QName;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;

public class ClientDemo {
    public static void main(String[] args) {
         ClientDemo client = new ClientDemo();
        //client.cifQuery();
        client.cifCXFXCKHSQ();
        //client.cifwsGMSFYZ_FSCX();
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
        params.add(createLbParameter("TMDAC","值")); // 题目答案串  题目与答案间以"|"分割，多个题目间以";"号分割，多选题间答案以","分割，示例:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
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
        params.add(createLbParameter("GYID","值")); // 柜员ID  登录柜员的ID
        params.add(createLbParameter("ZJLB","值")); // 证件类别  
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("KHXM","值")); // 客户姓名  
        params.add(createLbParameter("CXLX","值")); // 查询类型  强制送"1"
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
            System.out.println("O_NATION = " + mapOutputVar.get("O_NATION")); // 民族  
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

    // 非现场开户(恒生柜台)_开户申请
    public void cifwsFXCKH_HS_KHSQ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","值")); // 开户方式  1|临柜开户;2|见证开户;3|网上开户;11|全员开户
        params.add(createLbParameter("KHZD","值")); // 开户终端  1|Web方式;2|Android客户端;3|iPad客户端
        params.add(createLbParameter("JGBZ","值")); // 客户类型  0|个人;1|机构
        params.add(createLbParameter("YYB","值")); // 开户营业部  
        params.add(createLbParameter("KHLY","值")); // 客户来源  填写"8"(新客户)
        params.add(createLbParameter("KHXM","值")); // 客户姓名  
        params.add(createLbParameter("KHQC","值")); // 客户全称  
        params.add(createLbParameter("JZR","值")); // 见证人  见证开户时必填，其他开户方式时不填
        params.add(createLbParameter("KHXZR","值")); // 开户协助人  见证开户时必填，其他开户方式时不填
        params.add(createLbParameter("YYKH","值")); // 预约开户  1|预约开户;0|直接开户
        params.add(createLbParameter("ZJLB","值")); // 证件类别  数据字典.分类代码=GT_ZJLB
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("ZJQSRQ","值")); // 证件起始日期  yyyymmdd格式
        params.add(createLbParameter("ZJJZRQ","值")); // 证件截止日期  yyyymmdd格式
        params.add(createLbParameter("ZJDZ","值")); // 证件地址  
        params.add(createLbParameter("ZJDZYB","值")); // 证件地址邮编  
        params.add(createLbParameter("ZJFZJG","值")); // 发证机关  
        params.add(createLbParameter("ZJZP","值")); // 证件照片  影像上传成功后返回的字符串
        params.add(createLbParameter("ZJYZLY","值")); // 证件验证来源  1|二代证读卡器;2|公安部联网
        params.add(createLbParameter("EDZ","值")); // 二代证校验  0|未通过读卡器或公安部联网验证;1|通过读卡器或公安部联网验证
        params.add(createLbParameter("YYZZNJRQ","值")); // 营业执照年检日期  yyyymmdd格式
        params.add(createLbParameter("DZ","值")); // 通讯地址  
        params.add(createLbParameter("YZBM","值")); // 邮编  
        params.add(createLbParameter("JTDZ","值")); // 家庭地址  
        params.add(createLbParameter("JTDZYB","值")); // 家庭地址邮编  
        params.add(createLbParameter("JTDH","值")); // 家庭电话  
        params.add(createLbParameter("SJ","值")); // 手机  
        params.add(createLbParameter("DH","值")); // 电话  
        params.add(createLbParameter("CZ","值")); // 传真  
        params.add(createLbParameter("EMAIL","值")); // EMAIL  
        params.add(createLbParameter("GZDW","值")); // 工作单位  
        params.add(createLbParameter("GZDWDZ","值")); // 单位地址  
        params.add(createLbParameter("GZDWYB","值")); // 单位邮编  
        params.add(createLbParameter("GZDWDH","值")); // 单位电话  
        params.add(createLbParameter("QQ","值")); // QQ  
        params.add(createLbParameter("MSN","值")); // MSN  
        params.add(createLbParameter("HYZK","值")); // 婚姻状况  数据字典.分类代码=GT_HYZK
        params.add(createLbParameter("ZYDM","值")); // 职业  数据字典.分类代码=GT_ZYDM
        params.add(createLbParameter("XL","值")); // 学历  数据字典.分类代码=GT_XLDM
        params.add(createLbParameter("MZDM","值")); // 民族  数据字典.分类代码=MZDM
        params.add(createLbParameter("JG","值")); // 籍贯  
        params.add(createLbParameter("GJ","值")); // 国家  通过接口cifQueryGJDM(cif查询国籍代码)获取到的IBM(数字编码)
        params.add(createLbParameter("PROVINCE","值")); // 省份  通过接口cifQuerySFDM(cif查询省份代码)获取到的IBM(数字编码)
        params.add(createLbParameter("CITY","值")); // 城市  通过接口cifQueryCSDM(cif查询城市代码)获取到的POST(城市代码)
        params.add(createLbParameter("SEC","值")); // 区县  通过接口cifQueryCSDM(cif查询城市代码)获取到的POST(城市代码)
        params.add(createLbParameter("TBSM","值")); // 特别说明  
        params.add(createLbParameter("ZWDM","值")); // 职位  数据字典.分类代码=GT_ZWDM
        params.add(createLbParameter("NJSR","值")); // 年均收入  
        params.add(createLbParameter("ZGZSLX","值")); // 资格证书类型  数据字典.分类代码=GT_ZGZSLX
        params.add(createLbParameter("ZSBH","值")); // 证书编号  
        params.add(createLbParameter("CYSJ","值")); // 从业时间  yyyymmdd格式
        params.add(createLbParameter("JSFS","值")); // 寄送方式  数据字典.分类代码=GT_JSFS
        params.add(createLbParameter("LXFS","值")); // 联系方式  数据字典.分类代码=GT_LXFS
        params.add(createLbParameter("LLPL","值")); // 联络频率  数据字典.分类代码=GT_LLPL
        params.add(createLbParameter("GTKHLY","值")); // 柜台客户来源  数据字典.分类代码=GT_KHLY
        params.add(createLbParameter("QSXY","值")); // 签署协议  数据字典.分类代码=GT_QSXY
        params.add(createLbParameter("SFYZWT","值")); // 身份验证问题  数据字典.分类代码=KH_SFYZWT
        params.add(createLbParameter("SFYZDA","值")); // 身份验证答案  
        params.add(createLbParameter("FXQHYLB","值")); // 反洗钱行业类别  数据字典.分类代码=FXQHYLB
        params.add(createLbParameter("XQFXDJ","值")); // 洗钱风险等级  数据字典.分类代码=XQFXDJ
        params.add(createLbParameter("KHMB","值")); // 客户模板  客户模板ID
        params.add(createLbParameter("ZCSX","值")); // 资产属性  默认填""
        params.add(createLbParameter("KHFL","值")); // 客户分类  默认填""
        params.add(createLbParameter("KHFZ","值")); // 客户分组  默认填""
        params.add(createLbParameter("WTFS","值")); // 委托方式  填前端界面选择的委托方式
        params.add(createLbParameter("KHQX","值")); // 客户权限  填前端界面选择的客户权限
        params.add(createLbParameter("KHXZ","值")); // 客户限制  默认填""
        params.add(createLbParameter("GDQX","值")); // 股东权限  默认填""
        params.add(createLbParameter("GDXZ","值")); // 股东限制  默认填""
        params.add(createLbParameter("CBLX","值")); // 成本类型  默认填""
        params.add(createLbParameter("LLLB","值")); // 利率类别  默认填""
        params.add(createLbParameter("KHGFXX","值")); // 开户规范信息  默认填""
        params.add(createLbParameter("GSKHLX","值")); // 公司客户类型  默认填""
        params.add(createLbParameter("FXYSXX","值")); // 风险要素信息  默认填""
        params.add(createLbParameter("YXBZ","值")); // 允许币种  1|人民币;2|美元;3|港币填前端界面选择的客户开通币种,多种币种间以";"号分割，如"1;2;3"
        params.add(createLbParameter("CPBZ","值")); // 产品标志  默认填""
        params.add(createLbParameter("KHKH","值")); // 客户卡号  
        params.add(createLbParameter("KHH","值")); // 客户号  
        params.add(createLbParameter("FXCSNL","值")); // 风险承受能力  数据字典.分类代码=GT_FXCSNL
        params.add(createLbParameter("KFYYB","值")); // 开发营业部  开发营业部的ID(见证开户,全员开户模式下为开户柜员的归属营业部)
        params.add(createLbParameter("KFGY","值")); // 开发柜员  开发柜员的ID(见证开户,全员开户模式下为开户柜员的ID)
        params.add(createLbParameter("SFTB","值")); // 是否同步  0|不同步;1|同步
        params.add(createLbParameter("JYMM","值")); // 交易密码  默认填""
        params.add(createLbParameter("ZJMM","值")); // 资金密码  默认填""
        params.add(createLbParameter("FWMM","值")); // 服务密码  默认填""
        params.add(createLbParameter("SQJJZH","值")); // 申请基金账号  多选值，多个基金公司ID间以";"号分割，示例"401;411;427"通过接口cifQueryJJGSCS(cif查询基金公司参数)获取到的ID(基金公司ID)列表
        params.add(createLbParameter("GDKH_SH","值")); // 沪A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SH","值")); // 登记沪A股东号  GDKH_SH=9时填入沪A股东账户
        params.add(createLbParameter("GDKH_HB","值")); // 沪B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_HB","值")); // 登记沪B股东号  默认填""
        params.add(createLbParameter("GDKH_SZ","值")); // 深A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SZ","值")); // 登记深A股东号  GDKH_SZ=9时填入深A股东账户
        params.add(createLbParameter("GDKH_SB","值")); // 深B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_SB","值")); // 登记深B股东号  默认填""
        params.add(createLbParameter("GDKH_TA","值")); // 三板A股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_TA","值")); // 登记三板A股东号  默认填""
        params.add(createLbParameter("GDKH_TU","值")); // 三板B股东开户  默认填"0"
        params.add(createLbParameter("GDDJ_TU","值")); // 登记三板B股东号  默认填""
        params.add(createLbParameter("GDJYQX_SH","值")); // 沪A股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_HB","值")); // 沪B股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_SZ","值")); // 深A股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_SB","值")); // 深B股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_TA","值")); // 三板A股东交易权限  默认填""
        params.add(createLbParameter("GDJYQX_TU","值")); // 三板B股东交易权限  默认填""
        params.add(createLbParameter("SHZDJY","值")); // 沪A指定  默认填""
        params.add(createLbParameter("HBZDJY","值")); // 沪B指定  默认填""
        params.add(createLbParameter("SFWLFW","值")); // 是否需要网络服务  默认填""
        params.add(createLbParameter("WLFWMM","值")); // 网络服务密码  默认填""
        params.add(createLbParameter("CGYH","值")); // 存管银行  通过接口cifQueryYHCS(cif查询银行参数)获取到的YHDM(银行代码)
        params.add(createLbParameter("CGYHZH","值")); // 存管银行账户  一步式指定时填入银行账户两步式指定时填""
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
        params.add(createLbParameter("DJR","值")); // 操作人  开户柜员的ID
        params.add(createLbParameter("CZZD","值")); // 操作站点  客户端机器的IP地址或MAC地址
        params.add(createLbParameter("WJID","值")); // 问卷ID  风险测评调查问卷的ID号
        params.add(createLbParameter("TMDAC","值")); // 题目答案串  题目与答案间以"|"分割，多个题目间以";"号分割，多选题间答案以","分割，示例:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
        params.add(createLbParameter("LCWJID","值")); // 理财问卷ID  理财服务信息调查问卷的ID号
        params.add(createLbParameter("LCTMDAC","值")); // 理财题目答案串  题目与答案间以"|"分割，多个题目间以";"号分割，多选题间答案以","分割，示例:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
        params.add(createLbParameter("LXRXXSTR","值")); // 联系人信息串  示例：[{"LXRXM":"姚明明", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"领导"}, {"LXRXM":"李小四", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"下属"}]
        params.add(createLbParameter("DLRSTR","值")); // 代理人串  示例：[{"DLRXM":"张一三", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"长期",  "ZJFZJG":"福州市鼓楼公安局", "CSRQ":"19840101", "ZJDZ":"福州市湖东路165号", "DZ":"福州市五四路150号",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
        params.add(createLbParameter("YXSTR","值")); // 影像串  示例：[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        params.add(createLbParameter("JGKGGDSTR","值")); // 机构控股股东串  默认填""
        params.add(createLbParameter("DJJJZHSTR","值")); // 登记基金账号  默认填""
        params.add(createLbParameter("FJSXSTR","值")); // 附加属性串  默认填""
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_HS_KHSQ", "", params, null);
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
        params.add(createLbParameter("FLAG","值")); // 查询标志  1-按申请ID查询[返回以下所有出参]2-按发起人查询(见证开户)[返回字段ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]3-按手机号查询(网上开户)[返回字段ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]4-按证件编号查询(网上开户)[返回字段ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]
        params.add(createLbParameter("SQID","值")); // 申请ID  FLAG=1时必填
        params.add(createLbParameter("GYID","值")); // 柜员ID  FLAG=2时必填
        params.add(createLbParameter("SJ","值")); // 手机  FLAG=3时必填
        params.add(createLbParameter("ZJBH","值")); // 证件编号  FLAG=4时必填
        params.add(createLbParameter("ZJLB","值")); // 证件类别  FLAG=4时必填
        params.add(createLbParameter("STEP","值")); // 开户步骤  FLAG=2,3,4时选填
        params.add(createLbParameter("JGBZ","值")); // 机构标志  0-个人;1-机构;不填查询所有
        params.add(createLbParameter("KSRQ","值")); // 开始日期  yyyymmdd格式,非必填
        params.add(createLbParameter("JSRQ","值")); // 结束日期  yyyymmdd格式,非必填
        params.add(createLbParameter("KHFS","值")); // 开户方式  1|临柜开户;2|见证开户;3|网上开户
        params.add(createLbParameter("CXNR","值")); // 查询内容  支持对客户姓名、身份证号进行模糊查询
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 申请ID  
            System.out.print("SQRQ = " + mapData.get("SQRQ") + "\t "); // 申请日期  yyyymmdd格式
            System.out.print("SQSJ = " + mapData.get("SQSJ") + "\t "); // 申请时间  
            System.out.print("SHZT = " + mapData.get("SHZT") + "\t "); // 审核状态  数据字典.分类代码=SHZT(0|未审核;1|审核通过;2|审核不通过)
            System.out.print("STEP = " + mapData.get("STEP") + "\t "); // 开户步骤  数据字典.分类代码=KH_STEP
            System.out.print("CLJGSM = " + mapData.get("CLJGSM") + "\t "); // 处理结果说明  
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // 开户方式  1|临柜开户;2|见证开户;3|网上开户;11|全员开户
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 客户类型  0|个人;1|机构
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // 开户营业部  
            System.out.print("KHLY = " + mapData.get("KHLY") + "\t "); // 客户来源  
            System.out.print("KHXM = " + mapData.get("KHXM") + "\t "); // 客户姓名  
            System.out.print("KHQC = " + mapData.get("KHQC") + "\t "); // 客户全称  
            System.out.print("JZR = " + mapData.get("JZR") + "\t "); // 见证人  见证开户时必填，其他开户方式时不填
            System.out.print("KHXZR = " + mapData.get("KHXZR") + "\t "); // 开户协助人  见证开户时必填，其他开户方式时不填
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // 证件类别  数据字典.分类代码=GT_ZJLB
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // 证件编号  
            System.out.print("ZJQSRQ = " + mapData.get("ZJQSRQ") + "\t "); // 证件起始日期  
            System.out.print("ZJJZRQ = " + mapData.get("ZJJZRQ") + "\t "); // 证件截止日期  
            System.out.print("ZJDZ = " + mapData.get("ZJDZ") + "\t "); // 证件地址  
            System.out.print("ZJDZYB = " + mapData.get("ZJDZYB") + "\t "); // 证件地址邮编  
            System.out.print("ZJFZJG = " + mapData.get("ZJFZJG") + "\t "); // 发证机关  
            System.out.print("ZJZP = " + mapData.get("ZJZP") + "\t "); // 证件照片  影像上传成功后返回的字符串
            System.out.print("ZJYZLY = " + mapData.get("ZJYZLY") + "\t "); // 证件验证来源  数据字典.分类代码=KH_ZJYZLY
            System.out.print("EDZ = " + mapData.get("EDZ") + "\t "); // 二代证校验  
            System.out.print("YYZZNJRQ = " + mapData.get("YYZZNJRQ") + "\t "); // 营业执照年检日期  
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // 通讯地址  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // 邮编  
            System.out.print("JTDZ = " + mapData.get("JTDZ") + "\t "); // 家庭地址  
            System.out.print("JTDZYB = " + mapData.get("JTDZYB") + "\t "); // 家庭地址邮编  
            System.out.print("JTDH = " + mapData.get("JTDH") + "\t "); // 家庭电话  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // 手机  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // 电话  
            System.out.print("CZ = " + mapData.get("CZ") + "\t "); // 传真  
            System.out.print("EMAIL = " + mapData.get("EMAIL") + "\t "); // EMAIL  
            System.out.print("GZDW = " + mapData.get("GZDW") + "\t "); // 工作单位  
            System.out.print("GZDWDZ = " + mapData.get("GZDWDZ") + "\t "); // 单位地址  
            System.out.print("GZDWYB = " + mapData.get("GZDWYB") + "\t "); // 单位邮编  
            System.out.print("GZDWDH = " + mapData.get("GZDWDH") + "\t "); // 单位电话  
            System.out.print("QQ = " + mapData.get("QQ") + "\t "); // QQ  
            System.out.print("MSN = " + mapData.get("MSN") + "\t "); // MSN  
            System.out.print("HYZK = " + mapData.get("HYZK") + "\t "); // 婚姻状况  数据字典.分类代码=GT_HYZK
            System.out.print("ZYDM = " + mapData.get("ZYDM") + "\t "); // 职业  数据字典.分类代码=GT_ZYDM
            System.out.print("XL = " + mapData.get("XL") + "\t "); // 学历  数据字典.分类代码=GT_XLDM
            System.out.print("MZDM = " + mapData.get("MZDM") + "\t "); // 民族  数据字典.分类代码=MZDM
            System.out.print("JG = " + mapData.get("JG") + "\t "); // 籍贯  
            System.out.print("GJ = " + mapData.get("GJ") + "\t "); // 国家  通过接口cifQueryGJDM(cif查询国籍代码)获取到的IBM(数字编码)
            System.out.print("PROVINCE = " + mapData.get("PROVINCE") + "\t "); // 省份  通过接口cifQuerySFDM(cif查询省份代码)获取到的IBM(数字编码)
            System.out.print("CITY = " + mapData.get("CITY") + "\t "); // 城市  通过接口cifQueryCSDM(cif查询城市代码)获取到的POST(城市代码)
            System.out.print("SEC = " + mapData.get("SEC") + "\t "); // 区县  通过接口cifQueryCSDM(cif查询城市代码)获取到的POST(城市代码)
            System.out.print("TBSM = " + mapData.get("TBSM") + "\t "); // 特别说明  
            System.out.print("ZWDM = " + mapData.get("ZWDM") + "\t "); // 职位  数据字典.分类代码=GT_ZWDM
            System.out.print("NJSR = " + mapData.get("NJSR") + "\t "); // 年均收入  
            System.out.print("ZGZSLX = " + mapData.get("ZGZSLX") + "\t "); // 资格证书类型  数据字典.分类代码=GT_ZGZSLX
            System.out.print("ZSBH = " + mapData.get("ZSBH") + "\t "); // 证书编号  
            System.out.print("CYSJ = " + mapData.get("CYSJ") + "\t "); // 从业时间  
            System.out.print("JSFS = " + mapData.get("JSFS") + "\t "); // 寄送方式  数据字典.分类代码=GT_JSFS
            System.out.print("LXFS = " + mapData.get("LXFS") + "\t "); // 联系方式  数据字典.分类代码=GT_LXFS
            System.out.print("LLPL = " + mapData.get("LLPL") + "\t "); // 联络频率  数据字典.分类代码=GT_LLPL
            System.out.print("GTKHLY = " + mapData.get("GTKHLY") + "\t "); // 柜台客户来源  数据字典.分类代码=GT_KHLY
            System.out.print("QSXY = " + mapData.get("QSXY") + "\t "); // 签署协议  数据字典.分类代码=GT_QSXY
            System.out.print("SFYZWT = " + mapData.get("SFYZWT") + "\t "); // 身份验证问题  数据字典.分类代码=KH_SFYZWT
            System.out.print("SFYZDA = " + mapData.get("SFYZDA") + "\t "); // 身份验证答案  
            System.out.print("FXQHYLB = " + mapData.get("FXQHYLB") + "\t "); // 反洗钱行业类别  数据字典.分类代码=FXQHYLB
            System.out.print("XQFXDJ = " + mapData.get("XQFXDJ") + "\t "); // 洗钱风险等级  数据字典.分类代码=XQFXDJ
            System.out.print("KHMB = " + mapData.get("KHMB") + "\t "); // 客户模板  客户模板ID
            System.out.print("ZCSX = " + mapData.get("ZCSX") + "\t "); // 资产属性  
            System.out.print("KHFL = " + mapData.get("KHFL") + "\t "); // 客户分类  
            System.out.print("KHFZ = " + mapData.get("KHFZ") + "\t "); // 客户分组  
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // 委托方式  
            System.out.print("KHQX = " + mapData.get("KHQX") + "\t "); // 客户权限  
            System.out.print("KHXZ = " + mapData.get("KHXZ") + "\t "); // 客户限制  
            System.out.print("GDQX = " + mapData.get("GDQX") + "\t "); // 股东权限  
            System.out.print("GDXZ = " + mapData.get("GDXZ") + "\t "); // 股东限制  
            System.out.print("CBLX = " + mapData.get("CBLX") + "\t "); // 成本类型  
            System.out.print("LLLB = " + mapData.get("LLLB") + "\t "); // 利率类别  
            System.out.print("KHGFXX = " + mapData.get("KHGFXX") + "\t "); // 开户规范信息  
            System.out.print("GSKHLX = " + mapData.get("GSKHLX") + "\t "); // 公司客户类型  
            System.out.print("FXYSXX = " + mapData.get("FXYSXX") + "\t "); // 风险要素信息  
            System.out.print("YXBZ = " + mapData.get("YXBZ") + "\t "); // 允许币种  
            System.out.print("CPBZ = " + mapData.get("CPBZ") + "\t "); // 产品标志  
            System.out.print("KHKH = " + mapData.get("KHKH") + "\t "); // 客户卡号  
            System.out.print("KHH = " + mapData.get("KHH") + "\t "); // 客户号  
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // 风险承受能力  数据字典.分类代码=GT_FXCSNL
            System.out.print("KFYYB = " + mapData.get("KFYYB") + "\t "); // 开发营业部  开发营业部的ID(见证开户,全员开户模式下为开户柜员的归属营业部)
            System.out.print("KFGY = " + mapData.get("KFGY") + "\t "); // 开发柜员  开发柜员的ID(见证开户,全员开户模式下为开户柜员的ID)
            System.out.print("SFTB = " + mapData.get("SFTB") + "\t "); // 是否同步  0|不同步;1|同步
            System.out.print("JYMM = " + mapData.get("JYMM") + "\t "); // 交易密码  
            System.out.print("ZJMM = " + mapData.get("ZJMM") + "\t "); // 资金密码  
            System.out.print("FWMM = " + mapData.get("FWMM") + "\t "); // 服务密码  
            System.out.print("SQJJZH = " + mapData.get("SQJJZH") + "\t "); // 申请基金账号  多选值，多个基金公司ID间以"";""号分割，示例""401;411;427""通过接口cifQueryJJGSCS(cif查询基金公司参数)获取到的ID(基金公司ID)列表
            System.out.print("GDKH_SH = " + mapData.get("GDKH_SH") + "\t "); // 沪A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
            System.out.print("GDDJ_SH = " + mapData.get("GDDJ_SH") + "\t "); // 登记沪A股东号  沪A股东账户
            System.out.print("GDKH_HB = " + mapData.get("GDKH_HB") + "\t "); // 沪B股东开户  
            System.out.print("GDDJ_HB = " + mapData.get("GDDJ_HB") + "\t "); // 登记沪B股东号  
            System.out.print("GDKH_SZ = " + mapData.get("GDKH_SZ") + "\t "); // 深A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
            System.out.print("GDDJ_SZ = " + mapData.get("GDDJ_SZ") + "\t "); // 登记深A股东号  深A股东账户
            System.out.print("GDKH_SB = " + mapData.get("GDKH_SB") + "\t "); // 深B股东开户  
            System.out.print("GDDJ_SB = " + mapData.get("GDDJ_SB") + "\t "); // 登记深B股东号  
            System.out.print("GDKH_TA = " + mapData.get("GDKH_TA") + "\t "); // 三板A股东开户  
            System.out.print("GDDJ_TA = " + mapData.get("GDDJ_TA") + "\t "); // 登记三板A股东号  
            System.out.print("GDKH_TU = " + mapData.get("GDKH_TU") + "\t "); // 三板B股东开户  
            System.out.print("GDDJ_TU = " + mapData.get("GDDJ_TU") + "\t "); // 登记三板B股东号  
            System.out.print("GDJYQX_SH = " + mapData.get("GDJYQX_SH") + "\t "); // 沪A股东交易权限  
            System.out.print("GDJYQX_HB = " + mapData.get("GDJYQX_HB") + "\t "); // 沪B股东交易权限  
            System.out.print("GDJYQX_SZ = " + mapData.get("GDJYQX_SZ") + "\t "); // 深A股东交易权限  
            System.out.print("GDJYQX_SB = " + mapData.get("GDJYQX_SB") + "\t "); // 深B股东交易权限  
            System.out.print("GDJYQX_TA = " + mapData.get("GDJYQX_TA") + "\t "); // 三板A股东交易权限  
            System.out.print("GDJYQX_TU = " + mapData.get("GDJYQX_TU") + "\t "); // 三板B股东交易权限  
            System.out.print("SHZDJY = " + mapData.get("SHZDJY") + "\t "); // 沪A指定  
            System.out.print("HBZDJY = " + mapData.get("HBZDJY") + "\t "); // 沪B指定  
            System.out.print("SFWLFW = " + mapData.get("SFWLFW") + "\t "); // 是否需要网络服务  
            System.out.print("WLFWMM = " + mapData.get("WLFWMM") + "\t "); // 网络服务密码  
            System.out.print("CGYH = " + mapData.get("CGYH") + "\t "); // 存管银行  通过接口cifQueryYHCS(cif查询银行参数)获取到的YHDM(银行代码)
            System.out.print("CGYHZH = " + mapData.get("CGYHZH") + "\t "); // 存管银行账户  
            System.out.print("CGYHMM = " + mapData.get("CGYHMM") + "\t "); // 存管银行密码  
            System.out.print("YHDM_USD = " + mapData.get("YHDM_USD") + "\t "); // 美元转账银行  
            System.out.print("DJFS_USD = " + mapData.get("DJFS_USD") + "\t "); // 美元登记方式  
            System.out.print("YHZH_USD = " + mapData.get("YHZH_USD") + "\t "); // 美元银行账号  
            System.out.print("YHMM_USD = " + mapData.get("YHMM_USD") + "\t "); // 美元银行密码  
            System.out.print("YHDM_HKD = " + mapData.get("YHDM_HKD") + "\t "); // 港币转账银行  
            System.out.print("DJFS_HKD = " + mapData.get("DJFS_HKD") + "\t "); // 港币登记方式  
            System.out.print("YHZH_HKD = " + mapData.get("YHZH_HKD") + "\t "); // 港币银行账号  
            System.out.print("YHMM_HKD = " + mapData.get("YHMM_HKD") + "\t "); // 港币银行密码  
            System.out.print("KHZP = " + mapData.get("KHZP") + "\t "); // 客户照片  影像上传成功后返回的字符串
            System.out.print("KHSP = " + mapData.get("KHSP") + "\t "); // 客户视频  视频上传成功后返回的字符串
            System.out.print("DJR = " + mapData.get("DJR") + "\t "); // 操作人  开户柜员的ID
            System.out.print("CZZD = " + mapData.get("CZZD") + "\t "); // 操作站点  客户端机器的IP地址或MAC地址
            System.out.print("CPSCORE = " + mapData.get("CPSCORE") + "\t "); // 测评得分  
            System.out.print("CPFXCSNL = " + mapData.get("CPFXCSNL") + "\t "); // 测评风险承受能力  数据字典.分类代码=GT_FXCSNL
            System.out.print("LXRXXSTR = " + mapData.get("LXRXXSTR") + "\t "); // 联系人信息串  示例：[{"LXRXM":"姚明明", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"领导"}, {"LXRXM":"李小四", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"下属"}]
            System.out.print("DLRSTR = " + mapData.get("DLRSTR") + "\t "); // 代理人串  示例：[{"DLRXM":"张一三", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"长期",  "ZJFZJG":"福州市鼓楼公安局", "CSRQ":"19840101", "ZJDZ":"福州市湖东路165号", "DZ":"福州市五四路150号",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
            System.out.print("YXSTR = " + mapData.get("YXSTR") + "\t "); // 影像串  示例：[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]

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
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // 返回流水号  
        }
    }

    // cif查询临柜审核柜员信息
    public void cifCXLGSHGYXX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","值")); // 业务审核代码  全员开户传参"00001"见证开户传参"00002"网上开户传参"00003"
        params.add(createLbParameter("GYID","值")); // 柜员ID  全员开户，见证开户填写登录用户的ID网上开户可不填
        QueryResult qrs = client.query(getSessionId(), "cifCXLGSHGYXX", params, null, queryOption);
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
            System.out.print("USERID = " + mapData.get("USERID") + "\t "); // 柜员代码  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 柜员姓名  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询业务审核参数
    public void cifCXYWSHCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","值")); // 业务审核代码  全员开户传参"00001"见证开户传参"00002"网上开户传参"00003"
        params.add(createLbParameter("GYID","值")); // 柜员ID  全员开户，见证开户填写登录用户的ID网上开户可不填
        QueryResult qrs = client.query(getSessionId(), "cifCXYWSHCS", params, null, queryOption);
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
            System.out.print("ZZFLCS = " + mapData.get("ZZFLCS") + "\t "); // 组织分类参数  0-总部参数,1-组织分类参数
            System.out.print("LGSHBZ = " + mapData.get("LGSHBZ") + "\t "); // 临柜审核标志  0-无需临柜审核,1-需要临柜审核
            System.out.print("LGSHJS = " + mapData.get("LGSHJS") + "\t "); // 临柜审核角色  
            System.out.print("LGSHYZFS = " + mapData.get("LGSHYZFS") + "\t "); // 临柜审核验证方式  1-密码验证
            System.out.print("HTSHBZ = " + mapData.get("HTSHBZ") + "\t "); // 后台审核标志  0-无需后台审核,1-需要后台审核

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询证件编号是否允许开户
    public void cifCXZJBHYXKH() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("ZJLB","值")); // 证件类别  
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        QueryResult qrs = client.query(getSessionId(), "cifCXZJBHYXKH", params, null, queryOption);
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
            System.out.print("CODE = " + mapData.get("CODE") + "\t "); // 返回码  总是返回1
            System.out.print("FLAG = " + mapData.get("FLAG") + "\t "); // 是否允许开户  <0 禁止开户(如该证件已在系统中开过户，或通迅异常等)>0 允许开户
            System.out.print("NOTE = " + mapData.get("NOTE") + "\t "); // 返回说明  FLAG<0时,返回具体的错误信息(如该证件已在系统中开过户，或通迅异常等)FLAG>0时,返回"允许开户"

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询可操作营业部
    public void cifCXKCZYYB() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","值")); // 业务审核代码  全员开户传参"00001"见证开户传参"00002"网上开户传参"00003"
        params.add(createLbParameter("GYID","值")); // 柜员ID  全员开户，见证开户填写登录用户的ID网上开户可不填
        QueryResult qrs = client.query(getSessionId(), "cifCXKCZYYB", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ID  
            System.out.print("ORGCODE = " + mapData.get("ORGCODE") + "\t "); // 组织编码  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 组织名称  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询组织机构
    public void cifQueryZZJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYBBM","值")); // 营业部编码  不填查询全部机构
        QueryResult qrs = client.query(getSessionId(), "cifQueryZZJG", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 组织机构ID  
            System.out.print("FID = " + mapData.get("FID") + "\t "); // 上级组织机构ID  
            System.out.print("ORGTYPE = " + mapData.get("ORGTYPE") + "\t "); // 组织类型  1-公司;2-分公司;3-营业部;4-部门;5-小组
            System.out.print("ORGCODE = " + mapData.get("ORGCODE") + "\t "); // 组织编码  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 组织名称  
            System.out.print("ZZFL = " + mapData.get("ZZFL") + "\t "); // 组织分类  1-临柜复核营业部;2-非临柜复核营业部
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // 营业地址  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // 邮编  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // 咨询电话  
            System.out.print("PROVINCE = " + mapData.get("PROVINCE") + "\t "); // 省份  
            System.out.print("CITY = " + mapData.get("CITY") + "\t "); // 城市  
            System.out.print("SEC = " + mapData.get("SEC") + "\t "); // 区县  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询柜员信息
    public void cifQueryGYXX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","值")); // 查询标志  FLAG=1,按照柜员工号查询FLAG=2,按照身份证号查询
        params.add(createLbParameter("USERID","值")); // 柜员工号  FLAG=1时必填
        params.add(createLbParameter("SFZH","值")); // 身份证号  FLAG=2时必填
        QueryResult qrs = client.query(getSessionId(), "cifQueryGYXX", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 柜员ID  CIF系统中标识柜员的唯一主键
            System.out.print("ORGID = " + mapData.get("ORGID") + "\t "); // 柜员营业部  柜员归属的组织机构(营业部)ID
            System.out.print("USERID = " + mapData.get("USERID") + "\t "); // 柜员工号  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 用户姓名  
            System.out.print("SFZH = " + mapData.get("SFZH") + "\t "); // 身份证号  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // 手机  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // 电话  
            System.out.print("GRADE = " + mapData.get("GRADE") + "\t "); // 用户类别  0|内部员工;1|外部客户;2|系统管理员;3|开发人员;4|Webservice调用用户
            System.out.print("GYFL = " + mapData.get("GYFL") + "\t "); // 柜员分类  数据字典.分类代码='GT_GYFL'
            System.out.print("LASTLOGIN = " + mapData.get("LASTLOGIN") + "\t "); // 最近登录时间  
            System.out.print("LOGINS = " + mapData.get("LOGINS") + "\t "); // 登录次数  
            System.out.print("CHGPWDLIMIT = " + mapData.get("CHGPWDLIMIT") + "\t "); // 更密周期  
            System.out.print("CHGPWDTIME = " + mapData.get("CHGPWDTIME") + "\t "); // 密码变更时间  
            System.out.print("STATUS = " + mapData.get("STATUS") + "\t "); // 允许登陆  1|是;2|否
            System.out.print("IPLIMIT = " + mapData.get("IPLIMIT") + "\t "); // 登陆IP限制  
            System.out.print("LOCKTIME = " + mapData.get("LOCKTIME") + "\t "); // 锁定时间  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询数据字典
    public void cifQuerySJZD() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLDM","值")); // 分类代码  必填，为空则不返回任何数据
        params.add(createLbParameter("IBM","值")); // 数值编码  可以为空
        QueryResult qrs = client.query(getSessionId(), "cifQuerySJZD", params, null, queryOption);
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
            System.out.print("FLDM = " + mapData.get("FLDM") + "\t "); // 分类代码   
            System.out.print("FLMC = " + mapData.get("FLMC") + "\t "); // 分类名称   
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // 数字编码   
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // 字符编码   
            System.out.print("NOTE = " + mapData.get("NOTE") + "\t "); // 说明   
            System.out.print("FLAG = " + mapData.get("FLAG") + "\t "); // 标志   

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询国籍代码
    public void cifQueryGJDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GJDM","值")); // 国籍代码  非必填，为空则查询全部国籍代码
        QueryResult qrs = client.query(getSessionId(), "cifQueryGJDM", params, null, queryOption);
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
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // 数字编码  开户申请时往对应接口传入该值
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // 字母编码  
            System.out.print("YWMC = " + mapData.get("YWMC") + "\t "); // 英文名称  
            System.out.print("GJMC = " + mapData.get("GJMC") + "\t "); // 国家名称  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询省份代码
    public void cifQuerySFDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("SFDM","值")); // 省份代码  非必填，为空则查询全部省份代码
        QueryResult qrs = client.query(getSessionId(), "cifQuerySFDM", params, null, queryOption);
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
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // 数字编码  开户申请时往对应接口传入该值
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // 字母编码  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 省市名称  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询城市代码
    public void cifQueryCSDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("CSDM","值")); // 城市代码  城市代码与上级代码都为空时查询全部城市代码
        params.add(createLbParameter("SJDM","值")); // 上级代码  
        QueryResult qrs = client.query(getSessionId(), "cifQueryCSDM", params, null, queryOption);
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
            System.out.print("POST = " + mapData.get("POST") + "\t "); // 城市代码  开户申请时往对应接口传入该值
            System.out.print("CODE = " + mapData.get("CODE") + "\t "); // 拼音编码  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // 市区名称  
            System.out.print("PARENT = " + mapData.get("PARENT") + "\t "); // 上级代码  
            System.out.print("PHONECODE = " + mapData.get("PHONECODE") + "\t "); // 电话区号  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询银行参数
    public void cifQueryYHCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YHDM","值")); // 银行代码  非必填，为空则查询全部银行参数
        QueryResult qrs = client.query(getSessionId(), "cifQueryYHCS", params, null, queryOption);
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
            System.out.print("YHDM = " + mapData.get("YHDM") + "\t "); // 银行代码  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // 适用币种  数据字典.分类代码='GT_BZ'
            System.out.print("YHMC = " + mapData.get("YHMC") + "\t "); // 银行名称  
            System.out.print("YYBFW = " + mapData.get("YYBFW") + "\t "); // 适用营业部  多个适用营业部ID间以";"号分割
            System.out.print("CGBZ = " + mapData.get("CGBZ") + "\t "); // 存管标志  1-存管;0-银证转账
            System.out.print("CGZDFS = " + mapData.get("CGZDFS") + "\t "); // 存管指定方式  多选值,多个值间以";"号分割1-直接指定;2-预指定示例: "1;2"表示该银行同时支持直接指定、预指定

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询问卷评分标准
    public void cifQueryWJPFBZ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("WJID","值")); // 问卷ID  必填，为空则不返回任何数据
        QueryResult qrs = client.query(getSessionId(), "cifQueryWJPFBZ", params, null, queryOption);
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
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // 风险承受能力  数据字典.分类代码='GT_FXCSNL'
            System.out.print("PFXX = " + mapData.get("PFXX") + "\t "); // 评分下限  评分计算算法: 评分下限<=得分<评分上限
            System.out.print("PFSX = " + mapData.get("PFSX") + "\t "); // 评分上限  
            System.out.print("TZMS = " + mapData.get("TZMS") + "\t "); // 特征描述  
            System.out.print("WJID = " + mapData.get("WJID") + "\t "); // 调查问卷  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询公民身份验证申请
    public void cifQueryGMSFYZSQ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("SQID","值")); // 申请ID  必填，为空则不返回任何数据
        QueryResult qrs = client.query(getSessionId(), "cifQueryGMSFYZSQ", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 申请ID  
            System.out.print("SQRQ = " + mapData.get("SQRQ") + "\t "); // 申请日期  
            System.out.print("SQSJ = " + mapData.get("SQSJ") + "\t "); // 申请时间  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // 证件编号  
            System.out.print("SQXM = " + mapData.get("SQXM") + "\t "); // 申请姓名  
            System.out.print("CSRQ = " + mapData.get("CSRQ") + "\t "); // 出生日期  
            System.out.print("XM = " + mapData.get("XM") + "\t "); // 姓名  
            System.out.print("NATION = " + mapData.get("NATION") + "\t "); // 民族  数据字典.分类代码='MZDM'
            System.out.print("XB = " + mapData.get("XB") + "\t "); // 性别  数据字典.分类代码='GT_XB'
            System.out.print("ZJBHHCJG = " + mapData.get("ZJBHHCJG") + "\t "); // 身份号码核查结果  
            System.out.print("XMHCJG = " + mapData.get("XMHCJG") + "\t "); // 姓名核查结果  
            System.out.print("CLJG = " + mapData.get("CLJG") + "\t "); // 处理结果  CLJG>0,表示查询成功CLJG<0,表示查询失败CLJG=0,表示尚未获取到查询数据
            System.out.print("JGSM = " + mapData.get("JGSM") + "\t "); // 结果说明  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询证券从业证书
    public void cifQueryZQCYZS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","值")); // 查询标志  1-按照身份证号查询2-批量分页查询
        params.add(createLbParameter("SFZH","值")); // 身份证号  FLAG=1时必填,从业人员身份证号(可通过cifQueryGYXX接口获得)
        params.add(createLbParameter("KSID","值")); // 开始ID  FLAG=2时必填
        QueryResult qrs = client.query(getSessionId(), "cifQueryZQCYZS", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 从业证书ID  
            System.out.print("XM = " + mapData.get("XM") + "\t "); // 姓名  
            System.out.print("XB = " + mapData.get("XB") + "\t "); // 性别  数据字典.分类代码='GT_XB'
            System.out.print("ZYJG = " + mapData.get("ZYJG") + "\t "); // 执业机构  
            System.out.print("ZSBH = " + mapData.get("ZSBH") + "\t "); // 证书编号  
            System.out.print("ZYGW = " + mapData.get("ZYGW") + "\t "); // 执业岗位  
            System.out.print("XL = " + mapData.get("XL") + "\t "); // 学历  数据字典.分类代码='GT_XLDM'
            System.out.print("ZSQDRQ = " + mapData.get("ZSQDRQ") + "\t "); // 证书取得日期  yyyymmdd格式
            System.out.print("ZSYXRQ = " + mapData.get("ZSYXRQ") + "\t "); // 证书有效截止日期  yyyymmdd格式
            System.out.print("SFZH = " + mapData.get("SFZH") + "\t "); // 身份证号  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询开户影像采集
    public void cifQueryKHYXCJ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","值")); // 开户方式  2-见证开户;3-网上开户;11-全员开户
        params.add(createLbParameter("JGBZ","值")); // 机构标志  0-个人;1-机构
        QueryResult qrs = client.query(getSessionId(), "cifQueryKHYXCJ", params, null, queryOption);
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
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // 开户方式  2-见证开户;3-网上开户;11-全员开户
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 机构标志  0-个人;1-机构
            System.out.print("WJLX = " + mapData.get("WJLX") + "\t "); // 文件类型  1|图像文件;2|视频文件
            System.out.print("YXLX = " + mapData.get("YXLX") + "\t "); // 影像类型ID  
            System.out.print("YXLXMC = " + mapData.get("YXLXMC") + "\t "); // 影像类型名称  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询开户投资者教育
    public void cifQueryKHTZZJY() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        QueryResult qrs = client.query(getSessionId(), "cifQueryKHTZZJY", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 投资者教育协议ID  
            System.out.print("JYLB = " + mapData.get("JYLB") + "\t "); // 教育类别  数据字典.分类代码='KH_JYLB'
            System.out.print("BT = " + mapData.get("BT") + "\t "); // 标题  
            System.out.print("ZW = " + mapData.get("ZW") + "\t "); // 正文  
            System.out.print("WJBB = " + mapData.get("WJBB") + "\t "); // 文件版本  
            System.out.print("YDSJ = " + mapData.get("YDSJ") + "\t "); // 阅读时间(秒)  
            System.out.print("YXQSRQ = " + mapData.get("YXQSRQ") + "\t "); // 有效起始日期  yyyymmdd格式
            System.out.print("YXJZRQ = " + mapData.get("YXJZRQ") + "\t "); // 有效截止日期  yyyymmdd格式
            System.out.print("ZT = " + mapData.get("ZT") + "\t "); // 状态  0|正常;1|作废
            System.out.print("PX = " + mapData.get("PX") + "\t "); // 排序  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询中登股东查询结果
    public void cifQueryZDGDCXJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("ZJBH","值")); // 证件编号  
        params.add(createLbParameter("ZJLB","值")); // 证件类别  数据字典.分类代码='GT_ZJLB'
        params.add(createLbParameter("GDH","值")); // 股东号  
        QueryResult qrs = client.query(getSessionId(), "cifQueryZDGDCXJG", params, null, queryOption);
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
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // 证件类别  数据字典.分类代码='GT_ZJLB'
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // 证件编号  
            System.out.print("YZRQ = " + mapData.get("YZRQ") + "\t "); // 验证日期  yyyymmdd格式
            System.out.print("JYS = " + mapData.get("JYS") + "\t "); // 交易所  数据字典.分类代码='GT_JYS'
            System.out.print("ZDGDH = " + mapData.get("ZDGDH") + "\t "); // 股东号  
            System.out.print("ZDGDZT = " + mapData.get("ZDGDZT") + "\t "); // 中登股东状态  数据字典.分类代码='GT_GDZT'
            System.out.print("ZDGDXM = " + mapData.get("ZDGDXM") + "\t "); // 中登股东姓名  
            System.out.print("ZJYXQ = " + mapData.get("ZJYXQ") + "\t "); // 证件有效期  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询开户默认属性
    public void cifQueryKHMRSX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","值")); // 开户方式  2-见证开户;3-网上开户;11-全员开户
        params.add(createLbParameter("JGBZ","值")); // 机构标志  0-个人;1-机构
        QueryResult qrs = client.query(getSessionId(), "cifQueryKHMRSX", params, null, queryOption);
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
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // 开户方式  2-见证开户;3-网上开户;11-全员开户
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 机构标志  0-个人;1-机构
            System.out.print("ENWTFS = " + mapData.get("ENWTFS") + "\t "); // 允许委托方式  多选值,多个值间以";"号分割数据字典.分类代码='GT_WTFS'
            System.out.print("ENKHQX = " + mapData.get("ENKHQX") + "\t "); // 允许客户权限  多选值,多个值间以";"号分割数据字典.分类代码='GT_KHQX'
            System.out.print("ENGDQX_SH = " + mapData.get("ENGDQX_SH") + "\t "); // 允许沪A股东权限  多选值,多个值间以";"号分割数据字典.分类代码='GT_GDQX'
            System.out.print("ENGDQX_SZ = " + mapData.get("ENGDQX_SZ") + "\t "); // 允许深A股东权限  多选值,多个值间以";"号分割数据字典.分类代码='GT_GDQX'
            System.out.print("WJBM = " + mapData.get("WJBM") + "\t "); // 问卷编码  
            System.out.print("LCFWWJBM = " + mapData.get("LCFWWJBM") + "\t "); // 理财服务问卷编码  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询基金公司参数
    public void cifQueryJJGSCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("JJGSDM","值")); // 基金公司代码  非必填,两位的基金公司代码
        QueryResult qrs = client.query(getSessionId(), "cifQueryJJGSCS", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 基金公司ID  
            System.out.print("JJGSDM = " + mapData.get("JJGSDM") + "\t "); // 基金公司代码  
            System.out.print("JJGSQC = " + mapData.get("JJGSQC") + "\t "); // 基金公司全称  
            System.out.print("JJGSJC = " + mapData.get("JJGSJC") + "\t "); // 基金公司简称  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // cif查询客户模板
    public void cifQueryKHMB() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","值")); // 营业部ID  
        params.add(createLbParameter("KHFS","值")); // 开户方式  2-见证开户;3-网上开户;11-全员开户
        params.add(createLbParameter("JGBZ","值")); // 机构标志  0-个人;1-机构
        QueryResult qrs = client.query(getSessionId(), "cifQueryKHMB", params, null, queryOption);
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // 客户模板ID  
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // 营业部ID  
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // 开户方式  2-见证开户;3-网上开户;11-全员开户
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // 机构标志  0-个人;1-机构
            System.out.print("MBMC = " + mapData.get("MBMC") + "\t "); // 模板名称  
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // 委托方式  多选值,多个值间以";"号分割数据字典.分类代码='GT_WTFS'
            System.out.print("KHQX = " + mapData.get("KHQX") + "\t "); // 客户权限  多选值,多个值间以";"号分割数据字典.分类代码='GT_KHQX'
            System.out.print("GDQX = " + mapData.get("GDQX") + "\t "); // 股东权限  多选值,多个值间以";"号分割数据字典.分类代码='GT_GDQX'
            System.out.print("YXBZ = " + mapData.get("YXBZ") + "\t "); // 允许币种  多选值,多个值间以";"号分割数据字典.分类代码='GT_BZ'

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // 重置客户密码_重置密码
    public void cifwsCZKHMM_CZMM() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","值")); // 营业部ID  
        params.add(createLbParameter("KHH","值")); // 客户号  
        params.add(createLbParameter("ZCZH","值")); // 资产账号  
        params.add(createLbParameter("MMLB","值")); // 密码类别  1-资金密码;2-交易密码;9-服务密码
        params.add(createLbParameter("NEWMM","值")); // 新密码  密码明文
        params.add(createLbParameter("ZY","值")); // 操作摘要  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsCZKHMM_CZMM", "", params, null);
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

    // 指定存管银行
    public void cifwsZDCGYH_ZDCGYH() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","值")); // 营业部ID  
        params.add(createLbParameter("KHH","值")); // 客户号  
        params.add(createLbParameter("ZCZH","值")); // 资产账号  
        params.add(createLbParameter("BZ","值")); // 币种  1-人民币;2-美元;3-港币
        params.add(createLbParameter("YHDM","值")); // 银行代码  
        params.add(createLbParameter("YHZH","值")); // 银行账号  
        params.add(createLbParameter("YHMM","值")); // 银行密码  密码明文
        params.add(createLbParameter("ZY","值")); // 操作摘要  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsZDCGYH_ZDCGYH", "", params, null);
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
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // 流水号  
            System.out.println("O_YHDM = " + mapOutputVar.get("O_YHDM")); // 银行代码  
            System.out.println("O_YHZH = " + mapOutputVar.get("O_YHZH")); // 银行账号  
            System.out.println("O_ZY = " + mapOutputVar.get("O_ZY")); // 摘要  
        }
    }

    // 开户回访
    public void cifwsKHHF_KHHF() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHH","值")); // 客户号  
        params.add(createLbParameter("HFBZ","值")); // 回访标志  1|回访激活;-1|回访失败;0|回访记录摘要(不驱动流程)
        params.add(createLbParameter("ZY","值")); // 摘要  
        params.add(createLbParameter("HFGYDM","值")); // 回访柜员代码  
        params.add(createLbParameter("HFGYXM","值")); // 回访柜员姓名  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsKHHF_KHHF", "", params, null);
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

    // cif查询银行转账委托结果
    public void cifCXYHZZWTJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YHDM","值")); // 银行代码  
        params.add(createLbParameter("LSH","值")); // 流水号  
        QueryResult qrs = client.query(getSessionId(), "cifCXYHZZWTJG", params, null, queryOption);
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
            System.out.print("JYRQ = " + mapData.get("JYRQ") + "\t "); // 交易日期  yyyymmdd格式
            System.out.print("LSH = " + mapData.get("LSH") + "\t "); // 流水号  
            System.out.print("YHDM = " + mapData.get("YHDM") + "\t "); // 银行代码  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // 币种  1-人民币;2-美元;3-港币
            System.out.print("KHH = " + mapData.get("KHH") + "\t "); // 客户号  
            System.out.print("ZCZH = " + mapData.get("ZCZH") + "\t "); // 资产账号  
            System.out.print("RQ = " + mapData.get("RQ") + "\t "); // 当前日期  yyyymmdd格式
            System.out.print("ZT = " + mapData.get("ZT") + "\t "); // 请求状态  0-未报;1-已报;2-成功;3-作废;4-待撤;5-撤销;7-待冲正;8-已冲正;A-待报;P-正报
            System.out.print("YHZH = " + mapData.get("YHZH") + "\t "); // 银行账号  
            System.out.print("FSJE = " + mapData.get("FSJE") + "\t "); // 发生金额  
            System.out.print("SXF = " + mapData.get("SXF") + "\t "); // 手续费  
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // 证件类别  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // 证件编号  
            System.out.print("CZGY = " + mapData.get("CZGY") + "\t "); // 操作柜员  
            System.out.print("ZY = " + mapData.get("ZY") + "\t "); // 摘要  
            System.out.print("BZXX = " + mapData.get("BZXX") + "\t "); // 备注信息  
            System.out.print("CGJGSM = " + mapData.get("CGJGSM") + "\t "); // 存管结果说明  

           System.out.println("");
        }
        //printQueryResult(qrs); // 控制抬打印全部结果集
    }

    // 用户管理_认证
    public void cifwsUser_Auth() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("UID","值")); // 用户名  柜员工号
        params.add(createLbParameter("PWD","值")); // 密码  柜员密码明文
        params.add(createLbParameter("KHFS","值")); // 开户方式  1-临柜开户;2-见证开户;3-网上开户;11-全员开户
        params.add(createLbParameter("DLLX","值")); // 登录类型  KHFS=2(见证开户)时必填,其他开户方式下不填1-见证人,2-开户协助人
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsUser_Auth", "", params, null);
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

    // 非现场开户(恒生柜台)_修改提交
    public void cifwsFXCKH_HS_XGTJ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("BDID","值")); // 表单ID  表单的ID号
        params.add(createLbParameter("ZJQSRQ","值")); // 证件起始日期  yyyymmdd格式
        params.add(createLbParameter("ZJJZRQ","值")); // 证件截止日期  yyyymmdd格式
        params.add(createLbParameter("ZJDZ","值")); // 证件地址  
        params.add(createLbParameter("ZJDZYB","值")); // 证件地址邮编  
        params.add(createLbParameter("ZJFZJG","值")); // 发证机关  
        params.add(createLbParameter("GDKH_SH","值")); // 沪A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SH","值")); // 登记沪A股东号  GDKH_SH=9时填入沪A股东账户
        params.add(createLbParameter("GDKH_SZ","值")); // 深A股东开户  0|不开户;1|新开A股账户;2|新开场内基金账户;9|已开
        params.add(createLbParameter("GDDJ_SZ","值")); // 登记深A股东号  GDKH_SZ=9时填入深A股东账户
        params.add(createLbParameter("GDJYQX_SH","值")); // 沪A股东交易权限  
        params.add(createLbParameter("GDJYQX_SZ","值")); // 深A股东交易权限  
        params.add(createLbParameter("YXSTR","值")); // 影像串  示例：[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_HS_XGTJ", "", params, null);
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
