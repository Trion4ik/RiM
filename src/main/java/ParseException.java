import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 10.12.13
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class ParseException extends Parse {
    public ParseException(String aUrl){
       super(aUrl);
       System.err.printf("Создано исключение при парсинге страницы %s", aUrl);
    }

    public static String getMessage (Except ex){
        StringBuilder sb = new StringBuilder();
        return sb.append(ex.getMessage())
                .append(" Код ошибки: ")
                .append(ex.getCode())
                .toString();
    }
    @Override
    public LinkedList<String> linkSeachD() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String parseCompanyName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String parseVacancyDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String parseVacancyName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String parseSalary() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public LinkedHashSet<String> vacancyFilter(LinkedHashSet<String> links) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String parseDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
