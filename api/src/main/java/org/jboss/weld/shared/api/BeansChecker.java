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

/**
 * Check WebInf for beans.xml.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface BeansChecker
{
   /**
    * Do we have beans.xml.
    *
    * @return true if we have beans.xml, false if not, null if we don't know
    */
   Boolean hasBeans();

   /**
    * Set beans flag.
    *
    * @param flag the beans flag
    */
   void setBeansFlag(boolean flag);

   /**
    * Do we have beans.xml in WEB-INF.
    *
    * @return true if beans.xml exist in WEB-INF, false otherwise
    * @throws IOException for any I/O error
    */
   boolean checkWebInf() throws IOException;

   /**
    * Get app classloader.
    *
    * @return the app classloader
    */
   ClassLoader getClassLoader();
}
