<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("utf-8");%>
<jsp:useBean id="qdto" class="pack.question.QuestionDto" />
<jsp:setProperty property="*" name="qdto"/>
<jsp:useBean id="qmgr" class="pack.question.QuestionMgr_u" />
<%
// qdto.setUser(request.getRemoteAddr()); //클라이언트에 ip주소 들어감

int newNum = qmgr.currentMaxNum()+1;
boolean result = false;
if (request.getParameter("flag").equals("insert")) {
	result = qmgr.saveData(request,newNum);   
} else {  
	%>
	<script>
	alert("질문 등록에 실패했습니다.");
	</script>
	<%
}


response.sendRedirect("questionlist.jsp?page=1");



%>