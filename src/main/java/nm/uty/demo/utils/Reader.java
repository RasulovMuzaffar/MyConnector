//package nm.uty.demo.utils;
//
//import nm.uty.demo.pojo.MyData;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Stream;
//@Component
//public class Reader {
//    private static final String REG = "\\d{2}\\s+(?<wNumber>\\d{8})\\s+\\d{4}\\s+\\d{3}\\s+\\d{5}\\s+\\d{5}\\s+\\d{4}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{1}\\s+\\d{2}\\/\\d{2}\\s+\\d{2}\\s+\\d{2}\\s+\\d{3}\\s+(?<tara>\\d{4})";
//
//    public static void main(String[] args) {
//
//    }
//    public void reader(String filePath){
////        String filePath = "/home/muzaffar/922.txt";
//        Charset charset = Charset.forName("cp866");
//        StringBuilder sb = new StringBuilder();
//        try (Stream<String> stream = Files.lines(Paths.get(filePath), charset)) {
//
//            stream.forEach(sb::append);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final Pattern pattern = Pattern.compile(REG, Pattern.MULTILINE);
//        final Matcher matcher = pattern.matcher(sb.toString());
//        List<MyData> myDataList = new ArrayList<>();
//
//        while (matcher.find()) {
//            Integer wNumber = Integer.parseInt(matcher.group("wNumber"));
//            Integer tara = Integer.parseInt(matcher.group("tara")) * 100;
//            MyData mydata = new MyData();
//            mydata.setTara(tara);
//            mydata.setWNumber(wNumber);
//            myDataList.add(mydata);
////            for (int i = 1; i <= matcher.groupCount(); i++) {
////                System.out.println("Group " + i + ": " + matcher.group(i));
////            }
//        }
//        System.out.println(myDataList.size());
//        myDataList.stream().forEach(System.out::println);
//    }
//}
