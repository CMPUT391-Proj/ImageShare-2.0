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
 * Retrieves the image for a given image id.
 * 
 * A request of: image?12 will retrieve a image with photo_id = 12.
 */
public class ImageDisplayServlet extends HttpServlet implements
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
                    .getImageInputStream(Integer.parseInt(photoId));

            int imageByte;
            while ((imageByte = input.read()) != -1) {
                out.write(imageByte);
            }

            input.close();

            // update hits
            OracleHandler.getInstance().increaseImageHits(
                    Integer.parseInt(photoId));

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.toString());
        }
    }
}
