package com.landmark.mediasessionlib.controller.utils.MP3ID3v2;

import android.os.Build;

import androidx.annotation.RequiresApi;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MetaInfoParser_MP3 {
    // Stores the data
    private String m_artist;
    private String m_title;
    private String m_lrc;

    public String getArtist() {
        return m_artist == null ? "Unknown" : m_artist;
    }

    public String getTitle() {
        return m_title == null ? "Unknown" : m_title;
    }

    public String getLrc() {
        return m_lrc == null ? "Unknown" : m_lrc;
    }

    // Same as parse but doesn't throw exceptions
    public boolean parseNoThrow(File mp3file) {
        try {
            return parse(mp3file);
        } catch (Exception ex) {
            return false;
        }
    }

    // Parses the MP3 file, and sets up artist and/or title.
    // Returns true if the file is MP3 file, it contains the ID3 tag, and at least artist or title is parsed
    public boolean parse(File mp3file) throws IOException {
        m_artist = null;
        m_title = null;

        // Read the first five bytes of the ID3 header - http://id3.org/id3v2-00
        RandomAccessFile file = new RandomAccessFile(mp3file, "r");
        byte[] headerBuf = new byte[10];
        file.read(headerBuf);
        // Parse it quickly
        if (headerBuf[0] != 'I' || headerBuf[1] != 'D' || headerBuf[2] != '3') {
            file.close();
            return false;
        }
        // True if the tag is pre-V3 tag (shorter headers)
        final int TagVersion = headerBuf[3];

        // Check the version
        if (TagVersion < 0 || TagVersion > 4) {
            file.close();
            return false;
        }

        // Get the ID3 tag size and flags; see 3.1
        int tagsize = (headerBuf[9] & 0xFF) | ((headerBuf[8] & 0xFF) << 7) | ((headerBuf[7] & 0xFF) << 14) | ((headerBuf[6] & 0xFF) << 21) + 10;
        boolean uses_synch = (headerBuf[5] & 0x80) != 0;
        boolean has_extended_hdr = (headerBuf[5] & 0x40) != 0;

        // Read the extended header length and skip it
        if (has_extended_hdr) {
            int headersize = file.read() << 21 | file.read() << 14 | file.read() << 7 | file.read();
            file.skipBytes(headersize - 4);
        }

        // Read the whole tag
        byte[] buffer = new byte[tagsize];
        file.read(buffer);
        file.close();

        // Prepare to parse the tag
        int length = buffer.length;

        // Recreate the tag if desynchronization is used inside; we need to replace 0xFF 0x00 with 0xFF
        if (uses_synch) {
            int newpos = 0;
            byte[] newbuffer = new byte[tagsize];

            for (int i = 0; i < buffer.length; i++) {
                if (i < buffer.length - 1 && (buffer[i] & 0xFF) == 0xFF && buffer[i + 1] == 0) {
                    newbuffer[newpos++] = (byte) 0xFF;
                    i++;
                    continue;
                }

                newbuffer[newpos++] = buffer[i];
            }
            length = newpos;
            buffer = newbuffer;
        }

        // Set some params
        int pos = 0;
        final int ID3FrameSize = TagVersion < 3 ? 6 : 10;

        // Parse the tags
        while (true) {
            int rembytes = length - pos;

            // Do we have the frame header?
            if (rembytes < ID3FrameSize)
                break;

            // Is there a frame?
            if (buffer[pos] < 'A' || buffer[pos] > 'Z')
                break;
            // Frame name is 3 chars in pre-ID3v3 and 4 chars after
            String framename;
            int framesize;
            if (TagVersion < 3) {
                framename = new String(buffer, pos, 3);
                framesize = ((buffer[pos + 5] & 0xFF) << 8) | ((buffer[pos + 4] & 0xFF) << 16) | ((buffer[pos + 3] & 0xFF) << 24);
            } else {
                framename = new String(buffer, pos, 4);
                framesize = (buffer[pos + 7] & 0xFF) | ((buffer[pos + 6] & 0xFF) << 8) | ((buffer[pos + 5] & 0xFF) << 16) | ((buffer[pos + 4] & 0xFF) << 24);
            }
            if (pos + framesize > length)
                break;
            if (framename.equals("TPE1") || framename.equals("TPE2") || framename.equals("TPE3") || framename.equals("TPE")) {
                if (m_artist == null)
                    m_artist = parseTextField(buffer, pos + ID3FrameSize, framesize);
            }
            if (framename.equals("TIT2") || framename.equals("TIT")) {
                if (m_title == null)
                    m_title = parseTextField(buffer, pos + ID3FrameSize, framesize);
            }
            if (framename.equals("SYLT") || framename.equals("USLT")) {
                if (m_lrc == null)
                    m_lrc = parseTextField2(buffer, pos + ID3FrameSize, framesize);
            }
            pos += framesize + ID3FrameSize;
        }
        return m_title != null || m_artist != null;
    }

    private String parseTextField(final byte[] buffer, int pos, int size) {
        if (size < 2)
            return null;
        Charset charset;
        int charcode = buffer[pos];
        if (charcode == 0)
            charset = StandardCharsets.ISO_8859_1;
        else if (charcode == 3)
            charset = StandardCharsets.UTF_8;
        else
            charset = StandardCharsets.UTF_16;
        return charset.decode(ByteBuffer.wrap(buffer, pos + 1, size - 1)).toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String parseTextField2(final byte[] buffer, int pos, int size) {
        if (size < 2)
            return null;
        Charset charset = StandardCharsets.UTF_16;
        String UTF16 = charset.decode(ByteBuffer.wrap(buffer, pos + 1, size - 1)).toString();
//        Charset charset1 = StandardCharsets.UTF_16LE;
//        String UTF_16LE = charset1.decode(ByteBuffer.wrap(buffer, pos + 1, size - 1)).toString();
//        LogUtils.error(new String(buffer));
//        LogUtils.error(UTF_16LE);
        return UTF16;
    }

}