
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<center>
	<video width="600" ,height="600" controls>
		<source src=<%=request.getAttribute("url")%> type="video/mp4"></source>
	</video>
</center>

