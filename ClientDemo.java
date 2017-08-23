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
        // WebService �û�:webuser/111111
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
    // cif��ѯ���ղ�����Ŀ
    public void cifCXFXCPTM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("WJBM","ֵ")); // �ʾ����  ȡ����Ĭ�����Ա�(TKH_MRSX)�е��ʾ����(WJBM)
        params.add(createLbParameter("JGBZ","ֵ")); // ������־  0-����,1-����
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
            System.out.print("WJBM = " + mapData.get("WJBM") + "\t "); // �ʾ����  
            System.out.print("WJMC = " + mapData.get("WJMC") + "\t "); // �ļ�����  
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // ������־  0-����,1-����,-1��ʾ����
            System.out.print("WJID = " + mapData.get("WJID") + "\t "); // �ʾ�ID  
            System.out.print("QID = " + mapData.get("QID") + "\t "); // ��ĿID  
            System.out.print("QTYPE = " + mapData.get("QTYPE") + "\t "); // ��Ŀ����  0|��ѡ��;1|��ѡ��;2|�ж���;3|�����;4|�����;5|������
            System.out.print("QDESCRIBE = " + mapData.get("QDESCRIBE") + "\t "); // ��Ŀ����  
            System.out.print("SANSWER = " + mapData.get("SANSWER") + "\t "); // ��ѡ��  ����Ĵ��б��Էֺŷָͬ�Ĵ𰸡�ʾ����A|A.����֮�ڣ����ڣ�;B|B.2��5�꣨���ڣ�;C|C.6�����ϣ��г��ڻ��ڣ�
            System.out.print("ANSWER = " + mapData.get("ANSWER") + "\t "); // ��  �����ָ���𰸣���ѡC
            System.out.print("SCORE = " + mapData.get("SCORE") + "\t "); // �÷�  ����ָ���𰸶�Ӧ�÷֣���8��

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // �ͻ����ղ���_���ղ���
    public void cifwsKHFXCP_FXCP() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("CPGY","ֵ")); // ������Ա  ���ͻ���������Ĺ�ԱID
        params.add(createLbParameter("CZZD","ֵ")); // ����վ��  
        params.add(createLbParameter("KHH","ֵ")); // �ͻ���  
        params.add(createLbParameter("WJID","ֵ")); // �ʾ�ID  �������ʾ��ID��
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("KHXM","ֵ")); // �ͻ�����  
        params.add(createLbParameter("FXCSNL","ֵ")); // ���ճ�������  
        params.add(createLbParameter("TMDAC","ֵ")); // ��Ŀ�𰸴�  ��Ŀ��𰸼���"|"�ָ�����Ŀ����";"�ŷָ��ѡ������","�ָʾ��:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsKHFXCP_FXCP", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // ������ˮ��  
        }
    }

    // ���������֤_���Ͳ�ѯ
    public void cifwsGMSFYZ_FSCX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ��¼��Ա��ID
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("KHXM","ֵ")); // �ͻ�����  
        params.add(createLbParameter("CXLX","ֵ")); // ��ѯ����  ǿ����"1"
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsGMSFYZ_FSCX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_FLAG = " + mapOutputVar.get("O_FLAG")); // ��־  1-��TCIF_GMZJCXJG��������2-��TGMSFCXSQ��������
            System.out.println("O_ID = " + mapOutputVar.get("O_ID")); // ���IDֵ  
            System.out.println("O_YZRQ = " + mapOutputVar.get("O_YZRQ")); // ��֤����  
            System.out.println("O_SFZH = " + mapOutputVar.get("O_SFZH")); // ���֤��  
            System.out.println("O_CSRQ = " + mapOutputVar.get("O_CSRQ")); // ��������  
            System.out.println("O_XM = " + mapOutputVar.get("O_XM")); // ����  
            System.out.println("O_NATION = " + mapOutputVar.get("O_NATION")); // ����  
            System.out.println("O_XB = " + mapOutputVar.get("O_XB")); // �Ա�  
        }
    }

    // �е���֤_�е��˻���ѯ
    public void cifwsZDYZ_ZDZHCX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ��¼��Ա��ID
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("KHXM","ֵ")); // �ͻ�����  
        params.add(createLbParameter("GJDM","ֵ")); // ��������  ǿ����"156"
        params.add(createLbParameter("SCDM","ֵ")); // �г�����  ǿ���Ϳ�
        params.add(createLbParameter("CFBZ","ֵ")); // �ط���־  0-��ʾ�������벻����,1-ǿ���ط�.Ĭ����"0"
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsZDYZ_ZDZHCX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // ������ˮ��  
        }
    }

    // Ӱ�����_�ϴ�Ӱ��
    public void cifwsMedia_Upload() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("BASE64STR","ֵ")); // ͼƬBASE64���봮  
        params.add(createLbParameter("FILENAME","ֵ")); // �ļ���  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsMedia_Upload", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_FILEPATH = " + mapOutputVar.get("O_FILEPATH")); // ����Ӱ��·�����ܴ�  
        }
    }

    // Ӱ�����_����Ӱ��
    public void cifwsMedia_Download() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FILEPATH","ֵ")); // Ӱ��·�����ܴ�  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsMedia_Download", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_BASE64STR = " + mapOutputVar.get("O_BASE64STR")); // ����ͼƬBASE64���봮  
        }
    }

    // ���ֳ�����(������̨)_��������
    public void cifwsFXCKH_HS_KHSQ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���;11|ȫԱ����
        params.add(createLbParameter("KHZD","ֵ")); // �����ն�  1|Web��ʽ;2|Android�ͻ���;3|iPad�ͻ���
        params.add(createLbParameter("JGBZ","ֵ")); // �ͻ�����  0|����;1|����
        params.add(createLbParameter("YYB","ֵ")); // ����Ӫҵ��  
        params.add(createLbParameter("KHLY","ֵ")); // �ͻ���Դ  ��д"8"(�¿ͻ�)
        params.add(createLbParameter("KHXM","ֵ")); // �ͻ�����  
        params.add(createLbParameter("KHQC","ֵ")); // �ͻ�ȫ��  
        params.add(createLbParameter("JZR","ֵ")); // ��֤��  ��֤����ʱ�������������ʽʱ����
        params.add(createLbParameter("KHXZR","ֵ")); // ����Э����  ��֤����ʱ�������������ʽʱ����
        params.add(createLbParameter("YYKH","ֵ")); // ԤԼ����  1|ԤԼ����;0|ֱ�ӿ���
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  �����ֵ�.�������=GT_ZJLB
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJQSRQ","ֵ")); // ֤����ʼ����  yyyymmdd��ʽ
        params.add(createLbParameter("ZJJZRQ","ֵ")); // ֤����ֹ����  yyyymmdd��ʽ
        params.add(createLbParameter("ZJDZ","ֵ")); // ֤����ַ  
        params.add(createLbParameter("ZJDZYB","ֵ")); // ֤����ַ�ʱ�  
        params.add(createLbParameter("ZJFZJG","ֵ")); // ��֤����  
        params.add(createLbParameter("ZJZP","ֵ")); // ֤����Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
        params.add(createLbParameter("ZJYZLY","ֵ")); // ֤����֤��Դ  1|����֤������;2|����������
        params.add(createLbParameter("EDZ","ֵ")); // ����֤У��  0|δͨ���������򹫰���������֤;1|ͨ���������򹫰���������֤
        params.add(createLbParameter("YYZZNJRQ","ֵ")); // Ӫҵִ���������  yyyymmdd��ʽ
        params.add(createLbParameter("DZ","ֵ")); // ͨѶ��ַ  
        params.add(createLbParameter("YZBM","ֵ")); // �ʱ�  
        params.add(createLbParameter("JTDZ","ֵ")); // ��ͥ��ַ  
        params.add(createLbParameter("JTDZYB","ֵ")); // ��ͥ��ַ�ʱ�  
        params.add(createLbParameter("JTDH","ֵ")); // ��ͥ�绰  
        params.add(createLbParameter("SJ","ֵ")); // �ֻ�  
        params.add(createLbParameter("DH","ֵ")); // �绰  
        params.add(createLbParameter("CZ","ֵ")); // ����  
        params.add(createLbParameter("EMAIL","ֵ")); // EMAIL  
        params.add(createLbParameter("GZDW","ֵ")); // ������λ  
        params.add(createLbParameter("GZDWDZ","ֵ")); // ��λ��ַ  
        params.add(createLbParameter("GZDWYB","ֵ")); // ��λ�ʱ�  
        params.add(createLbParameter("GZDWDH","ֵ")); // ��λ�绰  
        params.add(createLbParameter("QQ","ֵ")); // QQ  
        params.add(createLbParameter("MSN","ֵ")); // MSN  
        params.add(createLbParameter("HYZK","ֵ")); // ����״��  �����ֵ�.�������=GT_HYZK
        params.add(createLbParameter("ZYDM","ֵ")); // ְҵ  �����ֵ�.�������=GT_ZYDM
        params.add(createLbParameter("XL","ֵ")); // ѧ��  �����ֵ�.�������=GT_XLDM
        params.add(createLbParameter("MZDM","ֵ")); // ����  �����ֵ�.�������=MZDM
        params.add(createLbParameter("JG","ֵ")); // ����  
        params.add(createLbParameter("GJ","ֵ")); // ����  ͨ���ӿ�cifQueryGJDM(cif��ѯ��������)��ȡ����IBM(���ֱ���)
        params.add(createLbParameter("PROVINCE","ֵ")); // ʡ��  ͨ���ӿ�cifQuerySFDM(cif��ѯʡ�ݴ���)��ȡ����IBM(���ֱ���)
        params.add(createLbParameter("CITY","ֵ")); // ����  ͨ���ӿ�cifQueryCSDM(cif��ѯ���д���)��ȡ����POST(���д���)
        params.add(createLbParameter("SEC","ֵ")); // ����  ͨ���ӿ�cifQueryCSDM(cif��ѯ���д���)��ȡ����POST(���д���)
        params.add(createLbParameter("TBSM","ֵ")); // �ر�˵��  
        params.add(createLbParameter("ZWDM","ֵ")); // ְλ  �����ֵ�.�������=GT_ZWDM
        params.add(createLbParameter("NJSR","ֵ")); // �������  
        params.add(createLbParameter("ZGZSLX","ֵ")); // �ʸ�֤������  �����ֵ�.�������=GT_ZGZSLX
        params.add(createLbParameter("ZSBH","ֵ")); // ֤����  
        params.add(createLbParameter("CYSJ","ֵ")); // ��ҵʱ��  yyyymmdd��ʽ
        params.add(createLbParameter("JSFS","ֵ")); // ���ͷ�ʽ  �����ֵ�.�������=GT_JSFS
        params.add(createLbParameter("LXFS","ֵ")); // ��ϵ��ʽ  �����ֵ�.�������=GT_LXFS
        params.add(createLbParameter("LLPL","ֵ")); // ����Ƶ��  �����ֵ�.�������=GT_LLPL
        params.add(createLbParameter("GTKHLY","ֵ")); // ��̨�ͻ���Դ  �����ֵ�.�������=GT_KHLY
        params.add(createLbParameter("QSXY","ֵ")); // ǩ��Э��  �����ֵ�.�������=GT_QSXY
        params.add(createLbParameter("SFYZWT","ֵ")); // �����֤����  �����ֵ�.�������=KH_SFYZWT
        params.add(createLbParameter("SFYZDA","ֵ")); // �����֤��  
        params.add(createLbParameter("FXQHYLB","ֵ")); // ��ϴǮ��ҵ���  �����ֵ�.�������=FXQHYLB
        params.add(createLbParameter("XQFXDJ","ֵ")); // ϴǮ���յȼ�  �����ֵ�.�������=XQFXDJ
        params.add(createLbParameter("KHMB","ֵ")); // �ͻ�ģ��  �ͻ�ģ��ID
        params.add(createLbParameter("ZCSX","ֵ")); // �ʲ�����  Ĭ����""
        params.add(createLbParameter("KHFL","ֵ")); // �ͻ�����  Ĭ����""
        params.add(createLbParameter("KHFZ","ֵ")); // �ͻ�����  Ĭ����""
        params.add(createLbParameter("WTFS","ֵ")); // ί�з�ʽ  ��ǰ�˽���ѡ���ί�з�ʽ
        params.add(createLbParameter("KHQX","ֵ")); // �ͻ�Ȩ��  ��ǰ�˽���ѡ��Ŀͻ�Ȩ��
        params.add(createLbParameter("KHXZ","ֵ")); // �ͻ�����  Ĭ����""
        params.add(createLbParameter("GDQX","ֵ")); // �ɶ�Ȩ��  Ĭ����""
        params.add(createLbParameter("GDXZ","ֵ")); // �ɶ�����  Ĭ����""
        params.add(createLbParameter("CBLX","ֵ")); // �ɱ�����  Ĭ����""
        params.add(createLbParameter("LLLB","ֵ")); // �������  Ĭ����""
        params.add(createLbParameter("KHGFXX","ֵ")); // �����淶��Ϣ  Ĭ����""
        params.add(createLbParameter("GSKHLX","ֵ")); // ��˾�ͻ�����  Ĭ����""
        params.add(createLbParameter("FXYSXX","ֵ")); // ����Ҫ����Ϣ  Ĭ����""
        params.add(createLbParameter("YXBZ","ֵ")); // �������  1|�����;2|��Ԫ;3|�۱���ǰ�˽���ѡ��Ŀͻ���ͨ����,���ֱ��ּ���";"�ŷָ��"1;2;3"
        params.add(createLbParameter("CPBZ","ֵ")); // ��Ʒ��־  Ĭ����""
        params.add(createLbParameter("KHKH","ֵ")); // �ͻ�����  
        params.add(createLbParameter("KHH","ֵ")); // �ͻ���  
        params.add(createLbParameter("FXCSNL","ֵ")); // ���ճ�������  �����ֵ�.�������=GT_FXCSNL
        params.add(createLbParameter("KFYYB","ֵ")); // ����Ӫҵ��  ����Ӫҵ����ID(��֤����,ȫԱ����ģʽ��Ϊ������Ա�Ĺ���Ӫҵ��)
        params.add(createLbParameter("KFGY","ֵ")); // ������Ա  ������Ա��ID(��֤����,ȫԱ����ģʽ��Ϊ������Ա��ID)
        params.add(createLbParameter("SFTB","ֵ")); // �Ƿ�ͬ��  0|��ͬ��;1|ͬ��
        params.add(createLbParameter("JYMM","ֵ")); // ��������  Ĭ����""
        params.add(createLbParameter("ZJMM","ֵ")); // �ʽ�����  Ĭ����""
        params.add(createLbParameter("FWMM","ֵ")); // ��������  Ĭ����""
        params.add(createLbParameter("SQJJZH","ֵ")); // ��������˺�  ��ѡֵ���������˾ID����";"�ŷָʾ��"401;411;427"ͨ���ӿ�cifQueryJJGSCS(cif��ѯ����˾����)��ȡ����ID(����˾ID)�б�
        params.add(createLbParameter("GDKH_SH","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SH","ֵ")); // �Ǽǻ�A�ɶ���  GDKH_SH=9ʱ���뻦A�ɶ��˻�
        params.add(createLbParameter("GDKH_HB","ֵ")); // ��B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_HB","ֵ")); // �Ǽǻ�B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_SZ","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SZ","ֵ")); // �Ǽ���A�ɶ���  GDKH_SZ=9ʱ������A�ɶ��˻�
        params.add(createLbParameter("GDKH_SB","ֵ")); // ��B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_SB","ֵ")); // �Ǽ���B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_TA","ֵ")); // ����A�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_TA","ֵ")); // �Ǽ�����A�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_TU","ֵ")); // ����B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_TU","ֵ")); // �Ǽ�����B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDJYQX_SH","ֵ")); // ��A�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_HB","ֵ")); // ��B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_SZ","ֵ")); // ��A�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_SB","ֵ")); // ��B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_TA","ֵ")); // ����A�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_TU","ֵ")); // ����B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("SHZDJY","ֵ")); // ��Aָ��  Ĭ����""
        params.add(createLbParameter("HBZDJY","ֵ")); // ��Bָ��  Ĭ����""
        params.add(createLbParameter("SFWLFW","ֵ")); // �Ƿ���Ҫ�������  Ĭ����""
        params.add(createLbParameter("WLFWMM","ֵ")); // �����������  Ĭ����""
        params.add(createLbParameter("CGYH","ֵ")); // �������  ͨ���ӿ�cifQueryYHCS(cif��ѯ���в���)��ȡ����YHDM(���д���)
        params.add(createLbParameter("CGYHZH","ֵ")); // ��������˻�  һ��ʽָ��ʱ���������˻�����ʽָ��ʱ��""
        params.add(createLbParameter("CGYHMM","ֵ")); // �����������  Ĭ����""
        params.add(createLbParameter("YHDM_USD","ֵ")); // ��Ԫת������  Ĭ����""
        params.add(createLbParameter("DJFS_USD","ֵ")); // ��Ԫ�ǼǷ�ʽ  Ĭ����""
        params.add(createLbParameter("YHZH_USD","ֵ")); // ��Ԫ�����˺�  Ĭ����""
        params.add(createLbParameter("YHMM_USD","ֵ")); // ��Ԫ��������  Ĭ����""
        params.add(createLbParameter("YHDM_HKD","ֵ")); // �۱�ת������  Ĭ����""
        params.add(createLbParameter("DJFS_HKD","ֵ")); // �۱ҵǼǷ�ʽ  Ĭ����""
        params.add(createLbParameter("YHZH_HKD","ֵ")); // �۱������˺�  Ĭ����""
        params.add(createLbParameter("YHMM_HKD","ֵ")); // �۱���������  Ĭ����""
        params.add(createLbParameter("KHZP","ֵ")); // �ͻ���Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
        params.add(createLbParameter("KHSP","ֵ")); // �ͻ���Ƶ  ��Ƶ�ϴ��ɹ��󷵻ص��ַ���
        params.add(createLbParameter("DJR","ֵ")); // ������  ������Ա��ID
        params.add(createLbParameter("CZZD","ֵ")); // ����վ��  �ͻ��˻�����IP��ַ��MAC��ַ
        params.add(createLbParameter("WJID","ֵ")); // �ʾ�ID  ���ղ��������ʾ��ID��
        params.add(createLbParameter("TMDAC","ֵ")); // ��Ŀ�𰸴�  ��Ŀ��𰸼���"|"�ָ�����Ŀ����";"�ŷָ��ѡ������","�ָʾ��:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
        params.add(createLbParameter("LCWJID","ֵ")); // ����ʾ�ID  ��Ʒ�����Ϣ�����ʾ��ID��
        params.add(createLbParameter("LCTMDAC","ֵ")); // �����Ŀ�𰸴�  ��Ŀ��𰸼���"|"�ָ�����Ŀ����";"�ŷָ��ѡ������","�ָʾ��:25|A,B;26|B;27|A;28|C;29|B;30|C;31|A,B,C
        params.add(createLbParameter("LXRXXSTR","ֵ")); // ��ϵ����Ϣ��  ʾ����[{"LXRXM":"Ҧ����", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"�쵼"}, {"LXRXM":"��С��", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"����"}]
        params.add(createLbParameter("DLRSTR","ֵ")); // �����˴�  ʾ����[{"DLRXM":"��һ��", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"����",  "ZJFZJG":"�����й�¥������", "CSRQ":"19840101", "ZJDZ":"�����к���·165��", "DZ":"����������·150��",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
        params.add(createLbParameter("YXSTR","ֵ")); // Ӱ��  ʾ����[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        params.add(createLbParameter("JGKGGDSTR","ֵ")); // �����عɹɶ���  Ĭ����""
        params.add(createLbParameter("DJJJZHSTR","ֵ")); // �Ǽǻ����˺�  Ĭ����""
        params.add(createLbParameter("FJSXSTR","ֵ")); // �������Դ�  Ĭ����""
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_HS_KHSQ", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // ������ˮ��  
        }
    }

    // cif��ѯ���ֳ���������
    public void cifCXFXCKHSQ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","ֵ")); // ��ѯ��־  1-������ID��ѯ[�����������г���]2-�������˲�ѯ(��֤����)[�����ֶ�ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]3-���ֻ��Ų�ѯ(���Ͽ���)[�����ֶ�ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]4-��֤����Ų�ѯ(���Ͽ���)[�����ֶ�ID,JGBZ,KHXM,KHQC,YYB,SQRQ,SQSJ,SJ,ZJBH,ZJLB,STEP]
        params.add(createLbParameter("SQID","ֵ")); // ����ID  FLAG=1ʱ����
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  FLAG=2ʱ����
        params.add(createLbParameter("SJ","ֵ")); // �ֻ�  FLAG=3ʱ����
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  FLAG=4ʱ����
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  FLAG=4ʱ����
        params.add(createLbParameter("STEP","ֵ")); // ��������  FLAG=2,3,4ʱѡ��
        params.add(createLbParameter("JGBZ","ֵ")); // ������־  0-����;1-����;�����ѯ����
        params.add(createLbParameter("KSRQ","ֵ")); // ��ʼ����  yyyymmdd��ʽ,�Ǳ���
        params.add(createLbParameter("JSRQ","ֵ")); // ��������  yyyymmdd��ʽ,�Ǳ���
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���
        params.add(createLbParameter("CXNR","ֵ")); // ��ѯ����  ֧�ֶԿͻ����������֤�Ž���ģ����ѯ
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ����ID  
            System.out.print("SQRQ = " + mapData.get("SQRQ") + "\t "); // ��������  yyyymmdd��ʽ
            System.out.print("SQSJ = " + mapData.get("SQSJ") + "\t "); // ����ʱ��  
            System.out.print("SHZT = " + mapData.get("SHZT") + "\t "); // ���״̬  �����ֵ�.�������=SHZT(0|δ���;1|���ͨ��;2|��˲�ͨ��)
            System.out.print("STEP = " + mapData.get("STEP") + "\t "); // ��������  �����ֵ�.�������=KH_STEP
            System.out.print("CLJGSM = " + mapData.get("CLJGSM") + "\t "); // ������˵��  
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���;11|ȫԱ����
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // �ͻ�����  0|����;1|����
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // ����Ӫҵ��  
            System.out.print("KHLY = " + mapData.get("KHLY") + "\t "); // �ͻ���Դ  
            System.out.print("KHXM = " + mapData.get("KHXM") + "\t "); // �ͻ�����  
            System.out.print("KHQC = " + mapData.get("KHQC") + "\t "); // �ͻ�ȫ��  
            System.out.print("JZR = " + mapData.get("JZR") + "\t "); // ��֤��  ��֤����ʱ�������������ʽʱ����
            System.out.print("KHXZR = " + mapData.get("KHXZR") + "\t "); // ����Э����  ��֤����ʱ�������������ʽʱ����
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // ֤�����  �����ֵ�.�������=GT_ZJLB
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // ֤�����  
            System.out.print("ZJQSRQ = " + mapData.get("ZJQSRQ") + "\t "); // ֤����ʼ����  
            System.out.print("ZJJZRQ = " + mapData.get("ZJJZRQ") + "\t "); // ֤����ֹ����  
            System.out.print("ZJDZ = " + mapData.get("ZJDZ") + "\t "); // ֤����ַ  
            System.out.print("ZJDZYB = " + mapData.get("ZJDZYB") + "\t "); // ֤����ַ�ʱ�  
            System.out.print("ZJFZJG = " + mapData.get("ZJFZJG") + "\t "); // ��֤����  
            System.out.print("ZJZP = " + mapData.get("ZJZP") + "\t "); // ֤����Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
            System.out.print("ZJYZLY = " + mapData.get("ZJYZLY") + "\t "); // ֤����֤��Դ  �����ֵ�.�������=KH_ZJYZLY
            System.out.print("EDZ = " + mapData.get("EDZ") + "\t "); // ����֤У��  
            System.out.print("YYZZNJRQ = " + mapData.get("YYZZNJRQ") + "\t "); // Ӫҵִ���������  
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // ͨѶ��ַ  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // �ʱ�  
            System.out.print("JTDZ = " + mapData.get("JTDZ") + "\t "); // ��ͥ��ַ  
            System.out.print("JTDZYB = " + mapData.get("JTDZYB") + "\t "); // ��ͥ��ַ�ʱ�  
            System.out.print("JTDH = " + mapData.get("JTDH") + "\t "); // ��ͥ�绰  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // �ֻ�  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // �绰  
            System.out.print("CZ = " + mapData.get("CZ") + "\t "); // ����  
            System.out.print("EMAIL = " + mapData.get("EMAIL") + "\t "); // EMAIL  
            System.out.print("GZDW = " + mapData.get("GZDW") + "\t "); // ������λ  
            System.out.print("GZDWDZ = " + mapData.get("GZDWDZ") + "\t "); // ��λ��ַ  
            System.out.print("GZDWYB = " + mapData.get("GZDWYB") + "\t "); // ��λ�ʱ�  
            System.out.print("GZDWDH = " + mapData.get("GZDWDH") + "\t "); // ��λ�绰  
            System.out.print("QQ = " + mapData.get("QQ") + "\t "); // QQ  
            System.out.print("MSN = " + mapData.get("MSN") + "\t "); // MSN  
            System.out.print("HYZK = " + mapData.get("HYZK") + "\t "); // ����״��  �����ֵ�.�������=GT_HYZK
            System.out.print("ZYDM = " + mapData.get("ZYDM") + "\t "); // ְҵ  �����ֵ�.�������=GT_ZYDM
            System.out.print("XL = " + mapData.get("XL") + "\t "); // ѧ��  �����ֵ�.�������=GT_XLDM
            System.out.print("MZDM = " + mapData.get("MZDM") + "\t "); // ����  �����ֵ�.�������=MZDM
            System.out.print("JG = " + mapData.get("JG") + "\t "); // ����  
            System.out.print("GJ = " + mapData.get("GJ") + "\t "); // ����  ͨ���ӿ�cifQueryGJDM(cif��ѯ��������)��ȡ����IBM(���ֱ���)
            System.out.print("PROVINCE = " + mapData.get("PROVINCE") + "\t "); // ʡ��  ͨ���ӿ�cifQuerySFDM(cif��ѯʡ�ݴ���)��ȡ����IBM(���ֱ���)
            System.out.print("CITY = " + mapData.get("CITY") + "\t "); // ����  ͨ���ӿ�cifQueryCSDM(cif��ѯ���д���)��ȡ����POST(���д���)
            System.out.print("SEC = " + mapData.get("SEC") + "\t "); // ����  ͨ���ӿ�cifQueryCSDM(cif��ѯ���д���)��ȡ����POST(���д���)
            System.out.print("TBSM = " + mapData.get("TBSM") + "\t "); // �ر�˵��  
            System.out.print("ZWDM = " + mapData.get("ZWDM") + "\t "); // ְλ  �����ֵ�.�������=GT_ZWDM
            System.out.print("NJSR = " + mapData.get("NJSR") + "\t "); // �������  
            System.out.print("ZGZSLX = " + mapData.get("ZGZSLX") + "\t "); // �ʸ�֤������  �����ֵ�.�������=GT_ZGZSLX
            System.out.print("ZSBH = " + mapData.get("ZSBH") + "\t "); // ֤����  
            System.out.print("CYSJ = " + mapData.get("CYSJ") + "\t "); // ��ҵʱ��  
            System.out.print("JSFS = " + mapData.get("JSFS") + "\t "); // ���ͷ�ʽ  �����ֵ�.�������=GT_JSFS
            System.out.print("LXFS = " + mapData.get("LXFS") + "\t "); // ��ϵ��ʽ  �����ֵ�.�������=GT_LXFS
            System.out.print("LLPL = " + mapData.get("LLPL") + "\t "); // ����Ƶ��  �����ֵ�.�������=GT_LLPL
            System.out.print("GTKHLY = " + mapData.get("GTKHLY") + "\t "); // ��̨�ͻ���Դ  �����ֵ�.�������=GT_KHLY
            System.out.print("QSXY = " + mapData.get("QSXY") + "\t "); // ǩ��Э��  �����ֵ�.�������=GT_QSXY
            System.out.print("SFYZWT = " + mapData.get("SFYZWT") + "\t "); // �����֤����  �����ֵ�.�������=KH_SFYZWT
            System.out.print("SFYZDA = " + mapData.get("SFYZDA") + "\t "); // �����֤��  
            System.out.print("FXQHYLB = " + mapData.get("FXQHYLB") + "\t "); // ��ϴǮ��ҵ���  �����ֵ�.�������=FXQHYLB
            System.out.print("XQFXDJ = " + mapData.get("XQFXDJ") + "\t "); // ϴǮ���յȼ�  �����ֵ�.�������=XQFXDJ
            System.out.print("KHMB = " + mapData.get("KHMB") + "\t "); // �ͻ�ģ��  �ͻ�ģ��ID
            System.out.print("ZCSX = " + mapData.get("ZCSX") + "\t "); // �ʲ�����  
            System.out.print("KHFL = " + mapData.get("KHFL") + "\t "); // �ͻ�����  
            System.out.print("KHFZ = " + mapData.get("KHFZ") + "\t "); // �ͻ�����  
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // ί�з�ʽ  
            System.out.print("KHQX = " + mapData.get("KHQX") + "\t "); // �ͻ�Ȩ��  
            System.out.print("KHXZ = " + mapData.get("KHXZ") + "\t "); // �ͻ�����  
            System.out.print("GDQX = " + mapData.get("GDQX") + "\t "); // �ɶ�Ȩ��  
            System.out.print("GDXZ = " + mapData.get("GDXZ") + "\t "); // �ɶ�����  
            System.out.print("CBLX = " + mapData.get("CBLX") + "\t "); // �ɱ�����  
            System.out.print("LLLB = " + mapData.get("LLLB") + "\t "); // �������  
            System.out.print("KHGFXX = " + mapData.get("KHGFXX") + "\t "); // �����淶��Ϣ  
            System.out.print("GSKHLX = " + mapData.get("GSKHLX") + "\t "); // ��˾�ͻ�����  
            System.out.print("FXYSXX = " + mapData.get("FXYSXX") + "\t "); // ����Ҫ����Ϣ  
            System.out.print("YXBZ = " + mapData.get("YXBZ") + "\t "); // �������  
            System.out.print("CPBZ = " + mapData.get("CPBZ") + "\t "); // ��Ʒ��־  
            System.out.print("KHKH = " + mapData.get("KHKH") + "\t "); // �ͻ�����  
            System.out.print("KHH = " + mapData.get("KHH") + "\t "); // �ͻ���  
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // ���ճ�������  �����ֵ�.�������=GT_FXCSNL
            System.out.print("KFYYB = " + mapData.get("KFYYB") + "\t "); // ����Ӫҵ��  ����Ӫҵ����ID(��֤����,ȫԱ����ģʽ��Ϊ������Ա�Ĺ���Ӫҵ��)
            System.out.print("KFGY = " + mapData.get("KFGY") + "\t "); // ������Ա  ������Ա��ID(��֤����,ȫԱ����ģʽ��Ϊ������Ա��ID)
            System.out.print("SFTB = " + mapData.get("SFTB") + "\t "); // �Ƿ�ͬ��  0|��ͬ��;1|ͬ��
            System.out.print("JYMM = " + mapData.get("JYMM") + "\t "); // ��������  
            System.out.print("ZJMM = " + mapData.get("ZJMM") + "\t "); // �ʽ�����  
            System.out.print("FWMM = " + mapData.get("FWMM") + "\t "); // ��������  
            System.out.print("SQJJZH = " + mapData.get("SQJJZH") + "\t "); // ��������˺�  ��ѡֵ���������˾ID����"";""�ŷָʾ��""401;411;427""ͨ���ӿ�cifQueryJJGSCS(cif��ѯ����˾����)��ȡ����ID(����˾ID)�б�
            System.out.print("GDKH_SH = " + mapData.get("GDKH_SH") + "\t "); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
            System.out.print("GDDJ_SH = " + mapData.get("GDDJ_SH") + "\t "); // �Ǽǻ�A�ɶ���  ��A�ɶ��˻�
            System.out.print("GDKH_HB = " + mapData.get("GDKH_HB") + "\t "); // ��B�ɶ�����  
            System.out.print("GDDJ_HB = " + mapData.get("GDDJ_HB") + "\t "); // �Ǽǻ�B�ɶ���  
            System.out.print("GDKH_SZ = " + mapData.get("GDKH_SZ") + "\t "); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
            System.out.print("GDDJ_SZ = " + mapData.get("GDDJ_SZ") + "\t "); // �Ǽ���A�ɶ���  ��A�ɶ��˻�
            System.out.print("GDKH_SB = " + mapData.get("GDKH_SB") + "\t "); // ��B�ɶ�����  
            System.out.print("GDDJ_SB = " + mapData.get("GDDJ_SB") + "\t "); // �Ǽ���B�ɶ���  
            System.out.print("GDKH_TA = " + mapData.get("GDKH_TA") + "\t "); // ����A�ɶ�����  
            System.out.print("GDDJ_TA = " + mapData.get("GDDJ_TA") + "\t "); // �Ǽ�����A�ɶ���  
            System.out.print("GDKH_TU = " + mapData.get("GDKH_TU") + "\t "); // ����B�ɶ�����  
            System.out.print("GDDJ_TU = " + mapData.get("GDDJ_TU") + "\t "); // �Ǽ�����B�ɶ���  
            System.out.print("GDJYQX_SH = " + mapData.get("GDJYQX_SH") + "\t "); // ��A�ɶ�����Ȩ��  
            System.out.print("GDJYQX_HB = " + mapData.get("GDJYQX_HB") + "\t "); // ��B�ɶ�����Ȩ��  
            System.out.print("GDJYQX_SZ = " + mapData.get("GDJYQX_SZ") + "\t "); // ��A�ɶ�����Ȩ��  
            System.out.print("GDJYQX_SB = " + mapData.get("GDJYQX_SB") + "\t "); // ��B�ɶ�����Ȩ��  
            System.out.print("GDJYQX_TA = " + mapData.get("GDJYQX_TA") + "\t "); // ����A�ɶ�����Ȩ��  
            System.out.print("GDJYQX_TU = " + mapData.get("GDJYQX_TU") + "\t "); // ����B�ɶ�����Ȩ��  
            System.out.print("SHZDJY = " + mapData.get("SHZDJY") + "\t "); // ��Aָ��  
            System.out.print("HBZDJY = " + mapData.get("HBZDJY") + "\t "); // ��Bָ��  
            System.out.print("SFWLFW = " + mapData.get("SFWLFW") + "\t "); // �Ƿ���Ҫ�������  
            System.out.print("WLFWMM = " + mapData.get("WLFWMM") + "\t "); // �����������  
            System.out.print("CGYH = " + mapData.get("CGYH") + "\t "); // �������  ͨ���ӿ�cifQueryYHCS(cif��ѯ���в���)��ȡ����YHDM(���д���)
            System.out.print("CGYHZH = " + mapData.get("CGYHZH") + "\t "); // ��������˻�  
            System.out.print("CGYHMM = " + mapData.get("CGYHMM") + "\t "); // �����������  
            System.out.print("YHDM_USD = " + mapData.get("YHDM_USD") + "\t "); // ��Ԫת������  
            System.out.print("DJFS_USD = " + mapData.get("DJFS_USD") + "\t "); // ��Ԫ�ǼǷ�ʽ  
            System.out.print("YHZH_USD = " + mapData.get("YHZH_USD") + "\t "); // ��Ԫ�����˺�  
            System.out.print("YHMM_USD = " + mapData.get("YHMM_USD") + "\t "); // ��Ԫ��������  
            System.out.print("YHDM_HKD = " + mapData.get("YHDM_HKD") + "\t "); // �۱�ת������  
            System.out.print("DJFS_HKD = " + mapData.get("DJFS_HKD") + "\t "); // �۱ҵǼǷ�ʽ  
            System.out.print("YHZH_HKD = " + mapData.get("YHZH_HKD") + "\t "); // �۱������˺�  
            System.out.print("YHMM_HKD = " + mapData.get("YHMM_HKD") + "\t "); // �۱���������  
            System.out.print("KHZP = " + mapData.get("KHZP") + "\t "); // �ͻ���Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
            System.out.print("KHSP = " + mapData.get("KHSP") + "\t "); // �ͻ���Ƶ  ��Ƶ�ϴ��ɹ��󷵻ص��ַ���
            System.out.print("DJR = " + mapData.get("DJR") + "\t "); // ������  ������Ա��ID
            System.out.print("CZZD = " + mapData.get("CZZD") + "\t "); // ����վ��  �ͻ��˻�����IP��ַ��MAC��ַ
            System.out.print("CPSCORE = " + mapData.get("CPSCORE") + "\t "); // �����÷�  
            System.out.print("CPFXCSNL = " + mapData.get("CPFXCSNL") + "\t "); // �������ճ�������  �����ֵ�.�������=GT_FXCSNL
            System.out.print("LXRXXSTR = " + mapData.get("LXRXXSTR") + "\t "); // ��ϵ����Ϣ��  ʾ����[{"LXRXM":"Ҧ����", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"�쵼"}, {"LXRXM":"��С��", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"����"}]
            System.out.print("DLRSTR = " + mapData.get("DLRSTR") + "\t "); // �����˴�  ʾ����[{"DLRXM":"��һ��", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"����",  "ZJFZJG":"�����й�¥������", "CSRQ":"19840101", "ZJDZ":"�����к���·165��", "DZ":"����������·150��",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
            System.out.print("YXSTR = " + mapData.get("YXSTR") + "\t "); // Ӱ��  ʾ����[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // �Ϻ��״ν����ղ�ѯ
    public void cifwsSHSCJYR_CX() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ��¼��Ա��ID
        params.add(createLbParameter("ZQZH","ֵ")); // ֤ȯ�˺�  
        params.add(createLbParameter("JYS","ֵ")); // ������  ��1
        params.add(createLbParameter("CFBZ","ֵ")); // �ط���־  0-��ʾ�������벻����,1-ǿ���ط�.Ĭ����"0"
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsSHSCJYR_CX", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // ������ˮ��  
        }
    }

    // cif��ѯ�ٹ���˹�Ա��Ϣ
    public void cifCXLGSHGYXX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","ֵ")); // ҵ����˴���  ȫԱ��������"00001"��֤��������"00002"���Ͽ�������"00003"
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ȫԱ��������֤������д��¼�û���ID���Ͽ����ɲ���
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
            System.out.print("USERID = " + mapData.get("USERID") + "\t "); // ��Ա����  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // ��Ա����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯҵ����˲���
    public void cifCXYWSHCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","ֵ")); // ҵ����˴���  ȫԱ��������"00001"��֤��������"00002"���Ͽ�������"00003"
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ȫԱ��������֤������д��¼�û���ID���Ͽ����ɲ���
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
            System.out.print("ZZFLCS = " + mapData.get("ZZFLCS") + "\t "); // ��֯�������  0-�ܲ�����,1-��֯�������
            System.out.print("LGSHBZ = " + mapData.get("LGSHBZ") + "\t "); // �ٹ���˱�־  0-�����ٹ����,1-��Ҫ�ٹ����
            System.out.print("LGSHJS = " + mapData.get("LGSHJS") + "\t "); // �ٹ���˽�ɫ  
            System.out.print("LGSHYZFS = " + mapData.get("LGSHYZFS") + "\t "); // �ٹ������֤��ʽ  1-������֤
            System.out.print("HTSHBZ = " + mapData.get("HTSHBZ") + "\t "); // ��̨��˱�־  0-�����̨���,1-��Ҫ��̨���

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ֤������Ƿ�������
    public void cifCXZJBHYXKH() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
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
            System.out.print("CODE = " + mapData.get("CODE") + "\t "); // ������  ���Ƿ���1
            System.out.print("FLAG = " + mapData.get("FLAG") + "\t "); // �Ƿ�������  <0 ��ֹ����(���֤������ϵͳ�п���������ͨѸ�쳣��)>0 ������
            System.out.print("NOTE = " + mapData.get("NOTE") + "\t "); // ����˵��  FLAG<0ʱ,���ؾ���Ĵ�����Ϣ(���֤������ϵͳ�п���������ͨѸ�쳣��)FLAG>0ʱ,����"������"

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ�ɲ���Ӫҵ��
    public void cifCXKCZYYB() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YWSHDM","ֵ")); // ҵ����˴���  ȫԱ��������"00001"��֤��������"00002"���Ͽ�������"00003"
        params.add(createLbParameter("GYID","ֵ")); // ��ԱID  ȫԱ��������֤������д��¼�û���ID���Ͽ����ɲ���
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
            System.out.print("ORGCODE = " + mapData.get("ORGCODE") + "\t "); // ��֯����  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // ��֯����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ��֯����
    public void cifQueryZZJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYBBM","ֵ")); // Ӫҵ������  �����ѯȫ������
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ��֯����ID  
            System.out.print("FID = " + mapData.get("FID") + "\t "); // �ϼ���֯����ID  
            System.out.print("ORGTYPE = " + mapData.get("ORGTYPE") + "\t "); // ��֯����  1-��˾;2-�ֹ�˾;3-Ӫҵ��;4-����;5-С��
            System.out.print("ORGCODE = " + mapData.get("ORGCODE") + "\t "); // ��֯����  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // ��֯����  
            System.out.print("ZZFL = " + mapData.get("ZZFL") + "\t "); // ��֯����  1-�ٹ񸴺�Ӫҵ��;2-���ٹ񸴺�Ӫҵ��
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // Ӫҵ��ַ  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // �ʱ�  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // ��ѯ�绰  
            System.out.print("PROVINCE = " + mapData.get("PROVINCE") + "\t "); // ʡ��  
            System.out.print("CITY = " + mapData.get("CITY") + "\t "); // ����  
            System.out.print("SEC = " + mapData.get("SEC") + "\t "); // ����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ��Ա��Ϣ
    public void cifQueryGYXX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","ֵ")); // ��ѯ��־  FLAG=1,���չ�Ա���Ų�ѯFLAG=2,�������֤�Ų�ѯ
        params.add(createLbParameter("USERID","ֵ")); // ��Ա����  FLAG=1ʱ����
        params.add(createLbParameter("SFZH","ֵ")); // ���֤��  FLAG=2ʱ����
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ��ԱID  CIFϵͳ�б�ʶ��Ա��Ψһ����
            System.out.print("ORGID = " + mapData.get("ORGID") + "\t "); // ��ԱӪҵ��  ��Ա��������֯����(Ӫҵ��)ID
            System.out.print("USERID = " + mapData.get("USERID") + "\t "); // ��Ա����  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // �û�����  
            System.out.print("SFZH = " + mapData.get("SFZH") + "\t "); // ���֤��  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // �ֻ�  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // �绰  
            System.out.print("GRADE = " + mapData.get("GRADE") + "\t "); // �û����  0|�ڲ�Ա��;1|�ⲿ�ͻ�;2|ϵͳ����Ա;3|������Ա;4|Webservice�����û�
            System.out.print("GYFL = " + mapData.get("GYFL") + "\t "); // ��Ա����  �����ֵ�.�������='GT_GYFL'
            System.out.print("LASTLOGIN = " + mapData.get("LASTLOGIN") + "\t "); // �����¼ʱ��  
            System.out.print("LOGINS = " + mapData.get("LOGINS") + "\t "); // ��¼����  
            System.out.print("CHGPWDLIMIT = " + mapData.get("CHGPWDLIMIT") + "\t "); // ��������  
            System.out.print("CHGPWDTIME = " + mapData.get("CHGPWDTIME") + "\t "); // ������ʱ��  
            System.out.print("STATUS = " + mapData.get("STATUS") + "\t "); // �����½  1|��;2|��
            System.out.print("IPLIMIT = " + mapData.get("IPLIMIT") + "\t "); // ��½IP����  
            System.out.print("LOCKTIME = " + mapData.get("LOCKTIME") + "\t "); // ����ʱ��  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ�����ֵ�
    public void cifQuerySJZD() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLDM","ֵ")); // �������  ���Ϊ���򲻷����κ�����
        params.add(createLbParameter("IBM","ֵ")); // ��ֵ����  ����Ϊ��
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
            System.out.print("FLDM = " + mapData.get("FLDM") + "\t "); // �������   
            System.out.print("FLMC = " + mapData.get("FLMC") + "\t "); // ��������   
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // ���ֱ���   
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // �ַ�����   
            System.out.print("NOTE = " + mapData.get("NOTE") + "\t "); // ˵��   
            System.out.print("FLAG = " + mapData.get("FLAG") + "\t "); // ��־   

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ��������
    public void cifQueryGJDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("GJDM","ֵ")); // ��������  �Ǳ��Ϊ�����ѯȫ����������
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
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // ���ֱ���  ��������ʱ����Ӧ�ӿڴ����ֵ
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // ��ĸ����  
            System.out.print("YWMC = " + mapData.get("YWMC") + "\t "); // Ӣ������  
            System.out.print("GJMC = " + mapData.get("GJMC") + "\t "); // ��������  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯʡ�ݴ���
    public void cifQuerySFDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("SFDM","ֵ")); // ʡ�ݴ���  �Ǳ��Ϊ�����ѯȫ��ʡ�ݴ���
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
            System.out.print("IBM = " + mapData.get("IBM") + "\t "); // ���ֱ���  ��������ʱ����Ӧ�ӿڴ����ֵ
            System.out.print("CBM = " + mapData.get("CBM") + "\t "); // ��ĸ����  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // ʡ������  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ���д���
    public void cifQueryCSDM() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("CSDM","ֵ")); // ���д���  ���д������ϼ����붼Ϊ��ʱ��ѯȫ�����д���
        params.add(createLbParameter("SJDM","ֵ")); // �ϼ�����  
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
            System.out.print("POST = " + mapData.get("POST") + "\t "); // ���д���  ��������ʱ����Ӧ�ӿڴ����ֵ
            System.out.print("CODE = " + mapData.get("CODE") + "\t "); // ƴ������  
            System.out.print("NAME = " + mapData.get("NAME") + "\t "); // ��������  
            System.out.print("PARENT = " + mapData.get("PARENT") + "\t "); // �ϼ�����  
            System.out.print("PHONECODE = " + mapData.get("PHONECODE") + "\t "); // �绰����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ���в���
    public void cifQueryYHCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YHDM","ֵ")); // ���д���  �Ǳ��Ϊ�����ѯȫ�����в���
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
            System.out.print("YHDM = " + mapData.get("YHDM") + "\t "); // ���д���  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // ���ñ���  �����ֵ�.�������='GT_BZ'
            System.out.print("YHMC = " + mapData.get("YHMC") + "\t "); // ��������  
            System.out.print("YYBFW = " + mapData.get("YYBFW") + "\t "); // ����Ӫҵ��  �������Ӫҵ��ID����";"�ŷָ�
            System.out.print("CGBZ = " + mapData.get("CGBZ") + "\t "); // ��ܱ�־  1-���;0-��֤ת��
            System.out.print("CGZDFS = " + mapData.get("CGZDFS") + "\t "); // ���ָ����ʽ  ��ѡֵ,���ֵ����";"�ŷָ�1-ֱ��ָ��;2-Ԥָ��ʾ��: "1;2"��ʾ������ͬʱ֧��ֱ��ָ����Ԥָ��

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ�ʾ����ֱ�׼
    public void cifQueryWJPFBZ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("WJID","ֵ")); // �ʾ�ID  ���Ϊ���򲻷����κ�����
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
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // ���ճ�������  �����ֵ�.�������='GT_FXCSNL'
            System.out.print("PFXX = " + mapData.get("PFXX") + "\t "); // ��������  ���ּ����㷨: ��������<=�÷�<��������
            System.out.print("PFSX = " + mapData.get("PFSX") + "\t "); // ��������  
            System.out.print("TZMS = " + mapData.get("TZMS") + "\t "); // ��������  
            System.out.print("WJID = " + mapData.get("WJID") + "\t "); // �����ʾ�  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ���������֤����
    public void cifQueryGMSFYZSQ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("SQID","ֵ")); // ����ID  ���Ϊ���򲻷����κ�����
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ����ID  
            System.out.print("SQRQ = " + mapData.get("SQRQ") + "\t "); // ��������  
            System.out.print("SQSJ = " + mapData.get("SQSJ") + "\t "); // ����ʱ��  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // ֤�����  
            System.out.print("SQXM = " + mapData.get("SQXM") + "\t "); // ��������  
            System.out.print("CSRQ = " + mapData.get("CSRQ") + "\t "); // ��������  
            System.out.print("XM = " + mapData.get("XM") + "\t "); // ����  
            System.out.print("NATION = " + mapData.get("NATION") + "\t "); // ����  �����ֵ�.�������='MZDM'
            System.out.print("XB = " + mapData.get("XB") + "\t "); // �Ա�  �����ֵ�.�������='GT_XB'
            System.out.print("ZJBHHCJG = " + mapData.get("ZJBHHCJG") + "\t "); // ��ݺ���˲���  
            System.out.print("XMHCJG = " + mapData.get("XMHCJG") + "\t "); // �����˲���  
            System.out.print("CLJG = " + mapData.get("CLJG") + "\t "); // ������  CLJG>0,��ʾ��ѯ�ɹ�CLJG<0,��ʾ��ѯʧ��CLJG=0,��ʾ��δ��ȡ����ѯ����
            System.out.print("JGSM = " + mapData.get("JGSM") + "\t "); // ���˵��  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ֤ȯ��ҵ֤��
    public void cifQueryZQCYZS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("FLAG","ֵ")); // ��ѯ��־  1-�������֤�Ų�ѯ2-������ҳ��ѯ
        params.add(createLbParameter("SFZH","ֵ")); // ���֤��  FLAG=1ʱ����,��ҵ��Ա���֤��(��ͨ��cifQueryGYXX�ӿڻ��)
        params.add(createLbParameter("KSID","ֵ")); // ��ʼID  FLAG=2ʱ����
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ��ҵ֤��ID  
            System.out.print("XM = " + mapData.get("XM") + "\t "); // ����  
            System.out.print("XB = " + mapData.get("XB") + "\t "); // �Ա�  �����ֵ�.�������='GT_XB'
            System.out.print("ZYJG = " + mapData.get("ZYJG") + "\t "); // ִҵ����  
            System.out.print("ZSBH = " + mapData.get("ZSBH") + "\t "); // ֤����  
            System.out.print("ZYGW = " + mapData.get("ZYGW") + "\t "); // ִҵ��λ  
            System.out.print("XL = " + mapData.get("XL") + "\t "); // ѧ��  �����ֵ�.�������='GT_XLDM'
            System.out.print("ZSQDRQ = " + mapData.get("ZSQDRQ") + "\t "); // ֤��ȡ������  yyyymmdd��ʽ
            System.out.print("ZSYXRQ = " + mapData.get("ZSYXRQ") + "\t "); // ֤����Ч��ֹ����  yyyymmdd��ʽ
            System.out.print("SFZH = " + mapData.get("SFZH") + "\t "); // ���֤��  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ����Ӱ��ɼ�
    public void cifQueryKHYXCJ() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
        params.add(createLbParameter("JGBZ","ֵ")); // ������־  0-����;1-����
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
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // ������־  0-����;1-����
            System.out.print("WJLX = " + mapData.get("WJLX") + "\t "); // �ļ�����  1|ͼ���ļ�;2|��Ƶ�ļ�
            System.out.print("YXLX = " + mapData.get("YXLX") + "\t "); // Ӱ������ID  
            System.out.print("YXLXMC = " + mapData.get("YXLXMC") + "\t "); // Ӱ����������  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ����Ͷ���߽���
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // Ͷ���߽���Э��ID  
            System.out.print("JYLB = " + mapData.get("JYLB") + "\t "); // �������  �����ֵ�.�������='KH_JYLB'
            System.out.print("BT = " + mapData.get("BT") + "\t "); // ����  
            System.out.print("ZW = " + mapData.get("ZW") + "\t "); // ����  
            System.out.print("WJBB = " + mapData.get("WJBB") + "\t "); // �ļ��汾  
            System.out.print("YDSJ = " + mapData.get("YDSJ") + "\t "); // �Ķ�ʱ��(��)  
            System.out.print("YXQSRQ = " + mapData.get("YXQSRQ") + "\t "); // ��Ч��ʼ����  yyyymmdd��ʽ
            System.out.print("YXJZRQ = " + mapData.get("YXJZRQ") + "\t "); // ��Ч��ֹ����  yyyymmdd��ʽ
            System.out.print("ZT = " + mapData.get("ZT") + "\t "); // ״̬  0|����;1|����
            System.out.print("PX = " + mapData.get("PX") + "\t "); // ����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ�еǹɶ���ѯ���
    public void cifQueryZDGDCXJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  �����ֵ�.�������='GT_ZJLB'
        params.add(createLbParameter("GDH","ֵ")); // �ɶ���  
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
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // ֤�����  �����ֵ�.�������='GT_ZJLB'
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // ֤�����  
            System.out.print("YZRQ = " + mapData.get("YZRQ") + "\t "); // ��֤����  yyyymmdd��ʽ
            System.out.print("JYS = " + mapData.get("JYS") + "\t "); // ������  �����ֵ�.�������='GT_JYS'
            System.out.print("ZDGDH = " + mapData.get("ZDGDH") + "\t "); // �ɶ���  
            System.out.print("ZDGDZT = " + mapData.get("ZDGDZT") + "\t "); // �еǹɶ�״̬  �����ֵ�.�������='GT_GDZT'
            System.out.print("ZDGDXM = " + mapData.get("ZDGDXM") + "\t "); // �еǹɶ�����  
            System.out.print("ZJYXQ = " + mapData.get("ZJYXQ") + "\t "); // ֤����Ч��  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ����Ĭ������
    public void cifQueryKHMRSX() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
        params.add(createLbParameter("JGBZ","ֵ")); // ������־  0-����;1-����
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
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // ������־  0-����;1-����
            System.out.print("ENWTFS = " + mapData.get("ENWTFS") + "\t "); // ����ί�з�ʽ  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_WTFS'
            System.out.print("ENKHQX = " + mapData.get("ENKHQX") + "\t "); // ����ͻ�Ȩ��  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_KHQX'
            System.out.print("ENGDQX_SH = " + mapData.get("ENGDQX_SH") + "\t "); // ����A�ɶ�Ȩ��  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_GDQX'
            System.out.print("ENGDQX_SZ = " + mapData.get("ENGDQX_SZ") + "\t "); // ������A�ɶ�Ȩ��  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_GDQX'
            System.out.print("WJBM = " + mapData.get("WJBM") + "\t "); // �ʾ����  
            System.out.print("LCFWWJBM = " + mapData.get("LCFWWJBM") + "\t "); // ��Ʒ����ʾ����  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ����˾����
    public void cifQueryJJGSCS() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("JJGSDM","ֵ")); // ����˾����  �Ǳ���,��λ�Ļ���˾����
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // ����˾ID  
            System.out.print("JJGSDM = " + mapData.get("JJGSDM") + "\t "); // ����˾����  
            System.out.print("JJGSQC = " + mapData.get("JJGSQC") + "\t "); // ����˾ȫ��  
            System.out.print("JJGSJC = " + mapData.get("JJGSJC") + "\t "); // ����˾���  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // cif��ѯ�ͻ�ģ��
    public void cifQueryKHMB() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","ֵ")); // Ӫҵ��ID  
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
        params.add(createLbParameter("JGBZ","ֵ")); // ������־  0-����;1-����
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
            System.out.print("ID = " + mapData.get("ID") + "\t "); // �ͻ�ģ��ID  
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // Ӫҵ��ID  
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // ������ʽ  2-��֤����;3-���Ͽ���;11-ȫԱ����
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // ������־  0-����;1-����
            System.out.print("MBMC = " + mapData.get("MBMC") + "\t "); // ģ������  
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // ί�з�ʽ  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_WTFS'
            System.out.print("KHQX = " + mapData.get("KHQX") + "\t "); // �ͻ�Ȩ��  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_KHQX'
            System.out.print("GDQX = " + mapData.get("GDQX") + "\t "); // �ɶ�Ȩ��  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_GDQX'
            System.out.print("YXBZ = " + mapData.get("YXBZ") + "\t "); // �������  ��ѡֵ,���ֵ����";"�ŷָ������ֵ�.�������='GT_BZ'

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // ���ÿͻ�����_��������
    public void cifwsCZKHMM_CZMM() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","ֵ")); // Ӫҵ��ID  
        params.add(createLbParameter("KHH","ֵ")); // �ͻ���  
        params.add(createLbParameter("ZCZH","ֵ")); // �ʲ��˺�  
        params.add(createLbParameter("MMLB","ֵ")); // �������  1-�ʽ�����;2-��������;9-��������
        params.add(createLbParameter("NEWMM","ֵ")); // ������  ��������
        params.add(createLbParameter("ZY","ֵ")); // ����ժҪ  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsCZKHMM_CZMM", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
        }
    }

    // ָ���������
    public void cifwsZDCGYH_ZDCGYH() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YYB","ֵ")); // Ӫҵ��ID  
        params.add(createLbParameter("KHH","ֵ")); // �ͻ���  
        params.add(createLbParameter("ZCZH","ֵ")); // �ʲ��˺�  
        params.add(createLbParameter("BZ","ֵ")); // ����  1-�����;2-��Ԫ;3-�۱�
        params.add(createLbParameter("YHDM","ֵ")); // ���д���  
        params.add(createLbParameter("YHZH","ֵ")); // �����˺�  
        params.add(createLbParameter("YHMM","ֵ")); // ��������  ��������
        params.add(createLbParameter("ZY","ֵ")); // ����ժҪ  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsZDCGYH_ZDCGYH", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
            System.out.println("O_LSH = " + mapOutputVar.get("O_LSH")); // ��ˮ��  
            System.out.println("O_YHDM = " + mapOutputVar.get("O_YHDM")); // ���д���  
            System.out.println("O_YHZH = " + mapOutputVar.get("O_YHZH")); // �����˺�  
            System.out.println("O_ZY = " + mapOutputVar.get("O_ZY")); // ժҪ  
        }
    }

    // �����ط�
    public void cifwsKHHF_KHHF() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHH","ֵ")); // �ͻ���  
        params.add(createLbParameter("HFBZ","ֵ")); // �طñ�־  1|�طü���;-1|�ط�ʧ��;0|�طü�¼ժҪ(����������)
        params.add(createLbParameter("ZY","ֵ")); // ժҪ  
        params.add(createLbParameter("HFGYDM","ֵ")); // �طù�Ա����  
        params.add(createLbParameter("HFGYXM","ֵ")); // �طù�Ա����  
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsKHHF_KHHF", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
        }
    }

    // cif��ѯ����ת��ί�н��
    public void cifCXYHZZWTJG() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("YHDM","ֵ")); // ���д���  
        params.add(createLbParameter("LSH","ֵ")); // ��ˮ��  
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
            System.out.print("JYRQ = " + mapData.get("JYRQ") + "\t "); // ��������  yyyymmdd��ʽ
            System.out.print("LSH = " + mapData.get("LSH") + "\t "); // ��ˮ��  
            System.out.print("YHDM = " + mapData.get("YHDM") + "\t "); // ���д���  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // ����  1-�����;2-��Ԫ;3-�۱�
            System.out.print("KHH = " + mapData.get("KHH") + "\t "); // �ͻ���  
            System.out.print("ZCZH = " + mapData.get("ZCZH") + "\t "); // �ʲ��˺�  
            System.out.print("RQ = " + mapData.get("RQ") + "\t "); // ��ǰ����  yyyymmdd��ʽ
            System.out.print("ZT = " + mapData.get("ZT") + "\t "); // ����״̬  0-δ��;1-�ѱ�;2-�ɹ�;3-����;4-����;5-����;7-������;8-�ѳ���;A-����;P-����
            System.out.print("YHZH = " + mapData.get("YHZH") + "\t "); // �����˺�  
            System.out.print("FSJE = " + mapData.get("FSJE") + "\t "); // �������  
            System.out.print("SXF = " + mapData.get("SXF") + "\t "); // ������  
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // ֤�����  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // ֤�����  
            System.out.print("CZGY = " + mapData.get("CZGY") + "\t "); // ������Ա  
            System.out.print("ZY = " + mapData.get("ZY") + "\t "); // ժҪ  
            System.out.print("BZXX = " + mapData.get("BZXX") + "\t "); // ��ע��Ϣ  
            System.out.print("CGJGSM = " + mapData.get("CGJGSM") + "\t "); // ��ܽ��˵��  

           System.out.println("");
        }
        //printQueryResult(qrs); // ����̧��ӡȫ�������
    }

    // �û�����_��֤
    public void cifwsUser_Auth() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("UID","ֵ")); // �û���  ��Ա����
        params.add(createLbParameter("PWD","ֵ")); // ����  ��Ա��������
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  1-�ٹ񿪻�;2-��֤����;3-���Ͽ���;11-ȫԱ����
        params.add(createLbParameter("DLLX","ֵ")); // ��¼����  KHFS=2(��֤����)ʱ����,����������ʽ�²���1-��֤��,2-����Э����
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsUser_Auth", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
        }
    }

    // ���ֳ�����(������̨)_�޸��ύ
    public void cifwsFXCKH_HS_XGTJ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("BDID","ֵ")); // ��ID  ����ID��
        params.add(createLbParameter("ZJQSRQ","ֵ")); // ֤����ʼ����  yyyymmdd��ʽ
        params.add(createLbParameter("ZJJZRQ","ֵ")); // ֤����ֹ����  yyyymmdd��ʽ
        params.add(createLbParameter("ZJDZ","ֵ")); // ֤����ַ  
        params.add(createLbParameter("ZJDZYB","ֵ")); // ֤����ַ�ʱ�  
        params.add(createLbParameter("ZJFZJG","ֵ")); // ��֤����  
        params.add(createLbParameter("GDKH_SH","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SH","ֵ")); // �Ǽǻ�A�ɶ���  GDKH_SH=9ʱ���뻦A�ɶ��˻�
        params.add(createLbParameter("GDKH_SZ","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SZ","ֵ")); // �Ǽ���A�ɶ���  GDKH_SZ=9ʱ������A�ɶ��˻�
        params.add(createLbParameter("GDJYQX_SH","ֵ")); // ��A�ɶ�����Ȩ��  
        params.add(createLbParameter("GDJYQX_SZ","ֵ")); // ��A�ɶ�����Ȩ��  
        params.add(createLbParameter("YXSTR","ֵ")); // Ӱ��  ʾ����[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_HS_XGTJ", "", params, null);
        System.out.println("result message: " + result.getMessage());
        System.out.println("result code: " + result.getResult());
        if (result.getOutputVariables() != null) {
            Map<String, String> mapOutputVar = new HashMap<String, String>();
            // ���г���
            for (Iterator<LbParameter> it = result.getOutputVariables().iterator(); it.hasNext();) {
                LbParameter var = it.next();
                // System.out.println("����:  " + var.getName() + "=" + var.getValue());
                mapOutputVar.put(var.getName(), var.getValue());
            }
            // ����
            System.out.println("O_CODE = " + mapOutputVar.get("O_CODE")); // ���ش���  >0�ɹ�,<=0ʧ��
            System.out.println("O_MSG = " + mapOutputVar.get("O_MSG")); // ����˵��  
        }
    }

}
