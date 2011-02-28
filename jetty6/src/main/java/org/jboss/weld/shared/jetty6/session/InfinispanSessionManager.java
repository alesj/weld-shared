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

package org.jboss.weld.shared.jetty6.session;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.util.Map;

import org.jboss.weld.shared.plugins.session.InfinispanSessionManagerAdapter;

import org.infinispan.Cache;
import org.mortbay.jetty.servlet.AbstractSessionManager;

/**
 * Infinispan based session manager.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class InfinispanSessionManager extends AbstractSessionManager
{
   private static Method idHack = InfinispanSessionManagerAdapter.getClusterId(Session.class);
   private InfinispanSessionManagerAdapter<Session> adapter;

   public InfinispanSessionManager(Cache<String, Session> cache)
   {
      adapter = new Jetty6InfinispanSessionManagerAdapter(cache);
   }

   public Map getSessionMap()
   {
      return adapter.getSessionMap();
   }

   public int getSessions()
   {
      return adapter.getSessions();
   }

   protected void addSession(Session session)
   {
      adapter.addSession(session);
   }

   public Session getSession(String idInCluster)
   {
      return adapter.getSession(idInCluster);
   }

   protected void invalidateSessions()
   {
      adapter.invalidateSessions();
   }

   protected Session newSession(HttpServletRequest request)
   {
      return new Session(request)
      {
         protected Map newAttributeMap()
         {
            return adapter.newAttributeMap();
         }
      };
   }

   protected void removeSession(String idInCluster)
   {
      adapter.removeSession(idInCluster);
   }

   private class Jetty6InfinispanSessionManagerAdapter extends InfinispanSessionManagerAdapter<Session>
   {
      private Jetty6InfinispanSessionManagerAdapter(Cache<String, Session> cache)
      {
         super(cache);
      }

      protected String getId(Session session)
      {
         try
         {
            if (idHack != null)
               return (String) idHack.invoke(session);
         }
         catch (Throwable ignored)
         {
         }
         return session.getId();
      }

      protected void invalidate(Session session)
      {
         session.invalidate();
      }
   }
}
