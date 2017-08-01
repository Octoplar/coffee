
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <style>
        BODY { font-family: verdana, arial; font-size: 11px; color: black; margin: 10px;}

        TABLE { font-family: verdana, arial; font-size: 12px; margin: 0px; border: 0px;}

        TH { padding: 5px 5px 5px 5px;}

        TD { padding: 2px 5px 2px 5px; height: 23px;}

        INPUT.field { height: 18px; margin: 0px; font-family: verdana, arial; font-size: 12px;}
    </style>
    <title><spring:message code="coffeeList.title"/></title>
</head>
<body>
<form name="coffeeForm" method="post">
    <table id="coffeeTable" cellspacing="0px" cellpadding="0px" border="0px" style="border: 1px #B0B0B0 solid">
        <tr style="background-color: #C0C0C7">
            <th/>
            <th><spring:message code="coffeeType" /></th>
            <th><spring:message code="price" /></th>
            <th><spring:message code="quantity" /></th>
        </tr>
        <c:forEach items="${coffeeList}" var="item">
            <tr>
                <td><input  type="checkbox" id="c${item.id}" name="c${item.id}" onclick="onCheckBoxClick('c${item.id}', 'f${item.id}');"/></td>
                <td nowrap="true" name="typeName">${item.typeName}</td>
                <td nowrap="true">${item.price}</td>
                <td align="right"><input id="f${item.id}" name="${item.id}" class="field" type="text" size="5" disabled="true" maxlength="2"/></td>
            </tr>
        </c:forEach>
        <tr style="background-color: #F0F0F0">
            <td colspan="4" align="right"><input type="button" id="orderButton" value="<spring:message code="order"/>" onclick="onOrderButtonClick()"/></td>
        </tr>
    </table>
    <input type="hidden" name="_eventId_order" value="order">
    <font color="red">*</font> - ${freeCupMessage}
</form>

</body>

<script>
    var errorMessage="form contains errors"


    function onCheckBoxClick(id1, id2) {
        if(document.getElementById(id1).checked){
            e=document.getElementById(id2);
            e.required=true;
            e.disabled=false;
        }
        else{
            e=document.getElementById(id2);
            e.required=false;
            e.value="";
            e.disabled=true;
        }
    };

    function isValidQuantity(str) {
        var n = Math.floor(Number(str));
        return String(n) === str && n>0 && n<100;
    }

    function onOrderButtonClick() {
        var table=document.getElementById("coffeeTable");
        var ok=true;
        var orderedItemCount=0;
        var rowCount=table.rows.length-1;

        for (i = 1; i < rowCount; i++) {
            if (table.rows[i].children[0].children[0].checked){
                if (!isValidQuantity(table.rows[i].children[3].children[0].value)){
                    ok=false;
                    break;
                }
                else {
                    orderedItemCount++;
                }
            }
        }

        if (ok&&orderedItemCount>0){
            //submit as parameters
            document.coffeeForm.submit();
        }
        else {
            alert(errorMessage);
        }
    }


</script>
</html>

