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
package jcckit.data;

import java.text.MessageFormat;
import java.util.Vector;

/**
 * Abstract superclass of all data containers. A data container holds an
 * ordered list of {@link DataElement DataElements} of the same type.
 * <p>
 * Data elements can be added, inserted, removed, or replaced.
 * Such an action leads to a {@link DataEvent} which will be delivered to
 * all {@link DataListener DataListeners} observing this 
 * <code>DataContainer</code>. If this data container also implements 
 * {@link DataEvent} (as {@link DataCurve} does) also the listeners
 * registrated at the data container containg this container will be notified.
 * As a consequence a <tt>DataListener</tt> must only be registered at the
 * {@link DataPlot} instance and it will automatically also received events
 * caused by manipulating one of its <tt>DataCurves</tt>.
 * <p>
 * Concrete subclasses have to implement {@link #isValid} which
 * checks whether the added or inserted <tt>DataElement</tt> is of the right
 * type. This is an application of the Template Method Design Pattern.
 *
 * @author Franz-Josef Elmer
 */
public abstract class DataContainer {
  private final static String TEMPLATE
      = "Invalid operation: {0}, Element: {1}, Container: {2}";
  final static String ADD = "add",
                      REPLACE = "replace",
                      INSERT = "insert";

  private final Vector _listeners = new Vector();
  private final Vector _container = new Vector();

  /** Adds a {@link DataListener}. Does nothing if already added. */
  public void addDataListener(DataListener listener) {
    if (!_listeners.contains(listener)) {
      _listeners.addElement(listener);
    }
  }

  /** Removes a {@link DataListener}. Does nothing if already removed. */
  public void removeDataListener(DataListener listener) {
    _listeners.removeElement(listener);
  }

  private void notifyListeners(DataEvent event) {
    for (int i = 0, n = _listeners.size(); i < n; i++) {
      ((DataListener) _listeners.elementAt(i)).dataChanged(event);
    }
    // Notifies also parent container
    if (this instanceof DataElement) {
      DataContainer container = ((DataElement) this).getContainer();
      if (container != null) {
        container.notifyListeners(event);
      }
    }
  }

  /** Returns the number of elements of this container. */
  public int getNumberOfElements() {
    return _container.size();
  }

  /** Returns the element for the specified index. */
  public DataElement getElement(int index) {
    return (DataElement) _container.elementAt(index);
  }
  
  /**
   * Returns the index of the specified element.
   * @param element Element to be looked for.
   * @return -1 if not found.
   */
  public int getIndexOf(DataElement element) {
    return _container.indexOf(element);
  }

  /**
   * Adds a {@link DataElement}. After the element has been successfully
   * added all {@link DataListener DataListeners} will be informed.
   * @param element DataElement to be added.
   * @throws IllegalArgumentException if <tt>element</tt> is not of the correct
   *         type which will be checked by the method {@link #isValid}.
   */
  public void addElement(DataElement element) {
    if (isValid(element)) {
      _container.addElement(element);
      element.setContainer(this);
      notifyListeners(DataEvent.createAddEvent(this));
    } else {
      throwException(ADD, element);
    }
  }

  /**
   * Inserts a {@link DataElement} at the specified index. 
   * After the element has been successfully inserted
   * all {@link DataListener DataListeners} will be informed.
   * @param index Index at which <tt>element</tt> will be inserted.
   *        All elements with an index &gt;= <tt>index</tt> will be shifted.
   * @param element DataElement to be added.
   * @throws IllegalArgumentException if <tt>element</tt> is not of the correct
   *         type which will be checked by the method {@link #isValid}.
   */
  public void insertElementAt(int index, DataElement element) {
    if (isValid(element)) {
      _container.insertElementAt(element, index);
      element.setContainer(this);
      notifyListeners(DataEvent.createInsertEvent(this, index));
    } else {
      throwException(INSERT, element);
    }
  }

  /**
   * Removes a {@link DataElement} at the specified index. 
   * After the element has been successfully removed
   * all {@link DataListener DataListeners} will be informed.
   * @param index Index of the element which will be removed.
   *        All elements with an index &gt; <tt>index</tt> will be shifted.
   */
  public void removeElementAt(int index) {
    DataElement element = (DataElement) _container.elementAt(index);
    element.setContainer(null);
    _container.removeElementAt(index);
    notifyListeners(DataEvent.createRemoveEvent(this, index, element));
  }

  /**
   * Replaces the {@link DataElement} at the specified index. 
   * After the element has been successfully replaced
   * all {@link DataListener DataListeners} will be informed.
   * @param index Index of the element which will be replaced by 
   *        <tt>element</tt>.
   * @param element The new <tt>DataElement</tt>.
   * @throws IllegalArgumentException if <tt>element</tt> is not of the correct
   *         type which will be checked by the method {@link #isValid}.
   */
  public void replaceElementAt(int index, DataElement element) {
    if (isValid(element)) {
      DataElement oldElement = (DataElement) _container.elementAt(index);
      oldElement.setContainer(null);
      _container.setElementAt(element, index);
      element.setContainer(this);
      notifyListeners(DataEvent.createReplaceEvent(this, index, oldElement));
    } else {
      throwException(REPLACE, element);
    }
  }

  private void throwException(String operation, DataElement element) {
    throw new IllegalArgumentException(MessageFormat.format(TEMPLATE,
                new Object[] {operation, element, this.getClass().getName()}));
  }

  /**
   * Returns <tt>true</tt> if the specified {@link DataElement} has the
   * correct type. Concrete subclasses have to implement this method.
   * @param element <tt>DataElement</tt> to be checked.
   */
  protected abstract boolean isValid(DataElement element);
}
