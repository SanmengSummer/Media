package com.landmark.mediasessionlib.controller.utils.MP3ID3v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * MP3 Read Id3v2
 */
public class MP3ReadID3v2 {
    private InputStream mp3ips;
    //        public Charset charset =  Charset.forName("GB18030");
    public Charset charset = StandardCharsets.UTF_16LE;
    private Id3v2Info info;

    public MP3ReadID3v2(File file) {
        try {
            mp3ips = new FileInputStream(file);
            info = new Id3v2Info("unknown", "unknown", "unknown", null, null);
            readId3v2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readId3v2() throws Exception {
        try {
            readId3v2(1024 * 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readId3v2(int buffSize) throws Exception {
        try {
            if (buffSize > mp3ips.available()) {
                buffSize = mp3ips.available();
            }
            byte[] buff = new byte[buffSize];
            mp3ips.read(buff, 0, buffSize);

            if (ByteUtil.indexOf("ID3".getBytes(), buff, 1, 512) == -1)
                throw new Exception("No find ID3V2");
            if (ByteUtil.indexOf("APIC".getBytes(), buff, 1, 512) != -1) {
                int searLen = ByteUtil.indexOf(new byte[]{(byte) 0xFF,
                        (byte) 0xFB}, buff);
                int imgStart = ByteUtil.indexOf(new byte[]{(byte) 0xFF,
                        (byte) 0xD8}, buff);
                int imgEnd = ByteUtil.lastIndexOf(new byte[]{(byte) 0xFF,
                        (byte) 0xD9}, buff, 1, searLen) + 2;
                byte[] imgb = ByteUtil.cutBytes(imgStart, imgEnd, buff);
                info.setApic(imgb);
            }
            if (ByteUtil.indexOf("TIT2".getBytes(), buff, 1, 512) != -1) {
                info.setTit2(new String(readInfo(buff, "TIT2"), charset));
            }
            if (ByteUtil.indexOf("TPE1".getBytes(), buff, 1, 512) != -1) {
                info.setTpe1(new String(readInfo(buff, "TPE1"), charset));
            }
            if (ByteUtil.indexOf("TALB".getBytes(), buff, 1, 512) != -1) {
                info.setTalb(new String(readInfo(buff, "TALB"), charset));
            }
            if (ByteUtil.indexOf("USLT".getBytes(), buff, 1, 512) != -1) {
                info.setLrcContent(new String(readInfo(buff, "USLT"), charset));
            }
            if (ByteUtil.indexOf("SYLT".getBytes(), buff, 1, 512) != -1) {
                info.setLrcContent(new String(readInfo(buff, "SYLT"), charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mp3ips.close();
        }
    }

    private byte[] readInfo(byte[] buff, String tag) {
        int len;
        int offset = ByteUtil.indexOf(tag.getBytes(), buff);
        len = buff[offset + 4] & 0xFF;
        len = (len << 8) + (buff[offset + 5] & 0xFF);
        len = (len << 8) + (buff[offset + 6] & 0xFF);
        len = (len << 8) + (buff[offset + 7] & 0xFF);
        len = len - 1;
        return ByteUtil.cutBytes(ByteUtil.indexOf(tag.getBytes(), buff) + 11,
                ByteUtil.indexOf(tag.getBytes(), buff) + 11 + len, buff);
    }

    public void setInfo(Id3v2Info info) {
        this.info = info;
    }

    public Id3v2Info getInfo() {
        return info;
    }

    public String getName() {
        return getInfo().getTit2();
    }

    public String getAuthor() {
        return getInfo().getTpe1();
    }

    public String getSpecial() {
        return getInfo().getTalb();
    }

    public byte[] getImg() {
        return getInfo().getApic();
    }

    public String getLrc() {
        return getInfo() == null ? null : getInfo().getLrcContent();
    }
}