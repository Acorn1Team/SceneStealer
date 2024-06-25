<%@page import="pack.question.QuestionMgr_u"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="qmgr" class="pack.question.QuestionMgr_u" />    
<jsp:useBean id="qdto" class="pack.question.QuestionDto" />   
<%
String num = request.getParameter("num");
if (num == null) {
	num = "1";
}
String spage = request.getParameter("page");

qdto = qmgr.getData2(num);

String user_id = qdto.getUser();
String question_title = qdto.getTitle();
String question_pic = qdto.getPic();
String question_contents = qdto.getContents();
String question_date = qdto.getDate();
String answer_contents = qdto.getAnswer_contents();

%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SceneStealer</title>
<style>
</style>
<script type="text/javascript" src="../js/script.js"></script>
</head>
<body class="question-page">
<jsp:include page="../user/header_user.jsp"></jsp:include>
<h1>질문 상세 보기 페이지</h1>
<table>
	<tr>
		<td>작성자 : <%=user_id %></td>
		<td>작성일 : <%=question_date %></td>
	</tr>
	<tr>
		<td colspan="3">제목 : <%=question_title %></td>
	</tr>
	<tr>
		<td>이미지 : <img src="../upload/<%=question_pic %>"></td>
		<td><div>내용 : <%=question_contents %></div></td>
	</tr>
</table>
<table>
<tr><!-- 관리자댓글보이기 -->
	<td><div>관리자 : <%=answer_contents != null ? answer_contents : "댓글이 없습니다." %></div></td>
</tr>
<tr>
	<td>
		<a href="edit.jsp?num=<%=num %>&page=<%=spage %>">수정하기</a>
		<a href="delete.jsp?num=<%=num %>&page=<%=spage %>">삭제하기</a>
		<a href="questionlist.jsp?page=<%=spage %>">목록가기</a>
	</td>
</tr>
</table>
<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>