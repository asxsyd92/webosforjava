package com.webcore.utils.data;

public class MySqlStructure {

public  String column_name;

    public  String column_type;
    public  String data_type;

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public long getCharacter_maximum_length() {
        return character_maximum_length;
    }

    public void setCharacter_maximum_length(long character_maximum_length) {
        this.character_maximum_length = character_maximum_length;
    }

    public String getIs_nullable() {
        return is_nullable;
    }

    public void setIs_nullable(String is_nullable) {
        this.is_nullable = is_nullable;
    }

    public String getColumn_default() {
        return column_default;
    }

    public void setColumn_default(String column_default) {
        this.column_default = column_default;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    public  long character_maximum_length;
    public  String is_nullable;
    public  String column_default;
    public  String column_comment;
}

