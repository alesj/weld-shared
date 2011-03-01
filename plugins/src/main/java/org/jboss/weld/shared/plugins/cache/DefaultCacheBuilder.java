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

package org.jboss.weld.shared.plugins.cache;

import java.io.IOException;
import java.util.Map;

import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Build Infinispan cache.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultCacheBuilder<T> implements CacheBuilder<T>
{
   private EmbeddedCacheManager cacheManager;

   /**
    * Cache builder ctor.
    *
    * @param fileName the config file name
    * @param start the start flag
    * @throws java.io.IOException for any I/O error
    */
   public DefaultCacheBuilder(String fileName, boolean start) throws IOException
   {
      cacheManager = new DefaultCacheManager(fileName, start);
   }

   public void start()
   {
      if (cacheManager.getStatus() != ComponentStatus.RUNNING)
         cacheManager.start();
   }

   public void stop()
   {
      cacheManager.stop();
   }

   public Map<String, T> getCache(String cacheName)
   {
      return cacheManager.getCache(cacheName);
   }

   public <V> Map<String, V> getCache(String cacheName, Class<V> valueType)
   {
      return cacheManager.getCache(cacheName);
   }
}
