package com.ac.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StaffEty {
    private Integer id;	//ID
    private String name;	//员工名称
    private Integer age;	//年龄

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd" , timezone="GMT+8")
    private java.util.Date birthday;	//员工生日
    private String email;	//邮箱地址
    /**
     * 得到 ID
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }
    /**
     * 设置 ID
     * @param id,  : Integer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 得到 员工名称
     * @return String
     */
    public String getName() {
        return this.name;
    }
    /**
     * 设置 员工名称
     * @param name,  : String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 得到 年龄
     * @return Integer
     */
    public Integer getAge() {
        return this.age;
    }
    /**
     * 设置 年龄
     * @param age,  : Integer
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 得到 员工生日
     * @return java.util.Date
     */
    public java.util.Date getBirthday() {
        return this.birthday;
    }
    /**
     * 设置 员工生日
     * @param birthday,  : java.util.Date
     */
    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 得到 邮箱地址
     * @return String
     */
    public String getEmail() {
        return this.email;
    }
    /**
     * 设置 邮箱地址
     * @param email,  : String
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
