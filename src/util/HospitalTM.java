package util;

public class HospitalTM {
    private String hospital_id;
    private String hospital_name;
    private String hospital_city;
    private String district;
    private String capacity;
    private String director_name;
    private String director_contact_no;
    private String hospital_contact_no_1;
    private String hospital_contact_no_2;
    private String hospital_fax_no;
    private String hospital_email;

    public HospitalTM(){

    }

    public HospitalTM(String hospital_id,String hospital_name, String hospital_city,String district,String capacity,String director_name,String director_contact_no,String hospital_contact_no_1,String hospital_contact_no_2,String hospital_fax_no,String hospital_email){
        this.hospital_id = hospital_id;
        this.hospital_name = hospital_name;
        this.hospital_city = hospital_city;
        this.district = district;
        this.capacity = capacity;
        this.director_name = director_name;
        this.director_contact_no = director_contact_no;
        this.hospital_contact_no_1 = hospital_contact_no_1;
        this.hospital_contact_no_2 = hospital_contact_no_2;
        this.hospital_fax_no = hospital_fax_no;
        this.hospital_email = hospital_email;
    }

    public String getHospital_id() {return hospital_id;}
    public void setHospital_id(String hospital_id){this.hospital_id=hospital_id;}

    public String getHospital_name(){return hospital_name;}
    public void setHospital_name(String hospital_name){this.hospital_name=hospital_name;}

    public String getHospital_city(){return hospital_city;}
    public void setHospital_city(String hospital_city){this.hospital_city=hospital_city;}

    public String getDistrict(){return district;}
    public void setDistrict(String district){this.district=district;}

    public String getCapacity(){return capacity;}
    public void setCapacity(String capacity){this.capacity=capacity;}

    public String getDirector_name(){return director_name;}
    public void setDirector_name(String director_name){this.director_name=director_name;}

    public String getDirector_contact_no(){return director_contact_no;}
    public void setDirector_contact_no(String director_contact_no){this.director_contact_no=director_contact_no;}

    public String getHospital_contact_no_1(){return hospital_contact_no_1;}
    public void  setHospital_contact_no_1(String hospital_contact_no_1){this.hospital_contact_no_1=hospital_contact_no_1;}

    public String getHospital_contact_no_2(){return hospital_contact_no_2;}
    public void setHospital_contact_no_2(String hospital_contact_no_2){this.hospital_contact_no_2=hospital_contact_no_2;}

    public String getHospital_fax_no(){return hospital_fax_no;}
    public void setHospital_fax_no(String hospital_fax_no){this.hospital_fax_no=hospital_fax_no;}

    public String getHospital_email(){return hospital_email;}
    public void setHospital_email(String hospital_email){this.hospital_email=hospital_email;}

    @Override
    public String toString() {
        return hospital_name;
    }
}
