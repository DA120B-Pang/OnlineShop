package zaar.admin;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;
import zaar.product.Product;

import java.util.function.Predicate;

public class EditFilters {
    private static EditFilters ourInstance = new EditFilters();
    public static EditFilters getInstance() {
        return ourInstance;
    }


    public class EditFilterObject implements FilterInterfaces.SetFilter {
        private ObservableList<FilterInterfaces.FilterAction> filterList = FXCollections.observableArrayList();
        private FilteredList<Product> list;

        public EditFilterObject(FilteredList<Product> list){
            this.list = list;
        }

        @Override
        public void filther(FilterInterfaces.FilterAction filter, boolean remove) {
            if (filter != null && remove) {
                filterList.remove(filter);
            }
            if (filter != null && !filterList.contains(filter)) {
                filterList.add(filter);
            }
            list.setPredicate(predicateProduct());
        }


        public Predicate<Product> predicateProduct() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfaces.FilterAction f : filterList) {
                    if (!f.Compare(predicate)) {
                        return false;
                    }
                }
                return true;
            };
        }
    }


    public class ProdName implements FilterInterfaces.FilterAction {
        private TextField name;
        private FilterInterfaces.SetFilter filter;

        /**
         * removes filter from Product filter
         * @param textField
         */
        public void setName(TextField textField){
            name = textField;
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.isEmpty()) {
                    filter.filther(this, true);
                }else{
                    filter.filther(this, false);
                }
            });
        }

        public void setFilter(FilterInterfaces.SetFilter setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(Product p) {
            if(p.getName()!=null) {
                return p.getName().toLowerCase().contains(name.getText().toLowerCase());
            }
            return false;
        }
    }

    public enum PropertyCompare{
                MANUFACTURER,
                CATEGORY;
    }
    public class IntegerPropertyEquals implements FilterInterfaces.FilterAction {
        private SimpleIntegerProperty id;
        private FilterInterfaces.SetFilter filter;
        PropertyCompare type;//For deciding compare metod

        public IntegerPropertyEquals(PropertyCompare type){
            this.type = type;
        }
        /**
         * removes filter from Product filter
         * @param id SimpleIntegerProperty
         */
        public void setId(SimpleIntegerProperty id){
            this.id = id;
            id.addListener((observable, oldValue, newValue) -> {
                if(newValue.intValue()==0) {
                    filter.filther(this, true);
                }else{
                    filter.filther(this, false);
                }
            });
        }

        public void setFilter(FilterInterfaces.SetFilter setFilter) {
            filter = setFilter;
        }

        @Override
        public boolean Compare(Product p) {
            if (id.intValue() != 0) {
                if (type == PropertyCompare.MANUFACTURER) {
                    return p.getManufacturerId() == id.intValue();
                }
                else {//It is Category
                    return p.getProductCategory() == id.intValue();
                }
            }
            return true;
        }

    }
}
