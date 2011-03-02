/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.weld.shared.api;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.List;

import org.jboss.weld.bootstrap.api.SingletonProvider;
import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.environment.servlet.util.Reflections;

/**
 * Weld Shared utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class Utils
{
   /**
    * Set singleton provider.
    *
    * @param className the classname
    */
   public static void setSingletonProvider(String className)
   {
      SingletonProvider singletonProvider = Reflections.newInstance(className);
      SingletonProvider.initialize(singletonProvider);
   }

   /**
    * Is this Weld app.
    *
    * @param beansChecker the beans checker
    * @return true is Weld app, false otherwise
    */
   public static boolean isWeldApp(BeansChecker beansChecker)
   {
      try
      {
         if (beansChecker.checkWebInf())
            return true;

         ClassLoader classLoader = beansChecker.getClassLoader();
         Enumeration<URL> beans = classLoader.getResources("META-INF/beans.xml");
         return beans.hasMoreElements();
      }
      catch (IOException e)
      {
         throw new IllegalArgumentException("Cannot check for beans.xml");
      }
   }

   /**
    * Add Weld listener, if not already added.
    *
    * @param listeners the listeners
    * @return listeners with Weld listener
    */
   @SuppressWarnings({"unchecked"})
   public static EventListener[] applyListener(EventListener[] listeners)
   {
      int alreadyExists = 0;
      List<EventListener> newListeners = new ArrayList();
      for (EventListener listener : listeners)
      {
         boolean isWeldListener = (listener instanceof Listener);
         if (isWeldListener)
            alreadyExists++;

         // non Weld listeners, plus the first one configured
         if (isWeldListener == false || alreadyExists == 1)
            newListeners.add(listener);
      }

      if (alreadyExists == 1)
         return listeners;

      if (alreadyExists == 0)
         newListeners.add(0, new Listener());

      return newListeners.toArray(new EventListener[newListeners.size()]);
   }
}
