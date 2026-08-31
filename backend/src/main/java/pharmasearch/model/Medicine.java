package pharmasearch.model;

public class Medicine {

    private int id;
    private String name;
    private double price;
    private String manufacturer;
    private String type;
    private String packSize;
    private String composition1;
    private String composition2;

    public Medicine() {
    }

    public Medicine(int id, String name, double price,
                    String manufacturer, String type,
                    String packSize, String composition1,
                    String composition2) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
        this.type = type;
        this.packSize = packSize;
        this.composition1 = composition1;
        this.composition2 = composition2;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getComposition1() {
        return composition1;
    }

    public void setComposition1(String composition1) {
        this.composition1 = composition1;
    }

    public String getComposition2() {
        return composition2;
    }

    public void setComposition2(String composition2) {
        this.composition2 = composition2;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'' +
                ", type='" + type + '\'' +
                ", packSize='" + packSize + '\'' +
                ", composition1='" + composition1 + '\'' +
                ", composition2='" + composition2 + '\'' +
                '}';
    }
}