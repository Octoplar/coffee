<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>

    <title><spring:message code="thanksUser.title"/></title>
</head>
<body>
<h2><spring:message code="thanks.message"/></h2>
<br/>
<a href="${orderInfoUrl}"><link><spring:message code="thanks.link"/></link></a>
</body>
</html>
