package com.igeek.utils;

/**
 * @file_name : CommonUtils.java
 * @author : 
 * @date   : 2018年3月15日
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
 * date : 2018年3月15日
 * description :
 */
public class CommonUtils {

  
    /**用于id号   id=时间毫秒数+n位流水号*/
    private static long serial_number = 0;
    /**用于id号   id=时间毫秒数+n位流水号*/
    private static long SERIAL_NUMBER_FOR_TRANNO = 0;

    /**用于交易号  中的流水号 n位流水号*/
    private static long TRANSNO_SERIAL_NUMBER = 0;

    /**id附加流水号长度   id=时间毫秒数+n位流水号 */
    private static final int SIZE_OF_ECNO_SERIAL_NUMBER = 5;
    /**ESB接口全局交易流水号 tranno=yyyyMMddHHmmssSSS+6位流水号*/
    private static final int SIZE_OF_TRANNO_SERIAL_NUMBER = 6;

    // 当前进程PID
    public static final int PID = getProcessID();


   
 

    /**
     * @Description: 判断是否不为空, 如果是 ""," ","null",null 则返回false
     * @param: orignalValue 需要判断的值
     * @return: boolean
     */
    public static boolean isNotBlank(Object... obj) {
        return !isBlank(obj);
    }

    /**
     * @Description: 判断数组对象是否为空值 , 如果是 ""," ","null",null 则返回true, 对于Collection.size<1也空
     * @param: orignalValue 需要判断的值
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
     * 获取UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }


    /**
     * 方法名称: getTran<br>
     *
     * @Description:调用ESB获取全局流水号和交易时间
     * @author 
     *
     * @return
     */
    public static synchronized String[] getTran() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");// 设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 交易流水号时间格式
        Date date = getDate();
        String[] id = new String[] { "", "" };
        id[0] = String.valueOf(df.format(date));
        id[1] = String.valueOf(df2.format(date));
        SERIAL_NUMBER_FOR_TRANNO++;
        if (SERIAL_NUMBER_FOR_TRANNO >= (SIZE_OF_TRANNO_SERIAL_NUMBER - 1) * 10)// 超过长度,复位
            SERIAL_NUMBER_FOR_TRANNO = 0;

        String serialNumber = getEnoughLengthString(
                String.valueOf(SERIAL_NUMBER_FOR_TRANNO),
                SIZE_OF_TRANNO_SERIAL_NUMBER);
        id[1] = id[1] + serialNumber;
        return id;
    }

    /**
     * 获取当前时间
     * @return date 当前时间
     */
    public static Date getDate(){
        return new Date();
    }



    /**
     * 获取指定长度的字符串,不足左补零,超出位数,取倒数的足够位数
     *
     * @param string
     *            字符串
     * @param length
     *            指定长度
     * @return string 字符串
     */
    public static String getEnoughLengthString(String string, int length) {
        if (string == null)
            return "";

        int strLength = string.trim().length();// 字符串长度

        // 需要补零的个数
        int theNumberOfZeroCreated = length - strLength;

        if (theNumberOfZeroCreated < 0) {// 超出,去倒数足够位数
            string = string.substring(strLength - length);
        } else {// 左补零
            for (int i = 0; i < theNumberOfZeroCreated; i++) {
                string = "0" + string;
            }
        }
        return string;
    }

    /**
     * 方法名称: getWebProjectPath<br>
     *
     * @Description:获取web下的绝对文件路径 @authormxl 修改日期：2013-2-21下午03:39:52
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
     * @Description: 根据HttpServletRequest获取用户IP地址
     * @param request
     * @return IP
     * @author 
     * 修改时间： 2013-8-12 下午01:58:24
     * 修改内容：新建
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
     * 过滤跨站脚本关键字
     *
     * @param str
     * 需要过滤的字符串
     * @return 过滤后的字符串
     */
    public static String filterStr(String str){
        if(str==null)
            return "";
        str=str.replaceAll("<","");
        str=str.replaceAll(">","");
        str=str.replaceAll("‘","");
        str=str.replaceAll("&","＆");
        str=str.replaceAll("#","＃");
        str=str.replaceAll("%","％");
        str=str.replaceAll("\"","");
        return str;
    }

    /**
     * 方法名称: getParameter<br>
     * 描述:获取参数,如果为空,返回""
     * 作者: Administrator
     * 修改日期：2013-3-28下午07:08:22
     * @param request
     * @param key
     * @return
     */
    public static String getParameter(HttpServletRequest request,String key){
        Object value = request.getParameter(key);
        return value==null?"":value.toString();


    }

    /**
     * 根据身份证号获取生日
     * @param IDNo 身份证号
     * @return 返回生日 yyyy-MM-dd
     * @throws Exception
     * @throws Exception
     */
    public static String getBirthdayByIDNo(String IDNo) throws Exception{
        String birthday = "";

        if(IDNo==null||"".equals(IDNo))
            return "";

        if(IDNo.length()==18){//18位身份证号提取出生日期
            String birthday18 = IDNo.substring(6,14);
            birthday = birthday18.substring(0,4)+"-"+birthday18.substring(4,6)+"-"+birthday18.subSequence(6,8);
            System.out.println("18位身份证号的出生日期:"+birthday18);
        }else if(IDNo.length()==15){//15位身份证号提取出生日期
            String birthday15 = IDNo.substring(6,12);
            birthday = "19"+birthday15.substring(0,2)+"-"+birthday15.substring(2,4)+"-"+birthday15.substring(4,6);
            System.out.println("15位身份证号的出生日期:"+birthday15);
        }else {
            throw new Exception("输入的身份证号不是15位也不是18位,请查证!");
        }

        return birthday;
    }

    /**
     *  获取指定长度的字符串,不足左补零,超出位数,取倒数的足够位数
     * @param serialNoLong 字符串
     * @param length 指定长度
     * @return string 字符串
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
     * 获取指定长度的字符串,不足左补零,超出位数,取倒数的足够位数
     *
     * @param string
     *            字符串
     * @param length
     *            指定长度
     * @return string 字符串
     *//*

    public static String getEnoughLengthString(String string, int length) {
        if (string == null)
            return "";

        int strLength = string.trim().length();// 字符串长度

        // 需要补零的个数
        int theNumberOfZeroCreated = length - strLength;

        if (theNumberOfZeroCreated < 0) {// 超出,去倒数足够位数
            string = string.substring(strLength - length);
        } else {// 左补零
            for (int i = 0; i < theNumberOfZeroCreated; i++) {
                string = "0" + string;
            }
        }
        return string;
    }

*/

    /**
     * 判断是否为空, 如果是 ""," ","null",null 则返回true
     * @param orignalValue 需要判断的值
     * @return boolean
     */
    public static boolean isEmpty(Object orignalValue) {
        return isBlank(orignalValue);
    }


    /**
     * 判断是否不为空, 如果是 ""," ","null",null 则返回false
     * @param obj 需要判断的值
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj) {
        return isNotBlank(obj);
    }



    /**
     * 转换为 string 类型,如果为空("",null,"null")返回 ""
     * @param obj
     * @return 返回字符串
     */
    public static String filterToStr(Object obj) {
        return isBlank(obj)?"":obj.toString().trim();
    }


   

    /**
     * 转换成保留两位的  金额
     * @param num 金额
     * @return 保留两位的金额
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
     * 生成验证码
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
     * @param date 需要计算的日期
     * @param field the calendar field. eg. Calendar.YEAR(年),Calendar.MONTH(月),Calendar.DATE(天)
     * @param amount the amount of date or time to be added to the field.需要添加(+10)或者减少(-10)的数量
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
     * 将数字从一位小数变为String型的两位小数
     * @param dnumber 一位小数
     * @return String 两位小数
     */
    public static String decimalFormat(double dnumber){
        //将用户购买金额进行显示的转换，页面需要小数点两位。
        DecimalFormat df = new DecimalFormat( "########.00 ");
        String dff = df.format(dnumber);
        return dff;
    }

    /**
     *
     * 描述：生成唯一id号  id=时间毫秒数+5位流水号
     * @author lihengjun
     * 修改日期：2012-8-15下午03:29:04
     * @return 唯一id号
     */
    public static synchronized String getUniqueId(){
        String id =  String.valueOf(System.currentTimeMillis()) ;
        serial_number++;
        if(serial_number>=(SIZE_OF_ECNO_SERIAL_NUMBER-1)*10)//超过长度,复位
            serial_number=0;

        String serialNumber = getEnoughLengthString(serial_number,SIZE_OF_ECNO_SERIAL_NUMBER);
        id = id+serialNumber;
        return id;
    }



   

    /**
     *
     * 描述：通过出生日期获取年龄
     * @author 
     * 修改日期：2012-8-9下午01:38:53
     * @param birthday 出生日期
     * @return 当前年龄
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
     * 方法名称: getPeriodByChina<br>
     * 描述：根据保险期间编码转换为中文性别描述
     * 作者: mxl
     * 修改日期：2013-2-21下午03:00:11
     * @param period
     * @return
     */
    public static String getPeriodByChina(String period){
        if(period!=null){
            String lastStr = period.substring(period.length()-1,period.length());
            if("D".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"天";
            }else if("M".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"月";
            }else if("Y".equals(lastStr)){
                period = period.substring(0,period.length()-1)+"年";
            }else if("O".equals(lastStr)){
                period = "至"+period.substring(0,period.length()-3)+"周岁";
            }
        }
        return period;
    }

  

    /**
     * 计算日期（包含临界值）
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
     * @Description: 日期添加指定类型大小
     * @param compare
     * @param sdate
     * @param addNumber
     * @return
     * @author 
     * 修改时间： 2014-1-17 上午11:16:53
     * 修改内容：日期添加指定类型大小
     */
    public static String dateAdd(String compare, String sdate,int addNumber)
    {
        Date date  = null;
        SimpleDateFormat df = null;

        if(sdate.indexOf("-")>0)
            df = new SimpleDateFormat("yyyy-MM-dd");
        else
            df = new SimpleDateFormat("yyyy年MM月dd日");

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
     * @Description: 日期比较
     * @param firstDate
     * @param strSecondDate
     * @return
     * @throws RuntimeException
     * @author 
     * 修改时间： 2014-1-17 上午11:18:05
     * 修改内容：日期比较
     */
    public static int compareDate(Date firstDate,String strSecondDate)throws RuntimeException{
        Date secondDate=convertStringToDate(strSecondDate);
        return compareDate(firstDate,secondDate);
    }

    /**
     *
     * @Title: compareDate
     * @Description: 如果参数表示的时间等于此 Calendar 表示的时间，则返回 0 值；如果此 Calendar 的时间在参数表示的时间之前，则返回小于 0 的值；如果此 Calendar 的时间在参数表示的时间之后，则返回大于 0 的值。
     * @param firstDate
     * @param secondDate
     * @return
     * @throws RuntimeException
     * @author 
     * 修改时间： 2014-1-20 上午09:32:39
     * 修改内容：日期比较
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
     * @Description: 将字符串转换为日期类型
     * @param orignalValue
     * @return
     * @throws RuntimeException
     * @author 
     * 修改时间： 2014-1-20 上午09:28:36
     * 修改内容：添加
     */
    public static  Date convertStringToDate(String orignalValue) throws RuntimeException{
        String tempValue=orignalValue;
        try{
            tempValue=tempValue.replaceFirst("年","-");
            tempValue=tempValue.replaceFirst("月","-");
            tempValue=tempValue.replaceFirst("日","");
            tempValue=tempValue.replaceFirst("/","-");
            tempValue=tempValue.replaceFirst("/","-");

            if(tempValue.trim().length()<=10){
                tempValue=tempValue.trim() +" 0:00:00";
            }
            SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormatter.parse(tempValue);
        }catch(Exception ex){
            throw new RuntimeException(orignalValue +"不是日期型数据");
        }
    }

    /**
     * 计算日期（不包含临界值）
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
     * 方法名称: createHtml<br>
     * 描述：构造HTTP POST交易表单
     * 作者: 
     * 修改日期：2013-3-6下午04:22:45
     * @param action 表单提交地址
     * @param hiddens 以MAP形式存储的表单键值
     * @return 构造好的HTTP POST交易表单
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
        //sf.append("<input type=‘submit‘ value=‘支付‘ />");
        sf.append("</form>\n");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.getElementById(‘sform‘).submit();");
        sf.append("</script>");
        return sf.toString();
    }

    /**
     * 方法名称: coverResultString2Map<br>
     * 描述：分割Form字符串
     * 作者: 
     * 修改日期：2013-3-20下午05:27:42
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
     * 方法名称: getForm<br>
     * 描述：解析组装支付表单字符串
     * 作者: 
     * 修改日期：2013-3-6下午04:28:27
     * @param formStr 收费申请接口返回的form字符串
     * @return 拼装的HTML表单代码字符串
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
     * 方法名称: getTimeDiff<br>
     * 描述：获取时间差
     * 作者: 
     * 修改日期：2013-3-21下午02:52:38
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
        String result = day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
        return result;
    }

    /**
     * 方法名称: judgeUnknowPash<br>
     * 描述：判断地址连接是否含有非法字符
     * 作者: 
     * 修改日期：2013-3-25下午05:17:25
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
     * 方法名称: getPayUnitPeriod<br>
     * 描述：获取缴费期相关信息
     * 作者: 
     * 修改日期：2013-3-27上午10:41:28
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
     * 方法名称: getIsrUnitPeriod<br>
     * 描述：获取保险期信息
     * 作者: 
     * 修改日期：2013-3-27上午10:45:48
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
     * 方法名称: getPayType<br>
     * 描述：转换交费方式代码
     * 作者: 
     * 修改日期：2013-3-27上午10:54:10
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
     * 方法名称: getProjectPath<br>
     *
     * @Description:获取路径
     * @author mxl 修改日期：2013-2-21下午03:40:14
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
     * 方法名称: getProcessID<br>
     * 描述：获得当前进程ID（PID）<br>
     * 作者: 
     * 修改日期：2015年6月24日下午4:17:04<br>
     */
    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }

    /**
     * @Auth:dongyue
     * @Date:<br>
     * @Desc:验证手机号格式是否正确
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        if(null==str){
            return b;
        }
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


}