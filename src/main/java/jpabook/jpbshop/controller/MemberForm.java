package jpabook.jpbshop.controller;

import javax.validation.constraints.NotEmpty;

public class MemberForm {

    // ! javax valid 참고 할 것
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

    public String getName() {
        return name;
    }

    public MemberForm setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public MemberForm setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public MemberForm setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getZipcode() {
        return zipcode;
    }

    public MemberForm setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }
}
