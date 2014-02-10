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
    relationShipEntityDescriptors - list of beans describing individual entities. Bean has following properties:
        label - field label (type: String)
        name - field property name (type: String)
        dateTimeFormat - date/time/datetime formatting (type: String)
        blob - does field represents a large block of text? (type: boolean)
        maxSize - returns the size of a field if annotated with @Size (type: Integer)
        primaryKey - is field a primary key field? (type: boolean)
        readOnly - is the field a read-only field? (type: boolean)
        relationshipOne - does field represent one to one or many to one relationship (type: boolean)
        relationshipMany - does field represent one to many relationship (type: boolean)
        relationshipOwner - does the field represent the owning side of a many:many relationship? (type: boolean)
        relationClassName - class name for the related Entity (type: String)
        relationQualifiedClassName - fully qualified class name for the related Entity (type: String)
        getRelationsLabelName(String) - field name of the foreign entity field matching String (type: String)
        returnType - fully qualified data type of the field
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesListGetter - if item is of type 1:many or many:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
        valuesConverter - if item is of type 1:many or many:many relationship then use this
            for the converter binding of <h:selectOneMenu> or <h:selectManyMenu>
        versionField - is the field a Version field (type: boolean)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${controllerPackageName};

<#if importEntityFullClassName?? && importEntityFullClassName == true>
import ${entityFullClassName};
</#if>
<#if !cdiEnabled?? || cdiEnabled == false>
<#if importEjbFullClassName?? && importEjbFullClassName == true>
import ${ejbFullClassName};
</#if>
</#if>
<#if doRelationshipNavigation == true && hasRelationships>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
import ${controllerPackageName}.${relationshipEntityDescriptor.relationClassName}Controller;
</#list>
</#if>
import java.io.Serializable;
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
<#if myFacesCodiVersion??>
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.ViewAccessScoped;
<#elseif (jsfVersion.compareTo("2.2") >= 0)>
import javax.faces.view.ViewScoped;
<#else>
import javax.enterprise.context.SessionScoped;
</#if>
<#else>
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
</#if>
</#if>
<#if doRelationshipNavigation == true && hasRelationships>
import javax.inject.Inject;
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

<#if !cdiEnabled?? || cdiEnabled == false>
    @EJB
    private ${ejbClassName} ejbFacade;

    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }
</#if>
<#if doRelationshipNavigation == true && hasRelationships>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
    @Inject
    ${relationshipEntityDescriptor.relationClassName}Controller ${relationshipEntityDescriptor.relationClassName?uncap_first}Controller;
</#list>
</#if>

    public ${controllerClassName}() {
        super(${entityClassName}.class);
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
<#if doRelationshipNavigation == true && hasRelationships>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if relationshipEntityDescriptor.relationshipOne>
    public void prepare${relationshipEntityDescriptor.relationClassName}() {
        if (this.getSelected() != null) {
            ${relationshipEntityDescriptor.relationClassName?uncap_first}Controller.setSelected(this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
        }
    }
</#if>
<#if relationshipEntityDescriptor.relationshipMany>
    public String navigate${relationshipEntityDescriptor.relationClassName}() {
        if (this.getSelected() != null) {
            ${relationshipEntityDescriptor.relationClassName?uncap_first}Controller.setItems(this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
        }
        return "${jsfFolder}${r"/"}${relationshipEntityDescriptor.relationClassName?uncap_first}${r"/index?faces-redirect=true"}";
    }
</#if>
</#list>
</#if>


}
