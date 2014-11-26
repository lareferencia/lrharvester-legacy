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
	<xsl:variable name="type_list">info:eu-repo/semantics/article,info:eu-repo/semantics/bachelorThesis,info:eu-repo/semantics/masterThesis,info:eu-repo/semantics/doctoralThesis,info:eu-repo/semantics/book,info:eu-repo/semantics/bookPart,info:eu-repo/semantics/review,info:eu-repo/semantics/conferenceObject,info:eu-repo/semantics/lecture,info:eu-repo/semantics/workingPaper,info:eu-repo/semantics/preprint,info:eu-repo/semantics/report,info:eu-repo/semantics/annotation,info:eu-repo/semantics/contributionToPeriodical,info:eu-repo/semantics/patent,info:eu-repo/semantics/other</xsl:variable>
	<xsl:variable name="status_list">info:eu-repo/semantics/draft,info:eu-repo/semantics/acceptedVersion,info:eu-repo/semantics/submittedVersion,info:eu-repo/semantics/publishedVersion,info:eu-repo/semantics/updatedVersion</xsl:variable>
		   
	<!--  Aquí se definen los prefijos utilizados para detectar contenidos con trato diferencial -->   
	<xsl:variable name="driver_prefix">info:eu-repo/semantics/</xsl:variable> 
	<xsl:variable name="reponame_prefix">reponame:</xsl:variable>  
	<xsl:variable name="instname_prefix">instname:</xsl:variable>  
	
	
	<xsl:param name="networkAcronym" />
	<xsl:param name="networkName" />
	<xsl:param name="institutionName" />
	
	<xsl:param name="vufind_id" />
	<xsl:param name="header_id" />
	
	 
    <xsl:template match="oai_dc:dc">
            <doc>
                <!-- ID es parámetro -->
                <field name="id">
                	<xsl:value-of select="$vufind_id"/>
                </field>
                
                <!-- ID es parámetro -->
                <field name="oai_identifier">
                	<xsl:value-of select="$header_id"/>
                </field>         
	         
	         
	         	<field name="network_acronym">
                	<xsl:value-of select="$networkAcronym"/>
                </field>
                
                <!-- networkName es parámetro -->
                <field name="network_name">
                	<xsl:value-of select="$networkName"/>
                </field>   
	         
	         	<field name="thumbnail">
                       <xsl:value-of select="$networkAcronym"/>_cover.png
                </field> 
	         	
                <!--  field name="instname">
	                <xsl:value-of select="$institutionName"/>
	            </field>
				<field name="institution">
	                <xsl:value-of select="$institutionName"/>
	            </field>
	            
	            <field name="reponame">
	                <xsl:value-of select="$networkName"/>
	            </field>
				<field name="collection">
	                <xsl:value-of select="$networkName"/>
	            </field-->
                           
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
                        <xsl:choose>
	                        <xsl:when test="string-length() = 3">
								<field name="language">
									<xsl:value-of select="normalize-space()"/>
								</field>					
	                        </xsl:when>
	                        <xsl:otherwise>           
	                        	<field name="language_invalid">
									<xsl:value-of select="normalize-space()"/>
								</field>					                    
	                        </xsl:otherwise>
						</xsl:choose>  
                    </xsl:for-each>
                </xsl:if>

                <!-- type -->
               <xsl:for-each select="//dc:type">
                   <xsl:if test="starts-with(., $driver_prefix)">
                   
                  	 <xsl:choose>
						
						<xsl:when test="contains($type_list, .)">
							  <field name="format">
                           			<xsl:value-of select="substring(., string-length($driver_prefix)+1, string-length(.))" />
                       		  </field>
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
                                                        <field name="rights_invalid"> 
                                                                <xsl:value-of select="." /> 
                                                        </field> 
                                                </xsl:otherwise> 
                                        </xsl:choose> 
                    
               </xsl:for-each> 
 

                <!-- AUTHOR -->
                <xsl:if test="//dc:creator">
                    <xsl:for-each select="//dc:creator">
                        <xsl:if test="string-length(normalize-space()) &gt; '4'">
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
                <!--  xsl:if test="//dc:date">
                    <field name="publishDate">
                        <xsl:value-of select="substring(//dc:date, 1, 4)"/>
                    </field>
                    <field name="publishDateSort">
                        <xsl:value-of select="substring(//dc:date, 1, 4)"/>
                    </field>
                </xsl:if-->
                
                <xsl:for-each select="//dc:date">
				    	<xsl:sort select="."/>
			            <xsl:if test="position() = 1">
		
					    	<field name="publishDate">
			                	<xsl:value-of select="substring(., 1, 4)"/>
			                </field>
			
			                <field name="publishDateSort">
			                    <xsl:value-of select="substring(., 1, 4)"/>
			                </field>
				    	</xsl:if>
		        </xsl:for-each>	

                <!-- URL -->
				<xsl:for-each select="//dc:identifier">
					<xsl:choose>
						<xsl:when test="starts-with(., 'http')">
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
