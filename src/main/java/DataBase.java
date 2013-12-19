import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase {
    private static String  userName, password, url;
    private static Connection con;
    private static  Statement st;

    private static void getDbProperties() throws IOException {
        try {
            FileInputStream in = new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\main\\resources\\dbproperties.properties"));
            Properties dbProperties = new Properties();
            dbProperties.load(in);
            userName = dbProperties.getProperty("username");
            password = dbProperties.getProperty("password");
            url = dbProperties.getProperty("url");
//            userName="root";
//            url="jdbc:mysql://localhost/rm";
//            password="12345";
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, "Ошибка конфигурационного файла базы данных", ex);
        }
   }
   public static Statement getStatement()  {

            try {
                getDbProperties();
                Class.forName("com.mysql.jdbc.Driver").newInstance();
               con = DriverManager.getConnection(url, userName, password);
               st = con.createStatement();
                return st;
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.)
            } catch ( SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.)
            }
            return st;


        }
    //String & number argument
    public static String addInRow(Object[] data){
        String values="";
        for (int i =0; i<data.length; i++){
            if (data[i] instanceof String) {
                 values+=("'")+((String)data[i])+("'");
            }
            else if (data[i] instanceof Long){
                Long number = (Long)data[i];
                values+=(Long.toString(number));
            }
            if (i<(data.length-1)){
                values+=",";
            }
        }
        return values;
    }
    public static void update (String table, String[] columnName, Object[] data) {
        //инициализация соединения
        if (con==null) { st= getStatement(); }
        String column;

        //приведение колонок к виду понятному для БД в запросе
        column = getColumnName(columnName);

        StringBuilder sbu = new StringBuilder();
        String request = sbu.append("REPLACE INTO ")
                .append(table)
                .append(" (")
                .append(column)
                .append(") VALUES(")
                .append(addInRow(data))
                .append(");")
                .toString();

        try {
            st.executeUpdate(request);
           // System.out.println("Good insert");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.err.println("Bed insert");
        }

    }

    private static String getColumnName(String[] columnName) {
        switch(columnName.length){
            case 0: return "";
            case 1: return columnName[0];
            default:
                String column;
                StringBuilder sbc = new StringBuilder();
                for(int i=0; i<columnName.length-1; i++){
                    column=sbc.append(columnName[i]).append(",").toString();
                }
                column=sbc.append(columnName[columnName.length-1]).toString();
                return column;
        }
    }

    public static void disconect(){
        try {
            st.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    public static void writeLinkInDb(HashSet<String> uniqueUrl){
        String table = "url";
        String [] columnName = {"vacancy_id", "link"};
        Object[] rowData;
        String link;
        Long vacancyId;
        String state= "new";

        Iterator<String> it = uniqueUrl.iterator();
        while(it.hasNext()){
            link = it.next();
            vacancyId=Long.valueOf(link.hashCode());
            rowData = new Object[] {vacancyId, link} ;
            DataBase.update(table, columnName, rowData);
        }
       // disconect();
    }
    public static void writeVacancyInDb(String[] data){
        String table = "vacancy_copy";
        String [] columnName = {"id", "vacancyName", "vacancyDescription", "companyName", "salary", "dateUpdate" };
        update(table, columnName, data);

    }
    public static ResultSet  select(String table, String[] columnName, String command){
        if(st==null){st=getStatement();}
        //приведение колонок к виду понятному для БД в запросе
        String column = getColumnName(columnName);
        StringBuilder sbs = new StringBuilder();
        String request = sbs.append("SELECT ")
                .append(column)
                .append(" FROM ")
                .append(table)
                .append(" ")
                .append(command)
                .append(";")
                .toString();

        try {
            ResultSet result = st.executeQuery(request);
            return result;
            // System.out.println("Good select");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.err.println("Bed select");
        }

        return null;
    }
    public static LinkedHashSet<String> vacancyFromDb(String domain) {

        String table = "vacancy_copy, url";
        String [] columnName = {"url.link"};
        String command = "WHERE url.vacancy_id=vacancy_copy.id AND url.link LIKE '%"+domain+"%'";

        LinkedHashSet<String> result = new LinkedHashSet<>();
        try {
            ResultSet selectResult = select(table, columnName, command );
            if (selectResult==null) {
                System.err.println(Except.BD1.getMessage());
            }
            else{
                while (selectResult.next()){
                    result.add(selectResult.getString(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    public static void dataTransfer(){
        if (st==null){ st=getStatement(); }

        try {
            ResultSet transfer = st.executeQuery("{call vacancycopy}") ;
            transfer.next();
                System.out.println("Процедура vacancycopy() запущена");
            transfer.close();
            disconect();
        } catch (SQLException e) {
            System.out.println("Ошибка SQL в методе dataTransfer()"+e);
        }


    }
    public static void main(String[] args){


            try {
//                String [] columnName = {"id", "vacancyName", "vacancyDescription", "companyName"};
//                String []test = {"6", "Dev", "Description vacancy_", "Componentix26"};
//                update("vacancy", columnName, test);
//                test =new String[] {"7", "Java Junior", "Need Java Junior without expiriense", "Compone__ntix"};
//                update("vacancy", columnName, test);
               // HashSet<String> temp = new HashSet<String>();
               // temp.add("second row");
              //  writeLinkInDb(temp);

//                writeVacancyInDb(new String[]{"123", "НАзвание вакансии", "Описание вакансии", "Название вакансии"});
                long start = new Date().getTime();
                String d = "rabota.ua";
                LinkedHashSet<String> temp = vacancyFromDb(d);

//                for(String t:temp){
//                    System.out.println(t);
//                }
                disconect();
                long time = new Date().getTime() - start;
                System.out.println("База данных: Уже напарсено с "+d+" : "+temp.size());
                System.out.println("Cделано за "+ time/1000.0+" cекунд.");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
       }


       }




