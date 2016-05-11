package com.cskaoyan.birthday.whobirthday.bean;

/**
 * 某人生日的提前日期
 * Created by lamchaohao on 2016/4/21.
 */
public class PreDate {

    private String name;
    private String birthday;
    private String preOne;
    private String preThree;
    private String preSeven;
    private String preFifteen;
    private String preMonth;

    //相应的提前期是否提醒
    private boolean mindBirthday;
    private boolean mindOne;
    private boolean mindThree;
    private boolean mindSeven;
    private boolean mindFifteen;
    private boolean mindMonth;

    public PreDate() {
    }

    public PreDate(String name, String birthday, String preOne, String preThree, String preSeven, String preFifteen, String preMonth) {
        this.name = name;
        this.birthday = birthday;
        this.preOne = preOne;
        this.preThree = preThree;
        this.preSeven = preSeven;
        this.preFifteen = preFifteen;
        this.preMonth = preMonth;
    }

    public PreDate(String name, String birthday, String preOne, String preThree, String preSeven, String preFifteen, String preMonth, boolean mindBirthday, boolean mindOne, boolean mindThree, boolean mindSeven, boolean mindFifteen, boolean mindMonth) {
        this.name = name;
        this.birthday = birthday;
        this.preOne = preOne;
        this.preThree = preThree;
        this.preSeven = preSeven;
        this.preFifteen = preFifteen;
        this.preMonth = preMonth;
        this.mindBirthday = mindBirthday;
        this.mindOne = mindOne;
        this.mindThree = mindThree;
        this.mindSeven = mindSeven;
        this.mindFifteen = mindFifteen;
        this.mindMonth = mindMonth;
    }

    public PreDate(String name, boolean mindOne, boolean mindThree, boolean mindSeven, boolean mindFifteen, boolean mindMonth) {
        this.name = name;
        this.mindOne = mindOne;
        this.mindThree = mindThree;
        this.mindSeven = mindSeven;
        this.mindFifteen = mindFifteen;
        this.mindMonth = mindMonth;
    }

    public String getName() {
        return name;
    }

    public PreDate(boolean mindBirthday, boolean mindOne, boolean mindThree, boolean mindSeven, boolean mindFifteen, boolean mindMonth) {
        this.mindBirthday = mindBirthday;
        this.mindOne = mindOne;
        this.mindThree = mindThree;
        this.mindSeven = mindSeven;
        this.mindFifteen = mindFifteen;
        this.mindMonth = mindMonth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPreOne() {
        return preOne;
    }

    public void setPreOne(String preOne) {
        this.preOne = preOne;
    }

    public String getPreThree() {
        return preThree;
    }

    public void setPreThree(String preThree) {
        this.preThree = preThree;
    }

    public String getPreSeven() {
        return preSeven;
    }

    public void setPreSeven(String preSeven) {
        this.preSeven = preSeven;
    }

    public String getPreFifteen() {
        return preFifteen;
    }

    public void setPreFifteen(String preFifteen) {
        this.preFifteen = preFifteen;
    }

    public String getPreMonth() {
        return preMonth;
    }

    public void setPreMonth(String preMonth) {
        this.preMonth = preMonth;
    }

    public boolean isMindOne() {
        return mindOne;
    }

    public void setMindOne(boolean mindOne) {
        this.mindOne = mindOne;
    }

    public boolean isMindThree() {
        return mindThree;
    }

    public void setMindThree(boolean mindThree) {
        this.mindThree = mindThree;
    }

    public boolean isMindSeven() {
        return mindSeven;
    }

    public void setMindSeven(boolean mindSeven) {
        this.mindSeven = mindSeven;
    }

    public boolean isMindFifteen() {
        return mindFifteen;
    }

    public void setMindFifteen(boolean mindFifteen) {
        this.mindFifteen = mindFifteen;
    }

    public boolean isMindMonth() {
        return mindMonth;
    }

    public void setMindMonth(boolean mindMonth) {
        this.mindMonth = mindMonth;
    }

    public boolean isMindBirthday() {
        return mindBirthday;
    }

    public void setMindBirthday(boolean mindBirthday) {
        this.mindBirthday = mindBirthday;
    }
}
