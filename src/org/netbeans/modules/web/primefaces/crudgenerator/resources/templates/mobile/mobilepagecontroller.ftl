/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ${packageName};

<#if cdiEnabled?? && cdiEnabled>
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
</#if>
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
<#if !(cdiEnabled?? && cdiEnabled)>
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
</#if>
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import org.primefaces.mobile.component.page.Page;

<#if cdiEnabled?? && cdiEnabled>
@Named(value = "mobilePageController")
<#else>
@ManagedBean(name = "mobilePageController")
</#if>
@SessionScoped
public class MobilePageController implements Serializable {

    List<MobilePage> pageHistory;
    private MobilePage currentPage;

    /**
     * Creates a new instance of HistoryBean
     */
    public MobilePageController() {
    }
    
    public void clearPageHistory(ActionEvent event) {
        this.pageHistory.clear();
    this.currentPage = null;
    }

    public void currentPageListener(ActionEvent event) {
        MobilePage nearestPage = getNearestPage(event.getComponent());
        if (nearestPage != null && !nearestPage.equals(currentPage)) {
            this.currentPage = nearestPage;
        }
    }

    public String navigateWithHistory(String actionName) {
        if (currentPage != null) {
            pageHistory.add(currentPage);
        }
        return actionName;
    }

    public String navigateBackInHistory(String fallBackActionName) {
        if (pageHistory.size() > 0) {
            MobilePage lastPage = this.pageHistory.get(pageHistory.size() - 1);
            return "pm:" + lastPage.getPageComponent().getId();
        }
        return fallBackActionName;
    }

    private MobilePage getNearestPage(UIComponent component) {
        UIComponent nearestPageComponent = findPageComponent(component);
        if (nearestPageComponent != null) {
            return(new MobilePage(nearestPageComponent));
        }
        return null;
    }

    private UIComponent findPageComponent(UIComponent component) {
        if (Page.class.isAssignableFrom(component.getClass())) {
            return component;
        }
        if (component.getParent() != null) {
            UIComponent parentComponent = findPageComponent(component.getParent());
            if (parentComponent != null) {
                return parentComponent;
            }
        }
        return null;
    }

    @PostConstruct
    public void init() {
        this.pageHistory = new ArrayList<>();
    }

}
