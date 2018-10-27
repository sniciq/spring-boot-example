package com.ac.common.util;

public class JsonResultPager<T> extends JsonResult {

    public JsonResultPager(T data, long total) {
        PageData pageData = new PageData(data, total);
        setData(pageData);
    }

    class PageData {
        private long total;
        private T invdata;

        public PageData(T data, long total) {
            this.invdata = data;
            this.total = total;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public T getInvdata() {
            return invdata;
        }

        public void setInvdata(T invdata) {
            this.invdata = invdata;
        }
    }
}
