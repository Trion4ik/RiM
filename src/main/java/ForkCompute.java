//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.RecursiveAction;
//import java.util.concurrent.RecursiveTask;
//
//public class ForkCompute extends RecursiveTask<ArrayList> {
//    final static int threshold = 10;
//    protected static int countParsedPages = 0;
//    private ArrayList<String> resList = new ArrayList<>();
//
//    ArrayList<String> arr;
//    int start;
//    int end;
//
//    public ForkCompute(ArrayList<String> arrList, int start, int end) {
//        this.arr = arrList;
//        this.start = start;
//        this.end = end;
//    }
//
//    protected ArrayList<String> compute() {
//        if ((end - start) < threshold) {
//
//            for (int i = start; i < end; i++) {
//                  System.out.println("передаємо: " + (arr.get(i)));
////                  resList.addAll(Parser.parse(arr.get(i)));
//                  countParsedPages++;
//                  System.out.println("розмір resList після : " +resList.size());
//            }
//
//        } else {
//            int middle = (start + end) / 2;
//
//            ForkCompute subTaskA = new ForkCompute(arr, start, middle);
//            ForkCompute subTaskB = new ForkCompute(arr, middle, end);
//
//            subTaskA.fork();
//            subTaskB.fork();
//
//            resList.addAll(subTaskA.join());
//            resList.addAll(subTaskB.join());
//        }
//        return resList;
//    }
//}