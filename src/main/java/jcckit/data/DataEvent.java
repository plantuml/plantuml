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

/**
 *  Event to be sent to a {@link DataListener}.
 *
 *  @author Franz-Josef Elmer
 */
public class DataEvent {
  private final DataContainer _container;
  private final DataEventType _type;
  private final int _index;
  private final DataElement _deletedElement;

  /**
   *  Creates an instance for the specified parameters.
   *  @param container The container which has been changed.
   *  @param type Type of change.
   *  @param index Index of the element which has been added, inserted,
   *               replaced, or removed.
   *  @param deletedElement Element which has been replaced or removed.
   */
  private DataEvent(DataContainer container, DataEventType type, int index,
                    DataElement deletedElement) {
    _container = container;
    _type = type;
    _index = index;
    _deletedElement = deletedElement;
  }

  /**
   *  Creates an event of type {@link DataEventType#ELEMENT_ADDED} for the
   *  specified container.
   *  @param container Container where an element has been added.
   *  @return <tt>ELEMENT_ADDED</tt> event.
   */
  public static final DataEvent createAddEvent(DataContainer container) {
    return new DataEvent(container, DataEventType.ELEMENT_ADDED,
                         container.getNumberOfElements() - 1, null);
  }

  /**
   *  Creates an event of type {@link DataEventType#ELEMENT_INSERTED} for the
   *  specified container.
   *  @param container Container where an element has been inserted.
   *  @param index Index at which an element has been inserted.
   *  @return <tt>ELEMENT_INSERTED</tt> event.
   */
  public static final DataEvent createInsertEvent(DataContainer container,
                                                  int index) {
    return new DataEvent(container, DataEventType.ELEMENT_INSERTED, index, 
                         null);
  }

  /**
   *  Creates an event of type {@link DataEventType#ELEMENT_REPLACED} for the
   *  specified container.
   *  @param container Container where an element has been replaced.
   *  @param index Index of the replaced element.
   *  @param replacedElement The previous element at <tt>index</tt>.
   *  @return <tt>ELEMENT_REPLACED</tt> event.
   */
  public static final DataEvent createReplaceEvent(DataContainer container,
                                      int index, DataElement replacedElement) {
    return new DataEvent(container, DataEventType.ELEMENT_REPLACED, index, 
                         replacedElement);
  }

  /**
   *  Creates an event of type {@link DataEventType#ELEMENT_REMOVED} for the
   *  specified container.
   *  @param container Container where an element has been removed.
   *  @param index Index of the removed element.
   *  @param removedElement The previous element at <tt>index</tt>.
   *  @return <tt>ELEMENT_REMOVED</tt> event.
   */
  public static final DataEvent createRemoveEvent(DataContainer container,
                                      int index, DataElement removedElement) {
    return new DataEvent(container, DataEventType.ELEMENT_REMOVED, index, 
                         removedElement);
  }

  /** Returns the container. */
  public DataContainer getContainer() {
    return _container;
  }

  /** 
   * Returns the event type. Will be one of the constants 
   * {@link DataEventType#ELEMENT_ADDED}, 
   * {@link DataEventType#ELEMENT_INSERTED}, 
   * {@link DataEventType#ELEMENT_REMOVED}, or 
   * {@link DataEventType#ELEMENT_REPLACED}.
   */
  public DataEventType getType() {
    return _type;
  }

  /** Returns the index. */
  public int getIndex() {
    return _index;
  }

  /**
   *  Returns the deleted element.
   *  @return <tt>null</tt> if either an element has been added or inserted.
   */
  public DataElement getDeletedElement() {
    return _deletedElement;
  }
}
