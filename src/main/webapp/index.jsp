<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Стартовая страница RIM</title>
</head>
<body>
Версия сервиса RIM: 0.0.1.1 <br/>
<img src="RIM.jpg" />
<p>Напишите адресс сайта который Вы хотите пропарсить:</p>
<form name="form1"
      method="post"
      id = "test"
      action = "controller">
    <input type=text name="url" value="http://yandex.ru" />
    <input type=submit name="but" value="Пропарсить" />
</form>
</body>
</html>