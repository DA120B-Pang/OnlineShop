package zaar.admin;

import javafx.scene.control.Button;

public abstract class FilterShowHide {
    private FilterInterfaces.SetFilter setFilter;

    public FilterShowHide(FilterInterfaces.SetFilter setFilter){
        this.setFilter = setFilter;
    }

    public abstract void hide();
    public abstract void show(double x, double y);
    public abstract void close();
    public abstract boolean getIsShowing();

    public FilterInterfaces.SetFilter getSetFilter(){
        return setFilter;
    }
}
