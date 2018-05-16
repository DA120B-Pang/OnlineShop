package zaar.admin.edit.PredicateFilters.product;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import zaar.admin.edit.filterPopUps.CompareOperator;
import zaar.product.Product;

import java.util.function.Predicate;

public class EditFiltersProd {
    private static EditFiltersProd ourInstance = new EditFiltersProd();
    private EditFiltersProd(){
    }
    public static EditFiltersProd getInstance() {
        return ourInstance;
    }

    /**
     * Master control for Product predicate
     */
    public class EditFilterObject implements FilterInterfacesProd.SetFilterProduct {
        private ObservableList<FilterInterfacesProd.FilterActionProduct> filterList = FXCollections.observableArrayList();
        private FilteredList<Product> list;

        public EditFilterObject(FilteredList<Product> list){
            this.list = list;
    }

    /**
     * Adds and removes filter from Predicate controll
     * @param filter FilterActionProduct
     * @param remove boolean
     */
        @Override
        public void filter(FilterInterfacesProd.FilterActionProduct filter, boolean remove) {
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
            list.setPredicate(predicateProduct());//Filtering products
        }

        /**
         * Loops all Predicate filters
         * @return Predicate
         */
        public Predicate<Product> predicateProduct() {
            return predicate -> {//Lambda istället för new Predicate<Product>(){...}
                for (FilterInterfacesProd.FilterActionProduct f : filterList) {//Do all available filters in all Filter actions
                    if (!f.Compare(predicate)) {
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
    public class ProdName implements FilterInterfacesProd.FilterActionProduct {
        private TextField name;
        private FilterInterfacesProd.SetFilterProduct filter;

        /**
         * removes filter from Product filter
         * @param textField
         */
        public void setName(TextField textField){
            name = textField;
            name.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.isEmpty()) {
                    filter.filter(this, true);
                }else{
                    filter.filter(this, false);
                }
            });
        }
        /**
         * Sets master controll filter
         * @param setFilter
         */
        public void setFilter(FilterInterfacesProd.SetFilterProduct setFilter) {
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

    /**
     * Used to set type Category and Manufacturer in class IntegerPropertyEquals
     */
    public enum PropertyCompare{
                MANUFACTURER,
                CATEGORY;
    }
    /**
     * Controlls Predicate for Category and Manufacturer
     */
    public class IntegerPropertyEquals implements FilterInterfacesProd.FilterActionProduct {
        private SimpleIntegerProperty id;
        private FilterInterfacesProd.SetFilterProduct filter;
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
        public void setFilter(FilterInterfacesProd.SetFilterProduct setFilter) {
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
    /**
     * Controlls Predicate for Price and Quantity
     */
    public class CompareNumber<T extends Number> implements FilterInterfacesProd.FilterActionProduct {
        private CompareOperator operatorSelection;
        private FilterInterfacesProd.SetFilterProduct filter;
        private T number;
        /**
         * removes filter from Product filter
         * @param addRemoveFilter SimpleIntegerProperty
         */
        public void addRemoveFilter(boolean addRemoveFilter){
            if(!addRemoveFilter) {
                filter.filter(this, true);
            }else{
                filter.filter(this, false);
            }
        }

        public void setOperatorSelection(CompareOperator operatorSelection, T number  ){
            this.operatorSelection = operatorSelection;
            this.number = number;
        }

        @Override
        public void setFilter(FilterInterfacesProd.SetFilterProduct setFilter) {
            this.filter = setFilter;
        }

        @Override
        public boolean Compare(Product p) {
            int result;
            if(number instanceof Integer) {//If quantity then it is Integer
                result = ((Integer) p.getQuantity()).compareTo(((Integer)number));
            }
            else{//If ot quantity then t is Price wich is double

                result = ((Double)p.getPrice()).compareTo(((Double)number));
            }

            if(operatorSelection!=null){ //Check if operator is set
                switch (operatorSelection){
                    case LESS:
                        return result<0;
                    case EQUAL:
                        return result==0;
                    default:
                        return result>0;
                }
            }
            return false;
        }
    }
}
