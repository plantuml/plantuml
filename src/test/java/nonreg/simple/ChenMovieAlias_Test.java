package nonreg.simple;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nonreg.BasicTest;

/*

Test diagram MUST be put between triple quotes

"""
@startchen movies

entity "Director" as DIRECTOR {
  "No." as Number <<key>>
  Name {
    Fname
    Lname
  }
  Born : DATE
  Died
  Age
}

entity "Customer" as CUSTOMER {
  Number <<key>>
  Bonus <<derived>>
  Name <<multi>>
}

entity "Movie" as MOVIE {
  Code
}

relationship "was-rented-to" as RENTED_TO {
  Date
}

RENTED_TO -1- CUSTOMER
RENTED_TO -N- MOVIE
RENTED_TO -(N,M)- DIRECTOR

entity "Parent" as PARENT {
}

entity "Member" as MEMBER {
}

CUSTOMER ->- PARENT
MEMBER -<- CUSTOMER

entity "Kid" as CHILD <<weak>> {
  Name <<key>>
}

relationship "is-parent-of" as PARENT_OF <<identifying>> {
}

PARENT_OF -1- PARENT
PARENT_OF =N= CHILD

entity "Little Kid" as TODDLER {
  FavoriteToy
}

entity "Primary-Aged Kid" as PRIMARY_AGE {
  FavoriteColor
}

entity "Teenager" as TEEN {
  Hobby
}

CHILD =>= d { TODDLER, PRIMARY_AGE, TEEN }

entity "Human" as PERSON {
}

PERSON ->- U { CUSTOMER, DIRECTOR }

@endchen
"""

 */
public class ChenMovieAlias_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(30 entities)");
	}

}
