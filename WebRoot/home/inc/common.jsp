<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="class" uri="/WEB-INF/class.tld" %>
<%@ taglib prefix="link" uri="/WEB-INF/link.tld" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>
<c:set var="lang" value="<%=com.santrong.plt.system.Global.Language%>" ></c:set>
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="message" />