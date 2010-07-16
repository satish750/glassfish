/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.enterprise.v3.admin.cluster;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.xml.bind.*;

import org.glassfish.api.admin.*;
import org.jvnet.hk2.annotations.Service;
import org.jvnet.hk2.annotations.Scoped;
import org.jvnet.hk2.annotations.Inject;
import org.jvnet.hk2.component.PerLookup;

import org.glassfish.api.I18n;
import org.glassfish.api.Param;
import org.glassfish.api.ActionReport;
import org.glassfish.api.ActionReport.ExitCode;
import com.sun.enterprise.config.serverbeans.Applications;
import com.sun.enterprise.config.serverbeans.Servers;
import com.sun.enterprise.config.serverbeans.Server;
import com.sun.enterprise.util.cluster.SyncRequest;
import com.sun.enterprise.util.cluster.SyncRequest.ModTime;
import com.sun.enterprise.util.LocalStringManagerImpl;

/**
 * Synchronize files.  Accepts an XML document containing files
 * and mod times and sends the client new versions of anything
 * that's out of date.
 *
 * @author Bill Shannon
 */
@Service(name="_synchronize-files")
@Scoped(PerLookup.class)
@I18n("synchronize.command")
public class SynchronizeFiles implements AdminCommand {

    @Param(name = "file_list", primary = true)
    private File fileList;

    @Param(name = "syncarchive", optional = true)
    private boolean syncArchive;

    @Param(name = "syncallapps", optional = true)
    private boolean syncAllApps;

    @Inject(optional = true)
    private Applications applications;

    @Inject(optional = true)
    private Servers servers;

    @Inject
    InstanceState instanceState;

    @Inject
    private ServerSynchronizer sync;

    private Logger logger;

    private final static LocalStringManagerImpl strings =
        new LocalStringManagerImpl(SynchronizeFiles.class);

    public void execute(AdminCommandContext context) {
        ActionReport report = context.getActionReport();
        logger = context.getLogger();
        SyncRequest sr = null;
        try {
            /*
            try {
            BufferedInputStream in =
                new BufferedInputStream(new FileInputStream(fileList));
            byte[] buf = new byte[8192];
            int n = in.read(buf);
            System.out.write(buf, 0, n);
            in.close();
            } catch (IOException ex) {}
            */
            // read the input document
            JAXBContext jc = JAXBContext.newInstance(SyncRequest.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setSchema(null);       // XXX - needed?
            sr = (SyncRequest)unmarshaller.unmarshal(fileList);
            logger.finer("SynchronizeFiles: synchronize dir " + sr.dir);
        } catch (Exception ex) {
            logger.fine("SynchronizeFiles: Exception reading request");
            logger.fine(ex.toString());
            report.setActionExitCode(ExitCode.FAILURE);
            report.setMessage(
                        strings.getLocalString("sync.exception.reading",
                            "SynchronizeFiles: Exception reading request"));
            report.setFailureCause(ex);
            return;
        }

        try {
            // verify the server instance is valid
            Server server = null;
            if (servers != null)
                server = servers.getServer(sr.instance);
            if (server == null) {
                report.setActionExitCode(ExitCode.FAILURE);
                report.setMessage(
                        strings.getLocalString("sync.unknown.instance",
                            "Unknown server instance: {0}", sr.instance));
                return;
            }

            // Initialize a instance status object in habitat for this instance
            //TODO : Once GMS is integrated, this state should be STARTED
            instanceState.setState(sr.instance,
                                            InstanceState.StateType.RUNNING);

            sync.synchronize(server, sr, context.getOutboundPayload(), report,
                                logger);
        } catch (Exception ex) {
            logger.fine("SynchronizeFiles: Exception processing request");
            logger.fine(ex.toString());
            report.setActionExitCode(ExitCode.FAILURE);
            report.setMessage(
                        strings.getLocalString("sync.exception.processing",
                            "SynchronizeFiles: Exception processing request"));
            report.setFailureCause(ex);
        }
    }
}
