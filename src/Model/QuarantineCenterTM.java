package Model;

public class QuarantineCenterTM {
    private String quarantine_center_id;
    private String center_name;
    private String city;
    private String district;
    private String head_name;
    private String head_contact_no;
    private String center_contact_no_1;
    private String center_contact_no_2;
    private String capacity;

    public QuarantineCenterTM(String quarantine_center_id,String center_name,String city,String district,String head_name,String head_contact_no,String center_contact_no_1,String center_contact_no_2,String capacity){
        this.quarantine_center_id = quarantine_center_id;
        this.center_name = center_name;
        this.city = city;
        this.district = district;
        this.head_name = head_name;
        this.head_contact_no = head_contact_no;
        this.center_contact_no_1 = center_contact_no_1;
        this.center_contact_no_2 = center_contact_no_2;
        this.capacity = capacity;
    }

    public String getQuarantine_center_id() {return quarantine_center_id; }
    public void setQuarantine_center_id(String quarantine_center_id) {this.quarantine_center_id = quarantine_center_id;}

    public String getCenter_name() {return center_name;}
    public void setCenter_name(String center_name) {this.center_name = center_name;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    public String getDistrict() {return district;}
    public void setDistrict(String district) {this.district = district;}

    public String getHead_name() {return head_name;}
    public void setHead_name(String head_name) {this.head_name = head_name;}

    public String getHead_contact_no() {return head_contact_no;}
    public void setHead_contact_no(String head_contact_no) {this.head_contact_no = head_contact_no;}

    public String getCenter_contact_no_1() {return center_contact_no_1;}
    public void setCenter_contact_no_1(String center_contact_no_1) {this.center_contact_no_1 = center_contact_no_1;}

    public String getCenter_contact_no_2() {return center_contact_no_2;}
    public void setCenter_contact_no_2(String center_contact_no_2) {this.center_contact_no_2 = center_contact_no_2;}

    public String getCapacity() {return capacity;}
    public void setCapacity(String capacity) {this.capacity = capacity;}

    @Override
    public String toString() {
        return center_name;
    }
}
