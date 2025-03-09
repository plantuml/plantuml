package nonreg.simple;

import nonreg.BasicTest;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/*

Diagram under test (has to be in tripple quotes """):

"""
@startuml
interface Map<K,V>
class HashMap<Long,Customer>

Map <|.. HashMap
Shop [customerId: long] ---> "customer\n1" Customer
HashMap [id: Long] -r-> "value" Customer
@enduml
"""

*/

public class QualifiedAssoc002_Test extends BasicTest {

	@Test
	void testIssue1491() throws IOException {
		String tmp = runPlantUML("(4 entities)");
		checkImage("(4 entities)");
	}

}
