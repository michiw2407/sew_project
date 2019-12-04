package main.pluginManager;

import interfaces.Plugin;
import interfaces.PluginManager;

import java.util.List;

public class PluginManagerClass implements PluginManager {


    List<Plugin> lPlug;

    /**
     * Returns a list of all plugins. Never returns null.
     * TODO: Refactor to List<Plugin>, Enumeration is deprecated
     *
     * @return
     */
    @Override
    public List<Plugin> getPlugins() {
        return lPlug;
    }

    /**
     * Adds a new plugin. If the plugin was already added, nothing will happen.
     *
     * @param plugin
     */
    @Override
    public void add(Plugin plugin) {
        lPlug.add(plugin);
    }

    /**
     * Adds a new plugin by class name. If the plugin was already added, nothing will happen.
     * Throws an exception, when the type cannot be resoled or the class does not implement Plugin.
     *
     * @param plugin
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public void add(String plugin) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//            try {
//
//                if(pl != null)
//                    lPlug.add(pl);
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
    }

    /**
     * Clears all plugins
     */
    @Override
    public void clear() {
        lPlug.clear();
    }
}
