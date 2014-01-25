<#if comment>

  TEMPLATE DESCRIPTION:

  This is Bundle.properties template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    entities - list of beans with following properites:
        entityClassName - controller class name (type: String)
        entityDescriptors - list of beans describing individual entities. Bean has following properties:
            label - part of bundle key name for label (type: String)
            title - part of bundle key name for title (type: String)
            name - field property name (type: String)
            dateTimeFormat - date/time/datetime formatting (type: String)
            blob - does field represents a large block of text? (type: boolean)
            relationshipOne - does field represent one to one or many to one relationship (type: boolean)
            relationshipMany - does field represent one to many relationship (type: boolean)
            id - field id name (type: String)
            required - is field optional and nullable or it is not? (type: boolean)
            valuesGetter - if item is of type 1:1 or 1:many relationship then use this
                getter to populate <h:selectOneMenu> or <h:selectManyMenu>

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
PersistenceErrorOccured=A persistence error occurred.
Previous=Previous
Next=Next

AppName=Application Name
Cancel=Cancel
Create=Create
Delete=Delete
Edit=Edit
Close=Close
Save=Save
View=View
Yes=Yes
No=No
Maintenance=Maintenance
Home=Home
Welcome=Welcome
WelcomeMessage=Welcome to your PrimeFaces Application. Please explore by making a selection from the menu.
SelectOneMessage=Select One...
ConfirmationHeader=Confirmation
ConfirmDeleteMessage=Are you sure you want to proceed?
ConfirmEditMessage=Do you want to apply the changes?
ConfirmCreateMessage=Ready to create?

<#list entities as entity>
${entity.entityClassName}Heading=${entity.entityClassName}
${entity.entityClassName}Title=${entity.entityClassName} Maintenance
${entity.entityClassName}Created=${entity.entityClassName} was successfully created.
${entity.entityClassName}Updated=${entity.entityClassName} was successfully updated.
${entity.entityClassName}Deleted=${entity.entityClassName} was successfully deleted.
Create${entity.entityClassName}Title=Create New ${entity.entityClassName}
Create${entity.entityClassName}SaveLink=Save
Create${entity.entityClassName}ShowAllLink=Show All ${entity.entityClassName} Items
Create${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
Create${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
<#if entityDescriptor.required>Create${entity.entityClassName}RequiredMessage_${entityDescriptor.id?replace(".","_")}=The ${entityDescriptor.label} field is required.
</#if>Create${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
Create${entity.entityClassName}HelpText_${entityDescriptor.id?replace(".","_")}=Please provide information for ${entityDescriptor.label}
    </#list>
Edit${entity.entityClassName}Title=Edit ${entity.entityClassName}
Edit${entity.entityClassName}SaveLink=Save
Edit${entity.entityClassName}ViewLink=View
Edit${entity.entityClassName}ShowAllLink=Show All ${entity.entityClassName} Items
Edit${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
Edit${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
<#if entityDescriptor.required>Edit${entity.entityClassName}RequiredMessage_${entityDescriptor.id?replace(".","_")}=The ${entityDescriptor.label} field is required.
</#if>Edit${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
Edit${entity.entityClassName}HelpText_${entityDescriptor.id?replace(".","_")}=Please provide information for ${entityDescriptor.label}
    </#list>
View${entity.entityClassName}Title=View ${entity.entityClassName}
View${entity.entityClassName}DestroyLink=Destroy
View${entity.entityClassName}EditLink=Edit
View${entity.entityClassName}CreateLink=Create New ${entity.entityClassName}
View${entity.entityClassName}ShowAllLink=Show All ${entity.entityClassName} Items
View${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
View${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
View${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
    </#list>
List${entity.entityClassName}Title=List ${entity.entityClassName}
List${entity.entityClassName}Empty=(No ${entity.entityClassName} Items Found)
List${entity.entityClassName}DestroyLink=Destroy
List${entity.entityClassName}EditLink=Edit
List${entity.entityClassName}ViewLink=View
List${entity.entityClassName}CreateLink=Create New ${entity.entityClassName}
List${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
List${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
    </#list>
</#list>
