   <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
 <head>
 <link type="text/css" rel="stylesheet" href="<%= request.getContextPath() %>/styles/jmesa.css" media="all"/>
 
 </head>      
  <h2>et list</h2>
  <table>
		<tr><td align="left" style="font:bold;color:red"> 
	            <s:fielderror/> 	 	
                <s:actionerror/>
                <s:actionmessage/></td></tr>
     </table>
     <div class="jmesa">
   <table class="table">  
       <tr class="header">  
           <td>  
               airportcode&nbsp;&nbsp;&nbsp;&nbsp;  
           </td>  
           <td>  
               Name&nbsp;&nbsp;&nbsp;&nbsp;  
           </td>  
           <td>  
               Operate  
           </td>  
       </tr>  
       <s:iterator value="%{etAirports}" var="etairport" status="rowstatus">  
           <tr class='<s:if test="#rowstatus.odd==true">odd</s:if><s:else> even</s:else>' onmouseout='<s:if test="#rowstatus.odd==true">this.className="odd"</s:if><s:else>this.className="even"</s:else>' onmouseover="this.className='highlight'">  
               <td>  
                   <s:property value="%{airportCode}" />  
                   &nbsp;&nbsp;&nbsp;&nbsp;  
               </td>  
               <td>  
                   <s:property value="%{airportChBrief}" />  
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
               </td>  
               <td>  
                   <!-- s url action="user_editReady" portletUrlType="action"  
                       namespace="/user/edit" id="editUrl">  
                       <s param name="userId" value="%{id}" />  
                       <s param name="userName" value="%{userName}" />  
                       <s param name="password" value="%{password}" />  
                   <s url -->  
                   <!-- a href='<s property value="%{editUrl}" >'>Edit<a-->  
                    <s:url action="setupModify" id="editUrl">
                      <s:param name="saveKey" value="%{airportCode}"/>
                    </s:url>
              		<a href='<s:property value="%{editUrl}"/>'>
              		<img border="none" title="edit" src="<%=request.getContextPath()%>/images/pencil.png"/>
              		</a>&nbsp;
                   <s:a action="deleteetairport" >
                    <s:param name="saveKey" value="%{airportCode}"></s:param>
                    <img border="none" title="delete" src="<%=request.getContextPath()%>/images/delete.png"/>
                   </s:a>
               </td>  
           </tr>  
       </s:iterator>  
   </table>  
   </div>
   <s:url action="setupModify" id="newurl"></s:url>
   <a href='<s:property value="%{newurl}"/>'>new airport</a> 