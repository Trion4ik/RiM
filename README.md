RiM
===

Content parser sites: www.work.ua &amp; www.rabota.ua

Документация проэкта на русском языке!

Документация по RIM API 1.5
RIM API состоит из следующих классов: Parse, ParseWorkUa, ParseRobotaUa, ParseExeption, DomenParse, DataBase и enum класса Exept. Классы написаны с использованием JDK 1.7.
Для работы приложения так же требуется база данных MySQL 5.5 или MariaDb 5.5 c названием rabotamolodi и таблицами url, vacancy_copy, vacancy.  Данные для настройки доступа к базе данных находятся в файле dbproperties.properties, относительный путь к которому прописан в методе DataBase.getDbProperties(). 
Для управления всей системой используется класс DomenParse, который управляет всеми возможностями API.
Класс Parse
Класс Parse базовый класс всех парсеров (ParseWorkUa, ParseRobotaUa, ParseExeption) включает в себя реализацию всех методов общих для парсеров таких как  getUrl(), setUrl(String url), getDoc(String url),   checkData(String data), createCatalog(File catalog), createFile(File file), quotesRemove (String data), rightUrl(String someUrl, String aUrl), domainFilter(LinkedList<String> links, String domain). 
Также класс имеет private метод init (String aUrl), который проводит инициализацию полей библиотеки JSOUP(создает объект класса Document ), а все классы наследники уже не могут переопределять этот метод, а лишь используют результаты проведенной инициализации, которая осуществляется в конструкторе базового класса Parse.
Для каждой страницы сайта, которая парситься создается объект наследуемый от базового класса Parse. У котрого инициализированы поля url – ссылка на страницу сайта,  id – хеш код url, doc – Document объект из библиотеи JSOUP (этот объэкт дает возможность использовать все возможности этой бибиотеки).
Методы класса Parse:
parseAll – главный метод всех парсеров, создан для парсинга всех полей вакансии(зарплата, дата создания вакансии, название вакансии, название фирмы, описания вакансии), проверки данных парсинга и в случае их корректности записи данных в базу данных.
getUrl()– возвращает ссылку на страницу сайта для которой создан объект типа Parse;
getDoc(String url) – возвращает объект org.jsoup.nodes.Document;
checkData(String data) – проверяет строку на пустоту и возвращает true если строка не пустая и false если пустая. 
createCatalog(File catalog) (метод не используется с версии PiM 1.3) - создает дерево каталогов для складирования пропарсеной информации.
createFile(File file) (метод не используется с версии PiM 1.3) - создает файл для хранения пропарсеной информации.
quotesRemove (String data) – удаляет из входной строки все символы ( ' ) так как они потом нарушают валидность команд при занесении в базу данных.
rightUrl(String someUrl, String aUrl ) – из ссылки страницы которую нашел парсер someUrl и ссылки страницы с которой нашел ссылку формируется и возвращается методом корректная ссылка для дальнейшего парсинга (такого формата http://example.com/temp1.html).
domainFilter(LinkedList<String> links, String domain) – из списка ccылок он возвращает только список ссылок соответствующего домена.
Класс ParseWorkUa и Класс ParseRobotaUa
Классы ParseWorkUa и ParseRobotaUa идентичны по своей сути, но первый создан для парсирна страниц сайта http://work.ua, a другой для http://rabota.ua. Дальше в тексте будет употребляться просто слово сайт, для каждого класса это сайт будет свой.
  Эти классы имеют такие методы  vacancyFilter(LinkedHashSet<String> links),
linkSeachD(), parseCompanyName(),  parseVacancyDescription(), parseVacancyName(), parseDate(),  parseSalary(), countRegionVacancy(String url) и regionSeachVacancy(String urlStart).
Последних два метода countRegionVacancy(String url) и regionSeachVacancy(String urlStart) static методы, для доступа к которым не обязательно создание объектов класса.
Методы классов ParseWorkUa и ParseRobotaUa:
static countRegionVacancy(String url) – принимает ссылку из страницы поиска сайта для поиска работы по определенному региону (например для Винницы сайта work.ua это - http://work.ua/jobs/?search=&ss=1&region=33
rabota.ua - http://rabota.ua/jobsearch/vacancy_list?regionId=5 )  и возвращает целое число – количество вакансий по данному региону;
static regionSeachVacancy(String urlStart) – принимает ссылку из страницы поиска сайта для поиска работы по определенному региону(например для Винницы сайта work.ua это - http://work.ua/jobs/?search=&ss=1&region=33
rabota.ua - http://rabota.ua/jobsearch/vacancy_list?regionId=5 ) и возвращает LinkedHashSet коллекцию заполненную ссылками на вакансии сайта этого региона;
vacancyFilter(LinkedHashSet<String> links) – принимая LinkedHashSet коллекцию с ссылками на страницы сайта, возвращает LinkedHashSet коллекцию в которой ссылки только на страницы с вакансиями, отбрасывая все остальные из входной коллекции;
linkSeach() – возвращает все ссылки найденные на странице сайта для которого создан объект вызвавший метод;
linkSeachD() – возвращает только ссылки сайта, исключая ссылки на другие ресурсы, найденные на странице сайта для которого создан объект вызвавший метод;
parseCompanyName(),  parseVacancyDescription(), parseVacancyName(), parseDate(),  parseSalary() – возвращают строковое значение названия компании, описания вакансии, названия вакансии, дня создания вакансии и зарплаты по вакансии соответсвенно;
Класс DataBase
Класс DataBase – класс реализующий доступ к базе данных robotamolodi, а конкретно к таблицам url и vacancy_copy. Все методы класса статические.
Закрытые методы private: getDbProperties(), getStatement(), update (String table, String[] columnName, Object[] data), select(String table, String[] columnName, String command).
Открытые методы public: addInRow(Object[] data),  getColumnName(String[] columnName), disconect(), writeLinkInDb(HashSet<String> uniqueUrl), writeVacancyInDb(String[] data), vacancyFromDb(String domain), dataTransfer().
Методы класса DataBase
private getDbProperties() – получает настройки базы данных;
private getStatement() – возвращает объект типа Statement;
private select(String table, String[] columnName, String command) – реализует доступ к информации из базы данных таблицы table, колонок columnName, с дополнительными условиями command;
private update (String table, String[] columnName, Object[] data) – реализует запись в базу данных таблицы table, колонок columnName, информации из мас сива data)
addInRow(Object[] data) - формирует из массива данных строку для команды в SQL
getColumnName(String[] columnName) - формирует из массива названия колонок строку для команды в SQL
disconect() – команда закрывающая соединение с базой данных и открытые Statement объекты.
writeLinkInDb(HashSet<String> uniqueUrl) – записывает ссылки на вакансии в базу данных в таблицу url.
writeVacancyInDb(String[] data) – записывает информацию о вакансиях в базу данных в таблицу vacancy_copy.
vacancyFromDb(String domain) – вытягивает из базы данных список всех пропарсенных вакансий по сайту domain и возвращает коллекцию LinkedHashSet.
dataTransfer() – запускает хранимую процедуру переноса всей информации с vacancy_copy(временная таблица для парсинга) в vacancy(рабочая таблица сайта РаботаМолоді).

Enum класс Exept
Enum класс хранения текстов и кодов всех исключений сервиса RiM. Методы:   getCode(), getMessage().
Методы класса Exept
getCode(), getMessage() – возвращают код исключения и текстовое сообщение соответственно.

Класс DomainParse
Класс предназначен для управления всей системой парсинга на высоком уровне. Все методы класса статические: getParseClass(String url), domainSeachLink(String startUrl, int numberUrl), addReverseLast(LinkedHashSet<String> set, int numberLastElements), regionVacancy(), regionVacancyParse(). 
Методы класса DomainParse
getParseClass(String url) – определяет по входной ссылке url подходящий класс парсинга и возвращает обєкт этого класса, если ссылка не подходит ни для одного из классов возвращает объект типа ParseExeption.

domainSeachLink(String startUrl, int numberUrl) (метод не используется с версии PiM 1.4) парсит страницы сайта, начиная с  startUrl, количество страниц которые будут пропарсены задается параметром numberUrl. Метод возвращает LinkedHashSet<String> коллекцию со всеми найденными на этом сайте ссылками по домену ссылки startUrl.
addReverseLast(LinkedHashSet<String> set, int numberLastElements) (метод не используется с версии PiM 1.4) – возвращает LinkedHashSet<String> коллекцию с расположением в обратном порядке последних numberLastElements елементов (например addReverseLast (set, 1000) вернет последнюю тысячу из set в обратном порядке)
regionVacancy() – возвращает LinkedHashSet<String> коллекцию с всеми ссылками на вакансии по региону всех настроенных для парсинга сайтов. Ссылки для старта метода прописаны в теле метода, изменяя их можно задавать другие регионы. По умолчанию метод настроен на вакансии г. Винница. Метод сверяет ссылки вакансий с уже пропарсеными в базе данных и возвращает только те ссылки, которых в базе нет.
regionVacancyParse() - парсит те ссылки которые возвращает метод regionVacancy() в однопоточном режиме и все напарсенные данные записывает в базу данных.

