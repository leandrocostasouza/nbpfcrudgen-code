/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kay Wrobel
 */
public class LibraryUtil {

    private static final String MANIFEST_RESOURCE_NAME = "META-INF/MANIFEST.MF";
    private static final String MANIFEST_KEY_BUNDLE_NAME = "Bundle-Name";
    private static final String MANIFEST_KEY_BUNDLE_VERSION = "Bundle-Version";
    private static final Name BUNDLE_NAME = new Attributes.Name(MANIFEST_KEY_BUNDLE_NAME);
    private static final Name BUNDLE_VERSION = new Attributes.Name(MANIFEST_KEY_BUNDLE_VERSION);

    public static Version getVersion(ClassLoader classLoader, String libraryName) {
        Enumeration<URL> resources;
        String libraryVersion = "";
        // Retrieve all Manifest information that's part of the class loader.
        try {
            resources = classLoader.getResources(MANIFEST_RESOURCE_NAME);
        } catch (IOException ex) {
            Logger.getLogger(LibraryUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        while (resources.hasMoreElements()) {
            try {
                //Going to query for following manifest attributes
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                Attributes mainAttributes = manifest.getMainAttributes();

                // Find library version by Bundle information (Eclipse/OSGi)
                if (mainAttributes.containsKey(BUNDLE_NAME) && mainAttributes.containsKey(BUNDLE_VERSION)) {
                    if (mainAttributes.getValue(BUNDLE_NAME).equals(libraryName)) {
                        libraryVersion = mainAttributes.getValue(BUNDLE_VERSION);
                        return new Version(libraryVersion);
                    }
                }

                // If unsuccessful, try by default Manifest Headers
                if (mainAttributes.containsKey(Name.IMPLEMENTATION_TITLE) && mainAttributes.containsKey(Name.IMPLEMENTATION_VERSION)) {
                    if (mainAttributes.getValue(Name.IMPLEMENTATION_TITLE).equals(libraryName)) {
                        libraryVersion = mainAttributes.getValue(Name.IMPLEMENTATION_VERSION);
                        return new Version(libraryVersion);
                    }
                }


            } catch (IOException ex) {
                Logger.getLogger(LibraryUtil.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return null;
    }
}
