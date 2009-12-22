   <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%> 
       <%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
 <head>
 <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jmesa.css" media="all"/>
 <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jquery.jcarousel.css" media="all"/>
  <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jcarousel.css" media="all"/>
  <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jquery.fancybox-1.2.6.css" media="all"/>
 <sj:head compressed="false" useJqGridPlugin="false" jqueryui="false"  locale="zh-CN" defaultIndicator="myDefaultIndicator"/>
<script type="text/javascript" src="<s:url value='/scripts/jquery.jcarousel.pack.js' />"  ></script>
<script type="text/javascript" src="<s:url value='/scripts/jquery.fancybox-1.2.6.js' />"  ></script>
 </head>      
  <h2>et list</h2>
 
     <div id="mycarousel" class="jcarousel-skin-ie7">
  		<ul>
      <!-- The content will be dynamically loaded in here -->
    	</ul>
   </div>
   <script type="text/javascript">
<!--
function mycarousel_itemLoadCallback(carousel, state){
	var vcontext="<%= request.getContextPath() %>";
		var data=[];
 	if(state=="init"){
 		 /*jQuery.get('dynamic_ajax.txt', function(data) {
 	 		 
 	        mycarousel_itemAddCallback(carousel, carousel.first, carousel.last, data);
 	    });*/
 		
 		for(var i=0;i<11;i++){
 	 		data[i]=vcontext+'/images/jcarousel_demo/'+(i<9?'0':'')+(i+1)+".jpg";
 		}
 		mycarousel_itemAddCallback(carousel, carousel.first, carousel.last, data);
 	}else if(state=="next"){
 	}
 	return;
};

function mycarousel_itemAddCallback(carousel, first, last, data)
{
    // Simply add all items at once and set the size accordingly.
    var items = data;
    for (i = 0; i < items.length; i++) {
        carousel.add(i+1, mycarousel_getItemHTML(items[i]));
        var ili=$('a',carousel.get(i+1));
        ili.fancybox({
			'overlayOpacity'	:	0.7,
			'overlayColor'		:	'#FFF'
		});
		/*$("a.zoom2").fancybox({
			'zoomSpeedIn'		:	500,
			'zoomSpeedOut'		:	500
		});*/
    }
    carousel.size(items.length);
};
function mycarousel_getItemHTML(url)
{
    return '<a href='+url+'><img src="' + url + '" width="75" height="75" alt="" /></a>';
};
$(document).ready(function(){   
	var myjcarousel=$('#mycarousel').jcarousel(
	{
		vertical:false,
		scroll:4,
        itemLoadCallback:mycarousel_itemLoadCallback}
    );
});
//-->
</script>
   