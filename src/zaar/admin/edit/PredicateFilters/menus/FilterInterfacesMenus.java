package zaar.admin.edit.PredicateFilters.menus;

import zaar.product.Menu.Menus;

public class FilterInterfacesMenus {
    public interface SetFilterMenus {
        public void filter(FilterActionMenus filter, boolean remove);

        public void setPredicate();
    }

    public interface FilterActionMenus {
        /**
         * Predicate filter for Product
         * @param m Product
         * @return boolean
         */
        public boolean Compare(Menus m);

        public void setFilter(SetFilterMenus setFilter);
    }


}
