package imageshare.servlets;

import imageshare.model.Image;
import imageshare.oraclehandler.OracleHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
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
    private static final String SECURITY = "permissions";
    private static final String USER = "user";

    private static final String EXT_JPG = ".jpg";
    private static final String EXT_GIF = ".gif";

    private static final String FILE_ERROR = "At least one file with the correct extension (.jpg / .gif) must be used.";
    private static final String RETRIEVE_USER_ERROR = "Unable to get the current logged in user.";

    private static final int THUMBNAIL_SHRINK_FACTOR = 10;
    
    private static final String IMAGE_UPLOAD_JSP = "imageupload";
    private static final String IMAGE_SUCCESSFUL = "The image files have been uploaded.";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String user = null;
        String subject = null;
        String location = null;
        String description = null;
        Date date = null;
        int security = 2;
        
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> files = new ArrayList<FileItem>();
        
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
                        date = fileItem.getString().equals("") ? 
                                new Date() : new SimpleDateFormat("yyyy-MM-dd",
                                Locale.ENGLISH).parse(fileItem.getString());

                    else if (label.equalsIgnoreCase(DESCRIPTION))
                        description = fileItem.getString();

                    else if (label.equalsIgnoreCase(SECURITY))
                        security = Integer.parseInt(fileItem.getString());

                } else {
                    // Item is a file

                    String fileName = fileItem.getName().trim().toLowerCase();

                    if (fileName.endsWith(EXT_JPG) || fileName.endsWith(EXT_GIF)) {
                        files.add(fileItem);
                    }
                }
            }

            user = (String) req.getSession().getAttribute(USER);
            if (user == null)
                throw new FileUploadException(RETRIEVE_USER_ERROR);
            if (files.isEmpty())
                throw new FileUploadException(FILE_ERROR);
            
        } catch (Exception e) { 
            req.getSession(true).setAttribute("error", e.toString());
            resp.sendRedirect(IMAGE_UPLOAD_JSP);
            return;
        }

        for (FileItem file : files) {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            BufferedImage thumbnail = ImageUploadServlet.shrink(bufferedImage);
    
            Image image = new Image(user, security, subject, location, date,
                    description, thumbnail, bufferedImage);
    
            try {
                // store image
                OracleHandler.getInstance().storeImage(image);
            } catch (Exception e) {
                req.getSession(true).setAttribute("error", e.toString());
                resp.sendRedirect(IMAGE_UPLOAD_JSP);
                return;
            }
        }

        req.getSession(true).setAttribute("success", IMAGE_SUCCESSFUL);
        resp.sendRedirect(IMAGE_UPLOAD_JSP);
    }

    private static BufferedImage shrink(BufferedImage image) {
        int width = image.getWidth() / THUMBNAIL_SHRINK_FACTOR;
        int height = image.getHeight() / THUMBNAIL_SHRINK_FACTOR;

        BufferedImage shrunkImage = new BufferedImage(width, height,
                image.getType());

        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j)
                shrunkImage.setRGB(j, i,
                        image.getRGB(j * THUMBNAIL_SHRINK_FACTOR, i
                                * THUMBNAIL_SHRINK_FACTOR));
        
        return shrunkImage;
    }
}
