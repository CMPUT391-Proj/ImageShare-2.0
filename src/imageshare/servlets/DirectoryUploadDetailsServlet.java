package imageshare.servlets;

import imageshare.model.Image;
import imageshare.oraclehandler.OracleHandler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DirectoryUploadDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SUBJECT = "subject";
    private static final String LOCATION = "location";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String SECURITY = "permissions";

    private static final String IMAGES = "imagesDir";

    private static final String FILE_ERROR = "Atleast one file with the correct extension (.jpg / .gif) must be used.";
    private static final String RETRIEVE_USER_ERROR = "Unable to get the current logged in user.";

    private static final int THUMBNAIL_SHRINK_FACTOR = 10;

    private static final String USER = "user";
    
    private static final String DIR_UPLOAD_JSP = "directoryupload";
    private static final String DIR_SUCCESSFUL = "The image files have been uploaded.";

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String user = null;
        String subject = null;
        String location = null;
        String description = null;
        Date date = null;
        int security = 2;
        List<FileItem> files = null;

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        HttpSession session = req.getSession();

        try {
            /*
             * Retrieve the files from the session attribute
             */
            files = (List<FileItem>) session.getAttribute(IMAGES);

            if (files == null)
                throw new FileUploadException(FILE_ERROR);

            List<FileItem> fileItems = upload.parseRequest(req);

            /*
             * Attempt to read from the .jsp form
             */
            for (FileItem fileItem : fileItems) {

                if (fileItem.isFormField()) {
                    String label = fileItem.getFieldName().trim();

                    if (label.equalsIgnoreCase(SUBJECT))
                        subject = fileItem.getString();

                    else if (label.equalsIgnoreCase(LOCATION))
                        location = fileItem.getString();

                    else if (label.equalsIgnoreCase(DATE))
                        date = fileItem.getString().equals("") ? new Date()
                                : new SimpleDateFormat("yyyy-MM-dd",
                                        Locale.ENGLISH).parse(fileItem
                                        .getString());

                    else if (label.equalsIgnoreCase(DESCRIPTION))
                        description = fileItem.getString();

                    else if (label.equalsIgnoreCase(SECURITY))
                        security = Integer.parseInt(fileItem.getString());

                }
            }

            // Get the logged in user
            user = (String) req.getSession().getAttribute(USER);
            if (user == null)
                throw new FileUploadException(RETRIEVE_USER_ERROR);

        } catch (Exception e) {
            req.getSession(true).setAttribute("error", e.toString());
            resp.sendRedirect(DIR_UPLOAD_JSP);
            return;
        }

        for (FileItem imageFile : files) {
            BufferedImage bufferedImage = ImageIO.read(imageFile
                    .getInputStream());
            BufferedImage thumbnail = DirectoryUploadDetailsServlet
                    .shrink(bufferedImage);

            Image image = new Image(user, security, subject, location, date,
                    description, thumbnail, bufferedImage);

            try {
                // store image
                OracleHandler.getInstance().storeImage(image);
            } catch (Exception e) {
                req.getSession(true).setAttribute("error", e.toString());
                resp.sendRedirect(DIR_UPLOAD_JSP);
                return;
            }
        }

        session.setAttribute("success", DIR_SUCCESSFUL);
        resp.sendRedirect(DIR_UPLOAD_JSP);
    }

    private static BufferedImage shrink(BufferedImage image) {
        int width = image.getWidth() / THUMBNAIL_SHRINK_FACTOR;
        int height = image.getHeight() / THUMBNAIL_SHRINK_FACTOR;

        BufferedImage shrunkImage = new BufferedImage(width, height,
                image.getType());

        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j)
                shrunkImage.setRGB(
                        j,
                        i,
                        image.getRGB(j * THUMBNAIL_SHRINK_FACTOR, i
                                * THUMBNAIL_SHRINK_FACTOR));

        return shrunkImage;
    }
}
