<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<div id="login" class="dbx-box"
	style="position: relative; display: block;">
<h3 class="dbx-handle dbx-handle-cursor"
	style="position: relative; display: block;"><s:if test="logined">user info</s:if>
<s:else>login dailog</s:else></h3>
<div class="dbx-content">
<s:if test="logined">
	<div>wellcome <s:property value="svcUser.employeeId" /></div>
	<div>expireDate <s:property value="svcUser.expireDate" /></div>
	<div><s:a name="logout" action="logout">logout</s:a></div>
</s:if> <s:else>

	<table>
		<tr>
			<td align="left" style="font: bold; color: red">
			<s:fielderror >
			<s:param>username</s:param>
			<s:param>password</s:param>
			</s:fielderror>
			<s:actionerror /> <s:actionmessage /></td>
		</tr>
	</table>
	<s:form action="login" validate="true" id="loginfrm">
		<table class="tbl">
			<tr>
				<td class="label">employeeId</td>
				<td class="inp"><s:textfield name="username" size="10"
					maxlength="12"></s:textfield></td>
			</tr>
			<tr>
				<td class="label">password</td>
				<td class="inp"><s:password name="password" size="10"
					maxlength="10">
				</s:password></td>
			</tr>
			<tr>
				<td colspan="2"><s:submit id="login_btn" value="login" ></s:submit>
				</td>
			</tr>
		</table>
	</s:form>
</s:else>
</div>

</div>
