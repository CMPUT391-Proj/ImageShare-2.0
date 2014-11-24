package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Updates the image description for a given image.
 * 
 */
public class UpdateImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DIR_UPDATE = "updateimage";
    private static final String DIR_GALLERY = "gallery";

    private static final String SUBJECT = "subject";
    private static final String LOCATION = "location";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
    private static final String SECURITY = "permissions";

    private static final String PHOTOID = "photoId";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String subject = null;
        String location = null;
        String description = null;
        Date date = null;
        int security = 2;

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        HttpSession session = req.getSession();

        try {
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

            int photoId = (Integer) req.getSession().getAttribute(PHOTOID);

            OracleHandler.getInstance().updateImage(photoId, subject, location,
                    description, date, security);

        } catch (Exception e) {
            session.setAttribute("error", e.toString());
            resp.sendRedirect(DIR_UPDATE);
            return;
        }

        resp.sendRedirect(DIR_GALLERY);
    }
}
