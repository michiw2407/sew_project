package main.pluginManager;

import interfaces.Plugin;
import interfaces.PluginManager;
import main.plugin.PluginClass;
import main.pluginManager.plugins.NaviPlugin;
import main.pluginManager.plugins.TemperaturePlugin;

import java.util.LinkedList;
import java.util.List;

public class PluginManagerClass implements PluginManager {


    private List<Plugin> lPlug = new LinkedList<>();

    public PluginManagerClass() {
        lPlug.add(new NaviPlugin());
        lPlug.add(new TemperaturePlugin());
    }

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

    public Plugin getPlugin() {
        return new NaviPlugin();
//        return new TemperaturePlugin();
    }

    /**
     * Adds a new plugin. If the plugin was already added, nothing will happen.
     *
     * @param plugin
     */
    @Override
    public void add(PluginClass plugin) {
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
        Class<?> classInstance = Class.forName(plugin);

        if (PluginClass.class.isAssignableFrom(classInstance)) {
            try {
                PluginClass plInstance = (PluginClass) classInstance.getDeclaredConstructor().newInstance();
                lPlug.add(plInstance);
            } catch (Exception e) {
                throw new ClassNotFoundException("Class not found");
            }
        } else {
            throw new ClassNotFoundException("Class not found");
        }
    }

    /**
     * Clears all plugins
     */
    @Override
    public void clear() {
        lPlug.clear();
    }
}
