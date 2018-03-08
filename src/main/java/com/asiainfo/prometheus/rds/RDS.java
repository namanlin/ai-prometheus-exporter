package com.asiainfo.prometheus.rds;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import com.aliyun.openservices.ons.api.impl.authority.OnsAuthSigner;
import com.asiainfo.prometheus.util.CommonUtil;
import com.asiainfo.prometheus.util.HttpClientFactory;
import com.asiainfo.prometheus.util.SrvConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RDS {
    
    @Autowired
    private SrvConfig config;
    
    public HttpResponse postRequest(String url, String... args) {
        String accesskey = config.getAccesskey(); //2SbUQlH7FJKyur9V
        String securityKey = config.getSecurityKey(); //WGoQpjLNgTA4VwrHNQBcqe0zDbXZti
        String format = config.getRdsFormat();   //JSON
        String version = config.getRdsVersion();   //2014-08-15
        String signatureMethod = config.getRdsSignatureMethod();   //HMAC-SHA1
        String signatureVersion = config.getRdsSignatureVersion();   //1.0
        
//        String accesskey = "2SbUQlH7FJKyur9V";
//        String securityKey = "WGoQpjLNgTA4VwrHNQBcqe0zDbXZti";
//        String format = "JSON";
//        String version = "2014-08-15";
//        String signatureMethod = "HMAC-SHA1";
//        String signatureVersion = "1.0";
        
        Map<String, String> paras = new TreeMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        paras.put("Format", format);
        paras.put("Version", version);
        paras.put("SignatureMethod", signatureMethod);
        paras.put("SignatureNonce", String.valueOf(System.currentTimeMillis()));
        paras.put("SignatureVersion", signatureVersion);
        paras.put("AccessKeyId", accesskey);
        paras.put("Timestamp", df.format(new Date()));

        for (String arg : args) {
            String[] kv = arg.split(":", 2);
            paras.put(kv[0], kv[1]);
        }

        String origin = RDS.combineContent(paras);
        String signature = OnsAuthSigner.calSignature(origin, securityKey + "&");
        paras.put("Signature", signature);

        List<NameValuePair> data = new ArrayList<>();
        for (final Map.Entry<String, String> entry : paras.entrySet()) {
            data.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpResponse response;
        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(data, "UTF-8");
            url = url + "/?" + EntityUtils.toString(requestEntity);
            HttpUriRequest request =
                RequestBuilder.get(url).setConfig(HttpClientFactory.getDefaultRequestConfig()).build();
            response = HttpClientFactory.get().execute(request);
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Exception occurred when post a http request[%s], msg[%s]", url, e.getMessage()));
        }
        
        return response;
    }

    public static String combineContent(Map<String, String> params) {
        int isFirst = 0;
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isFirst > 0)
                sb.append("&");
            isFirst++;
            sb.append(new BasicNameValuePair(percent_encode(entry.getKey()), percent_encode(entry.getValue())));
        }

        return "GET&%2F&" + percent_encode(sb.toString());
    }

    public static String percent_encode(String encodeStr) {
        String res = "";
        try {
            res = URLEncoder.encode(encodeStr, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return res;
    }

    /**
     * 调用RDS的DescribeSlowLogs方法，获取慢查询sql数量。
     * @return
     */
    public RDSBean getSlowCount() {
        String rdsUrl = config.getRdsUrl();  
        String dbInstanceId = config.getRdsDBInstanceId();   
        String action = config.getRdsAction();  
        String dbName = config.getRdsDBName();

        RDSBean bean = new RDSBean();
        try {
            HttpResponse response =
                postRequest(rdsUrl, "Action:" + action, "DBInstanceId:" + dbInstanceId, "DBName:" + dbName,
                    "StartTime:" + CommonUtil.getBeforeDay() + "Z", "EndTime:" + CommonUtil.getCurrentDay() + "Z");
            
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            
            JSONObject object = new JSONObject(json);
            JSONObject items = (JSONObject) object.get("Items");
            JSONArray jsonArray = items.getJSONArray("SQLSlowLog");
            System.out.println(jsonArray);
            
            List<RDSItemDetail> details = new ArrayList<RDSItemDetail>();
            for (int i = 0; i < jsonArray.length(); i++) {
                RDSItemDetail vo = new RDSItemDetail();
                JSONObject obj = jsonArray.getJSONObject(i);
                vo.setHostAddress((String)obj.get("hostAddress"));
                vo.setQueryTimes((String)obj.get("queryTimes"));
                vo.setLockTimes((String)obj.get("lockTimes"));
                vo.setSqlText((String)obj.get("sqlText"));
                details.add(vo);
            }
            
            bean.setRdsShowCount((Integer)object.get("TotalRecordCount"));
            bean.setDbInstanceId(dbInstanceId);
            bean.setDbName(dbName);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bean;
    }
    
    public static void main(String[] args) {
//        // 查看慢日志统计
//        HttpResponse response = postRequest("http://rds.cpct.com.cn", "Action:DescribeSlowLogs",
//            "DBInstanceId:rds2439awoqx0363m0ib", "DBName:test", "StartTime:2018-01-01Z", 
//            "EndTime:2018-03-08Z");
//        // 查看慢日志明细
//        // HttpResponse response=postRequest("http://rds.cpct.com.cn",
//        // "Action:DescribeSlowLogRecords",
//        // "DBInstanceId:rds2439awoqx0363m0ib",
//        // "StartTime:2018-03-01T12:00Z",
//        // "EndTime:2018-03-01T13:00Z");
//
//        String json;
//        try {
//            json = EntityUtils.toString(response.getEntity());
//            System.out.println(json);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
      
    }
}