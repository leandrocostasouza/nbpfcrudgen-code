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
import ${ejbFullClassName};
import java.io.Serializable;
import javax.annotation.PostConstruct;
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
import javax.inject.Inject;
<#if myFacesCodiVersion??>
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
<#else>
import javax.enterprise.context.SessionScoped;
</#if>
<#else>
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
</#if>
</#if>
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
@Named(value="${managedBeanName}")
<#else>
@ManagedBean(name="${managedBeanName}")
</#if>
<#if cdiEnabled?? && cdiEnabled == true>
<#if myFacesCodiVersion??>
@ViewAccessScoped
<#else>
@SessionScoped
</#if>
<#else>
@SessionScoped
</#if>
</#if>
public class ${controllerClassName} extends ${abstractControllerClassName} <${entityClassName}> implements Serializable {

<#if cdiEnabled?? && cdiEnabled == true>
    @Inject
<#else>
    @EJB
</#if>
    private ${ejbClassName} ejbFacade;

    public ${controllerClassName}() {
        super(${entityClassName}.class);
    }

    @PostConstruct
    public void init() {
        super.setFacade(ejbFacade);
    }

    @FacesConverter(forClass=${entityClassName}.class)
    public static class ${controllerClassName}Converter implements Converter {
<#if keyEmbedded>

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";
</#if>

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ${controllerClassName} controller = (${controllerClassName})facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "${managedBeanName}");
<#if ejbClassName??>
            return controller.ejbFacade.find(getKey(value));
<#elseif jpaControllerClassName??>
            return controller.getJpaController().find${entityClassName}(getKey(value));
</#if>
        }

        ${keyType} getKey(String value) {
            ${keyType} key;
${keyBody}
            return key;
        }

        String getStringKey(${keyType} value) {
            StringBuffer sb = new StringBuffer();
${keyStringBody}
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ${entityClassName}) {
                ${entityClassName} o = (${entityClassName}) object;
                return getStringKey(o.${keyGetter}());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+${entityClassName}.class.getName());
            }
        }

    }

}
