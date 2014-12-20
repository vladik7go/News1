<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<div style="color:red">
 <html:errors/>
</div>

<span class="contentNewsTitle">
	<html:link action="NewsList">
		<bean:message key="news.news" />
	</html:link>
</span>
&gt;&gt;
<bean:message key="news.add.title" />
<br>

<br>

<html:form action="/SaveNews"
	onsubmit="return validateAddEditNewsForm(this)">
	
<!-- FOR TESTING WITHOUT JAVA-SCRIPT VALIDATION -->	<%-- <html:form action="/SaveNews"> --%> 
	
	<table>
		<tr>
			<td class="newsTableTitles"><bean:message key='news.title' /></td>
			<td><html:text size="100" name="newsForm" property="news.title" /></td>
		</tr>
		<tr>
			<td><bean:message key="news.date" /></td>
			<td><html:text size="10" name="newsForm" property="dateString" /></td>
		</tr>
		<tr>
			<td><bean:message key="news.brief" /></td>
			<td><html:textarea rows="5" cols="100" name="newsForm"
					property="news.brief" /></td>
		</tr>
		<tr>
			<td><bean:message key="news.content" /></td>
			<td><html:textarea rows="10" cols="100" name="newsForm"
					property="news.content" /></td>
		</tr>
	</table>
	<table class="newsListTable">
		<tr>
			<td class="afterTableButtons"><html:submit>
					<bean:message key="news.save" />
				</html:submit> <html:hidden name="newsForm" property="news.id" /></td>
			<td><input type="button"
				value="<bean:message key="news.cancel"/>"
				onclick="location.replace('Cancel.do')" /></td>
		</tr>
	</table>
	<br>
</html:form>