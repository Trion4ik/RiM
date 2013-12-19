/**
 * Это абстрактный базовый класс парсеров.
 * @author Maksimov Roman
 * @version 0.0.1
 * Date: 21.11.13
 * Time: 12:12
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.Regexp;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class Parse {
    /**Объявление полей*/
    private Document doc;
    private String url;
    private boolean initialization = false;
    private long id;

    /**Статические методы*/
//    static{
//        File catalogData = new File(System.getProperty("user.dir")+File.separator+"data");
//        createCatalog(catalogData);
//    }

    /**Конструктор*/
    public Parse(String aUrl){
        url=aUrl;
        init(url);
        id = url.hashCode();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**Методы*/


    public Document getDoc(String url)throws NullPointerException{
       return doc;
     }
    protected boolean checkData(String data){
            if (data.equals("")){
                System.out.println("checkData false:" + url);
                return false;
            }
        return true;
    }
    // создаем дерево каталогов для складирования пропарсеной информации
    protected static void createCatalog(File catalog){

            if(!catalog.exists()||!catalog.isDirectory()){
                catalog.mkdirs();
                System.out.println("Каталог "+catalog.getAbsolutePath()+" создан");
            }
            else{
                System.out.println("Каталог "+catalog+" уже существует");
            }

    }
    protected void createFile(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
                System.out.println("Файл "+file.getAbsolutePath()+" создан");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String quotesRemove (String data){
        return (data.replaceAll("'", ""));
    }

    public void parseAll() {
        String[] vacancyRow= new String[6];
        if(doc!=null){
            vacancyRow[1]= quotesRemove(parseVacancyName());
            if(checkData(vacancyRow[1])) {
                vacancyRow[3]= quotesRemove(parseCompanyName());
                if(checkData(vacancyRow[3])) {
                    vacancyRow[2]= quotesRemove(parseVacancyDescription());
                    if(checkData(vacancyRow[2])){
                        vacancyRow[0]=Long.toString(id);
                        vacancyRow[4]=quotesRemove(parseSalary());
                        vacancyRow[5]=quotesRemove(parseDate());
                        DataBase.writeVacancyInDb(vacancyRow);
                        System.out.println("Вакансия: "+url+" записана в БД");
                    }
                }
            }
        }
        else{
            System.err.println("Ошибка парсинга страницы " + url + ". Код ошибки: 001") ;
        }
    }

    private void init (String aUrl){
        initialization = true;
        try{
             doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla")
                    .get();
        }
        catch (IOException e){
            if ( e.getMessage().contains("Unhandled content type") ||
                    e.getMessage().contains("Premature EOF") ||
                    e.getMessage().contains("Read timed out") ||
                    e.getMessage().contains("Malformed URL") ||
                    e.getMessage().contains("403 error loading URL") ||
                    e.getMessage().contains("404 error loading URL") ||
                    e.getMessage().contains("405 error loading URL") ||
                    e.getMessage().contains("500 error loading URL") ||
                    e.getMessage().contains("503 error loading URL") ) {
                System.err.println("Внимание! Страницу "+url+" пропарсить не удалось! "+ e.getMessage());

            }
        }
    }
    public String rightUrl(String someUrl, String aUrl){
        if(someUrl.indexOf("'")!=-1){
            return "";
        }
        if(someUrl.indexOf("http:")!=-1){
            return someUrl;
        }
        else if(aUrl.indexOf(someUrl)>-1){
            return "";
        }
        else{
            String temp = aUrl.concat("/").concat(someUrl);
            if (temp.lastIndexOf("//")>8){
                temp = temp.replace("///", "/").replace("//", "/"); // убираем двойные и тройные "/"
                temp = temp.replace(":/", "://");  // возвращаем "/" в http://
            }
            return temp;
        }
    }

    public LinkedList<String> domainFilter(LinkedList<String> links, String domain){
        LinkedList<String> domainLinks = new LinkedList<String>();

        for (String l : links){
            if(l.startsWith("http://www.")) {
                l = l.replace("http://www.", "http://");
            }
            if(l.startsWith(domain)){
                domainLinks.add(l);
            }
        }
        return domainLinks;
    }


    //методы созданы для расширения в классах наследниках
    public abstract LinkedHashSet<String> vacancyFilter(LinkedHashSet<String> links);
    public abstract LinkedList<String> linkSeachD();
    public abstract String parseCompanyName();
    public abstract String parseVacancyDescription();
    public abstract String parseVacancyName();
    public abstract String parseDate();
    public abstract String parseSalary();

    }


