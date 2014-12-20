<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html>
<head><title>Error Page</title>
<link rel="stylesheet" type="text/css" href="css/style.css" media="screen">

</head>
<body>
<div class="error-page">
<bean:message key="news.error"></bean:message>
<html:form action="/Back">
	<html:submit>
		<bean:message key="news.back"></bean:message>
	</html:submit>
</html:form>
</div>
</body></html>