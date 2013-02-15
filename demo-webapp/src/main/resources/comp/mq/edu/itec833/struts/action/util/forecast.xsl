<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="2.0">
  <xsl:output method="html"/>
  
  <xsl:template match="/">
    <xsl:apply-templates select="CurrentWeather"/>
  </xsl:template>
  
  <xsl:template match="CurrentWeather">
    <div class="forecast">
      <table border="1">
        <tr>
          <th>Attribute</th><th>Value</th>
        </tr>
        <tr>
          <td>Wind</td>
          <td><xsl:value-of select="Wind"/></td>
        </tr>
        <tr>
          <td>Visibility</td>
          <td><xsl:value-of select="Visibility"/></td>
        </tr>
        <tr>
          <td>Temperature</td>
          <td><xsl:value-of select="Temperature"/></td>
        </tr>
        <tr>
          <td>Dew Point</td>
          <td><xsl:value-of select="DewPoint"/></td>
        </tr>
        <tr>
          <td>Relative Humidity</td>
          <td><xsl:value-of select="RelativeHumidity"/></td>
        </tr>
        <tr>
          <td>Pressure</td>
          <td><xsl:value-of select="Pressure"/></td>
        </tr>
      </table>
    </div>
  </xsl:template>
</xsl:stylesheet>