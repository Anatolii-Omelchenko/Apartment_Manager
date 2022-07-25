package prog.academy;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Objects;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @Column(name = "apartment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String district;

    private String address;

    @Column(nullable = false)
    private float area;

    @Column(nullable = false)
    private int rooms;

    private int price;

    public Apartment() {
    }

    public Apartment(String district, String address, float area, int rooms, int price) {
        this.district = district;
        this.address = address;
        this.area = area;
        this.rooms = rooms;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {

        DecimalFormat decimalFormat = new DecimalFormat( "#.##" );

        return "Apartment: " +
                "id = " + id +
                " | district: " + district +
                " | address: " + address +
                " | area: " + decimalFormat.format(area) + " m2" +
                " | rooms: " + rooms +
                " | price: " + price + " $.";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return id == apartment.id && Float.compare(apartment.area, area) == 0 && rooms == apartment.rooms && price == apartment.price && Objects.equals(district, apartment.district) && Objects.equals(address, apartment.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, district, address, area, rooms, price);
    }
}
