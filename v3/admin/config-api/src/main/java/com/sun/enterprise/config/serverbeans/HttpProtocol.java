/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */



package com.sun.enterprise.config.serverbeans;

import org.glassfish.api.amx.AMXConfigInfo;
import org.jvnet.hk2.component.Injectable;
import org.jvnet.hk2.config.Attribute;
import org.jvnet.hk2.config.ConfigBeanProxy;
import org.jvnet.hk2.config.Configured;

import java.beans.PropertyVetoException;


@AMXConfigInfo( amxInterfaceName="com.sun.appserv.management.config.HTTPProtocolConfig", singleton=true)
@Configured
@Deprecated
public interface HttpProtocol extends ConfigBeanProxy, Injectable  {

    /**
     * Gets the value of the version property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute (defaultValue="HTTP/1.1")
    String getVersion();

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setVersion(String value) throws PropertyVetoException;

    /**
     * Gets the value of the dnsLookupEnabled property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute  (defaultValue="false")
    String getDnsLookupEnabled();

    /**
     * Sets the value of the dnsLookupEnabled property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setDnsLookupEnabled(String value) throws PropertyVetoException;

    /**
     * Gets the value of the forcedType property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute (defaultValue="text/html; charset=iso-8859-1")
    String getForcedType();

    /**
     * Sets the value of the forcedType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setForcedType(String value) throws PropertyVetoException;

    /**
     * Gets the value of the defaultType property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute (defaultValue="text/html; charset=iso-8859-1")
    String getDefaultType();

    /**
     * Sets the value of the defaultType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setDefaultType(String value) throws PropertyVetoException;

    /**
     * Gets the value of the forcedResponseType property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute
    String getForcedResponseType();

    /**
     * Sets the value of the forcedResponseType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setForcedResponseType(String value) throws PropertyVetoException;

    /**
     * Gets the value of the defaultResponseType property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute
    String getDefaultResponseType();

    /**
     * Sets the value of the defaultResponseType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setDefaultResponseType(String value) throws PropertyVetoException;

    /**
     * Gets the value of the sslEnabled property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Attribute (defaultValue="true")
    String getSslEnabled();

    /**
     * Sets the value of the sslEnabled property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setSslEnabled(String value) throws PropertyVetoException;



}
