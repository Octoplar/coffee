<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <style>
        BODY { font-family: verdana, arial; font-size: 11px; color: black; margin: 10px;}

        TABLE { font-family: verdana, arial; font-size: 12px; margin: 0px; border: 0px;}

        TH { padding: 5px 5px 5px 5px;}

        TD { padding: 2px 5px 2px 5px; height: 23px;}

        INPUT.field { height: 18px; margin: 0px; font-family: verdana, arial; font-size: 12px;}
    </style>
    <title><spring:message code="addressInput.title"/></title>
</head>
<body>
<form name="nameAndAddressForm" method="post">
    <table cellspacing="0px" cellpadding="2px" border="0px" style="border: 1px #B0B0B0 solid">
        <tr style="background-color: #C0C0C7">
            <th colspan="2"><spring:message code="delivery" /></th>
        </tr>
        <tr style="background-color: #F0F0F0">
            <td><b><spring:message code="caption.name" /></b></td>
            <td nowrap="true"><input class="field" id="name" name="name" type="text" size="30" maxlength="100" /></td>
        </tr>
        <tr style="background-color: #F0F0F0">
            <td><b><spring:message code="caption.address" /></b></td>
            <td nowrap="true"><input class="field" id="address" name="address" type="text" size="30" maxlength="200" required/></td>
        </tr>
        <tr style="background-color: #F0F0F0">
            <td colspan="2" align="right"><input type="submit" name="_eventId_address_entered" value="<spring:message code="order" />" /></td>
        </tr>
    </table>
</form>

<br/>


<table cellspacing="0px" cellpadding="2px" border="0px" style="border: 1px #B0B0B0 solid">
    <tr style="background-color: #C0C0C7">
        <th><spring:message code="coffeeType" /></th>
        <th><spring:message code="price" /></th>
        <th><spring:message code="quantity" /></th>
        <th><spring:message code="total" /></th>
    </tr>
    <c:forEach items="${detailedOrderCost.costMap}" var="entry">
        <tr>
            <td nowrap="true">${entry.key.coffeeType.typeName}</td>
            <td nowrap="true">${entry.key.coffeeType.price}</td>
            <td align="right">${entry.key.quantity}</td>
            <td><font color="red">${entry.value}</font></td>
        </tr>
    </c:forEach>
    <tr style="background-color: #E0E0E0">
        <td colspan="3" align="right"><b><spring:message code="orderCost" /></b></td>
        <td align="right">${detailedOrderCost.orderCost}</td>
    </tr>
    <tr style="background-color: #F0F0F0">
        <td colspan="3" align="right"><b><spring:message code="deliveryCost" /></b></td>
        <td align="right">${detailedOrderCost.deliveryCost}</td>
    </tr>
    <tr style="background-color: #E0E0E0">
        <td colspan="3" align="right"><b><spring:message code="totalCost" /></b></td>
        <td align="right">${detailedOrderCost.orderCost + detailedOrderCost.deliveryCost}</td>
    </tr>
</table>

</body>
</html>
