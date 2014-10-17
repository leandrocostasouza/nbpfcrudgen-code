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
        maxSize - returns the size of a field if annotated with @Size (type: Integer)
        primaryKey - is field a primary key field? (type: boolean)
        readOnly - is the field a read-only field? (type: boolean)
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
        versionField - is the field a Version field (type: boolean)
    defaultDataTableRows - will be used for DataTable Paging
    defaultDataTableRowsPerPageTemplate - will be used for DataTable Paging
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    searchLabels - Comma-seperated list of field name artifacts to search for labels (type: String)
                   Use in conjunction with getRelationsLabelName.
    doCreate - Provide CREATE functionality (type: boolean)
    doRead   - Provide READ functionality   (type: boolean)
    doUpdate - Provide UPDATE functionality (type: boolean)
    doDelete - Provide DELETE functionality (type: boolean)
    doSort   - Provide SORT functionality   (type: boolean)
    doFilter - Provide FILTER functionality (type: boolean)
    doContextMenus - Use context menus instead of regular buttons (type: boolean)
    doRelationshipNavigation - Navigate to / display child/parent data (type: boolean)
    hasRelationships - Entity has foreign relationships (type: boolean)
    relationshipEntityDescriptors - List of child/parent entities (like entityDescriptors)
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<@templateMacros?interpret/>
<#assign columnCounter = 0/>
<#assign createButton  = "createButton"/>
<#assign readButton    = "viewButton"/>
<#assign updateButton  = "editButton"/>
<#assign deleteButton  = "deleteButton"/>
<#if growlMessages>
  <#assign messageUpdate = ":growl">
<#else>
  <#assign messageUpdate = ":" + entityName + "ListForm:messagePanel">
</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:h="http://java.sun.com/jsf/html"
                xmlns:f="http://java.sun.com/jsf/core"
                xmlns:p="http://primefaces.org/ui"
                xmlns:pm="http://primefaces.org/mobile"
                xmlns:pt="http://xmlns.jcp.org/jsf/passthrough">

    <pm:page id="${entityName}ListPage">
        <pm:header title="${r"#{"}${bundle}.List${entityName}Title${r"}"}">
<#if doCreate>
            <h:form>
                <p:button styleClass="ui-btn-left ui-btn-inline" value="${r"#{"}${bundle}.Menu${r"}"}" icon="ui-icon-arrow-l" outcome="${jsfMobileFolder}${r"/"}${appIndex}"/>
                <p:commandButton id="${createButton}" styleClass="ui-btn-right ui-btn-inline" icon="ui-icon-plus" value="${r"#{"}${bundle}.Create${r"}"}" actionListener="${r"#{"}${managedBean}.${r"prepareCreate}"}" update=":${entityName}CreatePage:${entityName}CreateForm" action="pm:${entityName}CreatePage"/>
            </h:form>
</#if>
        </pm:header>

        <pm:content>

        <h:form id="${entityName}ListForm">

<#if !growlMessages>
                <h:panelGroup id="messagePanel">
                    <p:messages id="listMessages" rendered="${r"#{"}!${managedBean}.validationFailed${r"}"}"/>
                </h:panelGroup>

</#if>
                <p:dataList id="datalist"
                            value="${r"#{"}${managedBeanProperty}${r"}"}"
                            var="${item}"
<#if defaultDataTableRows?? && defaultDataTableRows != "">
                            paginator="true"
                            rows="${defaultDataTableRows}"
                            rowsPerPageTemplate="${defaultDataTableRowsPerPageTemplate}"
</#if>
                            pt:data-inset="true">

                    <f:facet name="header">
                        <h:outputText value="${r"#{"}${bundle}.List${entityName}Title${r"}"}"/>
                    </f:facet>

                    <p:commandLink update=":${entityName}EditPage:${entityName}EditForm" action="pm:${entityName}EditPage">
                        <p>
<#list entityDescriptors as entityDescriptor>

  <#-- Skip this field if we are dealing with many:many -->
  <#if !entityDescriptor.relationshipMany && !entityDescriptor.versionField>
    <#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
        <#if entityDescriptor.getRelationsLabelName(searchLabels)??>
          <#assign relationLabelName = entityDescriptor.getRelationsLabelName(searchLabels)>
        <#else>
              <#assign relationLabelName = "">
        </#if>
    </#if>
<#assign columnCounter = columnCounter + 1/>
<#if (maxTableCols != 0 && columnCounter > maxTableCols)><!--</#if>
    <#if (columnCounter > 1)>,&nbsp;</#if>
    <#if entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
                            <f:convertDateTime pattern="${entityDescriptor.dateTimeFormat}" />
                        </h:outputText>
    <#elseif entityDescriptor.returnType?contains("boolean") || entityDescriptor.returnType?contains("Boolean")>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> disabled="true"/>
    <#elseif entityDescriptor.relationshipOne>
            <#if relationLabelName?? && relationLabelName != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"/>
            <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
<#if (jsfVersion.compareTo("2.2") < 0) && cdiEnabled?? && cdiEnabled>
                            <f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/>
<#else>
                            <f:converter converterId="${entityDescriptor.valuesConverter}"/>
</#if>
                        </h:outputText>
            </#if>
    <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}"/>
    </#if>
<#if (maxTableCols != 0 && columnCounter > maxTableCols)>--></#if>
  </#if>
</#list>
                        </p>
                        <f:setPropertyActionListener value="${r"#{"}${item}${r"}"}" target="${r"#{"}${managedBean}${r".selected}"}" />
                    </p:commandLink>

                    <f:facet name="footer">
                        <h:outputText value="${r"#{"}${bundle}.List${entityName}Title${r"}"}"/>
                    </f:facet>

                </p:dataList>

        </h:form>

        </pm:content>

    </pm:page>

</ui:composition>