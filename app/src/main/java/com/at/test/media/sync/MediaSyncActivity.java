package com.at.test.media.sync;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaSync;
import android.media.PlaybackParams;
import android.media.SyncParams;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class MediaSyncActivity extends Activity {

    private static final String TAG = "MediaSyncActivity";

    private MediaSync mediaSync;
    private SurfaceView svSync;
    private MediaCodec videoCodec;
    private MediaCodec audioCodec;
    private Surface inputSurface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_sync);
        svSync = findViewById(R.id.test_media_sync_sv);
        svSync.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                intiSync(holder.getSurface());
                syncVideo2();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        syncAudio();
                    }
                }.start();
                syncStart();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void syncStart() {
        if (Build.VERSION.SDK_INT > 22) {
            SyncParams sync = new SyncParams().allowDefaults();
            mediaSync.setSyncParams(sync);
            mediaSync.setPlaybackParams(new PlaybackParams().setSpeed(1f));
        }
    }

    private void syncVideo2() {
        if (Build.VERSION.SDK_INT > 22) {
            try {
                Log.v(TAG, "to syncVideo ...");
                String videoPath = PathUtil.getVideoExtractorMp4();

                final MediaExtractor extractor = new MediaExtractor();
                extractor.setDataSource(videoPath);

                for (int i = 0; i < extractor.getTrackCount(); i++) {
                    MediaFormat format = extractor.getTrackFormat(i);
                    String mimeType = format.getString(MediaFormat.KEY_MIME);
                    try {
                        int width = format.getInteger(MediaFormat.KEY_WIDTH);
                        int height = format.getInteger(MediaFormat.KEY_HEIGHT);
                        Log.v(TAG, "video codec mimeType : " + mimeType);
                        Log.v(TAG, "video codec width : " + width);
                        Log.v(TAG, "video codec height : " + height);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mimeType.startsWith("video/")) {
                        extractor.selectTrack(i);
                        videoCodec = MediaCodec.createDecoderByType(mimeType);
                        videoCodec.configure(format, inputSurface, null, 0);
                        break;
                    }
                }

                videoCodec.setCallback(new MediaCodec.Callback() {
                    @Override
                    public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                        Log.v(TAG, "video codec onInputBufferAvailable");
                        try {
                            ByteBuffer byteBuffer = codec.getInputBuffer(index);
                            int size = extractor.readSampleData(byteBuffer, 0);
                            if (size < 0) {
                                return;
                            } else {
                                codec.queueInputBuffer(index, 0, size, extractor.getSampleTime(), 0);
                            }
                            extractor.advance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                        codec.releaseOutputBuffer(index, info.presentationTimeUs * 1000);
                        Log.v(TAG, "video codec onOutputBufferAvailable");
                    }

                    @Override
                    public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                        Log.v(TAG, "video onError : " + e.getMessage());
                    }

                    @Override
                    public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                    }
                });
                videoCodec.start();
                Log.v(TAG, "to syncVideo start");
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "to syncVideo exception");
            }
        }
    }

    private void syncAudio() {
        if (Build.VERSION.SDK_INT > 22) {
            try {
                String audioPath = PathUtil.getVideoExtractorMp4();
                final MediaExtractor extractor = new MediaExtractor();
                extractor.setDataSource(audioPath);
                for (int i = 0; i < extractor.getTrackCount(); i++) {
                    MediaFormat format = extractor.getTrackFormat(i);
                    String mimeType = format.getString(MediaFormat.KEY_MIME);

                    if (mimeType.startsWith("audio/")) {
                        try {
                            int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                            int bitRate = format.getInteger(MediaFormat.KEY_BIT_RATE);
                            int aac_p = format.getInteger(MediaFormat.KEY_AAC_PROFILE);
                            int sample = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                            Log.v(TAG, "audio codec mimeType : " + mimeType);
                            Log.v(TAG, "audio codec channelCount : " + channelCount);
                            Log.v(TAG, "audio codec bitRate : " + bitRate);
                            Log.v(TAG, "audio codec sample : " + sample);
                            Log.v(TAG, "audio codec aac_p : " + aac_p);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        extractor.selectTrack(i);
                        audioCodec = MediaCodec.createDecoderByType(mimeType);
                        audioCodec.configure(format, null, null, 0);
                        break;
                    }
                }

                audioCodec.setCallback(new MediaCodec.Callback() {
                    @Override
                    public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                        Log.v(TAG, "audio codec onInputBufferAvailable");
                        try {
                            ByteBuffer byteBuffer = audioCodec.getInputBuffer(index);
                            int size = extractor.readSampleData(byteBuffer, 0);
                            if (size < 0) {
                                return;
                            }
                            extractor.advance();
                            codec.queueInputBuffer(index, 0, size, extractor.getSampleTime(), 0);

//                                int size = audioFis.read(audioBytes);
//                                Log.v(TAG, "audio codec onInputBufferAvailable size : " + size);
//                                if (index >= 0) {
//                                    if (size >= 0) {
//                                        Log.v(TAG, "audio codec onInputBufferAvailable index : " + index);
//                                        ByteBuffer byteBuffer = audioCodec.getInputBuffer(index);
//                                        byteBuffer.put(audioBytes);
//                                        codec.queueInputBuffer(index, 0, size, System.nanoTime(), 0);
//                                    } else {
////                                    codec.queueInputBuffer(0, 0, 0, 0, 0);
//                                        return;
//                                    }
//                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                        Log.v(TAG, "audio codec onOutputBufferAvailable");
                        if (info.size > 0) {
                            ByteBuffer byteBuffer = codec.getOutputBuffer(index);
                            if (byteBuffer == null) {
                                return;
                            }
                            mediaSync.queueAudio(byteBuffer, index, info.presentationTimeUs);
                        } else {
                            codec.releaseOutputBuffer(index, false);
                        }
                    }

                    @Override
                    public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                        Log.v(TAG, "onError " + e.getMessage());
                    }

                    @Override
                    public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                    }
                });
                audioCodec.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void intiSync(Surface surface) {
        if (Build.VERSION.SDK_INT > 22) {
            try {
                mediaSync = new MediaSync();
                mediaSync.setSurface(surface);
                inputSurface = mediaSync.createInputSurface();
                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                        44100,
                        AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT),
                        AudioTrack.MODE_STREAM);
                mediaSync.setAudioTrack(audioTrack);
                mediaSync.setCallback(new MediaSync.Callback() {
                    @Override
                    public void onAudioBufferConsumed(@NonNull MediaSync sync, @NonNull ByteBuffer audioBuffer, int bufferId) {
//                        audioBuffer.clear();
                        Log.v(TAG, "audio codec onAudioBufferConsumed");
                    }
                }, null);

                mediaSync.setOnErrorListener(new MediaSync.OnErrorListener() {
                    @Override
                    public void onError(@NonNull MediaSync sync, int what, int extra) {
                        Log.v(TAG, "OnErrorListener , what : " + what + " , extra : " + extra);
                    }
                }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void syncVideo() {
        if (Build.VERSION.SDK_INT > 22) {
            try {
                Log.v(TAG, "to syncVideo ...");
                String videoPath = PathUtil.getSyncVideoMp4Path();
                MediaFormat vformat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_MPEG4, 1080, 1920);
                videoCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_MPEG4);
                videoCodec.configure(vformat, inputSurface, null, 0);

                final FileInputStream videoFis = new FileInputStream(videoPath);
                final byte[] bytes = new byte[1024 * 256];
                videoCodec.setCallback(new MediaCodec.Callback() {
                    @Override
                    public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                        Log.v(TAG, "video codec onInputBufferAvailable");
                        try {
                            int size = videoFis.read(bytes);
                            ByteBuffer byteBuffer = codec.getInputBuffer(index);
                            byteBuffer.put(bytes);
                            if (size < 0) {
                                return;
                            } else {
                                codec.queueInputBuffer(index, 0, size, System.nanoTime(), 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                        codec.releaseOutputBuffer(index, info.presentationTimeUs);
                        Log.v(TAG, "video codec onOutputBufferAvailable");
                    }

                    @Override
                    public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                        Log.v(TAG, "video onError : " + e.getMessage());
                    }

                    @Override
                    public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                    }
                });
                videoCodec.start();
                Log.v(TAG, "to syncVideo start");
            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "to syncVideo exception");
            }
        }
    }

    private void logMetaData() {
        //                try {
//                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//                    mediaMetadataRetriever.setDataSource(audioPath);
//                    String mimeType = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
//                    String bitRate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
//                    String numTracks = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
//                    String cdTrack = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
//                    Log.v(TAG, "audio path mimeType : " + mimeType + " , bitRate : " + bitRate + " , numTracks : " + numTracks + " , cdTrack : " + cdTrack);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
    }

    private void logCodecList() {
        if (Build.VERSION.SDK_INT > 22) {
            MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
            for (MediaCodecInfo info : codecList.getCodecInfos()) {
                Log.v(TAG, "isEncoder : " + info.isEncoder() + ", name : " + info.getName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT > 22) {
            mediaSync.setPlaybackParams(new PlaybackParams().setSpeed(0.f));
            mediaSync.release();
            mediaSync = null;

            if (videoCodec != null) {
                videoCodec.stop();
                videoCodec.release();
                videoCodec = null;
            }

            if (audioCodec != null) {
                audioCodec.stop();
                audioCodec.release();
                audioCodec = null;
            }
        }
    }
}
