package imageshare.servlets;

import imageshare.oraclehandler.OracleHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Retrieves the thumbnail for a given image.
 * 
 * A request of: thumbnail?12 will retrieve a thumbnail with photo_id = 12.
 */
public class ImageThumbnailServlet extends HttpServlet implements
        SingleThreadModel {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String photoId = req.getQueryString();
        ServletOutputStream out = resp.getOutputStream();

        resp.setContentType("image/gif");

        try {
            InputStream input = OracleHandler.getInstance()
                    .getThumbnailInputStream(Integer.parseInt(photoId));
            
            int imageByte;
            while ((imageByte = input.read()) != -1) {
                out.write(imageByte);
            }
            
            input.close();
            
        } catch (Exception e) {
            req.getSession().setAttribute("error", e.toString());
        }
    }
}
