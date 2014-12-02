package org.granitemc.granite;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.granitemc.granite.api.API;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.api.ServerConfig;
import org.granitemc.granite.api.event.EventQueue;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.item.ItemType;
import org.granitemc.granite.api.permission.PermissionsHook;
import org.granitemc.granite.api.plugin.Plugin;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.event.GraniteEventQueue;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.GraniteServerComposite;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class GraniteAPI implements API {
    public static GraniteAPI instance;
    public static GraniteAPIHelper helper;

    private Set<PluginContainer> plugins;
    private Logger logger;

    private GraniteEventQueue eventQueue;

    private ServerConfig config;
    private PermissionsHook permissionsHook;

    public static void init() {
        try {
            Field impl = Granite.class.getDeclaredField("impl");
            impl.setAccessible(true);
            impl.set(null, instance = new GraniteAPI());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field impl = Granite.class.getDeclaredField("helper");
            impl.setAccessible(true);
            impl.set(null, helper = new GraniteAPIHelper());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private GraniteAPI() {
        plugins = new HashSet<>();
        logger = LogManager.getFormatterLogger("Granite");

        eventQueue = new GraniteEventQueue();

        config = new GraniteServerConfig();
    }

    public PluginContainer getPluginContainer(String name) {
        for (PluginContainer p : plugins) {
            if (Objects.equals(p.getName(), name)) {
                return p;
            }
        }
        return null;
    }

    public Set<PluginContainer> getPlugins() {
        return plugins;
    }

    public PluginContainer getPluginContainer(Object plugin) {
        return getPluginContainerByClass(plugin.getClass());
    }

    public PluginContainer getPluginContainerByClass(Class<?> pluginClass) {
        if (pluginClass.isAnnotationPresent(Plugin.class)) {
            for (PluginContainer p : plugins) {
                if (p.getMainClass().equals(pluginClass)) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public Class<?> getPluginClassByFile(File file) {
        try {
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            JarFile jf = new JarFile(file);

            Enumeration<JarEntry> entries = jf.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replaceAll("/", ".").substring(0, entry.getName().length() - ".class".length());

                    try {
                        Class<?> clazz = classLoader.loadClass(className);

                        PluginContainer pc = createPluginContainer(clazz);

                        if (pc != null) {
                            return clazz;
                        }
                    } catch (NoClassDefFoundError | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public PluginContainer createPluginContainer(Class<?> clazz) {
        for (Annotation a : clazz.getAnnotations()) {
            if (a.annotationType().equals(Plugin.class)) {
                PluginContainer container = new PluginContainer(clazz);
                return container;
                //container.enable();
            }
        }
        return null;
    }

    public void loadPluginFromJar(File file) {
        loadPluginFromClass(getPluginClassByFile(file));
    }

    public boolean dependenciesExist(String[] dependencies) {
        File[] files = GraniteAPI.instance.getServerConfig().getPluginDirectory().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        });
        
        ArrayList<String> existing = new ArrayList<String>();

        if (files != null) {
            for (File plugin : files) {
                PluginContainer pc = createPluginContainer(getPluginClassByFile(plugin));
                existing.add(pc.getId());
            }
        }
        
        for(String s : dependencies) {
            if(!existing.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public void loadPluginFromClass(Class<?> clazz) {
        PluginContainer pc = createPluginContainer(clazz);

        if (pc != null) {
            if(dependenciesExist(pc.getDependencies())) {
                plugins.add(pc);
                getLogger().info("Loaded %s (v%s)!", pc.getName(), pc.getVersion());
            } else {
                getLogger().warn("Could not load %s (v%s): missing plugin dependencies!", pc.getName(), pc.getVersion());
            }
            
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ItemStack createItemStack(ItemType type, int amount) throws InstantiationException, IllegalAccessException {
        return new GraniteItemStack(type, amount);
    }

    public Server getServer() {
        return GraniteServerComposite.instance;
    }

    @Override
    public EventQueue getEventQueue() {
        return eventQueue;
    }

    @Override
    public ServerConfig getServerConfig() {
        return config;
    }

    @Override
    public PermissionsHook getPermissionsHook() {
        return permissionsHook;
    }

    @Override
    public void setPermissionsHook(PermissionsHook hook) {
        this.permissionsHook = hook;
    }

    public void tick() {
        //TODO: scheduler
    }
}
