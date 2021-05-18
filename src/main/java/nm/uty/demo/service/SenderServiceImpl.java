package nm.uty.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.pojo.Train;
import nm.uty.demo.pojo.Wagon;
import nm.uty.demo.utils.DataCache;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SenderServiceImpl {

    @Value(value = "${remoteServer}")
    private String remoteServer;
    @Value(value = "${remoteServerPort}")
    private String remoteServerPort;
    private final DataCache dataCache;

    public SenderServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public String getToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("username", "johndoe");
            map.add("password", "secret");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    String.format("http://%s:%s/api/login", remoteServer.replace("_", "."), remoteServerPort), request, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                JSONParser parser = new JSONParser();
                log.info("token received successfully!");
                try {
                    JSONObject object = (JSONObject) parser.parse(response.getBody());
                    return String.valueOf(object.get("access_token"));
                } catch (ParseException e) {
                    log.warn("ParseException: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("getToken exception: {}", e.getMessage());
        }
        return null;
    }

    public void mySender(String index, Train train) {
        String token = getToken();
        if (token == null) {
            log.warn("token is null!");
            return;
        }
//dataCache.getIndexesWagons().get(index).


        /**
         * endpoint - api/paper/upload_lines
         * body - любой текст
         * headers - {Authorization: Bearer <token>}
         */
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);


        for (Map.Entry<String, Train> entry : dataCache.getIndexesWagons().entrySet()) {
            String key = entry.getKey();
            Train value = entry.getValue();
//            System.out.println(train);
            if (!dataCache.getSuccessSendIndexes().contains(key))
                try {
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    String jsonData = ow.writeValueAsString(value);
                    HttpEntity<String> request = new HttpEntity<>(jsonData, headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            String.format("http://%s:%s/api/paper/upload_lines", remoteServer.replace("_", "."), remoteServerPort),
                            request,
                            String.class);

                    if (response.getStatusCode().equals(HttpStatus.OK)) {
                        dataCache.delIndex(key);
                        dataCache.getIndexesWagons().remove(key);
                        dataCache.setSuccessSendIndexes(key);
                    } else {
                        Map<String, Train> map = new HashMap<>();
                        map.put(key, value);
                        dataCache.setIndexesWagons(map);
                    }
//                    log.info(request.getBody());
                    log.info("data with idx: {}, is sended to remote service!", key);
                } catch (JsonProcessingException e) {
                    log.warn("JsonProcessingException: " + e.getMessage());
                }
            else {
                dataCache.getIndexesWagons().remove(key);
                dataCache.delIndex(key);
            }
        }
        log.info("So GOOD!!!");


//        if (response.getStatusCode().equals(HttpStatus.OK)) {
//            JSONParser parser = new JSONParser();
//            try {
//                JSONObject object = (JSONObject) parser.parse(response.getBody());
//                System.out.println(object.get("access_token"));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
