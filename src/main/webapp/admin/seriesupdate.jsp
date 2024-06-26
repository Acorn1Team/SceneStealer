<%@page import="pack.main.SeriesDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="seriesMgr" class="pack.main.SeriesMgr"></jsp:useBean>
<%
// 작게 뜨는 창
String num = request.getParameter("num");
SeriesDto s = seriesMgr.getSeries(num);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script type="text/javascript" src="../js/mainedit.js"></script>
</head>
<body>
<form action="seriesproc.jsp?flag=update&num=<%=num %>" method="post" enctype="multipart/form-data">
	<input type="hidden" name="num" value="<%=s.getNum() %>">
	시리즈명 <input type="text" name="title" value="<%=s.getTitle() %>"><br>
	공개일자 <input type="date" name="date" value="<%=s.getDate() %>"><br><br>
	시리즈 대표사진 <br><img src="../upload/series/<%=s.getPic() %>"><br>
	대표사진 수정 ➡️ <input type="file" name="pic"><br><br><br>
	<input type="submit" value="시리즈 수정 및 선택하기">
</form>
</body>
</html>