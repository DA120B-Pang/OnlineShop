package zaar.admin.edit.filterPopUps;

import zaar.admin.edit.PredicateFilters.product.FilterInterfacesProd;

public interface FilterShowHide {

    /**
     * Hides filter window
     */
    void hide();
    /**
     * Shows filter window
     */
    void show(double x, double y);
    /**
     * Closes filter window
     */
    void close();

    /**
     * Returns if filter window is showing
     */
    boolean getIsShowing();

}
