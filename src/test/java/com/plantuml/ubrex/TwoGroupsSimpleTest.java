package com.plantuml.ubrex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class TwoGroupsSimpleTest {

    @Test
    public void testNoMatch() {
        UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〘a〘〇+b〙c〙9");
        UMatcher matcher = cut.match(TextNavigator.build("09"));
        assertEquals("", matcher.getAcceptedMatch());
    }

    @Test
    public void testTwoGroupsGroupMatch() {
        UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〘a〘〇+b〙c〙9");
        UMatcher matcher = cut.match(TextNavigator.build("0abc9"));
        assertEquals("0abc9", matcher.getAcceptedMatch());
    }


    @Test
    public void testTwoGroupsGroupMatch2() {
        UnicodeBracketedExpression cut = UnicodeBracketedExpression.build("0〘a〘〇+b〙c〙9");
        UMatcher matcher = cut.match(TextNavigator.build("0abbbc9"));
        assertEquals("0abbbc9", matcher.getAcceptedMatch());
    }

}
