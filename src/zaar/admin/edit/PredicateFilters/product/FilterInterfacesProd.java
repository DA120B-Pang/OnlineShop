package zaar.admin.edit.PredicateFilters.product;

import zaar.admin.edit.filterPopUps.CompareOperator;
import zaar.product.Product;

public class FilterInterfacesProd {
    public interface SetFilterProduct {
        public void filter(FilterActionProduct filter, boolean remove);

        public void setPredicate();
    }

    public interface FilterActionProduct {
        /**
         * Predicate filter for Product
         * @param p Product
         * @return boolean
         */
        public boolean Compare(Product p);

        public void setFilter(SetFilterProduct setFilter);
    }
}
