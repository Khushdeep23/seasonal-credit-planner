package model;

public class Farmer {

    private Long id;
    private String fullName;
    private String phone;
    private String village;

    public Farmer() {
    }

    public Farmer(String fullName, String phone, String village) {
        this.fullName = fullName;
        this.phone = phone;
        this.village = village;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}