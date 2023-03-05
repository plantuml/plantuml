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
package jcckit.plot;

/**
 * A plot event signales some changes of a {@link Plot}.
 * It has three attributes:
 * <ul><li><b>source</b>: Indicates the <tt>Plot</tt> instance responsible 
 *         for this event.
 *     <li><b>type</b>: The type of event.
 *     <li><b>message</b>: The message object. Its meaning depends on the 
 *         type of event:
 *         <table border=1 cellpadding=5>
 *           <tr><th>Type</th><th>Meaning of the message object</th></tr>
 *           <tr><td>{@link PlotEventType#DATA_PLOT_CONNECTED}, 
 *                   {@link PlotEventType#DATA_PLOT_DISCONNECTED}</td>
 *               <td>The {@link jcckit.data.DataPlot} (dis)connected with the 
 *                   {@link Plot} instance specified by the source.</td>
 *           <tr><td>{@link PlotEventType#DATA_PLOT_CHANGED}</td>
 *               <td>An <tt>Integer</tt> indicating the lowest index of 
 *                   those curves which have been changed.</td>
 *           <tr><td>{@link PlotEventType#DATA_CURVE_CHANGED}</td>
 *               <td>An <tt>Integer</tt> indicating the index of the curve
 *                   which has been changed.</td>
 *         </table>
 * </ul>
 *  
 *
 * @author Franz-Josef Elmer
 */
public class PlotEvent {
  private final Plot _source;
  private final PlotEventType _type;
  private final Object _message;

  /** 
   * Creates a new event for the specified source, type, and message. 
   * @param source Plot causing this event.
   * @param type Type of the event. Possible values are
   *        {@link PlotEventType#DATA_PLOT_CHANGED}, 
   *        {@link PlotEventType#DATA_CURVE_CHANGED}, 
   *        {@link PlotEventType#DATA_PLOT_CONNECTED}, and 
   *        {@link PlotEventType#DATA_PLOT_DISCONNECTED}.
   * @param message Message object. Can be <tt>null</tt>
   */
  public PlotEvent(Plot source, PlotEventType type, Object message) {
    _source = source;
    _type = type;
    _message = message;
  }

  /** Returns the source of this event. */
  public Plot getSource() {
    return _source;
  }

  /** 
   * Returns the event type. 
   * @return either {@link PlotEventType#DATA_PLOT_CHANGED}, 
   *         {@link PlotEventType#DATA_CURVE_CHANGED},
   *         {@link PlotEventType#DATA_PLOT_CONNECTED}, or 
   *         {@link PlotEventType#DATA_PLOT_DISCONNECTED}.
   */
  public PlotEventType getType() {
    return _type;
  }

  /** Returns the message object. */
  public Object getMessage() {
    return _message;
  }
}
