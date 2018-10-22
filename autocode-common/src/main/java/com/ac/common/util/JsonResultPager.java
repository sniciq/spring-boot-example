package com.ac.common.util;

public class JsonResultPager<T> extends JsonResult {

    public JsonResultPager(T data, int total) {
        PageData pageData = new PageData(data, total);
        setData(pageData);
    }

    class PageData {
        private int total;
        private T data;

        public PageData(T data, int total) {
            this.data = data;
            this.total = total;
        }

        public int getTotal() {
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
