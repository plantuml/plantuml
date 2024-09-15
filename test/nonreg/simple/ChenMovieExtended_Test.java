package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startchen movies

entity DIRECTOR {
  Number <<key>>
  Name {
    Fname
    Lname
  }
  Born : DATE
  Died
  Age
}

entity CUSTOMER #lightblue;line:blue {
  Number <<key>> #lime;line:orange
  Bonus <<derived>>
  Name <<multi>>
}

entity MOVIE {
  Code
}

relationship RENTED_TO #pink;line:red {
  Date
}

RENTED_TO -1- CUSTOMER #lime
RENTED_TO -N- MOVIE
RENTED_TO -(N,M)- DIRECTOR

entity PARENT {
}

entity MEMBER {
}

CUSTOMER ->- PARENT #line:purple
MEMBER -<- CUSTOMER #line:purple

entity CHILD <<weak>> {
  Name <<key>>
}

relationship PARENT_OF <<identifying>> {
}

PARENT_OF -1- PARENT
PARENT_OF =N= CHILD

entity TODDLER {
  FavoriteToy
}

entity PRIMARY_AGE {
  FavoriteColor
}

entity TEEN {
  Hobby
}

CHILD =>= d { TODDLER, PRIMARY_AGE, TEEN }

entity PERSON {
}

PERSON ->- U { CUSTOMER, DIRECTOR } #lime;line:green

@endchen
"""

 */
public class ChenMovieExtended_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(30 entities)");
	}

}
