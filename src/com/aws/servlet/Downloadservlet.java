package com.aws.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.aws.dao.DbConnection;

/**
 * Servlet implementation class Downloadservlet
 */
public class Downloadservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Downloadservlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));
		DbConnection connection = new DbConnection();
		Connection con = connection.connection();
		Statement stmt;
		try {
			String query = "select fileName,filelocation from s3data where id="
					+ id;
			ResultSet res = con.createStatement().executeQuery(query);
			String filename = null;
			String filepath = null;
			while (res.next()) {
				filename = res.getString("fileName");
				filepath = res.getString("filelocation");
			}

			response.setContentType(getServletContext().getMimeType(filename));
			AmazonS3 s3 = new AmazonS3Client(new PropertiesCredentials(
					UploadServlet.class.getClassLoader().getResourceAsStream(
							"aws.properties")));
			String split[] = filepath.split("/", 2);
			String bucketName = split[0];
			String key = split[1];
			Date date = new Date();
			date.setSeconds(date.getSeconds() + 60);
			String url1 = s3.generatePresignedUrl(bucketName,
					key + "/" + filename, date).toString();
			request.setAttribute("url", url1);
			System.out.println(url1);
			
			if (filename.contains("mp4")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("/streaming.jsp");
				rd.forward(request, response);
			} else {
				S3Object object = s3.getObject(new GetObjectRequest(bucketName,
						key + "/" + filename));

				response.setHeader("Content-Disposition", "inline; filename=\""
						+ filename);
				InputStream fileInputStream = object.getObjectContent();
				int i;
				while ((i = fileInputStream.read()) != -1) {
					out.write(i);
				}
				fileInputStream.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
