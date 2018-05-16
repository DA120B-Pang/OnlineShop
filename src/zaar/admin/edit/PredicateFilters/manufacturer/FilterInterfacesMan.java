package zaar.admin.edit.PredicateFilters.manufacturer;

import zaar.product.Manufacturer;

public class FilterInterfacesMan {
    public interface SetFilterManufacturer {
        public void filter(FilterActionManufacurer filter, boolean remove);

        public void setPredicate();
    }

    public interface FilterActionManufacurer {
        /**
         * Predicate filter for Product
         * @param m Manufacturer
         * @return boolean
         */
        public boolean Compare(Manufacturer m);

        public void setFilter(SetFilterManufacturer setFilter);
    }


}
