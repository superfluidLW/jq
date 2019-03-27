package com.chendu.jq.test;

import com.chendu.jq.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JnlcSpider {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
    private SimpleDateFormat toFormat = new SimpleDateFormat("yyMMddHHmmssZ");

    class LData implements Comparable<LData> {
        public String date;
        public String bankName;
        public Boolean principalProtection;
        public Double yield;

        @Override
        public int compareTo(LData o) {
            return LocalDate.parse(this.date).compareTo(LocalDate.parse(o.date));
        }
    }

    @Test
    public void jnlcParser(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\financialProduct.txt"));
            List<LData> arrData = new ArrayList<>();

            File file = new File("D:\\description.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            String line = reader.readLine();
            boolean headerWritten = false;
            while (line != null){
                HashMap<String, Object> map = JsonUtils.readValue(line, HashMap.class);
//                System.out.println(map.get("page"));
                List<LinkedHashMap<String, Object>> rows = (List<LinkedHashMap<String, Object>>)map.get("rows");
                for (LinkedHashMap<String, Object> row:rows
                     ) {
                    if(!headerWritten){
                        bw.write(header());
                        bw.newLine();
                        headerWritten = true;
                    }
                    arrData.add(toLData(row));
                    bw.write(concatenate(row));
                    bw.newLine();
                }
                line = reader.readLine();
            }

            reader.close();
            bw.close();

            arrData = arrData.stream().filter(e -> StringUtils.isNotEmpty(e.date) && e.yield != null).collect(Collectors.toList());
            Collections.sort(arrData);
            Map<String, List<LData>> dateLDataMap = arrData.stream().collect(Collectors.groupingBy(e -> e.date));
            List<String> sortedKeys = new ArrayList<>();
            sortedKeys.addAll(dateLDataMap.keySet());
            Collections.sort(sortedKeys);
            for (String date:sortedKeys
                 ) {
                if(StringUtils.isEmpty(date)){
                    continue;
                }
                Double yield = 0.0;
                Integer nonZeroNum = 0;
                String str = date + "\t";

                str += dateLDataMap.get(date).stream().filter(e -> e.principalProtection != null && e.principalProtection).count();
                str += "\t";
                str +=  dateLDataMap.get(date).stream().filter(e -> e.principalProtection != null &&e.principalProtection).filter(e -> e.yield != null).mapToDouble(e -> e.yield).average();
                str += "\t";
                str +=  dateLDataMap.get(date).stream().filter(e -> e.principalProtection != null && !e.principalProtection).count();
                str += "\t";
                str +=  dateLDataMap.get(date).stream().filter(e -> e.principalProtection != null && !e.principalProtection).filter(e -> e.yield != null).mapToDouble(e -> e.yield).average();
                System.out.println(str);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String header(){
        List<String> contents = Arrays.asList(
                "产品名称",
                "发行银行",
                "币种",
                "起售日",
                "停售日",
                "收益起计日",
                "到期日",
                "理财期限",
                "预期年化收益率（%）",
                "收益类型",
                "可否提前终止",
                "发行对象",
                "发行地区"
                );
        return StringUtils.join(contents, "\t");
    }

    private LData toLData(LinkedHashMap<String, Object> row){
        LData lData = new LData();
        lData.date = toyyyyMM((String)row.get("sdtFinaStart"));
        lData.bankName =(String)row.get("strOrganShort");
        lData.yield = row.get("dYqnhsylsx") != null ? Double.parseDouble(row.get("dYqnhsylsx").toString()) : null;
        lData.principalProtection = row.get("strProfitType") != null ? row.get("strProfitType").toString().startsWith("保本") : null;
        return lData;
    }

    private String concatenate(LinkedHashMap<String, Object> row) {
        List<String> contents = Arrays.asList(
                (String)row.get("strFinaName"),
                (String)row.get("strOrganShort"),
                (String)row.get("strMoneyType"),
                toDate((String)row.get("sdtSaleStart")),
                toDate((String)row.get("sdtSaleEnd")),
                toDate((String)row.get("sdtFinaStart")),
                toDate((String)row.get("sdtFinaEnd")),
                (String)row.get("strLimit"),
                row.get("dYqnhsylsx") != null ? row.get("dYqnhsylsx").toString() : "",
                (String)row.get("strProfitType"),
                (String)row.get("strBankStopRight"),
                (String)row.get("strSaleTo"),
                (String)row.get("strSaleWhereReal")
        );
        return StringUtils.join(contents, "\t");
    }

    @Test
    public void testDateConversion(){
        String dateStr = "/Date(1555948800000+0800)/";
        String newDateStr = toDate(dateStr);
        assert newDateStr.equals("2019-04-23");
    }

    private String toyyyyMM(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String str=dateStr.replace("/Date(","").replace(")/","");
        String time = str.substring(0,str.length()-5);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date) + "-01";
    }

    private String toDate(String dateStr){
        if(StringUtils.isEmpty(dateStr)){
            return "";
        }
        String str=dateStr.replace("/Date(","").replace(")/","");
        String time = str.substring(0,str.length()-5);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void  jnlcSpider(){
        String url = "http://bankdata.jnlc.com/SitePages/Layouts/JNPJFeature/search.ashx?qt=complex&qn=BankFinacleAllProducts&model=YHLC";

        spider(url);
    }

    private void spider(String url){
        try {
            File file = new File("D:\\financialProduct.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);

            for(int i = 1; i <= 200; ++i) {
                System.out.println("Fetching " + i + "th page") ;
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("_search", "true"));
                params.add(new BasicNameValuePair("nd", "1551705083745"));
                params.add(new BasicNameValuePair("rows", "200"));
                params.add(new BasicNameValuePair("sidx", "strFinaName"));
                params.add(new BasicNameValuePair("sord", "desc"));
                params.add(new BasicNameValuePair("filters", "{\"groupOp\":\"AND\",\"groups\":[{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"sdtFinaEnd\",\"op\":\"ge\",\"data\":\"'2012-01-01'\"}]}],\"rules\":[{\"field\":\"strFinaType\",\"op\":\"eq\",\"data\":\"'结构性产品'\"},{\"field\":\"strSaleTo\",\"op\":\"cn\",\"data\":\"\"}]}"));
                params.add(new BasicNameValuePair("page", ""+i));
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try (InputStream instream = entity.getContent()) {
                        String result = IOUtils.toString(instream, "UTF-8");
                        bw.write(result);
                        bw.newLine();
                        log.info(result);
                    }
                }

                bw.flush();
                Thread.sleep(2000);
            }

            bw.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

