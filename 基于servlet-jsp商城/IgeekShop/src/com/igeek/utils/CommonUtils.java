package com.igeek.utils;

/**
 * @file_name : CommonUtils.java
 * @author : 
 * @date   : 2018��3��15��
 * Description: 
 */




import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * file_name : CommonUtils.java
 * author : 
 * date : 2018��3��15��
 * description :
 */
public class CommonUtils {

  
    /**����id��   id=ʱ�������+nλ��ˮ��*/
    private static long serial_number = 0;
    /**����id��   id=ʱ�������+nλ��ˮ��*/
    private static long SERIAL_NUMBER_FOR_TRANNO = 0;

    /**���ڽ��׺�  �е���ˮ�� nλ��ˮ��*/
    private static long TRANSNO_SERIAL_NUMBER = 0;

    /**id������ˮ�ų���   id=ʱ�������+nλ��ˮ�� */
    private static final int SIZE_OF_ECNO_SERIAL_NUMBER = 5;
    /**ESB�ӿ�ȫ�ֽ�����ˮ�� tranno=yyyyMMddHHmmssSSS+6λ��ˮ��*/
    private static final int SIZE_OF_TRANNO_SERIAL_NUMBER = 6;

    // ��ǰ����PID
    public static final int PID = getProcessID();


   
 

    /**
     * @Description: �ж��Ƿ�Ϊ��, ����� ""," ","null",null �򷵻�false
     * @param: orignalValue ��Ҫ�жϵ�ֵ
     * @return: boolean
     */
    public static boolean isNotBlank(Object... obj) {
        return !isBlank(obj);
    }

    /**
     * @Description: �ж���������Ƿ�Ϊ��ֵ , ����� ""," ","null",null �򷵻�true, ����Collection.size<1Ҳ��
     * @param: orignalValue ��Ҫ�жϵ�ֵ
     * @return: boolean
     */
    public static boolean isBlank(Object... objArr) {
        if(objArr==null)
            return true;
        for (Object obj : objArr) {
            if(obj==null){
                return true;
            }else if("".equals(obj.toString().trim())){
                return true;
            }else if("null".equalsIgnoreCase(obj.toString().trim())){
                return true;
            }else if(obj instanceof Collection){
                Collection objCollection=(Collection)obj;
                if(objCollection==null||objCollection.size()<1){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ��ȡUUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }


    /**
     * ��������: getTran<br>
     *
     * @Description:����ESB��ȡȫ����ˮ�źͽ���ʱ��
     * @author 
     *
     * @return
     */
    public static synchronized String[] getTran() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");// �������ڸ�ʽ
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");// ������ˮ��ʱ���ʽ
        Date date = getDate();
        String[] id = new String[] { "", "" };
        id[0] = String.valueOf(df.format(date));
        id[1] = String.valueOf(df2.format(date));
        SERIAL_NUMBER_FOR_TRANNO++;
        if (SERIAL_NUMBER_FOR_TRANNO >= (SIZE_OF_TRANNO_SERIAL_NUMBER - 1) * 10)// ��������,��λ
            SERIAL_NUMBER_FOR_TRANNO = 0;

        String serialNumber = getEnoughLengthString(
                String.valueOf(SERIAL_NUMBER_FOR_TRANNO),
                SIZE_OF_TRANNO_SERIAL_NUMBER);
        id[1] = id[1] + serialNumber;
        return id;
    }

    /**
     * ��ȡ��ǰʱ��
     * @return date ��ǰʱ��
     */
    public static Date getDate(){
        return new Date();
    }



    /**
     * ��ȡָ�����ȵ��ַ���,��������,����λ��,ȡ�������㹻λ��
     *
     * @param string
     *            �ַ���
     * @param length
     *            ָ������
     * @return string �ַ���
     */
    public static String getEnoughLengthString(String string, int length) {
        if (string == null)
            return "";

        int strLength = string.trim().length();// �ַ�������

        // ��Ҫ����ĸ���
        int theNumberOfZeroCreated = length - strLength;

        if (theNumberOfZeroCreated < 0) {// ����,ȥ�����㹻λ��
            string = string.substring(strLength - length);
        } else {// ����
            for (int i = 0; i < theNumberOfZeroCreated; i++) {
                string = "0" + string;
            }
        }
        return string;
    }

    /**
     * ��������: getWebProjectPath<br>
     *
     * @Description:��ȡweb�µľ����ļ�·�� @authormxl �޸����ڣ�2013-2-21����03:39:52
     *
     * @param dir
     * @return
     */
    public static String getWebProjectPath(String dir) {
        String path = CommonUtils.class.getClassLoader().getResource("/").getPath() + dir;
        path = path.replaceAll("/target/classes", "/src/main/webapp").replaceAll("%20", " ");
        path = path.replaceAll("/WEB-INF/classes", "").replaceAll("%20", " ");
        return path;
    }

    /**
     *
     * @Title: getIpAddr
     * @Description: ����HttpServletRequest��ȡ�û�IP��ַ
     * @param request
     * @return IP
     * @author 
     * �޸�ʱ�䣺 2013-8-12 ����01:58:24
     * �޸����ݣ��½�
     */
    public static String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

    /**
     * ���˿�վ�ű��ؼ���
     *
     * @param str
     * ��Ҫ���˵��ַ���
     * @return ���˺���ַ���
     */
    public static String filterStr(String str){
        if(str==null)
            return "";
        str=str.replaceAll("<","");
        str=str.replaceAll(">","");
        str=str.replaceAll("��","");
        str=str.replaceAll("&","��");
        str=str.replaceAll("#","��");
        str=str.replaceAll("%","��");
        str=str.replaceAll("\"","");
        return str;
    }

    /**
     * ��������: getParameter<br>
     * ����:��ȡ����,���Ϊ��,����""
     * ����: Administrator
     * �޸����ڣ�2013-3-28����07:08:22
     * @param request
     * @param key
     * @return
     */
    public static String getParameter(HttpServletRequest request,String key){
        Object value = request.getParameter(key);
        return value==null?"":value.toString();


    }

    /**
     * �������֤�Ż�ȡ����
     * @param IDNo ���֤��
     * @return �������� yyyy-MM-dd
     * @throws Exception
     * @throws Exception
     */
    public static String getBirthdayByIDNo(String IDNo) throws Exception{
        String birthday = "";

        if(IDNo==null||"".equals(IDNo))
            return "";

        if(IDNo.length()==18){//18λ���֤����ȡ��������
            String birthday18 = IDNo.substring(6,14);
            birthday = birthday18.substring(0,4)+"-"+birthday18.substring(4,6)+"-"+birthday18.subSequence(6,8);
            System.out.println("18λ���֤�ŵĳ�������:"+birthday18);
        }else if(IDNo.length()==15){//15λ���֤����ȡ��������
            String birthday15 = IDNo.substring(6,12);
            birthday = "19"+birthday15.substring(0,2)+"-"+birthday15.substring(2,4)+"-"+birthday15.substring(4,6);
            System.out.println("15λ���֤�ŵĳ�������:"+birthday15);
        }else {
            throw new Exception("��������֤�Ų���15λҲ����18λ,���֤!");
        }

        return birthday;
    }

    /**
     *  ��ȡָ�����ȵ��ַ���,��������,����λ��,ȡ�������㹻λ��
     * @param serialNoLong �ַ���
     * @param length ָ������
     * @return string �ַ���
     */
    public static String getEnoughLengthString(long serialNoLong,int length){
        String str0 = (10*length +"").substring(1);
        NumberFormat nf = new DecimalFormat(str0);

        String result = nf.format(serialNoLong);

        return result;
    }


/*

    */
/**
     * ��ȡָ�����ȵ��ַ���,��������,����λ��,ȡ�������㹻λ��
     *
     * @param string
     *            �ַ���
     * @param length
     *            ָ������
     * @return string �ַ���
     *//*

    public static String getEnoughLengthString(String string, int length) {
        if (string == null)
            return "";

        int strLength = string.trim().length();// �ַ�������

        // ��Ҫ����ĸ���
        int theNumberOfZeroCreated = length - strLength;

        if (theNumberOfZeroCreated < 0) {// ����,ȥ�����㹻λ��
            string = string.substring(strLength - length);
        } else {// ����
            for (int i = 0; i < theNumberOfZeroCreated; i++) {
                string = "0" + string;
            }
        }
        return string;
    }

*/

    /**
     * �ж��Ƿ�Ϊ��, ����� ""," ","null",null �򷵻�true
     * @param orignalValue ��Ҫ�жϵ�ֵ
     * @return boolean
     */
    public static boolean isEmpty(Object orignalValue) {
        return isBlank(orignalValue);
    }


    /**
     * �ж��Ƿ�Ϊ��, ����� ""," ","null",null �򷵻�false
     * @param obj ��Ҫ�жϵ�ֵ
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj) {
        return isNotBlank(obj);
    }



    /**
     * ת��Ϊ string ����,���Ϊ��("",null,"null")���� ""
     * @param obj
     * @return �����ַ���
     */
    public static String filterToStr(Object obj) {
        return isBlank(obj)?"":obj.toString().trim();
    }


   

    /**
     * ת���ɱ�����λ��  ���
     * @param num ���
     * @return ������λ�Ľ��
     */
    public static String formatToMoney(Double num){
        String result = "";
        if(isBlank(num))
            return result;

        DecimalFormat df=new DecimalFormat("##0.00");
        result = df.format(num);

        return result;
    }


    /**
     * ������֤��
     */
    public static String getCheckCode(){
        SecureRandom random = new SecureRandom();
        String sRand = "";
        for (int i = 0; i < 4; i++){
            String rand = "";
            int x = random.nextInt(10);
            rand = rand+x;
            sRand += rand;
        }
        return sRand;
    }


    /**
     *
     * @param date ��Ҫ���������
     * @param field the calendar field. eg. Calendar.YEAR(��),Calendar.MONTH(��),Calendar.DATE(��)
     * @param amount the amount of date or time to be added to the field.��Ҫ���(+10)���߼���(-10)������
     * @return date
     */
    public static Date computeDate(Date date,int field, int amount){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field,amount);

        return calendar.getTime();
    }

    /**
     * �����ִ�һλС����ΪString�͵���λС��
     * @param dnumber һλС��
     * @return String ��λС��
     */
    public static String decimalFormat(double dnumber){
        //���û������������ʾ��ת����ҳ����ҪС������λ��
        DecimalFormat df = new DecimalFormat( "########.00 ");
        String dff = df.format(dnumber);
        return dff;
    }

    /**
     *
     * ����������Ψһid��  id=ʱ�������+5λ��ˮ��
     * @author lihengjun
     * �޸����ڣ�2012-8-15����03:29:04
     * @return Ψһid��
     */
    public static synchronized String getUniqueId(){
        String id =  String.valueOf(System.currentTimeMillis()) ;
        serial_number++;
        if(serial_number>=(SIZE_OF_ECNO_SERIAL_NUMBER-1)*10)//��������,��λ
            serial_number=0;

        String serialNumber = getEnoughLengthString(serial_number,SIZE_OF_ECNO_SERIAL_NUMBER);
        id = id+serialNumber;
        return id;
    }



   

    /**
     *
     * ������ͨ���������ڻ�ȡ����
     * @author 
     * �޸����ڣ�2012-8-9����01:38:53
     * @param birthday ��������
     * @return ��ǰ����
     */
    public static int getAge(Date birthday){
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * ��������: getPeriodByChina<br>
     * ���������ݱ����ڼ����ת��Ϊ�����Ա�����
     * ����: mxl
     * �޸����ڣ�2013-2-21����03:00:11
     * @param period
     * @return
     */
    public static String getPeriodByChina(String period){
        if(period!=null){
            String lastStr = period.substring(period.length()-1,period.length());
            if("D".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"��";
            }else if("M".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"��";
            }else if("Y".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"��";
            }else if("O".equals(lastStr)){
                period = "��"+period.substring(0,period.length()-3)+"����";
            }
        }
        return period;
    }

  

    /**
     * �������ڣ������ٽ�ֵ��
     * @param targetDate
     * @param format
     * @param dateNum
     * @param unit
     * @return
     */
    public static String dateAdd(Date targetDate,String format,int dateNum,String unit){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        cal.setTime(targetDate);
        if("Y".equals(unit)){
            cal.add(Calendar.YEAR, dateNum);
        }else if("M".equals(unit)){
            cal.add(Calendar.MONTH, dateNum);
        }else if("D".equals(unit)){
            cal.add(Calendar.DATE, dateNum);
        }
        return df.format(cal.getTime());
    }

    /**
     *
     * @Title: dateAdd
     * @Description: �������ָ�����ʹ�С
     * @param compare
     * @param sdate
     * @param addNumber
     * @return
     * @author 
     * �޸�ʱ�䣺 2014-1-17 ����11:16:53
     * �޸����ݣ��������ָ�����ʹ�С
     */
    public static String dateAdd(String compare, String sdate,int addNumber)
    {
        Date date  = null;
        SimpleDateFormat df = null;

        if(sdate.indexOf("-")>0)
            df = new SimpleDateFormat("yyyy-MM-dd");
        else
            df = new SimpleDateFormat("yyyy��MM��dd��");

        try{
            date = df.parse(sdate);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        Calendar cal = null;
        cal=Calendar.getInstance();
        cal.setTime(date);

        if(compare.equalsIgnoreCase("d"))
            cal.add(Calendar.DATE,addNumber);
        if(compare.equalsIgnoreCase("m"))
            cal.add(Calendar.MONTH,addNumber);
        if(compare.equalsIgnoreCase("y"))
            cal.add(Calendar.YEAR,addNumber);

        return df.format(cal.getTime());
    }

    /**
     *
     * @Title: compareDate
     * @Description: ���ڱȽ�
     * @param firstDate
     * @param strSecondDate
     * @return
     * @throws RuntimeException
     * @author 
     * �޸�ʱ�䣺 2014-1-17 ����11:18:05
     * �޸����ݣ����ڱȽ�
     */
    public static int compareDate(Date firstDate,String strSecondDate)throws RuntimeException{
        Date secondDate=convertStringToDate(strSecondDate);
        return compareDate(firstDate,secondDate);
    }

    /**
     *
     * @Title: compareDate
     * @Description: ���������ʾ��ʱ����ڴ� Calendar ��ʾ��ʱ�䣬�򷵻� 0 ֵ������� Calendar ��ʱ���ڲ�����ʾ��ʱ��֮ǰ���򷵻�С�� 0 ��ֵ������� Calendar ��ʱ���ڲ�����ʾ��ʱ��֮���򷵻ش��� 0 ��ֵ��
     * @param firstDate
     * @param secondDate
     * @return
     * @throws RuntimeException
     * @author 
     * �޸�ʱ�䣺 2014-1-20 ����09:32:39
     * �޸����ݣ����ڱȽ�
     */
    public static int compareDate(Date firstDate,Date secondDate) throws RuntimeException{
        Calendar calFirst = Calendar.getInstance();
        calFirst.setTime(firstDate);
        Calendar calSecond = Calendar.getInstance();
        calSecond.setTime(secondDate);

        return calFirst.compareTo(calSecond);
    }


    /**
     *
     * @Title: convertStringToDate
     * @Description: ���ַ���ת��Ϊ��������
     * @param orignalValue
     * @return
     * @throws RuntimeException
     * @author 
     * �޸�ʱ�䣺 2014-1-20 ����09:28:36
     * �޸����ݣ����
     */
    public static  Date convertStringToDate(String orignalValue) throws RuntimeException{
        String tempValue=orignalValue;
        try{
            tempValue=tempValue.replaceFirst("��","-");
            tempValue=tempValue.replaceFirst("��","-");
            tempValue=tempValue.replaceFirst("��","");
            tempValue=tempValue.replaceFirst("/","-");
            tempValue=tempValue.replaceFirst("/","-");

            if(tempValue.trim().length()<=10){
                tempValue=tempValue.trim() +" 0:00:00";
            }
            SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormatter.parse(tempValue);
        }catch(Exception ex){
            throw new RuntimeException(orignalValue +"��������������");
        }
    }

    /**
     * �������ڣ��������ٽ�ֵ��
     * @param targetDate
     * @param format
     * @param dateNum
     * @param unit
     * @return
     */
    public static String dateAddNoCriticality(Date targetDate,String format,int dateNum,String unit){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        cal.setTime(targetDate);
        if("Y".equals(unit)){
            cal.add(Calendar.YEAR, dateNum);
        }else if("M".equals(unit)){
            cal.add(Calendar.MONTH, dateNum);
        }else if("D".equals(unit)){
            cal.add(Calendar.DATE, dateNum);
        }
        cal.add(Calendar.DATE, 1);
        return df.format(cal.getTime());
    }


    /**
     * ��������: createHtml<br>
     * ����������HTTP POST���ױ�
     * ����: 
     * �޸����ڣ�2013-3-6����04:22:45
     * @param action ���ύ��ַ
     * @param hiddens ��MAP��ʽ�洢�ı���ֵ
     * @return ����õ�HTTP POST���ױ�
     */
    public static String createHtml(String action, Map<String, String> hiddens) {
        StringBuffer sf = new StringBuffer();
        sf.append("<form id = \"sform\" action=\"" + action + "\" method=\"post\">\n");
        if (null != hiddens && 0 != hiddens.size()) {
            Set<Map.Entry<String, String>> set = hiddens.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> ey = it.next();
                String key = ey.getKey();
                String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\""
                        + value + "\"/>\n");
            }
        }
        //sf.append("<input type=��submit�� value=��֧���� />");
        sf.append("</form>\n");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.getElementById(��sform��).submit();");
        sf.append("</script>");
        return sf.toString();
    }

    /**
     * ��������: coverResultString2Map<br>
     * �������ָ�Form�ַ���
     * ����: 
     * �޸����ڣ�2013-3-20����05:27:42
     * @param res
     * @return
     */
    public static Map<String, String> coverResultString2Map(String res) {
        Map<String, String> map = null;
        if (null != res && !"".equals(res.trim())) {
            String[] resArray = res.split("&");
            if (0 != resArray.length) {
                map = new HashMap<String, String>(resArray.length);
                for (String arrayStr : resArray) {
                    if (null == arrayStr || "".equals(arrayStr.trim())) {
                        continue;
                    }
                    int index = arrayStr.indexOf("=");
                    if (-1 == index) {
                        continue;
                    }
                    map.put(arrayStr.substring(0, index), arrayStr.substring(index + 1));
                }
            }
        }
        return map;
    }

    /**
     * ��������: getForm<br>
     * ������������װ֧�����ַ���
     * ����: 
     * �޸����ڣ�2013-3-6����04:28:27
     * @param formStr �շ�����ӿڷ��ص�form�ַ���
     * @return ƴװ��HTML�������ַ���
     */
    public static String getForm(String formStr){
        String form = "";
        formStr = formStr.replaceAll(" ","");
        String[] str = formStr.split("\\|");
        String action = str[0];
        String[] input = str[1].substring(1,str[1].length()-1).split(",");
        //Map<String, String> hiddens = coverResultString2Map(str[1]);
        Map<String, String> hiddens = new HashMap<String, String>();
        for(int i=0;i<input.length;i++)
        {
            String[] kv = input[i].split("=",2);
            if(kv.length == 1)
            {
                hiddens.put(kv[0],"");
            }
            else
            {
                hiddens.put(kv[0],kv[1]);
            }
        }
        form = createHtml(action,hiddens);

        return form;
    }

    /**
     * ��������: getTimeDiff<br>
     * ��������ȡʱ���
     * ����: 
     * �޸����ڣ�2013-3-21����02:52:38
     * @param start
     * @param end
     * @return
     */
    public static String getTimeDiff(Date start,Date end){

        long l = end.getTime() - start.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (l - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        String result = day + "��" + hour + "Сʱ" + min + "��" + s + "��" + ms + "����";
        return result;
    }

    /**
     * ��������: judgeUnknowPash<br>
     * �������жϵ�ַ�����Ƿ��зǷ��ַ�
     * ����: 
     * �޸����ڣ�2013-3-25����05:17:25
     * @param path
     * @return
     */
    public static boolean judgeUnknowPath(String path,String... unknowStrList){
        for(int i=0;i<unknowStrList.length;i++){
            if(path.indexOf(unknowStrList[i])>0){
                return true;
            }
        }
        return false;
    }

    /**
     * ��������: getPayUnitPeriod<br>
     * ��������ȡ�ɷ��������Ϣ
     * ����: 
     * �޸����ڣ�2013-3-27����10:41:28
     * @param paytype
     * @return
     */
    public static Map<String,String> getPayUnitPeriod(String paytype){
        Map<String,String> result = new HashMap<String,String>();

        if(!"".equals(paytype) && paytype != null){
            if(paytype.contains("ONCE")){
                result.put("unit","Y");
                result.put("period","1");
            }
            else if(paytype.contains("Y")){
                result.put("unit","Y");
                result.put("period",paytype.substring(0,paytype.length()-1));
            }
            else if(paytype.contains("M")){
                result.put("unit","M");
                result.put("period",paytype.substring(0,paytype.length()-1));
            }
            else if(paytype.contains("D")){
                result.put("unit","D");
                result.put("period",paytype.substring(0,paytype.length()-1));
            }
        }
        else{
            result.put("unit","");
            result.put("period","");
        }

        return result;
    }

    /**
     * ��������: getIsrUnitPeriod<br>
     * ��������ȡ��������Ϣ
     * ����: 
     * �޸����ڣ�2013-3-27����10:45:48
     * @param isrperiod
     * @return
     */
    public static Map<String, String> getIsrUnitPeriod(String isrperiod){

        Map<String, String> result = new HashMap<String, String>();

        if(!"".equals(isrperiod) && isrperiod != null){
            if(isrperiod.contains("TYO")){
                result.put("unit","A");
                result.put("period",isrperiod.substring(0,isrperiod.length() - 3));
            }
            else if(isrperiod.contains("Y")){
                result.put("unit","Y");
                result.put("period",isrperiod.substring(0,isrperiod.length() - 1));
            }
            else if(isrperiod.contains("M")){
                result.put("unit","M");
                result.put("period",isrperiod.substring(0,isrperiod.length() - 1));
            }
            else if(isrperiod.contains("D")){
                result.put("unit","D");
                result.put("period",isrperiod.substring(0,isrperiod.length() - 1));
            }
        }
        else{
            result.put("unit","");
            result.put("period","");
        }

        return result;
    }

    /**
     * ��������: getPayType<br>
     * ������ת�����ѷ�ʽ����
     * ����: 
     * �޸����ڣ�2013-3-27����10:54:10
     * @param paytype
     * @return
     */
    public static String getPayType(String paytype){
        String result = "";
        if(!"".equals(paytype) && paytype != null){
            if(paytype.contains("ONCE")){
                result = "W";
            }
            else if(paytype.contains("Y")){
                result = "Y";
            }
        }
        return result;
    }


 

    /**
     * ��������: getProjectPath<br>
     *
     * @Description:��ȡ·��
     * @author mxl �޸����ڣ�2013-2-21����03:40:14
     *
     * @param dir
     * @return
     */
    public static String getProjectPath(String dir) {
        String path = CommonUtils.class.getClassLoader().getResource("/")
                .getPath()
                + dir;
        return path;
    }

 

    /**
     * ��������: getProcessID<br>
     * ��������õ�ǰ����ID��PID��<br>
     * ����: 
     * �޸����ڣ�2015��6��24������4:17:04<br>
     */
    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }

    /**
     * @Auth:dongyue
     * @Date:<br>
     * @Desc:��֤�ֻ��Ÿ�ʽ�Ƿ���ȷ
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        if(null==str){
            return b;
        }
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // ��֤�ֻ���
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


}