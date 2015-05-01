package ${packageName};

import java.util.Objects;
import javax.faces.component.UIComponent;

public class MobilePage {
    
    private UIComponent pageComponent;

    public UIComponent getPageComponent() {
        return pageComponent;
    }

    public void setPageComponent(UIComponent pageComponent) {
        this.pageComponent = pageComponent;
    }

    public MobilePage(UIComponent pageComponent) {
        this.pageComponent = pageComponent;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.pageComponent);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MobilePage other = (MobilePage) obj;
        if (!Objects.equals(this.pageComponent, other.pageComponent)) {
            return false;
        }
        return true;
    }
    
   
    
}
