package zaar.admin.edit.PredicateFilters.menus;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;

import java.util.function.Predicate;

public class EditFiltersMenus {
    private static EditFiltersMenus ourInstance = new EditFiltersMenus();
    public static EditFiltersMenus getInstance() {
        return ourInstance;
    }
    private EditFiltersMenus(){
    }

    /**
     * Master control for Product predicate
     */
    public class EditFilterObject implements FilterInterfacesMenus.SetFilterMenus {
        private ObservableList<FilterInterfacesMenus.FilterActionMenus> filterList = FXCollections.observableArrayList();
        private FilteredList<Menus> list;

        public EditFilterObject(FilteredList<Menus> list){
            this.list = list;
        }

        /**
         * Adds and removes filter from Predicate controll
         * @param filter FilterActionProduct
         * @param remove boolean
         */
        @Override
        public void filter(FilterInterfacesMenus.FilterActionMenus filter, boolean remove) {
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
            list.setPredicate(predicateMenus());//Filtering products
        }

        /**
         * Loops all Predicate filters
         * @return Predicate
         */
        private Predicate<Menus> predicateMenus() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfacesMenus.FilterActionMenus m : filterList) {//Do all available filters in all Filter actions
                    if (!m.Compare(predicate)) {
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
    public class MenuName implements FilterInterfacesMenus.FilterActionMenus {
        private TextField name;
        private FilterInterfacesMenus.SetFilterMenus filter;

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
        public void setFilter(FilterInterfacesMenus.SetFilterMenus setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(Menus c) {
            if (c.getName() != null) {
                return c.getName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

    /**
     * Controlls Predicate for parentMenu Id
     */
    public class parentMenuId implements FilterInterfacesMenus.FilterActionMenus {
        private SimpleIntegerProperty id;
        private FilterInterfacesMenus.SetFilterMenus filter;

        /**
         * removes filter from Product filter
         * @param id SimpleIntegerProperty
         */
        public void setId(SimpleIntegerProperty id){
            this.id = id;
            id.addListener((observable, oldValue, newValue) -> {
                if(newValue.intValue()==0) {
                    filter.filter(this, true);
                }else{
                    filter.filter(this, false);
                }
            });
        }

        /**
         * Sets master controll filter
         * @param setFilter SetFilterProduct
         */
        public void setFilter(FilterInterfacesMenus.SetFilterMenus setFilter) {
            filter = setFilter;
        }


        @Override
        public boolean Compare(Menus m) {
            if (id.intValue() != 0) {
                return m.getParentMenuId() == id.intValue();

            }
            return true;
        }
    }

}
