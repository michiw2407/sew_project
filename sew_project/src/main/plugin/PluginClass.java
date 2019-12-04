package main.plugin;

import interfaces.Plugin;
import interfaces.Response;
import main.request.RequestClass;

public class PluginClass implements Plugin {

    String pluginName;

    public PluginClass(String pluginName) {
        this.pluginName = pluginName;
    }


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



        return 0;
    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(RequestClass req) {
        return null;
    }
}
