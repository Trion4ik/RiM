import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class ParseWorkUa extends Parse {
    /**Объявление полей*/
    private Document doc;
    private String url;
    private static File fileData;
    private static File catalogWorkUa;
    private long id;

    /**Статические методы*/
//    static{
//        catalogWorkUa = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"work.ua");
//        createCatalog(catalogWorkUa);
//    }

    /**Конструктор*/
    public ParseWorkUa(String aUrl){
        super(aUrl);
        this.doc=super.getDoc(aUrl);
        url = super.getUrl();
        id= url.hashCode();
        //fileData=new File(catalogWorkUa, Long.toString(id));
       // createFile(fileData);
    }

    /**Методы*/

//    public void parseAll() {
//        String[] vacancyRow= new String[5];
//        if(doc!=null){
//            vacancyRow[1]= parseVacancyName();
//            if(checkData(vacancyRow[1])) {
//                vacancyRow[3]= parseCompanyName();
//                if(checkData(vacancyRow[3])) {
//                    vacancyRow[2]= parseVacancyDescription();
//                    if(checkData(vacancyRow[2])){
//                        vacancyRow[0]=Long.toString(id);
//                        vacancyRow[4]=parseSalary();
//                        DataBase.writeVacancyInDb(vacancyRow);
//                    }
//                }
//            }
//        }
//        else{
//            System.err.println("Ошибка парсинга страницы " + url + ". Код ошибки: 001") ;
//        }
//    }
    public String parseSalary(){
        Elements salary = doc.select("p.salaryJob");
        for(Element findSalary: salary) {
//            System.out.println("Найдена зарплата!");
           return findSalary.text();
        }
        return "";
    }
    public String parseDate(){
        String date;
        String deleteInfo = "вакансия от";
        Elements salary = doc.select("div.normalJobDate");
        for(Element findDate: salary) {
//            System.out.println("Найдена дата:");
            date = findDate.text();
            if (date.startsWith(deleteInfo)){
                date = date.substring(deleteInfo.length()+1);
                return date;
            }

        }
        return "";
    }
    public
    String parseCompanyName(){
       // System.out.println("Start parseCompanyName");
        if(doc!=null)         {
            Elements cn=doc.select("a[title]");

            for(Element e: cn) {
                String title = e.attr("title");
                String text = e.text();
                 if(title.indexOf(text)!=-1&&title.indexOf("Работа в")!=-1){
                    // writeInFile(text);
                   //  System.out.println("Назва компанії: "+text);
                     return text;
                }
            }
        } else{
            System.err.println("Документ doc не существует. Код ошибки: 002");
        }
       return "";
    }
    public String parseVacancyDescription(){
        int start; // позиция начала описания вакансии
        int end;  // позиция конца описания вакансии
        String seachText = "<span>Описание вакансии</span>";
        String description;
        if(doc!=null ){
           // System.out.println("Start parseVacancyDescription()");
            Elements h2=doc.select("h2");
            for(Element e: h2 ) {
                String seachTemp = e.outerHtml();
                if(seachTemp.indexOf(seachText)!=-1){
                    String html = doc.outerHtml();
                    start =  html.indexOf("<h2>");
                    if(start!=-1){
                        description = html.substring(start);
                        end = description.indexOf("</div>");
                        if(end!=-1) {
                            description=description.substring(0, end);
                           // writeInFile(description);
                           // System.out.println("Описание вакансии "+id+ " записано в файл");
                            return description; // в случае удачного парсинга
                        }
                        else {
                            System.err.println("Oшибка окончания описании вакансии. "+url+" Код ошибки:003");
                        }
                    }
                    else {
                        System.err.println("Oшибка начала описания вакансии. "+url+" Код ошибки:004");

                    }
                }
            }
        }
        else{
            System.err.println("Документ doc не существует. Oшибка словлена в parseVacancyDescription().  Код ошибки: 005");
        }
        return ""; //в случае ошибок
    }
    public String parseVacancyName(){
        String name;
        String textWithName;
        if(doc!=null) {
           // System.out.println("Start parseVacancyName()");
            Elements h1 = doc.select("h1");
            Elements meta = doc.select("meta[name]");
            for(Element h: h1) {
                for(Element e: meta){
                    textWithName=e.attr("content");
                    name=h.text();
                    if (textWithName.indexOf(name)!=-1){
                        //System.out.println("Название вакансии: "+name);
                        //writeInFile(name);
                        return name;
                    }
                }
            }
        }
        else{
            System.err.println("Документ doc не существует. Oшибка словлена в parseVacancyName().  Код ошибки: 006");
        }
        return "";
    }
    public void writeInFile(String data){

        if(fileData.exists()) {
            try (FileWriter fw = new FileWriter(fileData, true)) {
                fw.write(data);
                fw.flush();
                fw.close();
            }
            catch (IOException e){
                e.getStackTrace() ;
            }
        }
        else {
            System.err.print("Oшибка инициализации. Файл с информацией не создан. Код Ошибки: 007");
        }
    }

    public LinkedList<String> linkSeachD(){
        LinkedList<String> links = this.linkSeach();
        return domainFilter(links, "http://work.ua");
    }
    public LinkedList<String> linkSeach(){
        LinkedList<String> links= new LinkedList<String>();
        String temp;
        Elements el;
        if (doc!=null){
            el= doc.select("a[href]");
            for (Element e: el){
                temp=rightUrl(e.attr("href"), "http://work.ua");
                if (!temp.equals("")){
                    links.add(temp);
                }
            }

            return links; // в случае если все удалось пропарсить
        }
        else {
            System.err.println("Ошибка инициализации! Запуск метода Parse.linkSeach() до инициализации");
        }
        return links; // в случае ошибок парсинга
    }
//    public String rightUrl(String someUrl){
//        if(someUrl.indexOf("'")!=-1){
//            return "";
//        }
//        if(someUrl.indexOf("http:")!=-1){
//             return someUrl;
//        }
//        else{
//            if (someUrl.indexOf("/")==0){
//                return "http://work.ua".concat(someUrl);
//            }
//            else {
//                return "http://work.ua/".concat(someUrl);
//            }
//        }
//    }
    private static LinkedList<String> addReverseLast(LinkedHashSet<String> set, int numberLastElements){
        Iterator<String>last = new LinkedList<String>(set).descendingIterator();
        LinkedList <String> lastElements = new LinkedList<String>();
        int i=0; //cчетчик добавленных елементов
        while(last.hasNext() && i <= numberLastElements){
            lastElements.add(last.next())  ;
            i++;
        }
        return  lastElements;
    }
//    public static LinkedHashSet<String> domainSeachLink(String startUrl, int parseUrl){
//        // DataBase db = new DataBase();
//        long startTime = new Date().getTime();
//        double tempTime;// время на 1 круг в минутах
//        double time; // время выполнения в часах
//
//        int dublicate=0; // cчетчик дублирующихся ссылок
//        LinkedHashSet<String> result = new LinkedHashSet<>();  //сохраняем результаты парсинга
//        LinkedList<String> unique; //храним уникальные ссылки
//        LinkedList<String> notUnique;
//        notUnique = new ParseWorkUa(startUrl).linkSeachD();
//        result.addAll(notUnique);
//        notUnique.clear();
//        //возвращается LinkedList c всеми елементами result или parseUrl в зависимости от того что меньше
//        unique=addReverseLast(result, parseUrl);
//        int i=1; // счетчик пропарсенных страниц
//        while(i<parseUrl)
//        {
//            for(String it: unique){
//                // Реальная работа метода Старт
//                notUnique.addAll(new ParseWorkUa(it).linkSeachD());
//
//                // Реальная работа метода
//                System.out.println(i+" Parse: "+it);
//                i++;
//                System.out.println("Found link: " + notUnique.size());
//                if(i>parseUrl) break;
//            }
//            result.addAll(notUnique); //всю пропарсенную информацию заносим в SET List
//            unique.clear();// Очищаем хранилище уникальных ссылок
//            unique = addReverseLast(result, 1000); // Наполняем хранилище уникальных ссылок последней тысячей уникальных
//            dublicate += (notUnique.size()-result.size());
//            System.out.println("Clear "+dublicate+" dublicate link");
//            tempTime = (new Date().getTime()- startTime)/1000.00;   //Время круга в секундах
//            System.out.println("Parsing time: "+ tempTime+" second");
//            notUnique.clear();
//        }
//        long dbTimeStart=new Date().getTime();
//        DataBase.writeLinkInDb(result);
//        long dbTimeEnd=new Date().getTime();
//        System.out.println("Время добавления в БД"+result.size()+": "+(dbTimeEnd-dbTimeStart));
//
//        time = (new Date().getTime()- startTime)/1000;
//
//        System.out.println("Parsing time "+time+" second");
//        System.out.println("One second parse and "+(unique.size()/time)+" unique links find");
//        return result;
//    }
    public LinkedHashSet<String> vacancyFilter(LinkedHashSet<String> links){
        LinkedHashSet<String> newLinks = new LinkedHashSet<String>();
        Iterator<String> link = links.iterator();
        String tempLink; //линк веб страницы, который мы изменяем для анализа на каждой итерации
        String inspectLink;  // не изменная уникальная ссылка на каждой итерации
        int startDesignSeach; // индекс начала конструкции для поиска выражения по которому фильтруем
        Pattern pattern = Pattern.compile(".*(/jobs/[0-9,/]+)$");  //регулярное выражение для фильтрации
        Matcher m;
        while(link.hasNext()){
            inspectLink = tempLink = link.next();
            m = pattern.matcher(tempLink);
            if(m.matches()){
                newLinks.add(inspectLink); //добавляем в коллекцию только те ссылки которые прошли фильтрацию
            }
        }
        System.out.println("Количество добавленных ссылок "+newLinks.size());
            return newLinks;
      }
    public static int countRegionVacancy(String url){
        int vacancyCount=0;
        String stringWithCount="";
        ParseRobotaUa p = new ParseRobotaUa(url);
        Document doc = p.getDoc(url);
        Elements h2 = doc.select("h2");
        for (Element i: h2){
            stringWithCount = i.text();
            stringWithCount=stringWithCount.replaceAll("[^0-9]", "");
            if(!stringWithCount.equals("")){

                System.out.println("На сайте work.ua "
                        .concat(stringWithCount)
                        .concat(" вакансий"));
                vacancyCount=Integer.parseInt(stringWithCount);
                return vacancyCount;
            }
        }
        return vacancyCount;
    }
    public static LinkedHashSet regionSeachVacancy(String url){
        String sufix ="&page=";
        String urlStart = url.endsWith("/")? url : url.concat("/");
        if(!urlStart.endsWith("/")) urlStart.concat("/"); //приводим стартовую ссылку к нужному виду
        LinkedHashSet<String> regionLinks=new LinkedHashSet<String>(); // храним все ссылки на страницы вакансий региона
        LinkedHashSet<String> links = new LinkedHashSet<String>();  //рабочая коллекция для манипуляций с данными
        ParseWorkUa p = new ParseWorkUa(urlStart); //обєкт для доступа к методам класса ParseRobotaUa
        links.addAll(p.linkSeach());
        links= p.vacancyFilter(links);
        int i = 2; //cчетчик страниц с списками вакансий в цикле сразу будем использовать 2-ю страницу
        while(!links.isEmpty()){  //парсим все страницы со списками вакансий
            regionLinks.addAll(links);
            System.out.println("Сейчас парсим: "+urlStart+sufix+i);
            p = new ParseWorkUa(urlStart+sufix+i);
            links= p.vacancyFilter(new LinkedHashSet<String>(p.linkSeachD()));
            i++;

        }
        System.out.println("Уже найдены ссылки на вакансии сайта work.ua: "+regionLinks.size());
        return regionLinks;
    }
    public static void main(String[] args)throws IOException{
        System.out.println(ParseWorkUa.countRegionVacancy("http://www.work.ua/jobs/?search=&ss=1&region=33"));
        regionSeachVacancy("http://www.work.ua/jobs/?search=&ss=1&region=33");
//        String urlStart = "http://www.work.ua" ;
//        LinkedHashSet<String> result= DomenParse.domainSeachLink(urlStart, 100000);
//        result = new ParseWorkUa(urlStart).vacancyFilter(result);
//        for(String next:result) {
//           ParseWorkUa p = new ParseWorkUa(next);
//            p.parseAll();
//        }
//        String[] array = {"http://www.work.ua/jobs/1465929/", "http://www.work.ua/jobs/1454546/"};
//        for (String a: array) {
//            ParseWorkUa p = new ParseWorkUa(a);
//            System.out.println(p.parseDate());
//        }

    }
}
