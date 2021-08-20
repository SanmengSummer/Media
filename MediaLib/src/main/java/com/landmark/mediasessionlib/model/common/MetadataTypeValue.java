package com.landmark.mediasessionlib.model.common;

/**********************************************
 * Filename：
 * Author:   qiang.chen@landmark-phb.com
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.xx  2021/8/10 16  chenqiang   1) …
 ***********************************************/
public enum MetadataTypeValue {
    TYPE_ARTIST("TYPE_ARTIST"),//singer
    TYPE_ALBUM("TYPE_ALBUM"),
    TYPE_GENRE("TYPE_GENRE"),
    TYPE_FOLDER("TYPE_FOLDER"),
    TYPE_MUSIC("TYPE_MUSIC");

    private String type;

    MetadataTypeValue(String artist) {
        type = artist;
    }

    public String getType() {
        return type;
    }
}
