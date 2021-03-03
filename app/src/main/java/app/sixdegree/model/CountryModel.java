package app.sixdegree.model;

public class CountryModel {
    String name;
    String c_code;
    String flag;
    boolean isSelected;

    public CountryModel(String name, boolean isSelected,String c_code,String flag) {
        this.name = name;
        this.isSelected = isSelected;
        this.c_code = c_code;
        this.flag = flag;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
            this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getC_code() {
        return c_code;
    }

    public void setC_code(String c_code) {
        this.c_code = c_code;
    }
}
