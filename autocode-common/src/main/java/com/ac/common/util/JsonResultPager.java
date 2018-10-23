package com.ac.common.util;

public class JsonResultPager<T> extends JsonResult {

    public JsonResultPager(T data, long total) {
        PageData pageData = new PageData(data, total);
        setData(pageData);
    }

    class PageData {
        private long total;
        private T data;

        public PageData(T data, long total) {
            this.data = data;
            this.total = total;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
