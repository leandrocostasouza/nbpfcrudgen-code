<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    bundle - name of the bundle variable set in faces-config.xml (type: String)
    entityName - name of entity being modified (type: String)
    managedBean - name of managed choosen in UI (type: String)
    managedBeanProperty - name of managed bean property choosen in UI (type: String)
    item - name of property used for dataTable iteration (type: String)
    comment - always set to "false" (type: Boolean)
    entityDescriptors - list of beans describing individual entities. Bean has following properties:
        label - field label (type: String)
        name - field property name (type: String)
        dateTimeFormat - date/time/datetime formatting (type: String)
        blob - does field represents a large block of text? (type: boolean)
        primaryKey - is field a primary key field? (type: boolean)
        relationshipOne - does field represent one to one or many to one relationship (type: boolean)
        relationshipMany - does field represent one to many relationship (type: boolean)
        relationshipOwner - does the field represent the owning side of a many:many relationship? (type: boolean)
        getRelationsLabelName(String) - field name of the foreign entity field matching String (type: String)
        returnType - fully qualified data type of the field
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesListGetter - if item is of type 1:many or many:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
        valuesConverter - if item is of type 1:many or many:many relationship then use this
            for the converter binding of <h:selectOneMenu> or <h:selectManyMenu>
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    servletMapping - Prefix mapping of the JSF servlet inside web.xml (type: String)
    searchLabels - Comma-seperated list of field name artifacts to search for labels (type: String)
                   Use in conjunction with getRelationsLabelName.
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<#if growlMessages>
  <#assign messageUpdate = ":growl">
<#else>
  <#assign messageUpdate = ":" + entityName + "ListForm:messagePanel,messages">
</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui">

    <ui:composition>

        <p:dialog id="${entityName}EditDlg" widgetVar="${entityName}EditDialog" modal="true" resizable="false" appendToBody="true" header="${r"#{"}${bundle}.Edit${entityName}Title${r"}"}">

            <h:form id="${entityName}EditForm">

<#if !growlMessages>
                <h:panelGroup id="messagePanel">
                    <p:messages id="messages"/>
                </h:panelGroup>

</#if>
                <h:panelGroup id="display">
                    <p:panelGrid  columns="2" rendered="${r"#{"}${managedBeanProperty} != null${r"}"}">
    <#list entityDescriptors as entityDescriptor>
     <#-- Skip this field if we are dealing with many:many and this entity is not the owner -->
     <#if !(entityDescriptor.relationshipMany && 
           !entityDescriptor.relationshipOwner)> 

        <#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
            <#if entityDescriptor.getRelationsLabelName(searchLabels)??>
              <#assign relationLabelName = entityDescriptor.getRelationsLabelName(searchLabels)>
            <#else>
              <#assign relationLabelName = "">
            </#if>
        </#if>
        <#if (primeFacesVersion.compareTo("3.3") >= 0 && !entityDescriptor.primaryKey && !entityDescriptor.embeddedKey)>
                        <p:outputLabel value="${r"#{"}${bundle}.Edit${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}" for="${entityDescriptor.id?replace(".","_")}" />
        <#else>
                        <h:outputLabel value="${r"#{"}${bundle}.Edit${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}" for="${entityDescriptor.id?replace(".","_")}" />
        </#if>
        <#if entityDescriptor.primaryKey || entityDescriptor.embeddedKey>
            <#if relationLabelName?? && relationLabelName != "">
                        <h:outputText id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"/>
            <#else>
                        <h:outputText id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" />
            </#if>
        <#elseif entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <p:calendar id="${entityDescriptor.id?replace(".","_")}" pattern="${entityDescriptor.dateTimeFormat}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> showOn="button"/>
        <#elseif entityDescriptor.returnType?contains("boolean")>
          <#if (primeFacesVersion.compareTo("3") >= 0)>
                        <p:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
          <#else>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
          </#if>
        <#elseif entityDescriptor.blob>
                        <p:inputTextarea rows="4" cols="30" id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
        <#elseif entityDescriptor.relationshipOne>
                        <p:selectOneMenu id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>>
                            <f:selectItem itemLabel="${r"#{"}${bundle}.SelectOneMessage${r"}"}"/>
                            <f:selectItems value="${r"#{"}${entityDescriptor.valuesListGetter}${r"}"}"
                                           var="${entityDescriptor.id?replace(".","_")}Item"
                                           itemValue="${r"#{"}${entityDescriptor.id?replace(".","_")}Item${r"}"}"
            <#if relationLabelName != "">
                                           itemLabel="${r"#{"}${entityDescriptor.id?replace(".","_")}Item.${relationLabelName}${r".toString()}"}"
            </#if>
                            />
                            <f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/>
                        </p:selectOneMenu>
        <#elseif entityDescriptor.relationshipMany>
          <#if entityDescriptor.relationshipOwner>
                        <p:selectManyMenu id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>>
                            <f:selectItems value="${r"#{"}${entityDescriptor.valuesListGetter}${r"}"}"
                                           var="${entityDescriptor.id?replace(".","_")}Item"
                                           itemValue="${r"#{"}${entityDescriptor.id?replace(".","_")}Item${r"}"}"
            <#if relationLabelName != "">
                                           itemLabel="${r"#{"}${entityDescriptor.id?replace(".","_")}Item.${relationLabelName}${r".toString()}"}"
            </#if>
                            />
                            <f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/>
                        </p:selectManyMenu>
          </#if>
        <#else>
                        <p:inputText id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
        </#if>
     </#if>
    </#list>
                    </p:panelGrid>
                    <p:commandButton actionListener="${r"#{"}${managedBean}${r".save}"}" value="${r"#{"}${bundle}.Save${r"}"}" update="display,:${entityName}ListForm:datalist,${messageUpdate}" oncomplete="handleSubmit(xhr,status,args,${entityName}EditDialog);"/>
                    <p:commandButton value="${r"#{"}${bundle}.Cancel${r"}"}" onclick="${entityName}EditDialog.hide()"/>
                </h:panelGroup>

            </h:form>

        </p:dialog>

    </ui:composition>

</html>
