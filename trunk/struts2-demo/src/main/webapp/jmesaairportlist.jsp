   <%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
     <%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
      <%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa" %>
     <head>
     <sj:head compressed="false" useJqGridPlugin="true" jqueryui="true"  locale="zh-CN" defaultIndicator="myDefaultIndicator"/>
     <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jmesa.css" media="all"/>
     <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery.jmesa.js"></script>
     </head>
  
	<form>
		<div id="etairports">
		 ${etairport_tbl}
		</div>
	</form>
	


<script type='text/javascript'>
$(document).ready(function () { 
	
//$.struts2_jquery.bind($('#gridtable'),options_gridtable);

 });  

function onInvokeAction(id) {
    $.jmesa.setExportToLimit(id, '');

    var parameterString = $.jmesa.createParameterStringForLimit(id);
    $.get('${pageContext.request.contextPath}/pgairport.action?ajax=true&' + parameterString, function(data) {
    $("#etairports").html(data);
});
}

function onInvokeExportAction(id) {
var parameterString = $.jmesa.createParameterStringForLimit(id);
location.href = '${pageContext.request.contextPath}/pgairport.action?ajax=false&' + parameterString;
}
</script>

   
     