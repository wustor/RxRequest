package com.fatchao.rxrequest.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */
public class LoginBean implements Serializable {

    private String code;
    private String message;

    private DataBean data;
    private ProfileBean profile;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }

    public static class DataBean {
        private String token;
        private int expiredSecond;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getExpiredSecond() {
            return expiredSecond;
        }

        public void setExpiredSecond(int expiredSecond) {
            this.expiredSecond = expiredSecond;
        }
    }

    public static class ProfileBean {
        private List<String> type;

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }
    }
}
