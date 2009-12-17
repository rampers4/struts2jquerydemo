   <%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
     <%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
     <head>
     <sj:head compressed="false" useJqGridPlugin="true" jqueryui="true"  locale="zh-CN" defaultIndicator="myDefaultIndicator"/>
     </head>
   <s:url id="remoteurl" action="jsontable"/>
    
    <sj:grid 
    	id="gridtable" 
    	caption="etairport Examples" 
    	dataType="json" 
    	href="%{remoteurl}" 
    	pager="true" 
    	gridModel="gridModel"
    	rowList="10,15,20"
    	rowNum="15"
    	rownumbers="true"
    >
    	<sj:gridColumn name="airportCode" index="airportCode" title="code"  sortable="false"/>
    	<sj:gridColumn name="airportChBrief" index="airportChBrief" title="name" sortable="true"/>
    	<sj:gridColumn name="weather"  index="airportCode"  formatter="currencyFmatter" title="operator" sortable="false"  />
    </sj:grid>
     
      
<div id="gridtable_pager"></div>
<table id="gridtable" class="scroll" cellpadding="0" cellspacing="0"
 class="scroll"></table>
 <script type="text/javascript">
<!--
jQuery.extend($.fn.fmatter , {
    currencyFmatter : function(cellvalue, options, rowdata) {
    return "<a href='<%=request.getContextPath() %>/setupModify.action?saveKey="+rowdata.airportCode+"'><img src='<%=request.getContextPath() %>/images/pencil.png'/></a>";//"$"+cellvalue;
}
});
//-->
</script>
<!-- 
<script type='text/javascript'>
$(document).ready(function () { 
	var options_gridtable = {};
	var options_gridtable_colnames = new Array();
	var options_gridtable_colmodels = new Array();

    	
options_gridtable_colmodels_airportCode = {};
options_gridtable_colmodels_airportCode.name = "airportCode";
options_gridtable_colmodels_airportCode.jsonmap = "airportCode";
options_gridtable_colmodels_airportCode.index = "airportCode";
options_gridtable_colmodels_airportCode.editable = false;
options_gridtable_colmodels_airportCode.sortable = false;
options_gridtable_colmodels_airportCode.resizable = true;
options_gridtable_colmodels_airportCode.search = true;
options_gridtable_colnames.push("code");
options_gridtable_colmodels.push(options_gridtable_colmodels_airportCode);
    	
options_gridtable_colmodels_airportChBrief = {};
options_gridtable_colmodels_airportChBrief.name = "airportChBrief";
options_gridtable_colmodels_airportChBrief.jsonmap = "airportChBrief";
options_gridtable_colmodels_airportChBrief.index = "airportChBrief";
options_gridtable_colmodels_airportChBrief.editable = false;
options_gridtable_colmodels_airportChBrief.sortable = true;
options_gridtable_colmodels_airportChBrief.resizable = true;
options_gridtable_colmodels_airportChBrief.search = true;
options_gridtable_colnames.push("name");
options_gridtable_colmodels.push(options_gridtable_colmodels_airportChBrief);
    	
options_gridtable_colmodels_weather = {};
options_gridtable_colmodels_weather.name = "weather";
options_gridtable_colmodels_weather.jsonmap = "weather";
options_gridtable_colmodels_weather.index = "weather";
options_gridtable_colmodels_weather.formatter = "currencyFmatter";
options_gridtable_colmodels_weather.editable = false;
options_gridtable_colmodels_weather.sortable = false;
options_gridtable_colmodels_weather.resizable = true;
options_gridtable_colmodels_weather.search = true;
options_gridtable_colnames.push("operator");
options_gridtable_colmodels.push(options_gridtable_colmodels_weather);
    	options_gridtable.datatype = "json";
	options_gridtable.url = "/jsontable";
	options_gridtable.height = 'auto';
	options_gridtable.pager = "gridtable_pager";
	options_gridtable.rowNum = 15;
	options_gridtable.rowList = [10,15,20];
	options_gridtable.caption = "etairport Examples";
	options_gridtable.shrinkToFit = true;
	options_gridtable.autoencode = true;
	options_gridtable.rownumbers = true;


	options_gridtable.colNames = options_gridtable_colnames;
	options_gridtable.colModel = options_gridtable_colmodels;
	options_gridtable.gridview = true;
	options_gridtable.jsonReader = {};
	options_gridtable.jsonReader.root = "gridModel";
	options_gridtable.jsonReader.page = "page";
	options_gridtable.jsonReader.total = "total";
	options_gridtable.jsonReader.records = "records";
	options_gridtable.jsonReader.repeatitems = false;
	options_gridtable.jqueryaction = "grid";
	options_gridtable.id = "gridtable";

$.struts2_jquery.bind($('#gridtable'),options_gridtable);

 }); 

</script>
-->