<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    controllerClassName - controller class name (type: String)
    controllerPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    importEntityFullClassName - whether to import entityFullClassName or not
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    importEjbFullClassName - whether to import ejbFullClassName or not
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    myFacesCodiVersion - Apache MyFaces CODI Implementation (type: Version)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primary key instance
    keySetter - entity setter method to set primary key instance
    embeddedIdFields - contains information about embedded primary Ids

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${controllerPackageName};

<#if importEntityFullClassName?? && importEntityFullClassName == true>
import ${entityFullClassName};
</#if>
<#if importEjbFullClassName?? && importEjbFullClassName == true>
import ${ejbFullClassName};
</#if>
import java.io.Serializable;
import javax.annotation.PostConstruct;
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
import javax.inject.Inject;
<#if myFacesCodiVersion??>
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
<#elseif (jsfVersion.compareTo("2.2") >= 0)>
import javax.faces.view.ViewScoped;
<#else>
import javax.enterprise.context.SessionScoped;
</#if>
<#else>
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
</#if>
</#if>

<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
@Named(value="${managedBeanName}")
<#if myFacesCodiVersion??>
@ViewAccessScoped
<#elseif (jsfVersion.compareTo("2.2") >= 0)>
@ViewScoped
<#else>
@SessionScoped
</#if>
<#else>
@ManagedBean(name="${managedBeanName}")
@ViewScoped
</#if>
</#if>
public class ${controllerClassName} extends ${abstractControllerClassName}<${entityClassName}> implements Serializable {

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

<#if embeddedIdFields??>
    @Override
    protected void setEmbeddableKeys() {
  <#list embeddedIdFields as fields>
        this.getSelected().${keyGetter}().${fields.getEmbeddedSetter()}(this.getSelected().${fields.getCodeToPopulate()});
  </#list>
    }
</#if>        

<#if keyEmbedded>
    @Override
    protected void initializeEmbeddableKey() {
            this.getSelected().${keySetter}(new ${keyType}());
    }
</#if>


}
