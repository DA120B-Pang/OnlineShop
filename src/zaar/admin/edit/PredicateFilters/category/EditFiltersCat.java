package zaar.admin.edit.PredicateFilters.category;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.product.Menu.Category;

import java.util.function.Predicate;

public class EditFiltersCat {
    private static EditFiltersCat ourInstance = new EditFiltersCat();
    public static EditFiltersCat getInstance() {
        return ourInstance;
    }
    private EditFiltersCat(){
    }

    /**
     * Master control for Product predicate
     */
    public class EditFilterObject implements FilterInterfacesCat.SetFilterCategory {
        private ObservableList<FilterInterfacesCat.FilterActionCategory> filterList = FXCollections.observableArrayList();
        private FilteredList<Category> list;

        public EditFilterObject(FilteredList<Category> list){
            this.list = list;
        }

        /**
         * Adds and removes filter from Predicate controll
         * @param filter FilterActionProduct
         * @param remove boolean
         */
        @Override
        public void filter(FilterInterfacesCat.FilterActionCategory filter, boolean remove) {
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
            list.setPredicate(predicateCategory());//Filtering products
        }

        /**
         * Loops all Predicate filters
         * @return Predicate
         */
        private Predicate<Category> predicateCategory() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfacesCat.FilterActionCategory c : filterList) {//Do all available filters in all Filter actions
                    if (!c.Compare(predicate)) {
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
    public class CatName implements FilterInterfacesCat.FilterActionCategory {
        private TextField name;
        private FilterInterfacesCat.SetFilterCategory filter;

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
        public void setFilter(FilterInterfacesCat.SetFilterCategory setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(Category c) {
            if (c.getName() != null) {
                return c.getName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

    /**
     * Controlls Predicate for parentMenu Id
     */
    public class parentMenuId implements FilterInterfacesCat.FilterActionCategory {
        private SimpleIntegerProperty id;
        private FilterInterfacesCat.SetFilterCategory filter;

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
        public void setFilter(FilterInterfacesCat.SetFilterCategory setFilter) {
            filter = setFilter;
        }


        @Override
        public boolean Compare(Category c) {
            if (id.intValue() != 0) {
                return c.getParentMenuId() == id.intValue();

            }
            return true;
        }
    }

}
