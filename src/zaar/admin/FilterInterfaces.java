package zaar.admin;

import zaar.product.Product;

import java.util.function.Predicate;

public class FilterInterfaces {
    public interface SetFilter {
        public void filther(FilterAction filter, boolean remove);
    }


    public interface FilterAction{
        public boolean Compare(Product p);
    }
}
