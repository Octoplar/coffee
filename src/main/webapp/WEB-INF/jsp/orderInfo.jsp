
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <title><spring:message code="orderInfo.title" /></title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<table class="table table-bordered" id="infoTable">
    <tr>
        <th><spring:message code="coffeeType" /></th>
        <th><spring:message code="quantity" /></th>
    </tr>
    <c:forEach items="${order.items}" var="item">
        <tr>
            <td name="typeName">${item.coffeeType.typeName}</td>
            <td name="quantity">${item.quantity}</td>
        </tr>
    </c:forEach>
</table>
<br/>
<a href="/"><link>HOME</link></a>
</body>
</html>
