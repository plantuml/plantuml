<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes" encoding="UTF-8"/>
  
  <xsl:template match="/JDepend">
    <html>
      <head>
        <meta charset="UTF-8"/>
        <title>JDepend Analysis Report</title>
        <style>
          body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }
          h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
          h2 { color: #666; margin-top: 30px; }
          .summary { background: white; padding: 20px; border-radius: 5px; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
          table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin: 20px 0; }
          th { background: #4CAF50; color: white; padding: 12px; text-align: left; font-weight: bold; }
          td { padding: 10px; border-bottom: 1px solid #ddd; }
          tr:hover { background: #f5f5f5; }
          .numeric { text-align: center; font-family: 'Courier New', monospace; }
          .package-name { font-weight: bold; color: #333; }
          .good { color: #4CAF50; font-weight: bold; }
          .warning { color: #FF9800; font-weight: bold; }
          .bad { color: #F44336; font-weight: bold; }
          .info { background: #E3F2FD; padding: 15px; border-left: 4px solid #2196F3; margin: 20px 0; }
          .metric-desc { font-size: 0.9em; color: #666; margin: 5px 0; }
          .cycles { background: #FFEBEE; padding: 15px; border-left: 4px solid #F44336; margin: 20px 0; }
        </style>
      </head>
      <body>
        <h1>JDepend Analysis Report</h1>
        
        <div class="summary">
          <p><strong>Total Packages Analyzed: </strong><xsl:value-of select="count(Packages/Package)"/></p>
        </div>
        
        <div class="info">
          <h3>Metric Definitions:</h3>
          <div class="metric-desc"><strong>TC:</strong> Total Classes - Number of classes and interfaces in the package</div>
          <div class="metric-desc"><strong>CC:</strong> Concrete Classes - Number of concrete classes</div>
          <div class="metric-desc"><strong>AC:</strong> Abstract Classes - Number of abstract classes and interfaces</div>
          <div class="metric-desc"><strong>Ca:</strong> Afferent Couplings - Number of packages that depend on this package</div>
          <div class="metric-desc"><strong>Ce:</strong> Efferent Couplings - Number of packages this package depends on</div>
          <div class="metric-desc"><strong>A:</strong> Abstractness (0-1) - Ratio of abstract classes to total classes</div>
          <div class="metric-desc"><strong>I:</strong> Instability (0-1) - Ratio Ce/(Ca+Ce), 0=stable, 1=unstable</div>
          <div class="metric-desc"><strong>D:</strong> Distance from Main Sequence (0-1) - |A+I-1|, ideal=0, problematic&gt;0.5</div>
        </div>
        
        <xsl:if test="Cycles/Package">
          <div class="cycles">
            <h3>⚠️ Dependency Cycles Detected</h3>
            <xsl:for-each select="Cycles/Package">
              <p><strong><xsl:value-of select="@name"/></strong></p>
            </xsl:for-each>
          </div>
        </xsl:if>
        
        <h2>Package Metrics</h2>
        <table>
          <thead>
            <tr>
              <th>Package</th>
              <th class="numeric">TC</th>
              <th class="numeric">CC</th>
              <th class="numeric">AC</th>
              <th class="numeric">Ca</th>
              <th class="numeric">Ce</th>
              <th class="numeric">A</th>
              <th class="numeric">I</th>
              <th class="numeric">D</th>
            </tr>
          </thead>
          <tbody>
            <xsl:apply-templates select="Packages/Package"/>
          </tbody>
        </table>
        
        <div class="info" style="margin-top: 30px;">
          <p><strong>Color Coding for Distance (D):</strong></p>
          <p><span class="good">■</span> Green (0-0.2): Good - Package is well-balanced</p>
          <p><span class="warning">■</span> Orange (0.2-0.5): Warning - Consider reviewing package design</p>
          <p><span class="bad">■</span> Red (&gt;0.5): Bad - Package is in Zone of Pain or Zone of Uselessness</p>
        </div>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="Package">
    <tr>
      <td class="package-name"><xsl:value-of select="@name"/></td>
      <td class="numeric"><xsl:value-of select="Stats/TotalClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/ConcreteClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/AbstractClasses"/></td>
      <td class="numeric"><xsl:value-of select="Stats/Ca"/></td>
      <td class="numeric"><xsl:value-of select="Stats/Ce"/></td>
      <td class="numeric"><xsl:value-of select="format-number(Stats/A, '0.00')"/></td>
      <td class="numeric"><xsl:value-of select="format-number(Stats/I, '0.00')"/></td>
      <td class="numeric">
        <xsl:attribute name="class">
          <xsl:text>numeric </xsl:text>
          <xsl:choose>
            <xsl:when test="Stats/D &lt;= 0.2">good</xsl:when>
            <xsl:when test="Stats/D &lt;= 0.5">warning</xsl:when>
            <xsl:otherwise>bad</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:value-of select="format-number(Stats/D, '0.00')"/>
      </td>
    </tr>
  </xsl:template>
  
</xsl:stylesheet>