import com.apex.crm.wsclient.*;

import javax.xml.namespace.QName;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;

public class ClientDemo {
    public static void main(String[] args) {
        ClientDemo client = new ClientDemo();
        System.out.println("-----------------------------cifͨ�ò�ѯ�ӿڰ���-----------------------");
        client.cifQuery();
        System.out.println("-----------------------------cif��ѯ���ֳ���������ӿڰ���-----------------------");
        client.cifCXFXCKHSQ();
        System.out.println("-----------------------------���������֤_���Ͳ�ѯ�ӿڰ���-----------------------");
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
    // cifͨ�ò�ѯ
    public void cifQuery() {
        LBEBusinessService client = getServiePort();
        QueryOption queryOption = new QueryOption();
        queryOption.setBatchNo(1);
        queryOption.setBatchSize(100);
        queryOption.setQueryCount(true);
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("DXID","102")); // ����ID  �ο�"��ṹ"ҳ�ж���Ķ���ID
        params.add(createLbParameter("CXTJ","ROWNUM < 3")); // ��ѯ����  �磺Field1= '1' and Field2 = '2002'
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
        
        printQueryResult(qrs); // ����̧��ӡȫ������� (���ݴ���""����ID""��ͬ�����صĽ����Ҳ��ͬ)
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
        params.add(createLbParameter("TMDAC","ֵ")); // ��Ŀ�𰸴�  ��Ŀ��𰸼���"|"�ָ�����Ŀ����";"�ŷָʾ��:25|A;26|B;27|A;28|C;29|B;30|C;31|A
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
		params.add(createLbParameter("GYID","1")); // ��ԱID  ��¼��Ա��ID
		params.add(createLbParameter("ZJLB","0")); // ֤�����
		params.add(createLbParameter("ZJBH","350521198406188556")); // ֤�����
		params.add(createLbParameter("KHXM","֣����")); // �ͻ�����
		params.add(createLbParameter("CXLX","0")); // ��ѯ����  ǿ����"1"

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

    // ���ֳ�����_��������
    public void cifwsFXCKH_KHSQ() {
        LBEBusinessService client = getServiePort();
        List<LbParameter> params = new ArrayList<LbParameter>();
        params.add(createLbParameter("KHFS","ֵ")); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���. ��ǰ��д"2"
        params.add(createLbParameter("JGBZ","ֵ")); // �ͻ�����  0|����;1|����
        params.add(createLbParameter("YYB","ֵ")); // ����Ӫҵ��  
        params.add(createLbParameter("KHLY","ֵ")); // �ͻ���Դ  TXTDM.FLDM=GT_YYKHLY(1-��վԤԼ����;2-������Ԥ����;3-����ԤԼ����;8-�¿ͻ�;9-�Ͽͻ�),��ǰ��д"8"
        params.add(createLbParameter("KHXM","ֵ")); // �ͻ�����  
        params.add(createLbParameter("KHQC","ֵ")); // �ͻ�ȫ��  
        params.add(createLbParameter("JZR","ֵ")); // ��֤��  ��֤����ʱ�������������ʽʱ����
        params.add(createLbParameter("KHXZR","ֵ")); // ����Э����  ��֤����ʱ�������������ʽʱ����
        params.add(createLbParameter("ZJLB","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJBH","ֵ")); // ֤�����  
        params.add(createLbParameter("ZJYXQ","ֵ")); // ֤����Ч��  
        params.add(createLbParameter("ZJDZ","ֵ")); // ֤����ַ  
        params.add(createLbParameter("ZJDZYB","ֵ")); // ֤����ַ�ʱ�  
        params.add(createLbParameter("ZJFZJG","ֵ")); // ��֤����  
        params.add(createLbParameter("ZJZP","ֵ")); // ֤����Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
        params.add(createLbParameter("ZJYZLY","ֵ")); // ֤����֤��Դ  TXTDM.FLDM=KH_ZJYZLY(1-����֤������;2-����������)
        params.add(createLbParameter("EDZ","ֵ")); // ����֤У��  
        params.add(createLbParameter("DZ","ֵ")); // ͨѶ��ַ  
        params.add(createLbParameter("YZBM","ֵ")); // �ʱ�  
        params.add(createLbParameter("SJ","ֵ")); // �ֻ�  
        params.add(createLbParameter("DH","ֵ")); // �绰  
        params.add(createLbParameter("CZ","ֵ")); // ����  
        params.add(createLbParameter("Email","ֵ")); // EMAIL  
        params.add(createLbParameter("GJ","ֵ")); // ����  
        params.add(createLbParameter("TBSM","ֵ")); // �ر�˵��  
        params.add(createLbParameter("FXQHYLB","ֵ")); // ��ϴǮ��ҵ���  
        params.add(createLbParameter("XQFXDJ","ֵ")); // ϴǮ���յȼ�  
        params.add(createLbParameter("BZ","ֵ")); // ����  Ĭ����"1"
        params.add(createLbParameter("KHQZ","ֵ")); // �ͻ�Ⱥ��  
        params.add(createLbParameter("KHKH","ֵ")); // �ͻ�����  Ĭ����""
        params.add(createLbParameter("GTKHH","ֵ")); // �ͻ���  Ĭ����""
        params.add(createLbParameter("WTFS","ֵ")); // ί�з�ʽ  
        params.add(createLbParameter("FWXM","ֵ")); // ������Ŀ  
        params.add(createLbParameter("FXJB","ֵ")); // ���ռ���  Ĭ����""
        params.add(createLbParameter("FXCSNL","ֵ")); // ������ճ�������  
        params.add(createLbParameter("GPFXCSNL","ֵ")); // ��Ʊ���ճ�������  Ĭ����""
        params.add(createLbParameter("SFTB","ֵ")); // �Ƿ�ͬ��  Ĭ����""
        params.add(createLbParameter("JYMM","ֵ")); // ��������  Ĭ����""
        params.add(createLbParameter("ZJMM","ֵ")); // �ʽ�����  Ĭ����""
        params.add(createLbParameter("FWMM","ֵ")); // ��������  Ĭ����""
        params.add(createLbParameter("SQJJZH","ֵ")); // ��������˺�  
        params.add(createLbParameter("GDKH_SH","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SH","ֵ")); // �Ǽǻ�A�ɶ���  ���뻦A�ɶ��˻�
        params.add(createLbParameter("GDKH_HB","ֵ")); // ��B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_HB","ֵ")); // �Ǽǻ�B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_SZ","ֵ")); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
        params.add(createLbParameter("GDDJ_SZ","ֵ")); // �Ǽ���A�ɶ���  ������A�ɶ��˻�
        params.add(createLbParameter("GDKH_SB","ֵ")); // ��B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_SB","ֵ")); // �Ǽ���B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_TA","ֵ")); // ����A�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_TA","ֵ")); // �Ǽ�����A�ɶ���  Ĭ����""
        params.add(createLbParameter("GDKH_TU","ֵ")); // ����B�ɶ�����  Ĭ����"0"
        params.add(createLbParameter("GDDJ_TU","ֵ")); // �Ǽ�����B�ɶ���  Ĭ����""
        params.add(createLbParameter("GDJYQX_SH","ֵ")); // ��A�ɶ�����Ȩ��  
        params.add(createLbParameter("GDJYQX_HB","ֵ")); // ��B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_SZ","ֵ")); // ��A�ɶ�����Ȩ��  
        params.add(createLbParameter("GDJYQX_SB","ֵ")); // ��B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_TA","ֵ")); // ����A�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("GDJYQX_TU","ֵ")); // ����B�ɶ�����Ȩ��  Ĭ����""
        params.add(createLbParameter("SHZDJY","ֵ")); // ��Aָ��  Ĭ����""
        params.add(createLbParameter("HBZDJY","ֵ")); // ��Bָ��  Ĭ����""
        params.add(createLbParameter("SFWLFW","ֵ")); // �Ƿ���Ҫ�������  Ĭ����""
        params.add(createLbParameter("WLFWMM","ֵ")); // �����������  Ĭ����""
        params.add(createLbParameter("CGYH","ֵ")); // �������  
        params.add(createLbParameter("CGYHZH","ֵ")); // ��������˻�  Ĭ����""
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
        params.add(createLbParameter("DJR","ֵ")); // ������  
        params.add(createLbParameter("CZZD","ֵ")); // ����վ��  
        params.add(createLbParameter("IBY1","ֵ")); // IBY1  Ĭ����""
        params.add(createLbParameter("IBY2","ֵ")); // IBY2  Ĭ����""
        params.add(createLbParameter("IBY3","ֵ")); // IBY3  Ĭ����""
        params.add(createLbParameter("CBY1","ֵ")); // CBY1  Ĭ����""
        params.add(createLbParameter("CBY2","ֵ")); // CBY2  Ĭ����""
        params.add(createLbParameter("CBY3","ֵ")); // CBY3  Ĭ����""
        params.add(createLbParameter("LXRXXSTR","ֵ")); // ��ϵ����Ϣ��  ʾ����[{"LXRXM":"Ҧ����", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"�쵼"}, {"LXRXM":"��С��", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"����"}]
        params.add(createLbParameter("DLRSTR","ֵ")); // �����˴�  ʾ����[{"DLRXM":"��һ��", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"����",  "ZJFZJG":"�����й�¥������", "CSRQ":"19840101", "ZJDZ":"�����к���·165��", "DZ":"����������·150��",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
        params.add(createLbParameter("YXSTR","ֵ")); // Ӱ��  ʾ����[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
        params.add(createLbParameter("JGKGGDSTR","ֵ")); // �����عɹɶ���  Ĭ����""
        params.add(createLbParameter("DJJJZHSTR","ֵ")); // �Ǽǻ����˺�  Ĭ����""
        params.add(createLbParameter("FJSXSTR","ֵ")); // �������Դ�  Ĭ����""
        params.add(createLbParameter("WJID","ֵ")); // �ʾ�ID  �������ʾ��ID��
        params.add(createLbParameter("TMDAC","ֵ")); // ��Ŀ�𰸴�  ��Ŀ��𰸼���"|"�ָ�����Ŀ����";"�ŷָʾ��:25|A;26|B;27|A;28|C;29|B;30|C;31|A
        BizProcessResult result = client.execBizProcess(getSessionId(), "cifwsFXCKH_KHSQ", "", params, null);
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
        params.add(createLbParameter("FLAG","1")); // ��ѯ��־  1-������ID��ѯ,2-�������˲�ѯ(��֤����),3-���ֻ��Ų�ѯ(���Ͽ���)
        params.add(createLbParameter("SQID","1")); // ����ID  FLAG=1ʱ����
        params.add(createLbParameter("GYID","")); // ��ԱID  FLAG=2ʱ����
        params.add(createLbParameter("SJ","")); // �ֻ�  FLAG=3ʱ����
        params.add(createLbParameter("KSRQ","")); // ��ʼ����  yyyymmdd��ʽ,�Ǳ���
        params.add(createLbParameter("JSRQ","")); // ��������  yyyymmdd��ʽ,�Ǳ���
        params.add(createLbParameter("KHFS","")); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���
        params.add(createLbParameter("CXNR","")); // ��ѯ����  ֧�ֶԿͻ����������֤�Ž���ģ����ѯ
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
            System.out.print("KHFS = " + mapData.get("KHFS") + "\t "); // ������ʽ  1|�ٹ񿪻�;2|��֤����;3|���Ͽ���. ��ǰ��д"2"
            System.out.print("JGBZ = " + mapData.get("JGBZ") + "\t "); // �ͻ�����  0|����;1|����
            System.out.print("YYB = " + mapData.get("YYB") + "\t "); // ����Ӫҵ��  
            System.out.print("KHLY = " + mapData.get("KHLY") + "\t "); // �ͻ���Դ  TXTDM.FLDM=GT_YYKHLY(1-��վԤԼ����;2-������Ԥ����;3-����ԤԼ����;8-�¿ͻ�;9-�Ͽͻ�),��ǰ��д"8"
            System.out.print("KHXM = " + mapData.get("KHXM") + "\t "); // �ͻ�����  
            System.out.print("KHQC = " + mapData.get("KHQC") + "\t "); // �ͻ�ȫ��  
            System.out.print("JZR = " + mapData.get("JZR") + "\t "); // ��֤��  ��֤����ʱ�������������ʽʱ����
            System.out.print("KHXZR = " + mapData.get("KHXZR") + "\t "); // ����Э����  ��֤����ʱ�������������ʽʱ����
            System.out.print("ZJLB = " + mapData.get("ZJLB") + "\t "); // ֤�����  
            System.out.print("ZJBH = " + mapData.get("ZJBH") + "\t "); // ֤�����  
            System.out.print("ZJYXQ = " + mapData.get("ZJYXQ") + "\t "); // ֤����Ч��  
            System.out.print("ZJDZ = " + mapData.get("ZJDZ") + "\t "); // ֤����ַ  
            System.out.print("ZJDZYB = " + mapData.get("ZJDZYB") + "\t "); // ֤����ַ�ʱ�  
            System.out.print("ZJFZJG = " + mapData.get("ZJFZJG") + "\t "); // ��֤����  
            System.out.print("ZJZP = " + mapData.get("ZJZP") + "\t "); // ֤����Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
            System.out.print("ZJYZLY = " + mapData.get("ZJYZLY") + "\t "); // ֤����֤��Դ  TXTDM.FLDM=KH_ZJYZLY(1-����֤������;2-����������)
            System.out.print("EDZ = " + mapData.get("EDZ") + "\t "); // ����֤У��  
            System.out.print("DZ = " + mapData.get("DZ") + "\t "); // ͨѶ��ַ  
            System.out.print("YZBM = " + mapData.get("YZBM") + "\t "); // �ʱ�  
            System.out.print("SJ = " + mapData.get("SJ") + "\t "); // �ֻ�  
            System.out.print("DH = " + mapData.get("DH") + "\t "); // �绰  
            System.out.print("CZ = " + mapData.get("CZ") + "\t "); // ����  
            System.out.print("Email = " + mapData.get("Email") + "\t "); // EMAIL  
            System.out.print("GJ = " + mapData.get("GJ") + "\t "); // ����  
            System.out.print("TBSM = " + mapData.get("TBSM") + "\t "); // �ر�˵��  
            System.out.print("FXQHYLB = " + mapData.get("FXQHYLB") + "\t "); // ��ϴǮ��ҵ���  
            System.out.print("XQFXDJ = " + mapData.get("XQFXDJ") + "\t "); // ϴǮ���յȼ�  
            System.out.print("BZ = " + mapData.get("BZ") + "\t "); // ����  Ĭ����"1"
            System.out.print("KHQZ = " + mapData.get("KHQZ") + "\t "); // �ͻ�Ⱥ��  
            System.out.print("KHKH = " + mapData.get("KHKH") + "\t "); // �ͻ�����  Ĭ����""
            System.out.print("GTKHH = " + mapData.get("GTKHH") + "\t "); // �ͻ���  Ĭ����""
            System.out.print("WTFS = " + mapData.get("WTFS") + "\t "); // ί�з�ʽ  
            System.out.print("FWXM = " + mapData.get("FWXM") + "\t "); // ������Ŀ  
            System.out.print("FXJB = " + mapData.get("FXJB") + "\t "); // ���ռ���  Ĭ����""
            System.out.print("FXCSNL = " + mapData.get("FXCSNL") + "\t "); // ������ճ�������  
            System.out.print("GPFXCSNL = " + mapData.get("GPFXCSNL") + "\t "); // ��Ʊ���ճ�������  Ĭ����""
            System.out.print("SFTB = " + mapData.get("SFTB") + "\t "); // �Ƿ�ͬ��  Ĭ����""
            System.out.print("JYMM = " + mapData.get("JYMM") + "\t "); // ��������  Ĭ����""
            System.out.print("ZJMM = " + mapData.get("ZJMM") + "\t "); // �ʽ�����  Ĭ����""
            System.out.print("FWMM = " + mapData.get("FWMM") + "\t "); // ��������  Ĭ����""
            System.out.print("SQJJZH = " + mapData.get("SQJJZH") + "\t "); // ��������˺�  
            System.out.print("GDKH_SH = " + mapData.get("GDKH_SH") + "\t "); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
            System.out.print("GDDJ_SH = " + mapData.get("GDDJ_SH") + "\t "); // �Ǽǻ�A�ɶ���  ���뻦A�ɶ��˻�
            System.out.print("GDKH_HB = " + mapData.get("GDKH_HB") + "\t "); // ��B�ɶ�����  Ĭ����"0"
            System.out.print("GDDJ_HB = " + mapData.get("GDDJ_HB") + "\t "); // �Ǽǻ�B�ɶ���  Ĭ����""
            System.out.print("GDKH_SZ = " + mapData.get("GDKH_SZ") + "\t "); // ��A�ɶ�����  0|������;1|�¿�A���˻�;2|�¿����ڻ����˻�;9|�ѿ�
            System.out.print("GDDJ_SZ = " + mapData.get("GDDJ_SZ") + "\t "); // �Ǽ���A�ɶ���  ������A�ɶ��˻�
            System.out.print("GDKH_SB = " + mapData.get("GDKH_SB") + "\t "); // ��B�ɶ�����  Ĭ����"0"
            System.out.print("GDDJ_SB = " + mapData.get("GDDJ_SB") + "\t "); // �Ǽ���B�ɶ���  Ĭ����""
            System.out.print("GDKH_TA = " + mapData.get("GDKH_TA") + "\t "); // ����A�ɶ�����  Ĭ����"0"
            System.out.print("GDDJ_TA = " + mapData.get("GDDJ_TA") + "\t "); // �Ǽ�����A�ɶ���  Ĭ����""
            System.out.print("GDKH_TU = " + mapData.get("GDKH_TU") + "\t "); // ����B�ɶ�����  Ĭ����"0"
            System.out.print("GDDJ_TU = " + mapData.get("GDDJ_TU") + "\t "); // �Ǽ�����B�ɶ���  Ĭ����""
            System.out.print("GDJYQX_SH = " + mapData.get("GDJYQX_SH") + "\t "); // ��A�ɶ�����Ȩ��  
            System.out.print("GDJYQX_HB = " + mapData.get("GDJYQX_HB") + "\t "); // ��B�ɶ�����Ȩ��  Ĭ����""
            System.out.print("GDJYQX_SZ = " + mapData.get("GDJYQX_SZ") + "\t "); // ��A�ɶ�����Ȩ��  
            System.out.print("GDJYQX_SB = " + mapData.get("GDJYQX_SB") + "\t "); // ��B�ɶ�����Ȩ��  Ĭ����""
            System.out.print("GDJYQX_TA = " + mapData.get("GDJYQX_TA") + "\t "); // ����A�ɶ�����Ȩ��  Ĭ����""
            System.out.print("GDJYQX_TU = " + mapData.get("GDJYQX_TU") + "\t "); // ����B�ɶ�����Ȩ��  Ĭ����""
            System.out.print("SHZDJY = " + mapData.get("SHZDJY") + "\t "); // ��Aָ��  Ĭ����""
            System.out.print("HBZDJY = " + mapData.get("HBZDJY") + "\t "); // ��Bָ��  Ĭ����""
            System.out.print("SFWLFW = " + mapData.get("SFWLFW") + "\t "); // �Ƿ���Ҫ�������  Ĭ����""
            System.out.print("WLFWMM = " + mapData.get("WLFWMM") + "\t "); // �����������  Ĭ����""
            System.out.print("CGYH = " + mapData.get("CGYH") + "\t "); // �������  
            System.out.print("CGYHZH = " + mapData.get("CGYHZH") + "\t "); // ��������˻�  Ĭ����""
            System.out.print("CGYHMM = " + mapData.get("CGYHMM") + "\t "); // �����������  Ĭ����""
            System.out.print("YHDM_USD = " + mapData.get("YHDM_USD") + "\t "); // ��Ԫת������  Ĭ����""
            System.out.print("DJFS_USD = " + mapData.get("DJFS_USD") + "\t "); // ��Ԫ�ǼǷ�ʽ  Ĭ����""
            System.out.print("YHZH_USD = " + mapData.get("YHZH_USD") + "\t "); // ��Ԫ�����˺�  Ĭ����""
            System.out.print("YHMM_USD = " + mapData.get("YHMM_USD") + "\t "); // ��Ԫ��������  Ĭ����""
            System.out.print("YHDM_HKD = " + mapData.get("YHDM_HKD") + "\t "); // �۱�ת������  Ĭ����""
            System.out.print("DJFS_HKD = " + mapData.get("DJFS_HKD") + "\t "); // �۱ҵǼǷ�ʽ  Ĭ����""
            System.out.print("YHZH_HKD = " + mapData.get("YHZH_HKD") + "\t "); // �۱������˺�  Ĭ����""
            System.out.print("YHMM_HKD = " + mapData.get("YHMM_HKD") + "\t "); // �۱���������  Ĭ����""
            System.out.print("KHZP = " + mapData.get("KHZP") + "\t "); // �ͻ���Ƭ  Ӱ���ϴ��ɹ��󷵻ص��ַ���
            System.out.print("KHSP = " + mapData.get("KHSP") + "\t "); // �ͻ���Ƶ  ��Ƶ�ϴ��ɹ��󷵻ص��ַ���
            System.out.print("DJR = " + mapData.get("DJR") + "\t "); // ������  
            System.out.print("CZZD = " + mapData.get("CZZD") + "\t "); // ����վ��  
            System.out.print("IBY1 = " + mapData.get("IBY1") + "\t "); // IBY1  Ĭ����""
            System.out.print("IBY2 = " + mapData.get("IBY2") + "\t "); // IBY2  Ĭ����""
            System.out.print("IBY3 = " + mapData.get("IBY3") + "\t "); // IBY3  Ĭ����""
            System.out.print("CBY1 = " + mapData.get("CBY1") + "\t "); // CBY1  Ĭ����""
            System.out.print("CBY2 = " + mapData.get("CBY2") + "\t "); // CBY2  Ĭ����""
            System.out.print("CBY3 = " + mapData.get("CBY3") + "\t "); // CBY3  Ĭ����""
            System.out.print("LXRXXSTR = " + mapData.get("LXRXXSTR") + "\t "); // ��ϵ����Ϣ��  ʾ����[{"LXRXM":"Ҧ����", "DH":"87866888", "SJ":"13701001234", "EMAIL":"yaomingming@apexsoft.com.cn", "GXSM":"�쵼"}, {"LXRXM":"��С��", "DH":"87866111", "SJ":"13701001111", "EMAIL":"lixiaosi@apexsoft.com.cn", "GXSM":"����"}]
            System.out.print("DLRSTR = " + mapData.get("DLRSTR") + "\t "); // �����˴�  ʾ����[{"DLRXM":"��һ��", "DLYXQ":"20201231", "DLQXFW":"1;7", "ZJLB":"0", "ZJBH":"350103840101123", "ZJYXQ":"����",  "ZJFZJG":"�����й�¥������", "CSRQ":"19840101", "ZJDZ":"�����к���·165��", "DZ":"����������·150��",  "YZBM":"350103", "SJ":"13701007766", "DH":"87861000", "EMAIL":"zhangyisan@apexsoft.com.cn", "CZ":"87883951", "XB":"1", "GJ":"156", "MZDM":"1", "ZYDM":"99",  "FILEPATH":"AA4yMDExMDcyODE4NTgzNDAuMDk2MDAyODc0Mw=="}]
            System.out.print("YXSTR = " + mapData.get("YXSTR") + "\t "); // Ӱ��  ʾ����[{"YXLX":"9", "FILEPATH":"Aw4wODIzMTUwOTIwMTI1MjEwMDUuMzkyMTQyODY2MQ=="},{"YXLX":"7", "FILEPATH":"Aw4wOTEzMDg1NjIwMTIzMTEwMDUuMzc0OTQzNDgwNA=="}]
            System.out.print("JGKGGDSTR = " + mapData.get("JGKGGDSTR") + "\t "); // �����عɹɶ���  Ĭ����""
            System.out.print("DJJJZHSTR = " + mapData.get("DJJJZHSTR") + "\t "); // �Ǽǻ����˺�  Ĭ����""
            System.out.print("FJSXSTR = " + mapData.get("FJSXSTR") + "\t "); // �������Դ�  Ĭ����""
            System.out.print("CPSCORE = " + mapData.get("CPSCORE") + "\t "); // �����÷�  
            System.out.print("CPFXCSNL = " + mapData.get("CPFXCSNL") + "\t "); // �������ճ�������  

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
        }
    }

}
