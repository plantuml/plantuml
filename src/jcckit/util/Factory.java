/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.util;

import java.lang.reflect.Constructor;

/**
 *  General purpose factory method based on {@link ConfigParameters}
 *  and Java's Reflection API.
 *
 *  @author Franz-Josef Elmer
 */
public class Factory {
  /** The constant defining the key <tt>className</tt>. */
  public static final String CLASS_NAME_KEY = "className";

  /** No public constructor necessary. */
  private Factory() {}
  
  /**
   * Creates an instance of the specified class.
   * @param className Fully-qualified name of a class with a default 
   *        constructor.
   * @return a new instance.
   * @throws IllegalArgumentException if the instance could be created.
   */
  public static Object create(String className) {
    try {
      return Class.forName(className).newInstance();
    } catch (Throwable t) {
      throw new IllegalArgumentException("Could not create an instance of " 
                                         + className + " because of " + t);
    }
  }

  /**
   *  Creates an object based on the specified configuration
   *  parameters. The class of the object is determined by the
   *  parameter with the key {@link #CLASS_NAME_KEY}.
   *  The constructor with a single argument of the type
   *  <tt>ConfigParameter</tt> is invoked with the argument 
   *  <tt>configParameters</tt>. If such a constructor
   *  does not exists the default constructor is invoked. If
   *  neither of these constructors exist a {@link FactoryException}
   *  is thrown.
   *  @param configParameters Configuration parameters.
   *  @return the newly created object.
   *  @throws IllegalArgumentException if key <tt>className</tt> is missing.
   *  @throws FactoryException wrapping any kind of exception or error occured.
   */
  public static Object create(ConfigParameters configParameters) {
    String className = configParameters.get(CLASS_NAME_KEY);
    return createObject(configParameters, className);
  }

  /**
   *  Creates an object based on the specified configuration
   *  parameters and default class name. If the
   *  parameter with the key {@link #CLASS_NAME_KEY} is missed in
   *  <tt>configParameters</tt> <tt>defaultClassName</tt> is used.
   *  Otherwise it works as {@link #create(jcckit.util.ConfigParameters)}.
   *  @param configParameters Configuration parameters.
   *  @param defaultClassName Default class name.
   *  @return the newly created object.
   *  @throws FactoryException wrapping any kind of exception or error occured.
   */
  public static Object create(ConfigParameters configParameters,
                              String defaultClassName) {
    String className = configParameters.get(CLASS_NAME_KEY, defaultClassName);
    return createObject(configParameters, className);
  }

  /**
   *  Creates an object based on the specified configuration
   *  parameters or returns the default object. This method behaves
   *  as {@link #create(jcckit.util.ConfigParameters)}, except that is does 
   *  not throw an <tt>IllegalArgumentException</tt> if key <tt>className</tt>
   *  is missing. Instead <tt>defaultObject</tt> is returned.
   */
  public static Object createOrGet(ConfigParameters configParameters,
                                   Object defaultObject) {
    String className = configParameters.get(CLASS_NAME_KEY, null);
    return className == null ? defaultObject
                             : createObject(configParameters, className);
  }

  private static Object createObject(ConfigParameters configParameters,
                                     String className) {
    try {
      Class c = Class.forName(className);
      Object result = null;
      Constructor constructor = null;
      try {
        constructor = c.getConstructor(new Class[] {ConfigParameters.class});
        result = constructor.newInstance(new Object[] {configParameters});
      } catch (NoSuchMethodException e) {
        result = c.newInstance();
      }
      return result;
    } catch (Throwable t) {
      throw new FactoryException(configParameters, CLASS_NAME_KEY, t);
    }
  }
}
