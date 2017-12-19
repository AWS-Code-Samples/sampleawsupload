<%@ page import="java.sql.*,com.aws.dao.DbConnection"%>
<%
	Class.forName("com.mysql.jdbc.Driver");
%>

<HTML>
<center>
<style type="text/css">
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color:#A0B0E0;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color:#FFFFFF;
}
</style>
<table class="gridtable">
<BODY>


	<%
	ServletContext context = request.getSession().getServletContext();
  String user=(String)context.getAttribute("user");
  DbConnection dbconnect=new DbConnection();
	Connection connection=dbconnect.connection();

		String query="select id,tenant,fileName,uri,filelocation,fileName,user,url from s3data where user=?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
    	preparedStatement.setString(1,user);

		ResultSet resultset = preparedStatement.executeQuery();
	
		if (!resultset.next()){
			out.print("you are not uploaded any files previously");
			out.println("");
		}
	
		else{
			%>
			<TR>
			<TH>tenant</TH>
			<TH>user</TH>
			<TH>fileName</TH>
			<TH>uri</TH> 
			<TH>action</TH>

		</TR>
			<%
			while(resultset.next( )){
	%>
		<TR>
			<TD><%=resultset.getString(2)%></TD>
			<TD><%=resultset.getString(7)%></TD>
			<TD><%=resultset.getString(3)%></TD>
			<TD><%=resultset.getString(4)%></TD>
			
			 	<td><a target="_blank", href=downloadservlet?id=<%=resultset.getString(1)%>>view </a> </TD> 
			
			</TR>
	
	<%
			}
		}
	%>
	
	</TABLE>
	<%
	     
			out.println("<br><br><center><form method='POST' action='UploadServlet' enctype='multipart/form-data'>");
				out.println("<input type='file'  name='uploadfile'/><br>");
				out.println("<input type='submit' value='upload' />");
				out.println("</form></center>");
				%>
			</BODY></center>
</HTML>