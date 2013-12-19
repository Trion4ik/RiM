import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.Regexp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 21.11.13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class ParseRobotaUa extends Parse {
    /**
     * Объявление полей
     */
    private Document doc;
    private String url;
    //    private static File fileData;
//    private static File catalogRobotaUa;
    private long id;

    /**Статические методы*/
//    static{
//        catalogRobotaUa = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"robota.ua");
//        createCatalog(catalogRobotaUa);
//    }

    /**
     * Конструктор
     */
    public ParseRobotaUa(String aUrl) {
        super(aUrl);
        this.doc = super.getDoc(aUrl);
        url = super.getUrl();
        id = url.hashCode();
        //fileData=new File(catalogWorkUa, Long.toString(id));
        // createFile(fileData);
    }

    /**
     * Методы
     */
    public String parseVacancyName() {
        // System.out.println("Start parseVacancyName()");
        String name;
        String textWithName;
        if (doc != null) {
            Elements h1 = doc.select("h1");
            Elements title = doc.select("title");
            for (Element h : h1) {
                for (Element t : title) {
                    textWithName = t.text();
                    name = h.text();
                    int condition1 = textWithName.indexOf(": работа ");
                    if ((condition1 != -1)) {
                        textWithName = textWithName.substring(condition1).trim();
                        int condition2 = textWithName.replaceAll(" ","").indexOf(name.replaceAll(" ","")); //перед сравнением удаляем все пробелы
                        if (condition2 != -1) {
//                             System.out.println("Название вакансии: "+name);
                            return name;  //в случае удачного парсинга
                        }
                    }
                }
            }
        } else {
            System.err.println("Документ doc не существует. Oшибка словлена в parseVacancyName().  Код ошибки: 006");
        }
        return "";
    }

    public String parseCompanyName() {
        // System.out.println("Start parseCompanyName");
        if (doc != null) {
            Elements title = doc.select("title");

            for (Element e : title) {
                String textWithCompanyName = e.text();
                int condition1 = textWithCompanyName.indexOf(": работа ");
                if (condition1 != -1) {
                    String companyName = textWithCompanyName.substring(0, condition1);
//                       System.out.println("Название компании:"+companyName);
                    return companyName;
                }
            }
        } else {
            System.err.println("Документ doc не существует. Код ошибки: 002");
        }
        return "";
    }

    public String parseVacancyDescription() {
        int start = 0; // позиция начала описания
        String seachText = "<p>&nbsp;</p>";
        String description = "";
        if (doc != null) {
            Elements div = doc.select("div");
            Elements p = div.select("p");
            description = p.outerHtml();
//             System.out.println(description.length()+" Описание вакансии:"+description);

            return description;// в случае удачного парсинга
        } else {
            System.err.println("Документ doc не существует. Oшибка словлена в parseVacancyDescription().  Код ошибки: 005");
        }
        return ""; //в случае ошибок
    }

    public String parseSalary() {
        Elements salary = doc.select("dd.rua-g-clearfix");
        for (Element findSalary : salary) {
            if (findSalary.text().endsWith("грн."))
//                 System.out.println("Найдена зарплата!"+findSalary.text());
                return findSalary.text();
        }
        return "";
    }

    public String parseDate() {
        Elements infoVacancy = doc.select("dd.rua-g-clearfix");
        for (Element findDate : infoVacancy) {
            if (findDate.text().replace(".", "").matches("[0-9]+")) {
                //System.out.println("Дата создания вакансии:"+findDate.text());
                return findDate.text();
            }
        }
        Calendar cal = Calendar.getInstance();
        Formatter f = new Formatter();
        String date = f.format("%td.%tm.%tY", cal, cal, cal).toString();
        //System.out.println("Датa cоздания вакансии не найдена! Присвоена: "+date);
        return date;
    }

    public LinkedHashSet<String> vacancyFilter(LinkedHashSet<String> links) {
        LinkedHashSet<String> newLinks = new LinkedHashSet<String>();
        Iterator<String> link = links.iterator();
        String inspectLink;  // не изменная уникальная ссылка на каждой итерации
        Pattern p = Pattern.compile(".*(/vacancy)([0-9]+)$");
        Matcher m;
        while (link.hasNext()) {
            inspectLink = link.next();
            m = p.matcher(inspectLink);
                if (m.matches()) {
                    newLinks.add(inspectLink);
                }
            }
//        System.out.println("Количество отфильтрованых ссылок: "+(links.size()-newLinks.size()));
//        System.out.println("Количество переданных фильтром ссылок: "+newLinks.size());
        return newLinks;
    }


//    public void writeInFile(String data){
//
//        if(fileData.exists()) {
//            try (FileWriter fw = new FileWriter(fileData, true)) {
//                fw.write(data);
//                fw.flush();
//                fw.close();
//            }
//            catch (IOException e){
//                e.getStackTrace() ;
//            }
//        }
//        else {
//            System.err.print("Oшибка инициализации. Файл с информацией не создан. Код Ошибки: 007");
//        }
//    }

    public LinkedList<String> linkSeachD() {
        LinkedList<String> links = linkSeach();
        return domainFilter(links, "http://rabota.ua");
    }

    public LinkedList<String> linkSeach() {
        LinkedList<String> links = new LinkedList<String>();
        String temp;
        Elements el;
        if (doc != null) {
            el = doc.select("a[href]");
            for (Element e : el) {
                temp = rightUrl(e.attr("href"), url);
                if (temp.equals("")) {
                } else {
                    links.add(temp);
                }
            }
            return links; // в случае если все удалось пропарсить
        } else {
            System.err.println("Ошибка инициализации! Запуск метода Parse.linkSeach() до инициализации");
        }
        return links; // в случае ошибок парсинга
    }

    public static int countRegionVacancy(String url) {
        int vacancyCount = 0;
        String stringWithCount = "";
        ParseRobotaUa p = new ParseRobotaUa(url);
        Document doc = p.getDoc(url);
        Elements span = doc.select("span[id]");
        for (Element s : span) {
            if (s.attr("id").equals("centerZone_vacancyList_ltCount")) {
                stringWithCount = s.text();
                stringWithCount = stringWithCount.replaceAll("[^0-9]", "");
                System.out.println("На сайте rabota.ua "
                        .concat(stringWithCount)
                        .concat(" вакансий"));
                vacancyCount = Integer.parseInt(stringWithCount);
                return vacancyCount;
            }
        }
        return vacancyCount;
    }

    public static LinkedHashSet regionSeachVacancy(String urlStart) {
        String sufixRabotaUa = "&pg=";
        LinkedHashSet<String> regionLinks = new LinkedHashSet<String>(); // храним все ссылки на страницы вакансий региона
        LinkedHashSet<String> links = new LinkedHashSet<String>();  //рабочая коллекция для манипуляций с данными
        ParseRobotaUa p = new ParseRobotaUa(urlStart); //обєкт для доступа к методам класса ParseRobotaUa
        links.addAll(p.linkSeachD());
        links = p.vacancyFilter(links);
        int i = 2; //cчетчик страниц с списками вакансий в цикле сразу будем использовать 2-ю страницу
        while (!links.isEmpty()) {  //парсим все страницы со списками вакансий
            regionLinks.addAll(links);
            System.out.println("Сейчас парсим: "+urlStart+sufixRabotaUa+i);
            p = new ParseRobotaUa(urlStart + sufixRabotaUa + i);
            links = p.vacancyFilter(new LinkedHashSet<String>(p.linkSeachD()));
            i++;

        }
        System.out.println("Уже найдены ссылки на вакансии сайта robota.ua: " + regionLinks.size());
        return regionLinks;
    }
//    public String rightUrl(String someUrl){
//        if(someUrl.indexOf("'")!=-1){
//            return "";
//        }
//        if(someUrl.indexOf("http:")!=-1){
//            return someUrl;
//        }
//
//        else{
//            if (someUrl.indexOf("/")==0){
//                return "http://robota.ua".concat(someUrl);
//            }
//            else {
//                return "http://robota.ua/".concat(someUrl);
//            }
//        }
//    }


    public static void main(String[] args) throws IOException {
//        String urlStart = "http://www.work.ua/jobs/?notitle=0&snowide=0&search=&days=125&searchTitle=1&swide=1&region=33&category=1&advs=0&ss=1" ;
//        LinkedHashSet<String> result= domainSeachLink(urlStart, 5);
//        result = vacancyFilter(result);
//        for(String next:result) {
//            ParseRobotaUa p = new ParseRobotaUa("http://rabota.ua");
//            String vacancy = p.parseVacancyName();
//            System.out.println(vacancy);
//            String company =  p.parseCompanyName();
//            String salary = p.parseSalary();
//            String date = p.parseDate();
//              System.out.println(ParseRobotaUa.countRegionVacancy("http://rabota.ua/jobsearch/vacancy_list?regionId=5"));
        regionSeachVacancy("http://rabota.ua/jobsearch/vacancy_list?regionId=5");

//            System.out.println(company);
//            String description =  p.parseVacancyDescription();
//        }
//        String[] array = {"http://www.work.ua/jobs/1465929/", "http://www.work.ua/jobs/1454546/"};
//        for (String a: array) {
//            ParseWorkUa p = new ParseWorkUa(a);
//            System.out.println(p.parseDate());
//        }

    }
}
