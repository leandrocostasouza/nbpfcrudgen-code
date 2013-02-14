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

import ${ejbFacadeFullClassName};
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import ${controllerPackageName}.util.JsfUtil;
import java.util.ResourceBundle;
import javax.ejb.EJBException;

/**
 * Represents an abstract shell of to be used as JSF Controller to be used in
 * AJAX-enabled applications. No outcomes will be generated from its methods
 * since handling is designed to be done inside one page.
 */
public abstract class ${abstractControllerClassName}<T> {

    private ${ejbFacadeClassName}<T> ejbFacade;
    private Class<T> itemClass;
    private T selected;
    private List<T> items;

    public ${abstractControllerClassName}() {
    }

    public ${abstractControllerClassName}(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    protected AbstractFacade<T> getFacade() {
        return ejbFacade;
    }

    protected void setFacade(AbstractFacade<T> ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        // Nothing to do if entity does not have any embeddable key.
    };

    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

    /**
     * Returns all items in a List object
     *
     * @return
     */
    public List<T> getItems() {
        if (items == null) {
            items = this.ejbFacade.findAll();
        }
        return items;
    }

    public void save(ActionEvent event) {
        if (selected != null) {
            this.setEmbeddableKeys();
            try {
                this.ejbFacade.edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("${bundle}").getString("${entityClassName}Created"));
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = JsfUtil.getRootCause(ex.getCause());
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("${bundle}").getString("PersistenceErrorOccured"));
            }
        }
    }

    public void saveNew(ActionEvent event) {
        save(event);
        items = null; // Invalidate list of items to trigger re-query.
    }

    public void delete(ActionEvent event) {
        if (selected != null) {
            this.ejbFacade.remove(selected);
            selected = null; // Remove selection
            items = null; // Invalidate list of items to trigger re-query.
        }
    }

    /**
     * Creates a new instance of an underlying entity and assigns it to Selected
     * property.
     *
     * @return
     */
    public T prepareCreate(ActionEvent event) {
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public SelectItem[] getItemsAvailableSelectMany() {
        return getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return getSelectItems(ejbFacade.findAll(), true);
    }

    private SelectItem[] getSelectItems(List<T> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

}

