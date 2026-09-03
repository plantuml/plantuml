<?xml version="1.0"?>

<!--
  
  Takes the XML output from JDepend and transforms it
  into the 'plantuml' language used by PlantUML
  (https://plantuml.com/)
  to generate a project dependency graph.

  The packages show up as rectangles with the package name
  and the number of classes.  Arrows point to other packages
  the package depends on.  The rectangle is colored blue, but
  it turns to darker shades of red the further the package is
  from the 'main line'.

  Contributed by The-Lum.
  Adapted from 'jdepend2dot.xsl' of David Bock (https://github.com/clarkware/jdepend/blob/master/contrib/jdepend2dot.xsl).

-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text"/>
<xsl:template match="JDepend">
<Root-Element>
@startuml
!pragma layout smetana
title
Jdepend-graph of plantuml
%version()
end title
set separator none
hide empty members
<xsl:apply-templates select="Packages"/>
@enduml
</Root-Element>
</xsl:template>

<xsl:template match="Packages">
    <xsl:apply-templates select="Package" mode="node"/>
</xsl:template>

<xsl:template match="Package" mode="node">
<xsl:text>
class "</xsl:text><xsl:value-of select="@name"/><xsl:text>"</xsl:text>
<xsl:choose>
	<xsl:when test="Stats/D">
		<xsl:text> %lighten("red", </xsl:text>
		<xsl:value-of select="100 - number(Stats/D/.) * 100"/>
		<xsl:text>) </xsl:text>
	</xsl:when>
</xsl:choose>
<xsl:text>{
</xsl:text>
<xsl:choose>
<xsl:when test="Stats/TotalClasses">
	<xsl:text>Total Classes: </xsl:text><xsl:value-of select="Stats/TotalClasses/."/>
</xsl:when>
</xsl:choose>
<xsl:text>
}
</xsl:text>
<xsl:apply-templates select="DependsUpon"/>
</xsl:template>

<xsl:template match="Package" mode="edge">
<xsl:text>"</xsl:text><xsl:value-of select="../../@name"/> <xsl:text>" --&gt; "</xsl:text><xsl:value-of select="."/><xsl:text>"
</xsl:text>
</xsl:template>

<xsl:template match="DependsUpon">
    <xsl:apply-templates select="Package" mode="edge"/>
</xsl:template>

</xsl:stylesheet>
