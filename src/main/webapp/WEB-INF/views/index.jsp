<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form"
    prefix="springForm"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FizzBuzz - Call Page</title>
<style>
.error {
    color: #ff0000;
    font-style: italic;
    font-weight: bold;
}
</style>
</head>
<body>
 
    <springForm:form method="POST" commandName="call" action="call">
        <table>
            <tr>
                <td>Phone</td>
                <td><springForm:input path="toNumber" /></td>
                <td><springForm:errors path="toNumber" cssClass="error" /></td>
            </tr>
            <tr>
                <td>Minutes to place call after (0-60, 0 for no delay) </td>
                <td><springForm:input path="callDelay" /></td>
                <td><springForm:errors path="callDelay" cssClass="error" /></td>
            </tr>
            <tr>
            </tr>
        </table>
     
         <br />
        
         <input type="submit" value="Call">
           
    </springForm:form>
 
</body>
</html>