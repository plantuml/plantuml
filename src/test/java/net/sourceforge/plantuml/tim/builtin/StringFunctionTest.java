package net.sourceforge.plantuml.tim.builtin;

import static net.sourceforge.plantuml.tim.TimTestUtils.assertTimExpectedOutputFromInput;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.sourceforge.plantuml.tim.EaterException;

public class StringFunctionTest {

    @ParameterizedTest(name = "[{index}] StringFunction({0}) = {1}")
    @CsvSource({
        " ''               , '' ",
        " 0                , 0 ",
        " ABC              , ABC ",
        " HelloWorld       , HelloWorld ",
        " 0123456789ABCDEF , 0123456789ABCDEF ",
        " '1+1'            , '1+1' ",
        " '!if ($i == 1)'  , '!if ($i == 1)' ", 
        " '\n\t\\'         , '\n\t\\' ", 
        " 'Aa!$*+-/=''\"'  , 'Aa!$*+-/=''\"' ", // Basic Latin (Range: 0000–007F) *ASCII
        " 'Àà¥©'           , 'Àà¥©' ",  // Latin-1 Supplement (Range: 0080–00FF)
        " '∀⊂'             , '∀⊂' ",  // Mathematical Operators (Range: 2200–22FF)
        " '😀'             , '😀' ",  // Emoticons (Range: 1F600–1F64F) *Surrogate pair
        " 'x́'              , 'x́' ",  // Combining Diacritical Marks (Range: 0300–036F)
     })
    public void testStringFunction(String input, String expected) throws EaterException {
        StringFunction stringFunction = new StringFunction();
        assertTimExpectedOutputFromInput(stringFunction, input, expected);
    }
    
}
