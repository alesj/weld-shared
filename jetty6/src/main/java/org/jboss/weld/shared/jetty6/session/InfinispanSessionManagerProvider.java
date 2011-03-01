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

import java.io.IOException;

import org.jboss.weld.shared.plugins.cache.CacheBuilder;
import org.jboss.weld.shared.plugins.cache.DefaultCacheBuilder;

import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.SessionManager;
import org.mortbay.jetty.servlet.AbstractSessionManager;

/**
 * Infinispan based session manager provider.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class InfinispanSessionManagerProvider extends AbstractLifeCycle implements SessionManagerProvider
{
   private boolean lazyStart = true;
   private CacheBuilder<AbstractSessionManager.Session> cacheBuilder;

   public InfinispanSessionManagerProvider(String fileName) throws IOException
   {
      cacheBuilder = new DefaultCacheBuilder<AbstractSessionManager.Session>(fileName, false);
   }

   public SessionManager createSessionManager(String applicationId)
   {
      return new InfinispanSessionManager(cacheBuilder, applicationId);
   }

   protected void doStart() throws Exception
   {
      if (lazyStart == false)
         cacheBuilder.start();
   }

   protected void doStop() throws Exception
   {
      cacheBuilder.stop();
   }

   public void setLazyStart(boolean lazyStart)
   {
      this.lazyStart = lazyStart;
   }
}
