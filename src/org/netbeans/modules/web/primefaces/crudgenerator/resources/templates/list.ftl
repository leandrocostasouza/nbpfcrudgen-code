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
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesGetter - if item is of type 1:1 or 1:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
    defaultDataTableRows - will be used for DataTable Paging
    defaultDataTableRowsPerPageTemplate - will be used for DataTable Paging

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:h="http://java.sun.com/jsf/html"
                xmlns:f="http://java.sun.com/jsf/core"
                xmlns:p="http://primefaces.org/ui">

        <h:form id="listForm">

            <p:panel header="${r"#{"}bundle.List${entityName}Title${r"}"}">
                <p:dataTable id="datalist" value="${r"#{"}${managedBeanProperty}${r"}"}" var="${item}"
                             selectionMode="single" selection="${r"#{"}${managedBean}${r".selected}"}"
<#list entityDescriptors as entityDescriptor>
    <#if entityDescriptor.primaryKey>
                             rowKey="${r"#{"}${entityDescriptor.name}${r"}"}"
    </#if>
</#list>
<#if defaultDataTableRows?? && defaultDataTableRows != "">
                             paginator="true"
                             rows="${defaultDataTableRows}"
                             rowsPerPageTemplate="${defaultDataTableRowsPerPageTemplate}"
</#if>
            >

                    <p:ajax event="rowSelect" update="viewButton editButton"/>  
                    <p:ajax event="rowUnselect" update="viewButton editButton"/>  

<#list entityDescriptors as entityDescriptor>
                    <p:column>
                        <f:facet name="header">
                            <h:outputText value="${r"#{"}bundle.List${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
                        </f:facet>
    <#if entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
                            <f:convertDateTime pattern="${entityDescriptor.dateTimeFormat}" />
                        </h:outputText>
    <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}"/>
    </#if>
                    </p:column>
</#list>
                    <f:facet name="footer">
                        <p:commandButton id="createButton" icon="ui-icon-plus"   value="${r"#{"}bundle.Create${r"}"}" actionListener="${r"#{"}${managedBean}.${r"prepareCreate}"}" update="${r":createForm:display"}" oncomplete="${r"createDialog.show()"}"/>
                        <p:commandButton id="viewButton"   icon="ui-icon-search" value="${r"#{"}bundle.View${r"}"}" update=":viewForm:display" oncomplete="viewDialog.show()" disabled="${r"#{empty "}${managedBean}${r".selected}"}"/>
                        <p:commandButton id="editButton"   icon="ui-icon-pencil" value="${r"#{"}bundle.Edit${r"}"}" update=":editForm:display" oncomplete="editDialog.show()" disabled="${r"#{empty "}${managedBean}${r".selected}"}"/>
                    </f:facet>

                </p:dataTable>

            </p:panel>

        </h:form>

</ui:composition>