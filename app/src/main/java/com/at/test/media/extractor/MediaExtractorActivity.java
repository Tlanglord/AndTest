package com.at.test.media.extractor;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音视频抽取
 */
public class MediaExtractorActivity extends Activity {

    private static final String TAG = "MediaExtractorActivity";

    private TextView tvExtractorVideo;
    private TextView tvExtractorAudio;
    private TextView tvExtractorMuxer;

    private MediaExtractor mediaExtractor;
    private MediaMuxer mediaMuxer;
    private int videoTrack = -1;

    private volatile boolean isDoVideoExtracting = false;
    private volatile boolean isDoAudioExtracting = false;
    private volatile boolean isDoExtractMux = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_extractor);
        tvExtractorVideo = findViewById(R.id.test_extractor_video);
        tvExtractorAudio = findViewById(R.id.test_extractor_audio);
        tvExtractorMuxer = findViewById(R.id.test_extractor_mux);

        tvExtractorVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDoVideoExtracting) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            isDoVideoExtracting = true;
                            doVideoExtractor(false);
                            isDoVideoExtracting = false;
                        }
                    }.start();
                }
            }
        });

        tvExtractorAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDoAudioExtracting) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            isDoAudioExtracting = true;
                            doVideoExtractor(true);
                            isDoAudioExtracting = false;
                        }
                    }.start();
                }
            }
        });

        tvExtractorMuxer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDoExtractMux) {
                    isDoExtractMux = true;
//                    doMux();
                    doMux2();
                    isDoExtractMux = false;
                }
            }
        });
    }

    private void doMux2() {
        Log.v(TAG, "to mux...");
        if (Build.VERSION.SDK_INT > 17) {
            try {
                int videoTrackIndex = -1;
                int audioTrackIndex = -1;
                MediaExtractor videoExtractor = new MediaExtractor();
                videoExtractor.setDataSource(PathUtil.getVideoExtractorMp4());

                String savePath = PathUtil.getVideoMuxerSavePath("10086");
                mediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                int trackCount = videoExtractor.getTrackCount();

                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = videoExtractor.getTrackFormat(i);
                    String mimeType = format.getString(MediaFormat.KEY_MIME);
                    if (mimeType.startsWith("audio/")) {
                        videoExtractor.selectTrack(i);
                        videoTrackIndex = mediaMuxer.addTrack(format);
                    } else if (mimeType.startsWith("video/")) {
                        audioTrackIndex = mediaMuxer.addTrack(format);
                    }
                    Log.v(TAG, mimeType);
                }
                mediaMuxer.start();
                Log.v(TAG, "do mux");
                ByteBuffer byteBuffer = ByteBuffer.allocate(100 * 1024);
                Log.v(TAG, "do mux video ...");
                while (true) {
                    int size = videoExtractor.readSampleData(byteBuffer, 0);
                    if (size < 0) {
                        break;
                    }
                    Log.v(TAG, "do mux video size : " + size);
                    int sampleTrackIndex = videoExtractor.getSampleTrackIndex();
                    MediaFormat format = videoExtractor.getTrackFormat(sampleTrackIndex);

                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = size;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(videoTrackIndex, byteBuffer, bufferInfo);
                    videoExtractor.advance();

                }
                Log.v(TAG, "done mux audio ...");

                byteBuffer.clear();

                Log.v(TAG, "do mux audio ...");
                videoExtractor.unselectTrack(videoTrackIndex);
                videoExtractor.selectTrack(audioTrackIndex);
                while (true) {
                    int size = videoExtractor.readSampleData(byteBuffer, 0);
                    if (size < 0) {
                        break;
                    }
                    Log.v(TAG, "do mux audio size : " + size);
                    int sampleTrackIndex = videoExtractor.getSampleTrackIndex();
                    MediaFormat format = videoExtractor.getTrackFormat(sampleTrackIndex);

                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = size;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(audioTrackIndex, byteBuffer, bufferInfo);
                    videoExtractor.advance();
                }
                Log.v(TAG, "done mux audio ...");

                mediaMuxer.stop();
                mediaMuxer.release();

                videoExtractor.release();

                Log.v(TAG, "done mux...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doMux() {
        Log.v(TAG, "to mux...");
        if (Build.VERSION.SDK_INT > 17) {
            try {
                int videoTrackIndex = -1;
                int audioTrackIndex = -1;
                MediaExtractor videoExtractor = new MediaExtractor();
                MediaExtractor audioExtractor = new MediaExtractor();
                videoExtractor.setDataSource(PathUtil.getVideoExtractorMp4());
                audioExtractor.setDataSource(PathUtil.getVideoExtractorMp4());

                String savePath = PathUtil.getVideoMuxerSavePath("10086");
                mediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                int trackCount = videoExtractor.getTrackCount();

                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = videoExtractor.getTrackFormat(i);
                    String mimeType = format.getString(MediaFormat.KEY_MIME);
                    if (mimeType.startsWith("audio/")) {
                        videoExtractor.selectTrack(i);
                        videoTrackIndex = mediaMuxer.addTrack(format);
                    } else if (mimeType.startsWith("video/")) {
                        audioExtractor.selectTrack(i);
                        audioTrackIndex = mediaMuxer.addTrack(format);
                    }
                    Log.v(TAG, mimeType);
                }
                mediaMuxer.start();
                Log.v(TAG, "do mux");
                ByteBuffer byteBuffer = ByteBuffer.allocate(100 * 1024);
                Log.v(TAG, "do mux video ...");
                while (true) {
                    int size = videoExtractor.readSampleData(byteBuffer, 0);
                    if (size < 0) {
                        break;
                    }
                    Log.v(TAG, "do mux video size : " + size);
                    int sampleTrackIndex = videoExtractor.getSampleTrackIndex();
                    MediaFormat format = videoExtractor.getTrackFormat(sampleTrackIndex);

                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = size;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(videoTrackIndex, byteBuffer, bufferInfo);
                    videoExtractor.advance();

                }
                Log.v(TAG, "done mux audio ...");

                byteBuffer.clear();

                Log.v(TAG, "do mux audio ...");
                while (true) {
                    int size = audioExtractor.readSampleData(byteBuffer, 0);
                    if (size < 0) {
                        break;
                    }
                    Log.v(TAG, "do mux audio size : " + size);
                    int sampleTrackIndex = audioExtractor.getSampleTrackIndex();
                    MediaFormat format = audioExtractor.getTrackFormat(sampleTrackIndex);

                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = size;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(audioTrackIndex, byteBuffer, bufferInfo);
                    audioExtractor.advance();
                }
                Log.v(TAG, "done mux audio ...");

                mediaMuxer.stop();
                mediaMuxer.release();

                videoExtractor.release();
                audioExtractor.release();

                Log.v(TAG, "done mux...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doVideoExtractor(boolean audio) {
        int trackIndex = -1;
        Log.v(TAG, "to extractor...");
        String doMimeType = audio ? "audio/" : "video/";
        if (Build.VERSION.SDK_INT > 17) {
            try {
                String sourcePath = PathUtil.getVideoExtractorMp4();
                mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(sourcePath);

                String savePath = audio ? PathUtil.getVideoExtractorAACSavePath("10086") : PathUtil.getVideoExtractorMp4SavePath("10086");
                mediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                int trackCount = mediaExtractor.getTrackCount();
                Log.v(TAG, "getTrackCount");

                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = mediaExtractor.getTrackFormat(i);
                    String mimeType = format.getString(MediaFormat.KEY_MIME);
                    try {
                        int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                        int bitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);
                        int aac_p = format.getInteger(MediaFormat.KEY_AAC_PROFILE);
                        int sample = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                        Log.v(TAG, "mimeType : " + mimeType);
                        Log.v(TAG, "channelCount : " + channelCount);
                        Log.v(TAG, "bitRate : " + bitRate);
                        Log.v(TAG, "sample : " + sample);
                        Log.v(TAG, "aac_p : " + aac_p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (mimeType.startsWith(doMimeType)) {
                        mediaExtractor.selectTrack(i);
                        trackIndex = mediaMuxer.addTrack(format);
                        Log.v(TAG, mimeType);
                        break;
                    }
                }
                mediaMuxer.start();
                Log.v(TAG, "do extractor");
                ByteBuffer byteBuffer = ByteBuffer.allocate(100 * 1024);
                while (true) {
                    int size = mediaExtractor.readSampleData(byteBuffer, 0);
                    Log.v(TAG, "mediaExtractor.readSampleData size: " + size);
                    if (size < 0) {
                        break;
                    }
                    int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                    MediaFormat format = mediaExtractor.getTrackFormat(sampleTrackIndex);

                    if (sampleTrackIndex == trackIndex) {
                        Log.v(TAG, "sampleTrackIndex == trackIndex");
                    }

                    MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                    bufferInfo.offset = 0;
                    bufferInfo.size = size;
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                    mediaMuxer.writeSampleData(trackIndex, byteBuffer, bufferInfo);

                    mediaExtractor.advance();
                }

                mediaMuxer.stop();
                mediaMuxer.release();

                mediaExtractor.release();
                Log.v(TAG, "done extractor");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (Build.VERSION.SDK_INT > 17) {
                if (mediaMuxer != null) {
                    mediaMuxer.stop();
                    mediaMuxer.release();
                    mediaMuxer = null;
                }

                if (mediaExtractor != null) {
                    mediaExtractor.release();
                    mediaExtractor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mux music
//                    MediaExtractor audioExtractor = new MediaExtractor();
//                    ByteBuffer buffer = ByteBuffer.allocate(100);
//                    int audioTrackIndex = 0;
//                    int index = audioExtractor.readSampleData(buffer, 0);
//                    if (index < 0) {
//                        audioExtractor.unselectTrack(audioTrackIndex);
//                        audioExtractor.selectTrack(audioTrackIndex);
//                        index = audioExtractor.readSampleData(buffer, 0);
//                    }
//                    audioExtractor.advance();
//                    MediaCodec.BufferInfo bf = new MediaCodec.BufferInfo();
//                    mediaMuxer.writeSampleData(1, buffer, bf);
}
