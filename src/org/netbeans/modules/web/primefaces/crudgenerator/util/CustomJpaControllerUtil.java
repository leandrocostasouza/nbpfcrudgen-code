/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.JpaControllerUtil;
import static org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.JpaControllerUtil.getSuperclassTypeElement;
import static org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.JpaControllerUtil.isAnnotatedWith;

/**
 *
 * @author Kay Wrobel
 */
public class CustomJpaControllerUtil extends JpaControllerUtil {

    /**
     * Returns all methods in class and its super classes which are entity
     * classes or mapped superclasses in order of superclasses.
     */
    public static ExecutableElement[] getEntityMethodsBySuperClass(TypeElement entityTypeElement) {
        List<ExecutableElement> result = new LinkedList<ExecutableElement>();
        TypeElement typeElement = entityTypeElement;
        List<TypeElement> typeElements = new LinkedList<TypeElement>();

        // First we will traverse up the element chain to get to the super classes
        while (typeElement != null) {
            if (isAnnotatedWith(typeElement, "javax.persistence.Entity") || isAnnotatedWith(typeElement, "javax.persistence.MappedSuperclass")) { // NOI18N
                typeElements.add(typeElement);
            }
            typeElement = getSuperclassTypeElement(typeElement);
        }

        // Now we will reverse the element order to get to the top element first
        Collections.reverse(typeElements);

        // Finally, grab the methods for each element
        for (TypeElement reversedElement : typeElements) {
            result.addAll(ElementFilter.methodsIn(reversedElement.getEnclosedElements()));
        }
        return result.toArray(new ExecutableElement[result.size()]);
    }

    public static String getPropNameFromMethod(String name) {
        //getABcd should be converted to ABcd, getFooBar should become fooBar
        //getA1 is "a1", getA_ is a_, getAB is AB
        //in case method doesn't start with "get" return name with brackets
        if (!name.startsWith("get") && !name.startsWith("set") && !name.startsWith("is")) {  //NOI18N
            return name + "()";   //NOI18n
        }

        // First, remove getter prefix
        name = StringHelper.removeBeanMethodPrefix(name);

        // Now proceed with making first character lower according to logic
        // outline in the comment above.
        return StringHelper.firstLower(name);
    }

}
