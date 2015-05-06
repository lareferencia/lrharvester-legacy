<!--
  Copyright (c) 2013 
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the GNU Public License v2.0
  which accompanies this distribution, and is available at
  http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
  
  Contributors:
      Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
      Emiliano Marmonti(emarmonti@gmail.com) - Coordinación del componente III
  
  Este software fue desarrollado en el marco de la consultoría "Desarrollo e implementación de las soluciones - Prueba piloto del Componente III -Desarrollador para las herramientas de back-end" del proyecto “Estrategia Regional y Marco de Interoperabilidad y Gestión para una Red Federada Latinoamericana de Repositorios Institucionales de Documentación Científica” financiado por Banco Interamericano de Desarrollo (BID) y ejecutado por la Cooperación Latino Americana de Redes Avanzadas, CLARA.
-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"    
    exclude-result-prefixes="oai_dc dc">
    
    <xsl:output method="xml" indent="yes" encoding="utf-8"/>

	<xsl:param name="networkAcronym" />
	<xsl:param name="networkName" />
	<xsl:param name="institutionName" />
	<xsl:param name="metadata" />
	
	<xsl:param name="vufind_id" />
	<xsl:param name="header_id" />
	<xsl:param name="record_id" />
	
	<xsl:strip-space elements="*"/>
	 
    <xsl:template match="oai_dc:dc">
    
            <doc>
            
                <!-- ID es parámetro -->
                <field name="item.id"><xsl:value-of select="$record_id"/></field>
               
                <!-- ID es parámetro -->
                <field name="item.handle"><xsl:value-of select="$header_id"/></field>   
                
                <field name="item.lastmodified">2015-04-15T18:45:39.174Z</field>    
                
                <field name="item.submitter">submitter</field>    
                
                <field name="item.deleted">false</field>
                <field name="item.public">true</field>    
                
                <field name="item.collections"><xsl:value-of select="$networkAcronym"/></field>
                
                <field name="item.communities">com_<xsl:value-of select="$networkAcronym"/></field>
                
                <xsl:for-each select="//dc:*">
                <field name="metadata.dc.{substring(name(.),4)}"><xsl:value-of select="." /></field>	   
                </xsl:for-each>
             
                
                 <field name="item.compile">&lt;metadata xmlns="http://www.lyncode.com/xoai" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.lyncode.com/xoai http://www.lyncode.com/xsd/xoai.xsd"&gt;
                  &lt;element name="dc"&gt;
                      <xsl:if test="//dc:title">&lt;element name="title"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:title">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:creator">&lt;element name="creator"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:creator">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:subject">&lt;element name="subject"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:subject">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:description">&lt;element name="description"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:description">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:publisher">&lt;element name="publisher"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:publisher">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:contributor">&lt;element name="contributor"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:contributor">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:date">&lt;element name="date"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:date">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:type">&lt;element name="type"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:type">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:format">&lt;element name="format"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:format">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:identifier">&lt;element name="identifier"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:identifier">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:source">&lt;element name="source"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:source">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:language">&lt;element name="language"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:language">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:relation">&lt;element name="relation"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:relation">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:coverage">&lt;element name="coverage"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:coverage">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                      <xsl:if test="//dc:rights">&lt;element name="rights"&gt;&lt;element name="none"&gt;<xsl:for-each select="//dc:rights">&lt;field name="value"&gt;<xsl:value-of select="replace(.,'&amp;','&amp;amp;')" />&lt;/field&gt;</xsl:for-each>&lt;/element&gt;&lt;/element&gt;</xsl:if>
                   &lt;/element&gt;

                    &lt;element name="bundles" /&gt;

                    &lt;element name="others"&gt;
                        &lt;field name="handle"&gt;<xsl:value-of select="$header_id"/>&lt;/field&gt;
                        &lt;field name="identifier"&gt;<xsl:value-of select="$header_id"/>&lt;/field&gt;
                        &lt;field name="lastModifyDate"&gt;2015-04-15 15:45:39.174&lt;/field&gt;
                    &lt;/element&gt;

                    &lt;element name="repository"&gt;
                        &lt;field name="mail"&gt;mail@mail.com&lt;/field&gt;
                        &lt;field name="name"&gt;<xsl:value-of select="$networkName"/> - <xsl:value-of select="$institutionName"/>&lt;/field&gt;
                    &lt;/element&gt;

                &lt;/metadata&gt;</field>
                </doc>
    </xsl:template>
    
</xsl:stylesheet>
