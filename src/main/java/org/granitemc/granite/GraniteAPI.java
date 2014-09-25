package org.granitemc.granite;

/*****************************************************************************************
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
 ****************************************************************************************/

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.granitemc.granite.api.API;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Server;
import org.granitemc.granite.api.chat.ChatComponentBuilder;
import org.granitemc.granite.api.event.EventHandlerContainer;
import org.granitemc.granite.api.event.EventQueue;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.plugin.Plugin;
import org.granitemc.granite.api.plugin.PluginContainer;
import org.granitemc.granite.chat.GraniteChatComponentBuilder;
import org.granitemc.granite.event.GraniteEventQueue;
import org.granitemc.granite.reflect.GraniteServerComposite;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("ReflectionForUnavailableAnnotation")
public class GraniteAPI implements API {
    public static GraniteAPI instance;

    private List<PluginContainer> plugins;
    private Logger logger;

    private GraniteEventQueue eventQueue;

    private GraniteAPI() {
        plugins = new ArrayList<>();
        logger = LogManager.getFormatterLogger("Granite");

        eventQueue = new GraniteEventQueue();
    }

    public static void init() {

        try {
            Field impl = Granite.class.getDeclaredField("impl");
            impl.setAccessible(true);
            impl.set(null, instance = new GraniteAPI());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public PluginContainer getPlugin(String name) {
        for (PluginContainer p : plugins) {
            if (Objects.equals(p.getName(), name)) {
                return p;
            }
        }
        return null;
    }

    public List<PluginContainer> getPlugins() {
        return plugins;
    }

    public PluginContainer getPlugin(Object plugin) {
        return getPlugin(plugin.getClass());
    }

    public PluginContainer getPlugin(Class<?> pluginClass) {
        if (pluginClass.isAnnotationPresent(Plugin.class)) {
            for (PluginContainer p : plugins) {
                if (p.getMainClass().equals(pluginClass)) {
                    return p;
                }
            }
        }
        return null;
    }

    public void loadPluginFromJar(File file) {
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

                        for (Annotation a : clazz.getAnnotations()) {
                            if (a.annotationType().equals(Plugin.class)) {
                                PluginContainer container = new PluginContainer(clazz);

                                getLogger().info("Loaded %s (v%s)!", container.getName(), container.getVersion());

                                plugins.add(container);

                                // TODO: make this part better
                                for (List<EventHandlerContainer> ehcList : container.getEvents().values()) {
                                    for (EventHandlerContainer ehc : ehcList) {
                                        eventQueue.addHandler(ehc.getEventType(), ehc);
                                    }
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ChatComponentBuilder getChatComponentBuilder() {
        return new GraniteChatComponentBuilder();
    }

    public ItemStack createItemStack() {
        return null;
        //TODO: create item stack
    }

    public Server getServer() {
        return GraniteServerComposite.instance;
    }

    @Override
    public EventQueue getEventQueue() {
        return eventQueue;
    }

    public void tick() {
        //TODO: scheduler
    }
}
