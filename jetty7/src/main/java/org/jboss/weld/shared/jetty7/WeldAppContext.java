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

package org.jboss.weld.shared.jetty7;

import java.io.IOException;
import java.util.EventListener;

import org.jboss.weld.shared.api.BeansChecker;
import org.jboss.weld.shared.api.Utils;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Custom Weld app context.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class WeldAppContext extends WebAppContext implements BeansChecker
{
   public boolean checkWebInf() throws IOException
   {
      Resource beansXml = getWebInf().addPath("beans.xml");
      return beansXml.exists();
   }

   public void setEventListeners(EventListener[] eventListeners)
   {
      if (Utils.isWeldApp(this))
      {
         eventListeners = Utils.applyListener(eventListeners);
      }
      super.setEventListeners(eventListeners);
   }
}
