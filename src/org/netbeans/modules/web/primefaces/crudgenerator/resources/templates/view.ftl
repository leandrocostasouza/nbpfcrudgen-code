<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

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
        returnType - fully qualified data type of the field
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesGetter - if item is of type 1:1 or 1:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
    primeFacesVersion - Version of the PrimeFaces library in use

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui">

    <ui:composition>

        <p:dialog id="viewDlg" widgetVar="viewDialog" modal="true" resizable="false" appendToBody="true" header="${r"#{"}bundle.View${entityName}Title${r"}"}">

            <h:form id="viewForm">

                <h:panelGroup id="display">
                    <p:panelGrid columns="2" rendered="${r"#{"}${managedBeanProperty} != null${r"}"}">
    <#list entityDescriptors as entityDescriptor>
        <#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
            <#if entityDescriptor.getRelationsLabelName(searchLabels)??>
              <#assign relationLabelName = entityDescriptor.getRelationsLabelName(searchLabels)>
            <#else>
              <#assign relationLabelName = "">
            </#if>
        </#if>
                        <h:outputText value="${r"#{"}bundle.View${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
        <#if entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}bundle.View${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}">
                            <f:convertDateTime pattern="${entityDescriptor.dateTimeFormat}" />
                        </h:outputText>
        <#elseif entityDescriptor.returnType?contains("boolean")>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}bundle.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}bundle.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> disabled="true"/>
        <#elseif entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
            <#if relationLabelName?? && relationLabelName != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"/>
            </#if>
        <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}bundle.View${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
        </#if>
    </#list>
                    </p:panelGrid>
                    <p:commandButton value="${r"#{bundle.Close}"}" onclick="${r"viewDialog.hide()"}"/>
                </h:panelGroup>

            </h:form>

        </p:dialog>

    </ui:composition>

</html>
