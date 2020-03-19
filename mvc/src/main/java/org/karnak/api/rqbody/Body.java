package org.karnak.api.rqbody;


public class Body {

    private String type;
    private Data data;

    public String get_type() { return this.type; }
    public Data get_data() { return this.data; }


    public void set_type(String type) { this.type = type; }
    public void set_data(Data data) { this.data = data; }

    public Body(String type, Data data)
    {
        this.type = type;
        this.data = data;
    }

}

