package zaar.product;




import java.util.Comparator;

public class Manufacturer implements Comparator<Manufacturer>{
    private int id;
    private String name;

    public Manufacturer(){
        this(0,"");
    }
    public Manufacturer(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
    @Override
    public int compare(Manufacturer o1, Manufacturer o2) {
        return o1.name.compareTo(o2.name);
    }


}
