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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a String-based Version that can be compared to other versions.
 * Version cannot contain any characters or [NumberFormatException] will be
 * thrown.
 *
 * @author Kay Wrobel
 * @version 1.0
 */
public final class Version implements Comparable<Version> {

    private static final String ACCEPTED_VERSION_PATTERN = "[0-9]+(\\.[0-9]+)*";
    private static final String DEFAULT_DELIMITER = ".";
    private String version;
    private String delimiter;
    private List<Integer> versionArtifacts;

    /**
     * Returns a list of version integers. This is being used for comparisons
     * by the compareTo method.
     * @return
     */
    public List<Integer> getVersionArtifacts() {
        return versionArtifacts;
    }

    private void setVersionArtifacts() {
        versionArtifacts = new ArrayList<Integer>();
        for (String artifact : this.version.split(this.delimiter)) {
            try {
                int parseInt = Integer.parseInt(artifact);
                versionArtifacts.add(parseInt);
            } catch (NumberFormatException ex) {
                break;
            }
        }
    }

    /**
     * Returns the delimiter currently being used by this Version.
     * @return
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the delimiter to be used by this Version for traversing during
     * comparisons. Also internally initializes a version artifact ArrayList.
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        // Add escape sequence in case it was not provided. This is important
        // for the regex matching to work properly.
        if (delimiter.contains("\\")) {
            this.delimiter = delimiter;
        } else {
            this.delimiter = "\\".concat(delimiter);
        }
        setVersionArtifacts(); // Turn version string into parsable Integers
    }

    @Override
    public String toString() {
        return this.version;
    }

    /**
     * Instantiates a Version with a given version string and sets a default
     * delimiter to be used for traversing versions in comparisons.
     * @param version
     * @throws IllegalArgumentException
     */
    public Version(String version) throws IllegalArgumentException {
        if (version == null) {
            throw new IllegalArgumentException("Version can not be null");
        }
        if (!version.matches(ACCEPTED_VERSION_PATTERN)) {
            throw new IllegalArgumentException("Invalid version format");
        }
        this.version = version;
        setDelimiter(DEFAULT_DELIMITER);
    }

    @Override
    public int compareTo(Version that) {
        if (versionArtifacts.isEmpty()) {
            throw new NumberFormatException("Source Version did not contain any parsable artifacts. Check your delimiter.");
        }
        if (that.getVersionArtifacts().isEmpty()) {
            throw new NumberFormatException("Target Version did not contain any parsable artifacts. Check your delimiter.");
        }

        int thisSize = versionArtifacts.size();
        int thatSize = that.getVersionArtifacts().size();
        int maxSize = Math.max(versionArtifacts.size(), that.getVersionArtifacts().size());
        for (int i = 0; i < maxSize; i++) {
            Integer thisArtifact = i < thisSize ? versionArtifacts.get(i) : 0;
            Integer thatArtifact = i < thatSize ? that.getVersionArtifacts().get(i) : 0;
            if (thisArtifact < thatArtifact) {
                return -1;
            } //Source version is smaller
            if (thisArtifact > thatArtifact) {
                return +1;
            } //Source version is larger
        }
        return 0;
    }
    
    public int compareTo(String that) {
        return compareTo(new Version(that));
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (this.getClass() != that.getClass()) {
            return false;
        }
        return (this.compareTo((Version) that) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 53 * hash + (this.delimiter != null ? this.delimiter.hashCode() : 0);
        return hash;
    }
}
