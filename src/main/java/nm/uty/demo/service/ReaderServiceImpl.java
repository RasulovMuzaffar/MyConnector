package nm.uty.demo.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.pojo.MyData;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
//@AllArgsConstructor
public class ReaderServiceImpl {
    private final String REG = "\\d{2}\\s+(?<wNumber>\\d{8})\\s+\\d{4}\\s+\\d{3}\\s+\\d{5}\\s+\\d{5}\\s+\\d{4}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{2}\\/\\d{2}\\s+\\d{2}\\s+\\d{2}\\s+\\d{3}\\s+(?<tara>\\d{4})";
    private final String REG57 = "(?<idx>\\d{4}\\+\\s?\\d{0,3}\\+\\d{4})";

    private Pattern pattern;
    private Matcher matcher;

    public ReaderServiceImpl() {
    }

    public void reader(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(filePath);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            while (line != null) {
                if (!line.equals("")) {
                    sb.append(line);
                    break;
                }
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        router(filePath, sb.toString());
    }

    private void router(String filePath, String str) {
        String routerRegex = "(?<sprNo>\\d{2,4})\\s+\\d{2}\\.\\d{2}\\s+\\d{2}\\-\\d{2}";
        pattern = Pattern.compile(routerRegex, Pattern.MULTILINE);
        matcher = pattern.matcher(str);
        int sprNo = 0;
        while (matcher.find()) {
            sprNo = Integer.parseInt(matcher.group("sprNo"));
        }
        if (sprNo == 57)
            reader57(filePath);
        else
            reader922(filePath);
    }

    private void reader57(String filePath) {
        log.info("reader57: " + filePath);
        StringBuilder sb = getStringBuilder(filePath);

        pattern = Pattern.compile(REG57, Pattern.MULTILINE);
        matcher = pattern.matcher(sb.toString());
        List<String> indexes = new ArrayList<>();

        while (matcher.find()) {
            String idx = matcher.group("idx");
            indexes.add(idx);
        }
        System.out.println(indexes.size());
        indexes.stream().forEach(System.out::println);
    }

    public void reader922(String filePath) {
        log.info("reader922: " + filePath);
        StringBuilder sb = getStringBuilder(filePath);

        pattern = Pattern.compile(REG, Pattern.MULTILINE);
        matcher = pattern.matcher(sb.toString());
        List<MyData> myDataList = new ArrayList<>();

        while (matcher.find()) {
            Integer wNumber = Integer.parseInt(matcher.group("wNumber"));
            Integer tara = Integer.parseInt(matcher.group("tara")) * 100;
            MyData mydata = new MyData();
            mydata.setTara(tara);
            mydata.setWNumber(wNumber);
            myDataList.add(mydata);
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                System.out.println("Group " + i + ": " + matcher.group(i));
//            }
        }
        System.out.println(myDataList.size());
        myDataList.stream().forEach(System.out::println);
    }

    private StringBuilder getStringBuilder(String filePath) {
        Charset charset = Charset.forName("cp866");
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), charset)) {
            stream.forEach(sb::append);
        } catch (IOException e) {
//            e.printStackTrace();
            log.warn("IOException for reader service");
        }
        return sb;
    }
}
