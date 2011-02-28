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

package org.jboss.weld.shared.plugins.session;

import javax.servlet.http.HttpSession;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.infinispan.Cache;

/**
 * Infinispan based session manager adapter.
 * It tries to provide one-to-one mapping to actual methods.
 *
 * @param <T> exact session type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class InfinispanSessionManagerAdapter<T extends HttpSession>
{
   private Cache<String, T> cache;

   protected InfinispanSessionManagerAdapter(Cache<String, T> cache)
   {
      if (cache == null)
         throw new IllegalArgumentException("Null cache");
      this.cache = cache;
   }

   public static Method getClusterId(final Class<?> sessionClass)
   {
      try
      {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Method>()
         {
            public Method run() throws Exception
            {
               Method getClusterId = sessionClass.getDeclaredMethod("getClusterId");
               getClusterId.setAccessible(true);
               return getClusterId;
            }
         });
      }
      catch (Throwable ignored)
      {
      }
      return null;
   }

   protected abstract String getId(T session);

   protected abstract void invalidate(T session);

   public Map newAttributeMap()
   {
      return new HashMap(3); // TODO
   }

   public Map getSessionMap()
   {
      return Collections.unmodifiableMap(cache);
   }

   public int getSessions()
   {
      return cache.size();
   }

   public void addSession(T session)
   {
      cache.put(getId(session), session);
   }

   public T getSession(String idInCluster)
   {
      return cache.get(idInCluster);
   }

   public void invalidateSessions()
   {
      Set<T> sessions = new HashSet<T>(cache.values());
      cache.clear();
      for (T session : sessions)
         invalidate(session);
   }

   public void removeSession(String idInCluster)
   {
      cache.remove(idInCluster);
   }
}
