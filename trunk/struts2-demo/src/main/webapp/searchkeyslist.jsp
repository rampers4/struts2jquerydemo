   <%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
     <%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
      <%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa" %>
     <head>
     <sj:head compressed="false" useJqGridPlugin="true" jqueryui="true"  locale="zh-CN" defaultIndicator="myDefaultIndicator"/>
     <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jmesa.css" media="all"/>
     <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery.jmesa.js"></script>
     </head>
  
	
		<label for="range_date">search key(fields dep_airport,psgname):</label>
		<input id="keyword" size="20"></input>
		
		<div id="traveler_container">
		 ${traveler_tbl}
		</div>
	
	


<script type='text/javascript'>

$(document).ready(function () { 
	var tableid="traveler_tbl";	
 $.jmesa.addTableFacade(tableid);
 $("#keyword").keyup(function(e)
	        {
     if (e.keyCode == 13)
     {
    	 var keyword=this.value;
    	 if(keyword!="" && keyword.length>0)
    	 retiredata(tableid,keyword);
     }
 });
 
 
}); 
function retiredata(tbl_id,keywords){
	var md=new Date();
	if(typeof md=='undefined')md=new Date();
	var bst=md.getTime()-25*24*3600*1000;
	
	
	$.jmesa.setExportToLimit(tbl_id, '');
	$.jmesa.setPageToLimit(tbl_id,"1");
	var tblface=$.jmesa.getTableFacade(tbl_id);
	var maxrows=tblface.limit.getMaxRows();
	if(typeof(maxrows)=="undefined" || maxrows==null)
		$.jmesa.setMaxRowsToLimit(tbl_id,"10");
	
	
var parameterString = $.jmesa.createParameterStringForLimit(tbl_id);
parameterString=parameterString+"&"+tbl_id+"_keywords="+keywords;
$.get('${pageContext.request.contextPath}/keywordstraveler.action?ajax=true&' + parameterString, function(data) {
$("#traveler_container").html(data);
});
return true;
} 
function onInvokeAction(id) {
    $.jmesa.setExportToLimit(id, '');
    var parameterString = $.jmesa.createParameterStringForLimit(id);
    $.get('${pageContext.request.contextPath}/keywordstraveler.action?ajax=true&' + parameterString, function(data) {
    $("#traveler_container").html(data);
});
}

function onInvokeExportAction(id) {
var parameterString = $.jmesa.createParameterStringForLimit(id);
location.href = '${pageContext.request.contextPath}/keywordstraveler.action?ajax=false&' + parameterString;
}
</script>

   
     