package net.sourceforge.plantuml.xmi;

public enum UMLAggregationKind {
		None(""), Aggregation("ak_aggregate"), Composite("ak_composite");

		public final String name;

		private UMLAggregationKind(String umlName) {
			this.name = umlName;
		}
}
