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
		<label for="range_date">range date:</label>
		<div id="range_date"></div>
		<div id="slider_date"></div>
		<div id="traveler_container">
		 ${traveler_tbl}
		</div>
	</form>
	


<script type='text/javascript'>

$(document).ready(function () { 
	var tableid="traveler_tbl";	
 $.jmesa.addTableFacade(tableid);
 
 var sld=$("#slider_date").slider({
		range: true,
		min: 0,
		max: 80,
		values: [0, 0],
		ndate:new Date(),
		slide: function(event, ui) {
			var md=this.ndate;
			if(typeof md=='undefined')md=new Date();
			var bst=md.getTime()-25*24*3600*1000;
			var t=parseInt(ui.values[0])*24*3600*1000;
			var mindate=new Date(bst+t);
			t=parseInt(ui.values[1])*24*3600*1000;
			var maxdate=new Date(bst+t);
			var strmin=mindate.getFullYear()+"-"+(mindate.getMonth()+1)+"-"+mindate.getDate();
			var strmax=maxdate.getFullYear()+"-"+(maxdate.getMonth()+1)+"-"+maxdate.getDate();
			$("#range_date").html( strmin + ' - ' + strmax);
		},
		change:function(event,ui){
			 retiredata("traveler_tbl",ui);
			 return true;
		}
		
 });
 
}); 
function retiredata(tbl_id,ui){
	var md=new Date();
	if(typeof md=='undefined')md=new Date();
	var bst=md.getTime()-25*24*3600*1000;
	var t=parseInt(ui.values[0])*24*3600*1000;
	var mindate=new Date(bst+t);
	t=parseInt(ui.values[1])*24*3600*1000;
	var maxdate=new Date(bst+t);
	var strmin=mindate.getFullYear()+"-"+(mindate.getMonth()+1)+"-"+mindate.getDate();
	var strmax=maxdate.getFullYear()+"-"+(maxdate.getMonth()+1)+"-"+maxdate.getDate();
	
	$.jmesa.setExportToLimit(tbl_id, '');
	$.jmesa.setPageToLimit(tbl_id,"1");
	var tblface=$.jmesa.getTableFacade(tbl_id);
	var maxrows=tblface.limit.getMaxRows();
	if(typeof(maxrows)=="undefined" || maxrows==null)
		$.jmesa.setMaxRowsToLimit(tbl_id,"10");
	//$.jmesa.setPageToLimit(tbl_id,"1");
	
var parameterString = $.jmesa.createParameterStringForLimit(tbl_id);
parameterString=parameterString+"&"+tbl_id+"_rng_min="+strmin+"&"+tbl_id+"_rng_max="+strmax;
$.get('${pageContext.request.contextPath}/pgtraveler.action?ajax=true&' + parameterString, function(data) {
$("#traveler_container").html(data);
});
return true;
} 
function onInvokeAction(id) {
    $.jmesa.setExportToLimit(id, '');
	
    var parameterString = $.jmesa.createParameterStringForLimit(id);
    
    $.get('${pageContext.request.contextPath}/pgtraveler.action?ajax=true&' + parameterString, function(data) {
    $("#traveler_container").html(data);
});
}

function onInvokeExportAction(id) {
var parameterString = $.jmesa.createParameterStringForLimit(id);
location.href = '${pageContext.request.contextPath}/pgtraveler.action?ajax=false&' + parameterString;
}
</script>

   
     