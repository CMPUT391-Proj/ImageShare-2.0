package imageshare.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet for uploading a image onto the server.
 * 
 */
public class ImageUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private static final String SUBJECT = "subject";
    private static final String LOCATION = "location";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String SECURITY = "security";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String subject;
        String location;
        String date;
        String description;
        int security;
        
        ServletFileUpload upload = new ServletFileUpload();

        try {
            List<FileItem> fileItems = upload.parseRequest(req);
            
            for (FileItem fileItem : fileItems) {
                
                if (fileItem.isFormField()) {
                    // Item is a form item
                    
                    String label = fileItem.getFieldName().trim();
                    
                    if (label.equalsIgnoreCase(ImageUploadServlet.SUBJECT))
                        subject = fileItem.getString();

                    else if (label.equalsIgnoreCase(ImageUploadServlet.LOCATION))
                        location = fileItem.getString();
                    
                    else if (label.equalsIgnoreCase(ImageUploadServlet.DATE))
                        date = fileItem.getString();
                    
                    else if (label.equalsIgnoreCase(ImageUploadServlet.DESCRIPTION))
                        description = fileItem.getString();
                    
                    else if (label.equalsIgnoreCase(ImageUploadServlet.SECURITY)) 
                        security = Integer.parseInt(fileItem.getString());
                
                } else {
                    // Item is a file
                    fileItem.getName();
                }
            }
            
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        
    }
}
