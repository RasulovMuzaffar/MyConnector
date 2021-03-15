package nm.uty.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.pojo.MyData;
import nm.uty.demo.utils.DataCache;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    private final DataCache dataCache;

    public SenderServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "johndoe");
        map.add("password", "secret");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://94.237.100.246:8001/api/login", request, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject object = (JSONObject) parser.parse(response.getBody());
                return String.valueOf(object.get("access_token"));
            } catch (ParseException e) {
                log.warn("ParseException: " + e.getMessage());
            }
        }
        return null;
    }

    public void mySender(String index, List<MyData> data) {
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


        dataCache.getIndexesWagons().entrySet().forEach(entry -> {
                    if (!dataCache.getSuccessSendIndexes().contains(entry.getKey()))
                        try {
                            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            String jsonData = ow.writeValueAsString(entry.getValue());
                            HttpEntity<String> request = new HttpEntity<>(jsonData, headers);

                            ResponseEntity<String> response = restTemplate.postForEntity(
                                    "http://94.237.100.246:8001/api/paper/upload_lines", request, String.class);

                            if (response.getStatusCode().equals(HttpStatus.OK)) {
                                dataCache.delIndex(entry.getKey());
                                dataCache.getIndexesWagons().remove(entry.getKey());
                                dataCache.setSuccessSendIndexes(entry.getKey());
                            } else {
                                Map<String, List<MyData>> map = new HashMap<>();
                                map.put(entry.getKey(), entry.getValue());
                                dataCache.setIndexesWagons(map);
                            }
                            System.out.println(request.getBody());
                        } catch (JsonProcessingException e) {
                            log.warn("JsonProcessingException: " + e.getMessage());
                        }
                    else {
                        dataCache.getIndexesWagons().remove(entry.getKey());
                        dataCache.delIndex(entry.getKey());
                    }
                }
        );


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
