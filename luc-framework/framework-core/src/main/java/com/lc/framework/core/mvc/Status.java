package com.lc.framework.core.mvc;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : rest响应状态封装，包含响应码规范与说明
 * @date : 2023/4/15 13:36
 */
public class Status {
    private Integer code;
    private String desc;

    public Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Status() {}

    public static Status generate(Integer code, String desc) { return new Status(code, desc); }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // o is null or o's class is not Status
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        // if you want compare two objects by o1.equals(o2), you must confirm that the properties of o1 is not null
        Status other = (Status) o;
        Object this$code = this.getCode();
        Object other$code = other.getCode();
        if (this$code == null){
            if (other$code != null) {
                return false;
            }
        }
        else if (!(this$code.equals(other$code))) {
            return false;
        }

        Object this$desc = this.getDesc();
        Object other$desc = other.getDesc();
        if(this$desc == null) {
            return other$desc == null;
        }
        else return this$desc.equals(other$desc);
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $code = this.getCode();
        result = 31 * result + ($code == null ? 43 : $code.hashCode());
        Object $desc = this.getDesc();
        result = 31 * result + ($code == null ? 43 : $desc.hashCode());
        return result;
    }
}
