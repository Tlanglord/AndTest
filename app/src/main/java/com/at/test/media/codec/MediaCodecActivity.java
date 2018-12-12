package com.at.test.media.codec;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.at.test.media.utils.PathUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaCodecActivity extends Activity implements OutputAACDelegate {

    private static final String TAG = "MediaCodecActivity";

    private MediaCodec mediaCodec;

    private long presentationTimeUs = 0;
    private MediaCodec.BufferInfo bufferInfo;

    private FileInputStream inputStream = null;
    private FileOutputStream outputStream = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread() {
            @Override
            public void run() {
                super.run();
//                doCodec();
                encode();
            }
        }.start();
//        encode();
    }

    private void encode() {
        long startTimeMills = System.currentTimeMillis();
        AudioEncoder audioEncoder = null;
        try {
            audioEncoder = new AudioEncoder(this, 44100, 2, 128 * 1024);
            audioEncoder.setRawAAC(true);
            inputStream = new FileInputStream(PathUtil.getCodecPcmSourcePath());
            outputStream = new FileOutputStream(PathUtil.getCodecPcm2AacSavePath("10086"));
            int bufferSize = 1024 * 256;
            byte[] buffer = new byte[bufferSize];
            int encodeBufferSize = 1024 * 10;
            byte[] encodeBuffer = new byte[encodeBufferSize];
            int len = -1;
            while ((len = inputStream.read(buffer)) > 0) {
                int offset = 0;
                while (offset < len) {
                    int encodeBufferLenth = Math.min(len - offset, encodeBufferSize);
                    System.arraycopy(buffer, offset, encodeBuffer, 0, encodeBufferLenth);
                    audioEncoder.fireAudio(encodeBuffer, encodeBufferLenth);
                    offset += encodeBufferLenth;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        audioEncoder.stop();
        int wasteTimeMills = (int) (System.currentTimeMillis() - startTimeMills);
        Log.i("success", "wasteTimeMills is : " + wasteTimeMills);
    }

    @Override
    public void outputAACPacket(byte[] data) {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doCodec() {
        if (Build.VERSION.SDK_INT > 20) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(PathUtil.getCodecPcm2AacSavePath("10086"));
                FileInputStream fileInputStream = new FileInputStream(PathUtil.getCodecPcmSourcePath());
                MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
                MediaFormat mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 44100, 2);
                String mime = mediaCodecList.findEncoderForFormat(mediaFormat);
                mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128 * 1024);//比特率
                mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1024 * 10);
                Log.v(TAG, "findEncoderForFormat : " + mime);
                mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
                mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                mediaCodec.start();

                ByteBuffer[] inputByteBuffers = mediaCodec.getInputBuffers();
                ByteBuffer[] outputByteBuffers = mediaCodec.getOutputBuffers();
                bufferInfo = new MediaCodec.BufferInfo();
                int len = -1;
//                byte[] bytes = new byte[1024 * 256];

                int bufferSize = 1024 * 256;
                byte[] buffer = new byte[bufferSize];
                int encodeBufferSize = 1024 * 10;
                byte[] encodeBuffer = new byte[encodeBufferSize];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    int offset = 0;
                    while (offset < len) {
                        int encodeBufferLenth = Math.min(len - offset, encodeBufferSize);
                        System.arraycopy(buffer, offset, encodeBuffer, 0, encodeBufferLenth);
                        int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
                        if (inputBufferIndex >= 0) {
//                    ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inputBufferIndex);
                            ByteBuffer byteBuffer = inputByteBuffers[inputBufferIndex];
                            Log.v(TAG, "to codec input...");
                            Log.v(TAG, "to codec input size : " + len);
                            byteBuffer.clear();
                            byteBuffer.put(encodeBuffer);
                            long pts = System.nanoTime();
                            mediaCodec.queueInputBuffer(inputBufferIndex, 0, encodeBufferLenth, pts, 0);
                        }

                        Log.v(TAG, "to codec...");

                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                        Log.v(TAG, "outputBufferIndex : " + outputBufferIndex);
                        if (outputBufferIndex >= 0) {
                            Log.v(TAG, "outputBufferIndexgt0 : " + outputBufferIndex);
                        }
                        boolean doRawAAC = true;
                        while (outputBufferIndex >= 0) {
                            if (doRawAAC) {
                                int outBitsSize = bufferInfo.size;
                                ByteBuffer byteBuffer = outputByteBuffers[outputBufferIndex];
                                if (byteBuffer == null) {
                                    continue;
                                }
                                byte[] outData = new byte[outBitsSize];
                                byteBuffer.get(outData);
                                Log.v(TAG, "to codec raw aac output...");
                                fileOutputStream.write(outData);
                            } else {
                                int outBitsSize = bufferInfo.size;
                                int outPacketSize = outBitsSize + 7;
//                    ByteBuffer byteBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);
                                ByteBuffer byteBuffer = outputByteBuffers[outputBufferIndex];
                                if (byteBuffer == null) {
                                    continue;
                                }
                                byteBuffer.position(bufferInfo.offset);
                                byteBuffer.limit(bufferInfo.offset + outBitsSize);

                                //添加ADTS头
                                byte[] outData = new byte[outPacketSize];
                                addADTStoPacket(outData, outPacketSize);

                                byteBuffer.get(outData, 7, outBitsSize);
                                byteBuffer.position(bufferInfo.offset);

                                Log.v(TAG, "to codec output...");
                                Log.v(TAG, "to codec output size : " + byteBuffer.limit());
                                fileOutputStream.write(outData);
                            }
                            mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                            // write to file
                        }

                        offset += encodeBufferLenth;
                    }


                }

                Log.v(TAG, "done codec ...");
                fileInputStream.close();
                fileOutputStream.close();

                mediaCodec.stop();
                mediaCodec.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给编码出的aac裸流添加adts头字段
     *
     * @param packet    要空出前7个字节，否则会搞乱数据
     * @param packetLen
     */
    private void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2;  //AAC LC
        int freqIdx = 4;  //44.1KHz
        int chanCfg = 2;  //CPE
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }


    //计算PTS，实际上这个pts对应音频来说作用并不大，设置成0也是没有问题的
    private long computePresentationTime(long frameIndex) {
        return frameIndex * 90000 * 1024 / 44100;
    }

}
