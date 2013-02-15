<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="2.0">
  <xsl:output method="html"/>
  
  <xsl:template match="/">
    <xsl:apply-templates select="results/group"/>
  </xsl:template>
  
  <xsl:template match="group">
    <div>
      <h3>Tag : <xsl:value-of select="@tag"/></h3>
      <ul>
        <xsl:apply-templates select="news"/>
      </ul>
    </div>
  </xsl:template>
  
  <xsl:template match="news">
    <li>
      <div class="news">
        <div class="title"><a href="{url/text()}"><xsl:value-of select="title/text()"/></a></div>        
        <div class="publishDate">published on <span class="time"><xsl:value-of select="publishDate/text()"/></span></div>
      </div>
    </li>
  </xsl:template>
</xsl:stylesheet>