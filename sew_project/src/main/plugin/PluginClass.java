package main.plugin;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;

public class PluginClass implements Plugin {
    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    @Override
    public float canHandle(RequestClass req) {

        System.out.println("CANHANDLE");
        System.out.println(req);
        float score = 0.1f;
        if (req.isValid() && req.url.getRawUrl().equals("/test"))
            score = 1.0f;

        return score;
    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public ResponseClass handle(RequestClass req) {
        ResponseClass resp = new ResponseClass();

        if (canHandle(req) > 0.0f) {
            resp.statusCode = 200;
        } else {
            resp.statusCode = 500;
        }
        resp.contentType = "text/html; charset=utf-8";
        resp.addHeader("Accept-Language", "de-AT");
        resp.setContent("Hello World// Test");

        return resp;
    }
}
