/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 10.12.13
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
public enum Except{
    E1("Класс Parse не иницилизирован", "001"),
    E2("Парсинг данного домена не поддерживается", "002"),
    BD1("Oшибка выборки из БД", "003");

    private final String message;

    public String getCode() {
        return code;
    }

    private final String code;
    Except(String aMessage, String aCode){
        message = aMessage;
        code = aCode;
    }

    public String getMessage() {
        return message;
    }
}
