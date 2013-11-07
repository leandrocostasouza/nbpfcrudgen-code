/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.util;

/**
 *
 * @author kuw
 */
public class StringHelper {

    public static String firstLower(String string) {
        boolean makeFirstLower = string.length() < 2 || (!Character.isUpperCase(string.charAt(1)));
        return makeFirstLower ? string.substring(0, 1).toLowerCase() + string.substring(1) : string;
    }

    public static String firstUpper(String string) {
        boolean makeFirstUpper = string.length() < 2 || (!Character.isLowerCase(string.charAt(1)));
        return makeFirstUpper ? string.substring(0, 1).toUpperCase() + string.substring(1) : string;
    }

    public static String removeBeanMethodPrefix(String methodName) {
        if (methodName.startsWith("get")) {  //NOI18N
            methodName = methodName.replaceFirst("get", "");
        }
        if (methodName.startsWith("set")) {  //NOI18N
            methodName = methodName.replaceFirst("set", "");
        }
        if (methodName.startsWith("is")) {  //NOI18N
            methodName = methodName.replaceFirst("is", "");
        }
        return methodName;
    }

}
