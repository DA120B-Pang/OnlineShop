package zaar.admin.edit.PredicateFilters.User;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.customer.User;
import zaar.product.Menu.Category;

import java.util.function.Predicate;

public class EditFiltersUser {
    private static EditFiltersUser ourInstance = new EditFiltersUser();
    public static EditFiltersUser getInstance() {
        return ourInstance;
    }
    private EditFiltersUser(){
    }

    /**
     * Master control for Product predicate
     */
    public class EditFilterObject implements FilterInterfacesUser.SetFilterUser {
        private ObservableList<FilterInterfacesUser.FilterActionUser> filterList = FXCollections.observableArrayList();
        private FilteredList<User> list;

        public EditFilterObject(FilteredList<User> list){
            this.list = list;
        }

        /**
         * Adds and removes filter from Predicate controll
         * @param filter FilterActionProduct
         * @param remove boolean
         */
        @Override
        public void filter(FilterInterfacesUser.FilterActionUser filter, boolean remove) {
            if (filter != null && remove) {
                filterList.remove(filter);
            }
            else if (filter != null && !filterList.contains(filter)) {
                filterList.add(filter);
            }
            setPredicate();
        }

        @Override
        public void setPredicate() {
            list.setPredicate(predicateUser());//Filtering products
        }

        /**
         * Loops all Predicate filters
         * @return Predicate
         */
        private Predicate<User> predicateUser() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfacesUser.FilterActionUser u : filterList) {//Do all available filters in all Filter actions
                    if (!u.Compare(predicate)) {
                        return false;
                    }
                }
                return true;
            };
        }
    }

    /**
     * Controlls Predicate for name
     */
    public class UserFirstName implements FilterInterfacesUser.FilterActionUser {
        private TextField name;
        private FilterInterfacesUser.SetFilterUser filter;

        /**
         * removes filter from Category filter
         *
         * @param textField
         */
        public void setName(TextField textField) {
            name = textField;
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    filter.filter(this, true);
                } else {
                    filter.filter(this, false);
                }
            });
        }

        /**
         * Sets master controll filter
         *
         * @param setFilter
         */
        public void setFilter(FilterInterfacesUser.SetFilterUser setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(User u) {
            if (u.getFirstName() != null) {
                return u.getFirstName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

    /**
     * Controlls Predicate for name
     */
    public class UserLastName implements FilterInterfacesUser.FilterActionUser {
        private TextField name;
        private FilterInterfacesUser.SetFilterUser filter;

        /**
         * removes filter from Category filter
         *
         * @param textField
         */
        public void setName(TextField textField) {
            name = textField;
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    filter.filter(this, true);
                } else {
                    filter.filter(this, false);
                }
            });
        }

        /**
         * Sets master controll filter
         *
         * @param setFilter
         */
        public void setFilter(FilterInterfacesUser.SetFilterUser setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(User u) {
            if (u.getLastName() != null) {
                return u.getLastName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

    /**
     * Controlls Predicate for name
     */
    public class LoginName implements FilterInterfacesUser.FilterActionUser {
        private TextField name;
        private FilterInterfacesUser.SetFilterUser filter;

        /**
         * removes filter from Category filter
         *
         * @param textField
         */
        public void setName(TextField textField) {
            name = textField;
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty()) {
                    filter.filter(this, true);
                } else {
                    filter.filter(this, false);
                }
            });
        }

        /**
         * Sets master controll filter
         *
         * @param setFilter
         */
        public void setFilter(FilterInterfacesUser.SetFilterUser setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(User u) {
            if (u.getLoginName() != null) {
                return u.getLoginName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

}
