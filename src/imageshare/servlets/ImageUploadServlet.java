package imageshare.servlets;

import imageshare.model.Image;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private static final String USER = "user";

    private static final String EXT_JPG = ".jpg";
    private static final String EXT_GIF = ".gif";

    private static final String FILE_ERROR = "A file with the correct extension (.jpg / .gif) must be used.";
    private static final String RETRIEVE_USER_ERROR = "Unable to get the current logged in user.";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String user = null;
        String subject = null;
        String location = null;
        String description = null;
        Date date = null;
        int security = 0;
        FileItem file;

        ServletFileUpload upload = new ServletFileUpload();

        /*
         * Attempt to read in the file items from the .jsp form
         */
        try {
            List<FileItem> fileItems = upload.parseRequest(req);

            for (FileItem fileItem : fileItems) {

                if (fileItem.isFormField()) {
                    // Item is a form item

                    String label = fileItem.getFieldName().trim();

                    if (label.equalsIgnoreCase(SUBJECT))
                        subject = fileItem.getString();

                    else if (label.equalsIgnoreCase(LOCATION))
                        location = fileItem.getString();

                    else if (label.equalsIgnoreCase(DATE))
                        date = new SimpleDateFormat("mm/dd/yyyy",
                                Locale.ENGLISH).parse(fileItem.getString());

                    else if (label.equalsIgnoreCase(DESCRIPTION))
                        description = fileItem.getString();

                    else if (label.equalsIgnoreCase(SECURITY))
                        security = Integer.parseInt(fileItem.getString());

                } else {
                    // Item is a file

                    String fileName = fileItem.getName().trim().toLowerCase();

                    if (!fileName.endsWith(EXT_JPG)
                            && !fileName.endsWith(EXT_GIF)) {
                        // file has an incorrect extension - must stop the
                        // request.
                        req.getSession().setAttribute("error", FILE_ERROR);
                        // TODO send response to .jsp
                        return;
                    }

                    file = fileItem;
                }
            }

            // Get the logged in user
            user = (String) req.getSession().getAttribute(USER);
            if (user == null)
                throw new FileUploadException(RETRIEVE_USER_ERROR);
            
        } catch (FileUploadException | ParseException e) {  // getting errors here? Check that you have java 7.
            req.getSession().setAttribute("error", e.toString());
            // TODO send response to .jsp
        }

        Image image = new Image(user, security, subject, location, date, description);
        
        // TODO image upload 
        // TODO send response to .jsp
    }
}
