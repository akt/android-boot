package designpattern;

public class Car {
    private final int id;
    private final String brand;
    private final String color;
    private final String size;
    private final String modelNO;
    private final float price;

    public Car(int id, String brand, String color, String size, String modelNO, float price) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.modelNO = modelNO;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getModelNO() {
        return modelNO;
    }

    public float getPrice() {
        return price;
    }

    private Car buildCar(int id, String brand, String color, String size, String modelNO, float price){
        Car car = new Builder()
                .id(1)
                .brand("Benz")
                .modelNO("B200")
                .color("black")
                .size("B")
                .price(350000.f)
                .build();
        return car;
    }

    public class Builder{
        private int id;
        private String brand;
        private String color;
        private String size;
        private String modelNO;
        private float price;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder size(String size) {
            this.size = size;
            return this;
        }

        public Builder modelNO(String modelNO) {
            this.modelNO = modelNO;
            return this;
        }

        public Builder price(float price) {
            this.price = price;
            return this;
        }

        public Car build(){
            return new Car(id, brand, color, size, modelNO, price);
        }
    }
}


