<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    converterClassName - converter class name (type: String)
    converterPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    myFacesCodiVersion - Apache MyFaces CODI Implementation (type: Version)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primaty key instance
    keySetter - entity setter method to set primary key instance

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
package ${converterPackageName};

import ${entityFullClassName};
import ${ejbFullClassName};
<#if cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
import javax.inject.Inject;
</#if>
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
<#if !cdiEnabled?? || cdiEnabled == false>
import javax.faces.convert.FacesConverter;
</#if>

<#if cdiEnabled?? && cdiEnabled == true>
@Named(value = "${converterClassName?uncap_first}")
<#else>
@FacesConverter("${converterClassName?uncap_first}")
</#if>
public class ${converterClassName?cap_first} implements Converter {

<#if cdiEnabled?? && cdiEnabled == true>
    @Inject
    private ${ejbClassName} ejbFacade;
</#if>

<#if keyEmbedded>
    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";
</#if>

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
<#if cdiEnabled?? && cdiEnabled == true>
        return this.ejbFacade.find(getKey(value));
<#else>
        ${abstractControllerClassName}<${entityClassName}> controller = (${abstractControllerClassName}<${entityClassName}>)facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "${managedBeanName}");
<#if ejbClassName??>
        return controller.getFacade().find(getKey(value));
<#elseif jpaControllerClassName??>
        return controller.getJpaController().find${entityClassName}(getKey(value));
</#if>
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

    @Override
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
