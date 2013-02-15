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
        <xsl:apply-templates select="tweet"/>
      </ul>
    </div>
  </xsl:template>
  
  <xsl:template match="tweet">
    <li>
      <div class="tweet">
        <div class="image">
          <a href="{author/authorUrl/text()}">
            <img src="{author/authorPicUrl/text()}" width="60" height="60"/>
          </a>
        </div>
        <div class="content">
          <div class="author">
            <span class="authorname"><xsl:value-of select="author/authorName/text()"/></span> on <span class="time"><xsl:value-of select="publishDate/text()"/></span>
          </div>
          <div class="text">
            <a href="{url/text()}">
              <xsl:value-of select="title/text()"/>
            </a>
          </div>
        </div>
        <div class="empty">&#160;</div>
      </div>
    </li>
  </xsl:template>
</xsl:stylesheet>