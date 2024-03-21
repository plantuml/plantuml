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

entity CUSTOMER {
  Number <<key>>
  Bonus <<derived>>
  Name <<multi>>
}

entity MOVIE {
  Code
}

relationship RENTED_TO {
  Date
}

RENTED_TO -1- CUSTOMER
RENTED_TO -N- MOVIE
RENTED_TO -(N,M)- DIRECTOR

@endchen
"""

 */
public class ChenMovie_Test extends BasicTest {

	@Test
	void testSimple() throws IOException {
		checkImage("(16 entities)");
	}

}
