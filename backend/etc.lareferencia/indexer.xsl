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
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    
    exclude-result-prefixes="oai_dc dc">
    
    <xsl:output method="xml" indent="yes" encoding="utf-8"/>
    
    <!-- Aquí van los listados para diferenciar type en tipo de documento y status -->    
	<xsl:variable name="type_list">info:eu-repo/semantics/article,info:eu-repo/semantics/masterThesis,info:eu-repo/semantics/doctoralThesis,info:eu-repo/semantics/report</xsl:variable>
	<xsl:variable name="status_list">info:eu-repo/semantics/draft,info:eu-repo/semantics/acceptedVersion,info:eu-repo/semantics/submittedVersion,info:eu-repo/semantics/publishedVersion,info:eu-repo/semantics/updatedVersion</xsl:variable>
	   
	<!--  Aquí se definen los prefijos utilizados para detectar contenidos con trato diferencial -->   
	<xsl:variable name="driver_prefix">info:eu-repo/semantics/</xsl:variable> 
	<xsl:variable name="reponame_prefix">reponame:</xsl:variable>  
	<xsl:variable name="instname_prefix">instname:</xsl:variable>  
	
	<xsl:param name="country" />
	<xsl:param name="country_iso" />
	
	<xsl:param name="solr_id" />
	<xsl:param name="vufind_id" />
	<xsl:param name="header_id" />
	
	 
    <xsl:template match="oai_dc:dc">
            <doc>
                <!-- ID es parámetro -->
                <field name="id">
                	<xsl:value-of select="$solr_id"/>
                </field>
                
                <!-- ID es parámetro -->
                <field name="oid">
                	<xsl:value-of select="$vufind_id"/>
                </field>
                
                <!-- ID es parámetro -->
                <field name="oai_identifier">
                	<xsl:value-of select="$header_id"/>
                </field>         
	         
                <!-- COUNTRY es parámetro -->
                <field name="country">
                	<xsl:value-of select="$country"/>
                </field>         
	
                <!-- COUNTRY_ISO es parámetro -->
                <field name="country_iso">
                	<xsl:value-of select="$country_iso"/>
                </field>        
                
                <!-- FULLRECORD Puede ser agregado desde java, pero hay que evaluar el peso para SolR -->
                     
                <!-- RECORDTYPE -->
                <field name="recordtype">driver</field>         

                <!-- ALLFIELDS -->
                <field name="allfields">
                    <xsl:value-of select="normalize-space(string(//oai_dc:dc))"/>
                </field>

                <!-- Instname y reponame-->
                 <xsl:if test="//dc:source">
                    <xsl:for-each select="//dc:source">
                    	<xsl:choose>
							<xsl:when test="starts-with(., $instname_prefix)">
							 	<field name="instname">
	                    			<xsl:value-of select="substring(., string-length($instname_prefix)+1, string-length(.))" />
	                			</field>
								<field name="institution">
	                    			<xsl:value-of select="substring(., string-length($instname_prefix)+1, string-length(.))" />
	                			</field>
							</xsl:when>
							<xsl:when test="starts-with(., $reponame_prefix)">
							 	<field name="reponame">
	                    			<xsl:value-of select="substring(., string-length($reponame_prefix)+1, string-length(.))" />
	                			</field>
								<field name="collection">
	                    			<xsl:value-of select="substring(., string-length($reponame_prefix)+1, string-length(.))" />
	                			</field>
							</xsl:when>
						</xsl:choose>      
                    </xsl:for-each>
                 </xsl:if>
                
                
                <!-- LANGUAGE -->
                <xsl:if test="//dc:language">
                    <xsl:for-each select="//dc:language">
                        <xsl:if test="string-length() > 0">
							<xsl:choose>
								<xsl:when test="//dc:language/text()='spa'">
								 <field name="language">Español</field>
								</xsl:when>
								<xsl:when test="//dc:language/text()='es'">
								 <field name="language">Español</field>
								</xsl:when>
								<xsl:when test="//dc:language/text()='esp'">
								 <field name="language">Español</field>
								</xsl:when>
								<xsl:when test="//dc:language/text()='in'">
								 <field name="language">Inglés</field>
								</xsl:when>
								<xsl:when test="//dc:language/text()='en'">
								 <field name="language">Inglés</field>
								</xsl:when>
								<xsl:when test="//dc:language/text()='eng'">
								 <field name="language">Inglés</field>
								</xsl:when>								
								<xsl:when test="//dc:language/text()='eng_US'">
								 <field name="language">Inglés</field>
								</xsl:when>								
								<xsl:when test="//dc:language/text()='fr'">
								 <field name="language">Francés</field>
								</xsl:when>								
								<xsl:when test="//dc:language/text()='bg'">
								 <field name="language">Belga</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='bra'">
								 <field name="language">Portugués</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='ca'">
								 <field name="language">Catalán</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='cat'">
								 <field name="language">Catalán</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='da'">
								 <field name="language">Danés</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='de'">
								 <field name="language">Alemán</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='deu'">
								 <field name="language">Alemán</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='fra'">
								 <field name="language">Francés</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='General'">
								 <field name="language">Otro</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='Germanic (Other)'">
								 <field name="language">Alemán</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='hr'">
								 <field name="language">Croata</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='it'">
								 <field name="language">Italiano</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='ita'">
								 <field name="language">Italiano</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='nl'">
								 <field name="language">Holandés</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='Phoenician'">
								 <field name="language">Fenicio</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='por'">
								 <field name="language">Portugués</field>
								</xsl:when>	
								<xsl:when test="//dc:language/text()='pt'">
								 <field name="language">Portugués</field>
								</xsl:when>	
								<xsl:otherwise>
									<field name="language">
										<xsl:value-of select="normalize-space()"/>
									</field>
								</xsl:otherwise>
							</xsl:choose>						

                        </xsl:if>
                    </xsl:for-each>
                </xsl:if>

                <!-- type -->
               <xsl:for-each select="//dc:type">
                   <xsl:if test="starts-with(., $driver_prefix)">
                   
                  	 <xsl:choose>
						<xsl:when test="contains($type_list, .)">
							<xsl:choose>
								<xsl:when test="//dc:type/text()='info:eu-repo/semantics/article'">
								 <field name="format">Artículo</field><field name="type">Artículo</field>
								</xsl:when>
								<xsl:when test="//dc:type/text()='info:eu-repo/semantics/masterThesis'">
								 <field name="format">Tesis de Maestría</field><field name="type">Tesis de Maestría</field>
								</xsl:when>
								<xsl:when test="//dc:type/text()='info:eu-repo/semantics/doctoralThesis'">
								 <field name="format">Tesis de Doctorado</field><field name="type">Tesis de Doctorado</field>
								</xsl:when>
								<xsl:otherwise>
									<field name="format">Reporte</field><field name="type">Reporte</field>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="contains($status_list, .)">
							  <field name="status">
                           			<xsl:value-of select="substring(., string-length($driver_prefix)+1, string-length(.))" />
                       		  </field>
						</xsl:when>
						
					</xsl:choose>
           
                   </xsl:if>
               </xsl:for-each>
 
 
                <!-- rights -->
               <xsl:for-each select="//dc:rights">
            
                   <xsl:choose>
						<xsl:when test="starts-with(., $driver_prefix)">
							<field name="eu_rights">
                           		<xsl:value-of select="substring(., string-length($driver_prefix)+1, string-length(.))" />
							</field>
						</xsl:when>
						<xsl:otherwise>
							<field name="rights">
								<xsl:value-of select="." />
							</field>
						</xsl:otherwise>
					</xsl:choose>
                   
               </xsl:for-each>

                <!-- AUTHOR -->
                <xsl:if test="//dc:creator">
                    <xsl:for-each select="//dc:creator">
                        <xsl:if test="normalize-space()">
                            <!-- author is not a multi-valued field, so we'll put
                                 first value there and subsequent values in author2.
                             -->
                            <xsl:if test="position()=1">
                                <field name="author">
                                    <xsl:value-of select="normalize-space()"/>
                                </field>
                                <field name="author-letter">
                                    <xsl:value-of select="normalize-space()"/>
                                </field>
                            </xsl:if>
                            <xsl:if test="position()>1">
                                <field name="author2">
                                    <xsl:value-of select="normalize-space()"/>
                                </field>
                            </xsl:if>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:if>
                
                 <!-- AUTHOR2 - Contributor -->
                <xsl:if test="//dc:contributor">
                    <xsl:for-each select="//dc:contributor">
                                <field name="author2">
                                    <xsl:value-of select="normalize-space()"/>
                                </field>
                    </xsl:for-each>
                </xsl:if>
                

                <!-- TITLE -->
                <xsl:if test="//dc:title[normalize-space()]">
                    <field name="title">
                        <xsl:value-of select="//dc:title[normalize-space()]"/>
                    </field>
                    <field name="title_short">
                        <xsl:value-of select="//dc:title[normalize-space()]"/>
                    </field>
                    <field name="title_full">
                        <xsl:value-of select="//dc:title[normalize-space()]"/>
                    </field>
                    <field name="title_sort">
                        <xsl:value-of select="//dc:title[normalize-space()]"/>
                    </field>
                </xsl:if>

                <!-- PUBLISHER -->
                <xsl:if test="//dc:publisher[normalize-space()]">
                    <field name="publisher">
                        <xsl:value-of select="//dc:publisher[normalize-space()]"/>
                    </field>
                </xsl:if>

                <!-- SUBJECT -->
                <xsl:if test="//dc:subject">
                    <xsl:for-each select="//dc:subject">
                        <xsl:if test="string-length(normalize-space()) > 0">
                            <field name="topic">
                                <xsl:value-of select="normalize-space()"/>
                            </field>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:if>

                <!-- DESCRIPTION -->
                <xsl:if test="//dc:description">
                    <field name="description">
                        <xsl:value-of select="//dc:description" />
                    </field>
                </xsl:if>
				
				
                <!-- PUBLISHDATE -->
                <xsl:if test="//dc:date">
                    <field name="publishDate">
                        <xsl:value-of select="substring(//dc:date, 1, 4)"/>
                    </field>
                    <field name="publishDateSort">
                        <xsl:value-of select="substring(//dc:date, 1, 4)"/>
                    </field>
                </xsl:if>

                <!-- URL -->
				<xsl:for-each select="//dc:identifier">
					<xsl:choose>
						<xsl:when test="starts-with(., 'http://')">
							<field name="url">
								<xsl:value-of select="." />
							</field>
						</xsl:when>
						<xsl:otherwise>
							<field name="identifier">
								<xsl:value-of select="." />
							</field>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
            </doc>
    </xsl:template>
</xsl:stylesheet>
