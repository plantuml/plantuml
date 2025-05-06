package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;

public class StrlenTest {
    
    @ParameterizedTest(name = "[{index}] Strlen({0}) = {1}")
    @CsvSource({
        " ''               , 0 ",
        " 0                , 1 ",
        " ABC              , 3 ",
        " HelloWorld       , 10",
        " 0123456789ABCDEF , 16",
        " '1+1'            , 3 ",              
        " '!if ($i == 1)'  , 13", 
        " '\n\t\\'         , 3 ", 
        " 'Aa!$*+-/=''\"'  , 11",  // Basic Latin (Range: 0000–007F) *ASCII
        " 'Àà¥©'           , 4 ",  // Latin-1 Supplement (Range: 0080–00FF)
        " '∀⊂'             , 2 ",  // Mathematical Operators (Range: 2200–22FF)
        " '😀'             , 2 ",  // Emoticons (Range: 1F600–1F64F) *Surrogate pair
        " 'x́'              , 2 ",  // Combining Diacritical Marks (Range: 0300–036F)
    })
    public void testStrlen(String input, String expected) throws EaterException {
        Strlen strlenFunction = new Strlen();
        assertTimExpectedOutputFromInput(strlenFunction, input, expected);
    }
    
}
