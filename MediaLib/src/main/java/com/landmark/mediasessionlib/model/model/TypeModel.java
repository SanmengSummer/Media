/**********************************************
 * Filename：
 * Author:   qiang.chen@landmark-phb.com
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.xx  2021/8/10 11  chenqiang   1) …
 ***********************************************/
package com.landmark.mediasessionlib.model.model;

/**
 * Type is converted to an object
 */
public class TypeModel {

    private String category; //genre album folder artist
    private String format;  // mp3 mp4
    private String category_name; //The name of the specific type
    private String mediaId; //song id
    private int page;
    private int size;

    public String getCategory() {
        return category;
    }

    public TypeModel setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public TypeModel setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getCategory_name() {
        return category_name;
    }

    public TypeModel setCategory_name(String category_name) {
        this.category_name = category_name;
        return this;
    }

    public String getMediaId() {
        return mediaId;
    }

    public TypeModel setMediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "TypeModel{" +
                "category='" + category + '\'' +
                ", format='" + format + '\'' +
                ", category_name='" + category_name + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
