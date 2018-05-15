package zaar.admin.edit.PredicateFilters.manufacturer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;

import java.util.function.Predicate;

public class EditFiltersMan {
    private static EditFiltersMan ourInstance = new EditFiltersMan();
    public static EditFiltersMan getInstance() {
        return ourInstance;
    }
    private EditFiltersMan(){
    }

    /**
     * Master control for Product predicate
     */
    public class EditFilterObject implements FilterInterfacesMan.SetFilterManufacturer {
        private ObservableList<FilterInterfacesMan.FilterActionManufacurer> filterList = FXCollections.observableArrayList();
        private FilteredList<Manufacturer> list;

        public EditFilterObject(FilteredList<Manufacturer> list){
            this.list = list;
        }

        /**
         * Adds and removes filter from Predicate controll
         * @param filter FilterActionProduct
         * @param remove boolean
         */
        @Override
        public void filter(FilterInterfacesMan.FilterActionManufacurer filter, boolean remove) {
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
            list.setPredicate(FilterActionManufacurer());//Filtering products
        }

        /**
         * Loops all Predicate filters
         * @return Predicate
         */
        private Predicate<Manufacturer> FilterActionManufacurer() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfacesMan.FilterActionManufacurer m : filterList) {//Do all available filters in all Filter actions
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
    public class ManName implements FilterInterfacesMan.FilterActionManufacurer {
        private TextField name;
        private FilterInterfacesMan.SetFilterManufacturer filter;

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
        public void setFilter(FilterInterfacesMan.SetFilterManufacturer setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(Manufacturer m) {
            if (m.getName() != null) {
                return m.getName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }
}
