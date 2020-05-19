package util;

public class UserTM {
    private String user_id;
    private String first_name;
    private String contact_no;
    private String email;
    private String user_name;
    private String password;
    private String user_role;
    private String location;

    public UserTM() {

    }


    public UserTM(String user_id,String first_name,String contact_no,String email,String user_name,String password,String user_role,String location){
        this.user_id = user_id;
        this.first_name = first_name;
        this.contact_no = contact_no;
        this.email = email;
        this.user_name = user_name;
        this.password = password;
        this.user_role = user_role;
        this.location = location;
    }

    public String getUser_id(){return user_id;}
    public void setUser_id(String user_id){this.user_id=user_id;}

    public String getFirst_name(){return first_name;}
    public void setFirst_name(String first_name){this.first_name=first_name;}

    public String getContact_no(){return contact_no;}
    public void setContact_no(String contact_no){this.contact_no=contact_no;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}

    public String getUser_name(){return user_name;}
    public void setUser_name(String user_name){this.user_name=user_name;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

    public String getUser_role(){return user_role;}
    public void setUser_role(String user_role){this.user_role=user_role;}

    public String getLocation(){return location;}
    public void setLocation(String location){this.location=location;}

    @Override
    public String toString() {
        return "UserTM{" +
                "user_id='" + user_id + '\'' +
                ",first_name='" + first_name + '\'' +
                ",contact_no='" + contact_no + '\'' +
                ",email='" + email + '\'' +
                ",user_name='" + user_name + '\'' +
                ",password='" + password + '\'' +
                ",user_role='" + user_role + '\'' +
                ",location='" + location + '\'' +
                '}';
    }
}
