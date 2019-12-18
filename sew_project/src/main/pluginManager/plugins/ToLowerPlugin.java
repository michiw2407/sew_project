package main.pluginManager.plugins;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class ToLowerPlugin implements Plugin {
    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    @Override
    public float canHandle(RequestClass req) throws Exception {
        if (req.isValid()) {
            if (req.url.getRawUrl().startsWith("/tolower")) {
                System.out.println("TOLOWER PLUGIN VALID");
                return 1.0f;
            }
        }
        return 0.0f;    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public ResponseClass handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException, SQLException {
        return null;
    }
}
