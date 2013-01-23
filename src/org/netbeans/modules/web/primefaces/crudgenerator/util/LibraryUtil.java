/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author kuw
 */
public class LibraryUtil {

    private static final String MANIFEST_RESOURCE_NAME = "META-INF/MANIFEST.MF";
    private static final String MANIFEST_KEY_BUNDLE_NAME = "Bundle-Name";
    private static final String MANIFEST_KEY_BUNDLE_VERSION = "Bundle-Version";
    private static final Name BUNDLE_NAME = new Attributes.Name(MANIFEST_KEY_BUNDLE_NAME);
    private static final Name BUNDLE_VERSION = new Attributes.Name(MANIFEST_KEY_BUNDLE_VERSION);

    public static String getVersion(ClassLoader classLoader, String libraryName) {
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
                        return libraryVersion;
                    }
                }

                // If unsuccessful, try by default Manifest Headers
                if (mainAttributes.containsKey(Name.IMPLEMENTATION_TITLE) && mainAttributes.containsKey(Name.IMPLEMENTATION_VERSION)) {
                    if (mainAttributes.getValue(Name.IMPLEMENTATION_TITLE).equals(libraryName)) {
                        libraryVersion = mainAttributes.getValue(Name.IMPLEMENTATION_VERSION);
                        return libraryVersion;
                    }
                }


            } catch (IOException ex) {
                Logger.getLogger(LibraryUtil.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return libraryVersion;
    }
}
