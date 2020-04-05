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

/**
 *  An implementation of <tt>ConfigData</tt> based on a flat
 *  representation of the hierachically organized key-value pairs.
 *  Concrete subclasses must implement the methods
 *  {@link #getValue} and {@link #createConfigData} in accordance
 *  with the Template Method pattern and Factory Method pattern,
 *  respectively.
 *  <p>
 *  In a flat representation of hierachically organized key-value
 *  pairs all key-value pairs are stored in a single <tt>Hashtable</tt>.
 *  Its key is the <em>full key</em> of the configuration data (i.e. the key
 *  including its path).
 *  <p>
 *  Example (using the notation for a <tt>.properties</tt> file):
 *  <pre>
 *  title = example
 *  symbolAttributes/className = jcckit.graphic.BasicDrawingAttributes
 *  symbolAttributes/fillColor = 0xcaffee
 *  symbolAttributes/lineColor = 0xff0000
 *  </pre>
 *  The following table shows the result of some method calls at a
 *  <tt>FlatConfigData</tt> instance prepared with
 *  this example:
 *  <p>
 *  <center>
 *  <table border=1 cellspacing=1 cellpadding=5>
 *  <tr><th>Method call</th><th>Result</th></tr>
 *  <tr><td>get(&quot;title&quot;)</td><td>example</td></tr>
 *  <tr><td>getNode(&quot;symbolAttributes&quot;).get(&quot;fillColor&quot;)
 *      </td><td>0xcaffee</td></tr>
 *  </table>
 *  </center>
 *  <p>
 *  In addition <tt>FlatConfigData</tt> implements <b>inheritance</b>
 *  of key-value pairs.
 *  Basically a node in the tree of key-value pairs
 *  may extend another node in the tree.
 *  The extended node inherit all key-value pairs from the extending
 *  one including the key-value pairs of all descendants.
 *  The value of a inherited key-value pair may be overridden.
 *  Also new key-value pairs may be placed in the inherited node or
 *  anywhere in the subtree.
 *  Note, that the extending node has to be a node which is not a
 *  descendant of the extended node (otherwise a circulary chain
 *  of references occurs). As a consequence not more than 20 inheritance
 *  levels are allowed.
 *  <p>
 *  The implementation of this kind of inheritance in a flat hashtable
 *  is done by an additional key-value pair of the form
 *  <pre>
 *    <i>extending-node</i><b>/</b> = <i>extended-node</i><b>/</b>
 *  </pre>
 *  Example:
 *  <pre>
 *  A/a/priority = high
 *  A/a/alpha/hello = universe
 *  A/a/alpha/answer = 42
 *  <b>A/b/1/ = A/a/</b>
 *  A/b/1/alpha/hello = world
 *  A/b/1/alpha/question = 6 * 7
 *  </pre>
 *  The following table shows the result of various method calls
 *  applied at the node <tt>A/b/1/</tt> of a <tt>FlatConfigData</tt> 
 *  instance prepared with this example:
 *  <p>
 *  <center>
 *  <table border=1 cellspacing=1 cellpadding=5>
 *  <tr><th>Method call</th><th>Result</th><th>Comment</th></tr>
 *  <tr><td>get(&quot;priority&quot;)</td><td>high</td><td>inherited</td></tr>
 *  <tr><td>getNode(&quot;alpha&quot;).get(&quot;hello&quot;)
 *      </td><td>world</td><td>overridden</td></tr>
 *  <tr><td>getNode(&quot;alpha&quot;).get(&quot;question&quot;)
 *      </td><td>6 * 7</td><td>added</td></tr>
 *  <tr><td>getNode(&quot;alpha&quot;).get(&quot;answer&quot;)
 *      </td><td>42</td><td>inherited</td></tr>
 *  </table>
 *  </center>
 *
 *  @author Franz-Josef Elmer
 */
public abstract class FlatConfigData implements ConfigData {
  private final String _path;

  /** Creates a new instance for the specified path. */
  public FlatConfigData(String path) {
    _path = path;
  }

  /**
   * Returns the full key.
   * @param key A (relative) key. <tt>null</tt> is not allowed.
   * @return the path concatenated with <tt>key</tt> or <tt>key</tt>
   *         if the path is undefined.
   */
  public String getFullKey(String key) {
    return _path == null ? key : _path + key;
  }

  /**
   * Returns the value associated with this key.
   * @param key The relative key. <tt>null</tt> is not allowed.
   * @return the associated value. Will be <tt>null</tt> if no value exists
   *         for <tt>key</tt>.
   */
  public String get(String key) {
    return get(_path, key, 0);
  }

  /** 
   * Obtains a value in accordance with hierarchy (<tt>path</tt>) and
   * inheritance (recursive calls of this routine).
   */
  private String get(String path, String key, int numberOfLevels) {
    String result = null;
    if (numberOfLevels < 20) {
      String fullKey = path == null ? key : path + key;
      result = getValue(fullKey);
      if (result == null) {
        // posAfterDelim is the index in path just after '/'
        int posAfterDelim = path == null ? -1 : path.length();
        String replacement;
        while (posAfterDelim > 0) {
          // look for a sub-tree
          replacement = getValue(path.substring(0, posAfterDelim));
          if (replacement != null) {
            // sub-tree found, add last part of the original path
            result = get(replacement + path.substring(posAfterDelim), key,
                         numberOfLevels + 1);
            // break whether result is null or not.
            break;
          }
          // remove last element from the path
          posAfterDelim = path.lastIndexOf('/', posAfterDelim - 2) + 1;
        }
      }
    }
    return result;
  }

  /**
   * Returns the <tt>ConfigData</tt> object associated with this key.
   * @param key The relative key.
   * @return the associated value. Will never return <tt>null</tt>.
   *         Instead an empty <tt>ConfigData</tt> is returned.
   */
  public ConfigData getNode(String key) {
    String path = (_path == null ? key : _path + key) + '/';
    return createConfigData(path);
  }

  /**
   * Returns the value for the specified full key from the flat
   * representation of the hierarchically organized key-value pairs.
   * @param fullKey The full key including path. <tt>null</tt> is not allowed.
   * @return the value or <tt>null</tt> if not found.
   */
  protected abstract String getValue(String fullKey);

  /**
   * Returns the <tt>FlatConfigData</tt> object for the specified full path.
   * In general <tt>path</tt> will be used in the constructor with
   * path argument.
   * @param path The full path.
   * @return a new instance in any case.
   */
  protected abstract ConfigData createConfigData(String path);
}
