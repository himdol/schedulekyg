package com.schedule.schedulekyg.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//push 기능
@Component
public class PushUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.push.client-id}")
    private String clientId;

    @Value("${api.push.client-secret}")
    private String clientSecret;

    @Value("${api.push.mid}")
    private String mid;

    @Value("${api.push.end-point1}")
    private String endPoint;

    @Value("${api.push.grant-type}")
    private String grantType;

    /**
     * accessToken 생성
     * @return result
     */

    public String accessToken() throws Exception{
        //HttpHeaders headers = null;
        Map<String, String> parameters = null;
        try{
           // headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_JSON);
            parameters = new HashMap<>();
            parameters.put("grant_type",grantType);
            parameters.put("client_id",clientId);
            parameters.put("client_secret",clientSecret);
            parameters.put("account_id",mid);
            //RestTemplate restTemplate = new RestTemplate();
            //HttpEntity<?> entity = new HttpEntity<>(parameters,headers);

//            ResponseEntity<Map> response =restTemplate.postForEntity(endPoint,entity,Map.class);
//            String result = (String)response.getBody().get("access_token");
            String result =  getData(endPoint,parameters, "");
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * push API
     * @param host
     * @param parameter
     * @return restTemplate
     */

    public ResponseEntity<Map> pushApi(String host, Map<String,Object> parameter) throws Exception{
        HttpHeaders headers = null;
        logger.info("host=>{}\n REQUEST=>{}",host,parameter);
        try{
            String accessToken = accessToken();
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<?> entity = new HttpEntity<>(parameter,headers);
        return restTemplate.postForEntity(host,entity, Map.class);
        }catch (Exception e){
            logger.error("api에러=>{}",e.getMessage());
            throw new RuntimeException("Push API에 에러가 발생했습니다.");
        }
    }

    //post API
    //파라미터 => host: 주소, parameters:매개변수, token: 토근값(없으면 빈값)
    /**
     * Push 알람 설정 API
     * @param host
     * @param parameter
     * @return String
     */
    public String postApi(String host, List<Map<String, Object>> parameter) throws Exception {
        String token = accessToken();
        return getData(host,parameter,token);
    }

    public String postApi(String host, Map<String, Object> parameter) throws Exception {
        String token = accessToken();
        return getData(host,parameter,token);
    }

    //post API
    private String getData(String host, Object parameters, String gbn) throws RuntimeException {
        logger.info("host=>{}\n REQUEST=>{}", host, parameters);
        try {

            URL url = new URL(host);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", String.valueOf(MediaType.APPLICATION_JSON));
            if (!StringUtils.isBlank(gbn)) {
                conn.setRequestProperty("Authorization", "Bearer " + gbn);
            }
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);

            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            String sendValue = new ObjectMapper().writeValueAsString(parameters);
            //wr.writeBytes(sendValue);
            wr.write(sendValue.getBytes(StandardCharsets.UTF_8));
            wr.flush();
            wr.close();

            Charset charset = StandardCharsets.UTF_8;
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            String result = String.valueOf(response);

            if (StringUtils.isBlank(gbn)) {
                Map<String, String> res = null;
                res = new ObjectMapper().readValue(result, new TypeReference<Map<String, String>>() {});
                return res.get("access_token");
            }else{
                return result;
            }

        } catch (MalformedURLException e) {
            logger.error("API 에러 =>{}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("API 에러 =>{}", e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("API 에러 =>{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
