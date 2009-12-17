   <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
      
  <h2>et edit</h2>
   <table>
		<tr><td align="left" style="font:bold;color:red"> 
	            <s:fielderror/> 	 	
                <s:actionerror/>
                <s:actionmessage/></td></tr>
     </table>
     <s:form >
   	<table class="tbl">
       <tr>  
           <th class="label">  
               <s:text name="label.etAirportCode"/>
           </th>  
           <th class="inp">  
              
               <s:textfield name="saveKey" size="3" readonly='updateble'/>
           </th>  
          
       </tr>  
        <tr>  
           <th class="label">  
               <s:text name="label.airportChBrief"/>
           </th>  
           <th class="inp">  
               <s:textfield name="target.airportChBrief" size="30"/>
           </th>  
            
       </tr> 
       <tr>  
           <th colspan="2">  
              <s:submit action="saveOrUpdateEtAirport" key="button.label.submit" cssClass="butStnd"/>
              &nbsp;&nbsp;
              <s:reset key="button.label.cancel" cssClass="butStnd"/>
           </th>  
          
            
       </tr>
       </table>
     </s:form>