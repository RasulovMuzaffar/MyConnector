package nm.uty.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nm.uty.demo.pojo.Train;
import nm.uty.demo.pojo.Wagon;
import nm.uty.demo.utils.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReaderServiceImpl {
    private final String HEADER922 = "\\(:922\\s+\\d{4}\\s+\\d{3,4}\\s+(?<idx>\\d{4}\\s{0,2}\\d{0,3}\\s{0,2}\\d{4})";
    private final String REG922 = "(?<no>\\d{2})\\s+(?<wNumber>\\d{8})\\s+\\d{4}\\s+(?<netto>\\d{3})\\s+(?<destinationStation>\\d{5})\\s+(?<cargoCode>\\d{5})\\s+\\d{4}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{2}\\/\\d{2}\\s+\\d{2}\\s+\\d{2}\\s+\\d{3}\\s+(?<tara>\\d{4})";
    //    private final String REG922 = "\\d{2}\\s+(?<wNumber>\\d{8})\\s+\\d{4}\\s+(?<netto>\\d{3})\\s+\\d{5}\\s+\\d{5}\\s+\\d{4}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{2}\\/\\d{2}\\s+\\d{2}\\s+\\d{2}\\s+\\d{3}\\s+(?<tara>\\d{4})";
    private final String REG57 = "(?<idx>\\d{4}\\+\\s?\\d{0,3}\\+\\d{4})";

    private final String REG91 = "(?<idx>\\d{4}\\s{1,3}\\d{1,3}\\s\\d{4})";

    final WriterServiceImpl writerService;
    final SenderServiceImpl senderService;
    final DataCache dataCache;

    private Pattern pattern;
    private Matcher matcher;


    public boolean reader(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(filePath), Charset.forName("CP866"))) {
            lines.forEach(sb::append);
//        try {
//            File file = new File(filePath);
//            //создаем объект FileReader для объекта File
//            FileReader fr = new FileReader(file);
//            //создаем BufferedReader с существующего FileReader для построчного считывания
//            BufferedReader reader = new BufferedReader(fr);
//            // считаем сначала первую строку
//            String line = reader.readLine();
//            while (line != null) {
//                if (!line.equals("")) {
//                    sb.append(line);
//                    break;
//                }
//                // считываем остальные строки в цикле
//                line = reader.readLine();
//            }
        } catch (FileNotFoundException e) {
            log.warn("FileNotFoundException: " + e.getMessage());
            return false;
        } catch (IOException e) {
            log.warn("IOException: " + e.getMessage());
            return false;
        }
        log.info(sb.toString());
        router(filePath, sb.toString());
        return true;
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
        else if (sprNo == 91)
            reader91(filePath);
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
            dataCache.setIndexes(parseIdx(idx));
        }
        writerService.writer(indexes);
    }

    private void reader91(String filePath) {
        log.info("reader91: " + filePath);
        StringBuilder sb = getStringBuilder(filePath);

        pattern = Pattern.compile(REG91, Pattern.MULTILINE);
        matcher = pattern.matcher(sb.toString());
        List<String> indexes = new ArrayList<>();

        while (matcher.find()) {
            String idx = matcher.group("idx");
            indexes.add(idx);
            dataCache.setIndexes(parseIdx(idx));
        }
        writerService.writer(indexes);
    }

    public void reader922(String filePath) {
        log.info("reader922: " + filePath);

        Train train = new Train();

        StringBuilder sb = getStringBuilder(filePath);

        pattern = Pattern.compile(HEADER922, Pattern.MULTILINE);
        matcher = pattern.matcher(sb.toString());
        String index = "";
        while (matcher.find()) {
            index = matcher.group("idx");
//            dataCache.setIndexes(index);
        }

        if (!index.equals("")) {
            train.setTrainIndex(index);
            pattern = Pattern.compile(REG922, Pattern.MULTILINE);
            matcher = pattern.matcher(sb.toString());
            List<Wagon> wagonList = new ArrayList<>();

            while (matcher.find()) {
                Integer no = Integer.parseInt(matcher.group("no"));
                Integer wNumber = Integer.parseInt(matcher.group("wNumber"));
                Integer tara = Integer.parseInt(matcher.group("tara")) * 100;
                Integer netto = Integer.parseInt(matcher.group("netto")) * 1000;
                Integer destinationStation = Integer.parseInt(matcher.group("destinationStation"));
                Integer cargoCode = Integer.parseInt(matcher.group("cargoCode"));

                Wagon wagon = new Wagon();
                wagon.setNo(no);
                wagon.setTara(tara);
                wagon.setWNumber(wNumber);
                wagon.setNetto(netto);
                wagon.setDestinationStation(destinationStation);
                wagon.setCargoCode(cargoCode);
                wagonList.add(wagon);
            }

            train.setWagons(wagonList);

            if (dataCache.getIndexesWagons().get(index) == null || dataCache.getIndexesWagons().get(index).getWagons().isEmpty()) {
                Map<String, Train> map = new HashMap<>();
                map.put(index, train);
                dataCache.setIndexesWagons(map);
            }
            senderService.mySender(index, train);
        } else log.info("The file does not contain the required data {}", filePath);
    }

    private StringBuilder getStringBuilder(String filePath) {
        Charset charset = Charset.forName("cp866");
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), charset)) {
            stream.forEach(sb::append);
        } catch (IOException e) {
            log.warn("IOException for reader service: " + e.getMessage());
        }

        try {
            if (Files.isWritable(Paths.get(filePath)))
                Files.delete(Paths.get(filePath));
        } catch (IOException e) {
            log.warn("IOException is: {}", e.getMessage());
        }

        return sb;
    }


    private String parseIdx(String idx) {
        return idx.replace("+", " ").replace("  ", " ");
    }
}
