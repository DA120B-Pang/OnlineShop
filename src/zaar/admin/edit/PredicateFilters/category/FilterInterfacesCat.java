package zaar.admin.edit.PredicateFilters.category;

import zaar.admin.edit.filterPopUps.CompareOperator;
import zaar.product.Menu.Category;

public class FilterInterfacesCat {
    public interface SetFilterCategory {
        public void filter(FilterActionCategory filter, boolean remove);

        public void setPredicate();
    }

    public interface FilterActionCategory {
        /**
         * Predicate filter for Product
         * @param c Product
         * @return boolean
         */
        public boolean Compare(Category c);

        public void setFilter(SetFilterCategory setFilter);
    }


}
