package com.landmark.mediasessionlib.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: chenhuaxia
 * Description:Lrc parsed into String
 * Date: 2021/8/5 13:49
 **/
public class LrcProcess {

    private final List<LrcContent> LrcList;

    public LrcProcess() {
        LrcList = new ArrayList<>();
        timeSort();
    }

    private void timeSort() {
        Collections.sort(LrcList, (o1, o2) -> o1.getLrc_time().compareTo(o2.getLrc_time()));
    }

    /**
     * Process xxx.lrc  use 'utf-8' in default.
     */
    public String readLRC(File f) {
        return readLRC(f, "UTF-8");
    }

    /**
     * Process xxx.lrc use decode
     */
    public String readLRC(File f, String decode) {
        StringBuilder stringBuilder = new StringBuilder();
        LrcContent mLrcContent;
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis, decode);
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null) {
                s += " ";
                if ((s.contains("[ar:")) || (s.contains("[ti:"))
                        || (s.contains("offset"))
                        || (s.contains("[by:"))
                        || (s.contains("[al:")) || s.equals(" ")) {
                    continue;
                }

                s = s.replace("[", "");

                String[] splitLrc_data = s.split("]");
                for (int i = 0; i < splitLrc_data.length - 1; i++) {
                    mLrcContent = new LrcContent(splitLrc_data[splitLrc_data.length - 1], TimeStr(splitLrc_data[i]));
                    LrcList.add(mLrcContent);
                }
                stringBuilder.append(s);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            mLrcContent = new LrcContent("No lrc", 0);
            LrcList.add(mLrcContent);
        } catch (IOException e) {
            stringBuilder.append("无法读取歌词文件！");
            mLrcContent = new LrcContent("No lrc", 0);
            LrcList.add(mLrcContent);
        }
        return stringBuilder.toString();
    }

    /**
     * Process xxx.lrc use decode
     */
    public String readLRCFormString(String lrc, String decode) {
        StringBuilder stringBuilder = new StringBuilder();
        LrcContent mLrcContent = null;
        ByteArrayInputStream bis;
        InputStreamReader is;
        BufferedReader br;
        try {
            bis = new ByteArrayInputStream(lrc.getBytes());
            is = new InputStreamReader(bis, decode);
            br = new BufferedReader(is);

            String s;
            while ((s = br.readLine()) != null) {
                s += " ";
                if ((s.contains("[ar:")) || (s.contains("[ti:"))
                        || (s.contains("offset"))
                        || (s.contains("[by:"))
                        || (s.contains("[al:")) || s.equals(" ")) {
                    continue;
                }
                s = s.replace("[", "");
                String[] splitLrc_data = s.split("]");
                for (int i = 0; i < splitLrc_data.length - 1; i++) {
                    mLrcContent = new LrcContent(splitLrc_data[splitLrc_data.length - 1], TimeStr(splitLrc_data[i]));
                    LrcList.add(mLrcContent);
                }
                stringBuilder.append(s);
            }
            br.close();
            is.close();
            bis.close();
        } catch (FileNotFoundException e) {
            mLrcContent = new LrcContent("No lrc", 0);
            LrcList.add(mLrcContent);
        } catch (IOException e) {
            stringBuilder.append("无法读取歌词文件！");
            mLrcContent = new LrcContent("No lrc", 0);
            LrcList.add(mLrcContent);
        }
        String s = stringBuilder.toString();
        if (mLrcContent == null && !s.isEmpty()) {
            mLrcContent = new LrcContent(s, 0);
            LrcList.add(mLrcContent);
        }
        return s;
    }

    /**
     * Time utils, second -> millisecond
     */
    private int TimeStr(String timeStr) {

        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");
        String[] timeData = timeStr.split("@");
        if (timeData.length <= 0) return 0;
        int currentTime = 0;
        try {
            int minute = Integer.parseInt(timeData[0].trim());
            int second = Integer.parseInt(timeData[1].trim());
            int millisecond = Integer.parseInt(timeData[2].trim());
            currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentTime;
    }

    public List<LrcContent> getLrcContent() {
        return LrcList;
    }

    /**
     * The bean of lrc(text of lyric , time of lyric)
     */
    public static class LrcContent implements Parcelable {
        private String Lrc;
        private Integer Lrc_time;

        public LrcContent() {
        }

        public LrcContent(String lrc, Integer lrc_time) {
            Lrc = lrc;
            Lrc_time = lrc_time;
        }

        public String getLrc() {
            return Lrc;
        }

        public void setLrc(String lrc) {
            Lrc = lrc;
        }

        public Integer getLrc_time() {
            return Lrc_time;
        }

        public void setLrc_time(int lrc_time) {
            Lrc_time = lrc_time;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.Lrc);
            dest.writeInt(this.Lrc_time);
        }

        public static final Creator<LrcContent> CREATOR = new Creator<LrcContent>() {
            @Override
            public LrcContent createFromParcel(Parcel source) {
                LrcContent vacItem = new LrcContent();
                vacItem.Lrc = source.readString();
                vacItem.Lrc_time = source.readInt();
                return vacItem;
            }

            @Override
            public LrcContent[] newArray(int size) {
                return new LrcContent[size];
            }
        };
    }

}
