package com.aws.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.aws.dao.DbConnection;

/**
 * A Java servlet that handles file upload from client.
 * 
 * @author www.codejava.net
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "upload";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	/**
	 * Upon receiving file upload submission, parses the request to read upload
	 * data and saves the file on disk.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error: Form must has enctype=multipart/form-data.");
			writer.flush();
			return;
		}
		ServletContext context = request.getSession().getServletContext();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// sets temporary location to store files
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);

		// sets maximum size of upload file
		upload.setFileSizeMax(MAX_FILE_SIZE);

		// sets maximum size of request (include file + form data)
		upload.setSizeMax(MAX_REQUEST_SIZE);

		// constructs the directory path to store upload file
		// this path is relative to application's directory
		String uploadPath = getServletContext().getRealPath("")
				+ File.separator + UPLOAD_DIRECTORY;

		System.out.println(uploadPath + "uploadPath");
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}

		try {
			// parses the request's content to extract file data
			@SuppressWarnings("unchecked")
			List<FileItem> formItems = upload.parseRequest(request);
			String filePath = null;
			String fileName = null;
			AWSCredentials credentials = null;
			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					// processes only fields that are not form fields
					if (!item.isFormField()) {
						fileName = new File(item.getName()).getName();
						filePath = uploadPath + File.separator + fileName;
						File storeFile = new File(filePath);
						// saves the file on disk
						item.write(storeFile);
						request.setAttribute("message",
								"Upload has been done successfully!");
					}
				}
			}
			
			// uploading file into a s3
			
			credentials = new PropertiesCredentials(UploadServlet.class
					.getClassLoader().getResourceAsStream("aws.properties"));
			System.out.println(credentials);
			System.out.println("...............");
			AmazonS3 s3 = new AmazonS3Client(credentials);
			String bucketName = "qliktestbucket1";
			s3.createBucket(bucketName);
			String filePath1 = filePath;
			FileInputStream stream = new FileInputStream(filePath1);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			String s3filepath = (String) context.getAttribute("tent") + "/"
					+ (String) context.getAttribute("userid"); // tenantname+userid+filename.ext
			TransferManager tm = new TransferManager(credentials);
			System.out.println("Hello");
			PutObjectRequest putObjectRequest = new PutObjectRequest(
					bucketName, s3filepath + "/" + fileName, stream,
					objectMetadata);
			Upload myUpload = tm.upload(putObjectRequest);
			System.out.println(myUpload);
			System.out.println("Hello2");
			if (myUpload.isDone() == false) {
				System.out.println("Transfer: " + myUpload.getDescription());
				System.out.println("  - State: " + myUpload.getState());
				System.out.println("  - Progress: "
						+ myUpload.getProgress().getBytesTransferred());
			}
			try {
				myUpload.waitForCompletion();
			} catch (AmazonServiceException e) {
				e.printStackTrace();
			} catch (AmazonClientException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tm.shutdownNow();
			
			// storing bucket information into a database.
			context.setAttribute("fileName", fileName);
			context.setAttribute("filelocation", filePath);
			context.setAttribute("bucketName", bucketName);
			context.setAttribute("uri", bucketName + "/" + fileName);
			context.setAttribute("permission", "read");

			Statement stmt = null;
			String user = (String) context.getAttribute("user");
			String tenantname = (String) context.getAttribute("tent");
			String uri = bucketName + "/" + s3filepath + "/" + fileName;
			String permission = "read";
			try {
				DbConnection dbconnection = new DbConnection();
				Connection conn = dbconnection.connection();
				System.out.println("Creating statement...");
				System.out.println(user);
				System.out.println(tenantname);
				System.out.println(fileName);
				System.out.println(bucketName + s3filepath);
				System.out.println(bucketName);
				System.out.println(uri);
				System.out.println(permission);
               Date date=new Date();
				date.setSeconds(date.getSeconds()+2);
				String url = s3.generatePresignedUrl(bucketName,
						s3filepath + "/" + fileName,
						date).toString();
				System.out.println("insert data successfully");
				String loc = bucketName + "/" + s3filepath;
				String insertTableSQL = "INSERT INTO s3data"
						+ "(user, tenant, fileName, filelocation,bucket,uri,permission,url) VALUES"
						+ "(?,?,?,?,?,?,?,?)";

				PreparedStatement preparedStatement = conn
						.prepareStatement(insertTableSQL);
				preparedStatement.setString(1, user);
				preparedStatement.setString(2, tenantname);
				preparedStatement.setString(3, fileName);
				preparedStatement.setString(4, loc);
				preparedStatement.setString(5, bucketName);
				preparedStatement.setString(6, uri);
				preparedStatement.setString(7, permission);
				preparedStatement.setString(8, url);
				preparedStatement.executeUpdate();
			} finally {

			}

		} catch (Exception ex) {
			request.setAttribute("message",
					"There was an error: " + ex.getMessage());
		}
		// redirects client to upload page
		getServletContext().getRequestDispatcher("/upload.jsp").forward(
				request, response);
	}
}