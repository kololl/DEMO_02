package com.example.demo_02.Model;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private  String  name;
    private String price;

    public Product() {
    }

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result= new HashMap<>();
        result.put("name", name);
        return  result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
