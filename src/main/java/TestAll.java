import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 19.12.13
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
public class TestAll {
    public static void main(String[] args){
//        ParseWorkUa p  = new ParseWorkUa ("http://www.work.ua/jobs/?search=&ss=1&region=33");
//        LinkedHashSet<String> links = new LinkedHashSet<>(p.linkSeach());
//        LinkedHashSet<String> newLinks = new LinkedHashSet<String>();
//        Iterator<String> link = links.iterator();
//        String tempLink; //линк веб страницы, который мы изменяем для анализа на каждой итерации
//        String inspectLink;  // не изменная уникальная ссылка на каждой итерации
//        int startDesignSeach; // индекс начала конструкции для поиска выражения по которому фильтруем
//        Pattern pattern = Pattern.compile(".*(/jobs/[0-9,/]+)$");
//        Matcher m;
//        while(link.hasNext()){
//            inspectLink = tempLink = link.next();
//                m = pattern.matcher(tempLink);
//                if(m.matches()){
//                    newLinks.add(inspectLink);
//                    System.out.println("Прошло через фильтр:"+inspectLink);
//                } else  System.out.println("Не прошло через фильтр:"+inspectLink);
//
//            }
//
//            System.out.println("Количество добавленных ссылок "+newLinks.size());
//            System.out.println("Количество отфильтрованых ссылок:"+(links.size() - newLinks.size()));

         String [] temp = {
        "http://work.ua/jobs/726723/",
        "http://work.ua/jobs/1473832/",
        "http://work.ua/jobs/1459136/"};
//        checkData false:http://rabota.ua/company810307/vacancy5153049
//        checkData false:http://rabota.ua/company1482581/vacancy5115506
//        checkData false:http://rabota.ua/company2132558/vacancy5363188
//        checkData false:http://work.ua/jobs/726723/
//        checkData false:http://work.ua/jobs/1473832/
//        checkData false:http://work.ua/jobs/1471906/
//        checkData false:http://work.ua/jobs/1459136/
//        checkData false:http://work.ua/jobs/1418841/
//        checkData false:http://work.ua/jobs/1084152/

        for (String t : temp){
            ParseRobotaUa p = new ParseRobotaUa(t);
            p.parseAll();
        }
    }
}
