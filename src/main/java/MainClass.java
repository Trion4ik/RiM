//import org.jsoup.nodes.Document;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.ListIterator;
//import java.util.concurrent.*;
//
//public class MainClass {
//
//    public static void main(String[] args) {
//        double startTime, endTime;
//        try {
//            Parser.createDataTree();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        String url1 = "http://microformats.org";
////        String url2 = "http://ukr.net";
//        ForkJoinPool fjp = new ForkJoinPool();
//        ArrayList<String> arrayList = new ArrayList<>(20_000);
//        ArrayList<String> arrayListTemp = new ArrayList<>();
//        arrayList.add("http://microformats.org");
//        arrayList.add("http://ukr.net");
//        arrayList.add("http://ya.ru");
////        arrayList.add(url2);
////        arrayList.ensureCapacity(10_000_000_000);
//        startTime = System.currentTimeMillis();
//        // в циклі запускаємо опрацювання задачі
//        for (int i = 0; i<2; i++) {
//            ForkCompute task = new ForkCompute(arrayList, 0, arrayList.size());
//
//            arrayListTemp = fjp.invoke(task);
//            arrayList.clear();
//            arrayList = arrayListTemp;
//            task.reinitialize();
//
//        }
//        endTime = System.currentTimeMillis();
//
////        for(String url: arrayList){
////            System.out.println(url);
////        }
//        System.out.println("arrayList.size(): " + arrayList.size());
//        System.out.println("Time of the parsing: " + (endTime - startTime));
//        System.out.println("Parsed pages amount: " + ForkCompute.countParsedPages);
//    }
//}
