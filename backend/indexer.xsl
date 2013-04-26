<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    
    exclude-result-prefixes="oai_dc dc">
    
    <xsl:output method="xml" indent="yes" encoding="utf-8"/>
    
    <!-- Aquí van los listados para diferenciar type en tipo de documento y status -->    
	<xsl:variable name="type_list">info:eu-repo/semantics/article,info:eu-repo/semantics/masterThesis,info:eu-repo/semantics/doctoralThesis,info:eu-repo/semantics/report</xsl:variable>
	<xsl:variable name="status_list">info:eu-repo/semantics/draft,info:eu-repo/semantics/acceptedVersion,info:eu-repo/semantics/submittedVersion,info:eu-repo/semantics/publishedVersion,info:eu-repo/semantics/updatedVersion</xsl:variable>
	   
	<!-- Aquí se definen los prefijos utilizados para detectar contenidos con trato diferencial -->   
	<xsl:variable name="driver_prefix">info:eu-repo/semantics/</xsl:variable> 
	<xsl:variable name="reponame_prefix">reponame:</xsl:variable>  
	<xsl:variable name="instname_prefix">instname:</xsl:variable>  
	 
    <xsl:template match="oai_dc:dc">
            <doc>
                <!-- ID es agregado desde java -->
                <!-- COUNTRY es agregado desde java -->
                <!-- COUNTRY_ISO es agregado desde java -->
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
							</xsl:when>
							<xsl:when test="starts-with(., $reponame_prefix)">
							 	<field name="reponame">
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
                            <field name="language">
                                <xsl:value-of select="normalize-space()"/>
                            </field>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:if>

                <!-- type -->
               <xsl:for-each select="//dc:type">
                   <xsl:if test="starts-with(., $driver_prefix)">
                   
                  	 <xsl:choose>
						<xsl:when test="contains($type_list, .)">
							  <field name="type">
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
