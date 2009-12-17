<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>strut2-demo</title>
    <s:head theme="simple"/>
</head>

<body>
hello world, struts2
<s:url id="golist" action="etairportlist"></s:url>
<a href="<s:property value="%{golist}" />">list</a>
</body>
</html>