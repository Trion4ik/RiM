import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.*;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DomenParse {
    //каталоги в которых хранится вся пропарсенная информация
   public DomenParse(String url){

    }
    public static Parse getParseClass(String url){
        if(url.contains("rabota.ua")){
            return new ParseRobotaUa(url);
        }
        else if (url.contains("work.ua")){
            return new ParseWorkUa(url);
        }
        return new ParseException(url);
    }
    public static LinkedHashSet<String> domainSeachLink(String startUrl, int numberUrl){
        // DataBase db = new DataBase();
        long startTime = new Date().getTime();
        double tempTime;// время на 1 круг в минутах
        double time; // время выполнения в часах
        LinkedHashSet<String> result = new LinkedHashSet<>();  //сохраняем результаты парсинга
        LinkedList<String> unique ; //храним уникальные ссылки
        LinkedList<String> notUnique= new LinkedList<String>();
        //наполняем ссылками
        Parse t =   getParseClass(startUrl);
        result.addAll(t.linkSeachD());
        int resultSize=result.size(); // cчетчик уникальных ссылок
        //возвращается LinkedList c всеми елементами result или numberUrl в зависимости от того что меньше
        unique=addReverseLast(result, numberUrl);
        int i=1; // счетчик пропарсенных страниц
        while(i<numberUrl)
        {
            for(String it: unique){
                // Реальная работа метода Старт
                notUnique.addAll(getParseClass(it).linkSeachD());
                // Реальная работа метода Конец
                System.out.println(i+" Parse: "+it);
                i++;
                if(i>numberUrl) break;
            }
            unique.clear();// Очищаем хранилище уникальных ссылок
            result.addAll(notUnique); //всю пропарсенную информацию заносим в SET List
            notUnique.clear();
            unique = addReverseLast(result, 1000); // Наполняем хранилище уникальных ссылок последней тысячей уникальных
            tempTime = (new Date().getTime()- startTime)/1000.00;   //Время круга в секундах
            System.out.printf("Parsing time: %s second", tempTime);
            System.out.println("Found link: "+ resultSize);
            System.out.println("New link in term: " + (result.size()-resultSize));
            resultSize = result.size();

        }
        long dbTimeStart=new Date().getTime();
        DataBase.writeLinkInDb(result);
        long dbTimeEnd=new Date().getTime();
        System.out.println("Время добавления в БД"+result.size()+": "+(dbTimeEnd-dbTimeStart));

        time = (new Date().getTime()- startTime)/1000;

        //System.out.println("Parsing time "+time+" second");
       // System.out.println("One second parse and "+(unique.size()/time)+" unique links find");
        return result;
    }
    private static LinkedList<String> addReverseLast(LinkedHashSet<String> set, int numberLastElements){
        Iterator<String>last = new LinkedList<String>(set).descendingIterator();
        LinkedList <String> lastElements = new LinkedList<String>();
        int i=1; //cчетчик добавленных елементов
        while(last.hasNext() && i <= numberLastElements){
            lastElements.add(last.next())  ;
            i++;
        }
        return  lastElements;
    }
    public static void regionSeach(String urlStart){
        String sufixRabotaUa ="&pg=";
        LinkedHashSet<String> regionLinks=new LinkedHashSet<String>(); // храним все ссылки на страницы вакансий региона
        LinkedHashSet<String> links;  //сохраняем все напарсенные ссылки на вакансии
        links=domainSeachLink(urlStart, 1); //парсим стартовую страницу

    }
    public static LinkedHashSet<String> regionVacancy(){
//        String urlRabotaUa = "http://rabota.ua/jobsearch/vacancy_list?regionId=5";
//        String urlWorkUa = "http://www.work.ua/jobs/?search=&ss=1&region=33";
        String urlRabotaUa = "http://rabota.ua/jobsearch/vacancy_list?regionId=18";
        String urlWorkUa = "http://www.work.ua/jobs/?region=48";
        int countRabotaUa = ParseRobotaUa.countRegionVacancy(urlRabotaUa);
        int countWorkUa = ParseWorkUa.countRegionVacancy(urlWorkUa);
        LinkedHashSet<String> allVacancy = ParseRobotaUa.regionSeachVacancy(urlRabotaUa);
        allVacancy.addAll(ParseWorkUa.regionSeachVacancy(urlWorkUa));
        int temp=allVacancy.size();
        allVacancy.removeAll((DataBase.vacancyFromDb("work.ua")));
        System.out.println("Сверено с базой данных и найдено по сайту work.ua " + (temp - allVacancy.size()) + " совпадений.");
        temp = allVacancy.size();
        allVacancy.removeAll(DataBase.vacancyFromDb("rabota.ua"));
        System.out.println("Сверено с базой данных и найдено по сайту rabota.ua " + (temp - allVacancy.size()) + " совпадений.");
        System.out.println(allVacancy.size());
        return  allVacancy;
    }
    public static void regionVacancyParse(){
        LinkedHashSet<String> vacancies = regionVacancy();
        DataBase.writeLinkInDb(vacancies);
        System.out.println("Cтарт парсинга!");
        long startTime = System.currentTimeMillis();
        for(String v: vacancies){
            getParseClass(v).parseAll();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Конец парсинга. Время парсинга: "+(endTime-startTime));
    }

    public static void main(String[] args)throws IOException{
//        DataBase.dataTransfer();
//          regionVacancyParse();
        long start = System.currentTimeMillis();
             regionVacancyParse();
        long end = System.currentTimeMillis();
        System.out.println("Время парсинга "+(end-start));
//       String urlStart = "http://www.work.ua/jobs/?search=&ss=1&region=33" ;
//       long timeStart = new Date().getTime();
//       LinkedHashSet<String> result= domainSeachLink(urlStart, 300);
//       // Parse t = getParseClass(urlStart);
//        result = getParseClass(urlStart).vacancyFilter(result);
//        for(String next:result) {
//            System.out.println(next);
//            getParseClass(next).parseAll();
//        }
//       long timeWork = new Date().getTime() - timeStart;
//        System.out.println("Время работы парсера:" + timeWork/1000+" секунд");

    }

}
