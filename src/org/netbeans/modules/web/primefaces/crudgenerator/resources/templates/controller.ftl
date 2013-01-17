<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    controllerClassName - controller class name (type: String)
    controllerPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primaty key instance
    keySetter - entity setter method to set primary key instance

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
package ${controllerPackageName};

import ${entityFullClassName};
import java.io.Serializable;
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
<#else>
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
</#if>
</#if>

<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
@Named(value="${managedBeanName}")
<#else>
@ManagedBean(name="${managedBeanName}")
</#if>
<#if cdiEnabled?? && cdiEnabled == true>
//Since CDI does not have a View scope, you would have to either program some
//ConversationScope logic. A better approach is to add Apache MyFaces CODI CDI
//extension to the project and use @ViewAccessScoped  which works very well.
//For a standard project in NetBeans that's CDI enabled, @SessionScoped is the
//only other working solution.
@SessionScoped
<#else>
@ViewScoped
</#if>
</#if>
public class ${controllerClassName} extends ${abstractControllerClassName} <${entityClassName}> implements Serializable {

    public ${controllerClassName}() {
        super(${entityClassName}.class);
    }

}
