package com.fatchao.rxrequest.bean;

import java.io.Serializable;

/**
 * Created by fatchao
 * 日期  2017-03-31.
 * 邮箱  fat_chao@163.com
 */

public class OneBean implements Serializable {

    /**
     * result : SUCCESS
     * hpEntity : {"strLastUpdateDate":"2017-03-31 03:50:13","strDayDiffer":"6","strHpId":"1657","strHpTitle":"VOL.1630","strThumbnailUrl":"http://image.wufazhuce.com/FkJa30EpkRFgPookYi7Uit1y08L5","strOriginalImgUrl":"http://image.wufazhuce.com/FkJa30EpkRFgPookYi7Uit1y08L5","strAuthor":"插画","strContent":"可不想让自己沉溺于虚荣，挣扎于诋毁，磨灭你本该拥有的自在欢愉，真情实性。那你就要记住，知道你自己是谁，比知道他们是谁重要。","strMarketTime":"2017-03-25","sWebLk":"http://m.wufazhuce.com/one/1657","strPn":"","wImgUrl":""}
     */

    private String result;
    private HpEntityBean hpEntity;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HpEntityBean getHpEntity() {
        return hpEntity;
    }

    public void setHpEntity(HpEntityBean hpEntity) {
        this.hpEntity = hpEntity;
    }

    public static class HpEntityBean {
        /**
         * strLastUpdateDate : 2017-03-31 03:50:13
         * strDayDiffer : 6
         * strHpId : 1657
         * strHpTitle : VOL.1630
         * strThumbnailUrl : http://image.wufazhuce.com/FkJa30EpkRFgPookYi7Uit1y08L5
         * strOriginalImgUrl : http://image.wufazhuce.com/FkJa30EpkRFgPookYi7Uit1y08L5
         * strAuthor : 插画
         * strContent : 可不想让自己沉溺于虚荣，挣扎于诋毁，磨灭你本该拥有的自在欢愉，真情实性。那你就要记住，知道你自己是谁，比知道他们是谁重要。
         * strMarketTime : 2017-03-25
         * sWebLk : http://m.wufazhuce.com/one/1657
         * strPn :
         * wImgUrl :
         */

        private String strLastUpdateDate;
        private String strDayDiffer;
        private String strHpId;
        private String strHpTitle;
        private String strThumbnailUrl;
        private String strOriginalImgUrl;
        private String strAuthor;
        private String strContent;
        private String strMarketTime;
        private String sWebLk;
        private String strPn;
        private String wImgUrl;

        public String getStrLastUpdateDate() {
            return strLastUpdateDate;
        }

        public void setStrLastUpdateDate(String strLastUpdateDate) {
            this.strLastUpdateDate = strLastUpdateDate;
        }

        public String getStrDayDiffer() {
            return strDayDiffer;
        }

        public void setStrDayDiffer(String strDayDiffer) {
            this.strDayDiffer = strDayDiffer;
        }

        public String getStrHpId() {
            return strHpId;
        }

        public void setStrHpId(String strHpId) {
            this.strHpId = strHpId;
        }

        public String getStrHpTitle() {
            return strHpTitle;
        }

        public void setStrHpTitle(String strHpTitle) {
            this.strHpTitle = strHpTitle;
        }

        public String getStrThumbnailUrl() {
            return strThumbnailUrl;
        }

        public void setStrThumbnailUrl(String strThumbnailUrl) {
            this.strThumbnailUrl = strThumbnailUrl;
        }

        public String getStrOriginalImgUrl() {
            return strOriginalImgUrl;
        }

        public void setStrOriginalImgUrl(String strOriginalImgUrl) {
            this.strOriginalImgUrl = strOriginalImgUrl;
        }

        public String getStrAuthor() {
            return strAuthor;
        }

        public void setStrAuthor(String strAuthor) {
            this.strAuthor = strAuthor;
        }

        public String getStrContent() {
            return strContent;
        }

        public void setStrContent(String strContent) {
            this.strContent = strContent;
        }

        public String getStrMarketTime() {
            return strMarketTime;
        }

        public void setStrMarketTime(String strMarketTime) {
            this.strMarketTime = strMarketTime;
        }

        public String getSWebLk() {
            return sWebLk;
        }

        public void setSWebLk(String sWebLk) {
            this.sWebLk = sWebLk;
        }

        public String getStrPn() {
            return strPn;
        }

        public void setStrPn(String strPn) {
            this.strPn = strPn;
        }

        public String getWImgUrl() {
            return wImgUrl;
        }

        public void setWImgUrl(String wImgUrl) {
            this.wImgUrl = wImgUrl;
        }
    }
}
