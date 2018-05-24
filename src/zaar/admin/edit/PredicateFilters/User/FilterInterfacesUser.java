package zaar.admin.edit.PredicateFilters.User;

import zaar.customer.User;

public class FilterInterfacesUser {
    public interface SetFilterUser {
        public void filter(FilterActionUser filter, boolean remove);

        public void setPredicate();
    }

    public interface FilterActionUser {
        /**
         * Predicate filter for Product
         * @param u Product
         * @return boolean
         */
        public boolean Compare(User u);

        public void setFilter(SetFilterUser setFilter);
    }


}
