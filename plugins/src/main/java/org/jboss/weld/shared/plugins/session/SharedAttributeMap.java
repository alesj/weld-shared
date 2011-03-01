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

import java.io.Serializable;
import java.util.*;

import org.infinispan.util.concurrent.ConcurrentHashSet;

/**
 * Infinispan based shared attribute map.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class SharedAttributeMap implements Map<String, Serializable>
{
   private String prefix;
   private Map<String, Serializable> delegate;
   private Set<String> usedKeys;

   SharedAttributeMap(String prefix, Map<String, Serializable> delegate)
   {
      this.prefix = prefix;
      this.delegate = delegate;
      this.usedKeys = new ConcurrentHashSet<String>();
   }

   private String toKey(Object key)
   {
      return prefix + key;
   }

   public int size()
   {
      return usedKeys.size();
   }

   public boolean isEmpty()
   {
      return usedKeys.isEmpty();
   }

   @SuppressWarnings({"SuspiciousMethodCalls"})
   public boolean containsKey(Object key)
   {
      return key != null && usedKeys.contains(key);
   }

   public boolean containsValue(Object value)
   {
      if (value == null)
         return false;

      for (String key : usedKeys)
      {
         Serializable kv = get(key);
         if (value.equals(kv))
            return true;
      }
      return false;
   }

   public Serializable get(Object key)
   {
      return delegate.get(toKey(key));
   }

   public Serializable put(String key, Serializable value)
   {
      Serializable result = delegate.put(toKey(key), value);
      usedKeys.add(key);
      return result;
   }

   @SuppressWarnings({"SuspiciousMethodCalls"})
   public Serializable remove(Object key)
   {
      Serializable result = delegate.remove(toKey(key));
      usedKeys.remove(key);
      return result;
   }

   public void putAll(Map<? extends String, ? extends Serializable> m)
   {
      delegate.putAll(m);
      usedKeys.addAll(m.keySet());
   }

   public void clear()
   {
      delegate.clear();
      usedKeys.clear();
   }

   public Set<String> keySet()
   {
      return usedKeys;
   }

   public Collection<Serializable> values()
   {
      List<Serializable> values = new ArrayList<Serializable>();
      for (String key : usedKeys)
      {
         Serializable value = get(key);
         values.add(value);
      }
      return values;
   }

   public Set<Entry<String, Serializable>> entrySet()
   {
      return new AbstractSet<Entry<String, Serializable>>()
      {
         public Iterator<Entry<String, Serializable>> iterator()
         {
            final Iterator<String> keyIterator = usedKeys.iterator();

            return new Iterator<Entry<String, Serializable>>()
            {
               private String currentKey;

               public boolean hasNext()
               {
                  return keyIterator.hasNext();
               }

               public Entry<String, Serializable> next()
               {
                  currentKey = keyIterator.next();
                  Serializable value = SharedAttributeMap.this.get(currentKey);
                  return new AbstractMap.SimpleEntry<String, Serializable>(currentKey, value);
               }

               public void remove()
               {
                  SharedAttributeMap.this.remove(currentKey);
               }
            };
         }

         public int size()
         {
            return SharedAttributeMap.this.size();
         }
      };
   }
}
