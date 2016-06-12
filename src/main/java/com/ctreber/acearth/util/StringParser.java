package com.ctreber.acearth.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Cuts a string in words separated by white space. Quotes and square
 * brackets are recognized (that is, white space within is ignored).
 *
 * <p>&copy; 2002 Christian Treber, ct@ctreber.com
 * @author Christian Treber, ct@ctreber.com
 *
 */
public class StringParser
{
  public static List parse(String pLine)
  {
    final List lSections = new ArrayList();

    // True if within word.
    boolean lInSectionP = false;
    // Current char
    char lChar;
    // Wait for this character before switching back to normal parsing.
    char lSeparator = ' ';
    // Part count.
    int lSectionNo = 0;
    // Part start position
    int lSectionStart = 0;
    // Part end position
    int lSectionEnd = 0;

    final int lLen = pLine.length();
    for(int lCharNo = 0; lCharNo <= lLen; lCharNo++)
    {
      if(lCharNo < lLen)
      {
        lChar = pLine.charAt(lCharNo);
      } else
      {
        // This is a fictional last character.
        lChar = ' ';
      }

      if(lInSectionP)
      {
        // In section. Termination is by space or specific separator.
        if((lChar != ' ') || (lSeparator != ' '))
        {
          // It's not a space, or it is a space, but we wait for a special separator.
          if(lChar == lSeparator)
          {
            // We waited for this separator. Switch back to normal parsing.
            lSeparator = ' ';
            lSectionEnd = lCharNo - 1;
          } else
          {
            lSectionEnd = lCharNo;
          }
        } else
        {
          // Section has ended (with a space).
          lSections.add(pLine.substring(lSectionStart, lSectionEnd + 1));
          lSectionNo++;
          lInSectionP = false;
        }
      } else
      {
        // Not in a section, skipping white space.
        if(lChar != ' ')
        {
          // No white space: a section has started.
          if(lChar == '"')
          {
            // Special parsing "string"
            lSeparator = '"';
            lSectionStart = lCharNo + 1;
          } else if(lChar == '[')
          {
            // Special parsing "square brackets"
            lSeparator = ']';
            lSectionStart = lCharNo + 1;
          } else
          {
            // Use normal parsing.
            lSeparator = ' ';
            lSectionEnd = lSectionStart = lCharNo;
          }
          lInSectionP = true;
        } else
        {
          // More void...
        }
      }
    }

    return lSections;
  }
}
