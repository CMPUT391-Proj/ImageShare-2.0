package imageshare.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DirectoryUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String EXT_JPG = ".jpg";
    private static final String EXT_GIF = ".gif";
    
    private static final String FILE_ERROR = "Atleast one file with the correct extension (.jpg / .gif) must be used.";
    private static final String DIR_UPLOAD_JSP = "directoryupload";
    
    private static final String IMAGES = "imagesDir";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        HttpSession session = req.getSession();
            
        /*
         * Retrieve the photos
         */
        List<FileItem> files = new ArrayList<FileItem>();

        try {
            /*
             * Attempt to read in the file items from the request
             */
            List<FileItem> fileItems = upload.parseRequest(req);

            for (FileItem fileItem : fileItems) {
                if (!fileItem.isFormField()) {
                    String fileName = fileItem.getName().trim().toLowerCase();

                    // Only care about the files which end with .jpg or .gif
                    if (fileName.endsWith(EXT_JPG) || fileName.endsWith(EXT_GIF)) {
                        files.add(fileItem);
                    }
                }
            }
        } catch (Exception e) {
            session.setAttribute("error", e.toString());
            resp.sendRedirect(DIR_UPLOAD_JSP);
        }
        
        if (files.isEmpty()) {
            session.setAttribute("error", FILE_ERROR);
            resp.sendRedirect(DIR_UPLOAD_JSP);
        }
        
        session.setAttribute(IMAGES, files);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
