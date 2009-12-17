<%@ taglib prefix="s" uri="/struts-tags" %>
 <div id="login" class="dbx-box"  style="position: relative; display: block;">
      	<h3 class="dbx-handle dbx-handle-cursor" style="position: relative; display: block;">
      	<s:if test="logined">user info</s:if>
      	<s:else>login dailog</s:else>
      	</h3>
      	<div class="dbx-content">
      	  <s:if test="logined">
      	   <table class=""></table>
      	  </s:if>
      	<s:else>login dailog</s:else>
      	</div>
</div>
