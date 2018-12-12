package com.at.test.media.audio;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.at.test.R;
import com.at.test.media.utils.PathUtil;
import com.at.test.utils.TypedUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioActivity extends Activity {

    private static final String TAG = "AudioActivity";

    private TextView tvRecorder;
    private TextView tvPlay;
    private TextView tvRefresh;
    private ListView lvPlayRecord;

    private AudioRecord audioRecord;
    private RecordOutputThread recordOutputThread;
    private AudioTrack audioTrack;
    private PlayRecordThread playRecordThread;
    private List<RecordInfo> recordInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_audio);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        loadRecorderFiles();
    }

    private void initEvent() {

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecorderFiles();
            }
        });

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordInfos == null || recordInfos.isEmpty()) {
                    Toast.makeText(AudioActivity.this, "暂无录音", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (v.isSelected()) {
                    tvPlay.setText("开始播放录音");
                    stopPlayRecorder();
                } else {
                    tvPlay.setText("停止播放录音");
                    String path = recordInfos.get(0).path;
                    play(path);
                }
                v.setSelected(!v.isSelected());
            }
        });

        tvRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    stopRecording();
                    tvRecorder.setText("开始录音");
                } else {
                    startRecording();
                    tvRecorder.setText("停止录音");
                }
                v.setSelected(!v.isSelected());
            }
        });

        lvPlayRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = recordInfos.get(position).path;
                tvPlay.setSelected(true);
                play(path);
            }
        });
    }

    private void loadRecorderFiles() {
        File file = new File(PathUtil.getAudioPcmRecorderSaveDir());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            recordInfos = new ArrayList<>();
            if (files != null) {
                for (File f : files) {
                    String path = f.getAbsolutePath();
                    String name = f.getName();
                    RecordInfo recordInfo = new RecordInfo();
                    recordInfo.name = name;
                    recordInfo.path = path;
                    recordInfos.add(recordInfo);
                }
            }
            lvPlayRecord.setAdapter(new PlayRecorderAdapter(recordInfos));
        }
    }

    public class RecordInfo {
        private String name;
        private String path;
    }

    public class PlayRecorderAdapter extends BaseAdapter {

        private List<RecordInfo> recordInfos;

        public PlayRecorderAdapter(List<RecordInfo> recordInfos) {
            this.recordInfos = recordInfos;
        }

        @Override
        public int getCount() {
            return recordInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecordInfo recordInfo = recordInfos.get(position);
            TextView textView = new TextView(parent.getContext());
            int px = TypedUtil.dp2px(10);
            textView.setPadding(px, px, px, px);
            textView.setText(recordInfo.name);
            return textView;
        }
    }

    private void initView() {
        tvRecorder = findViewById(R.id.test_audio_start_recording);
        tvPlay = findViewById(R.id.test_audio_play_recording_status);
        tvRefresh = findViewById(R.id.test_audio_play_recording_refresh);
        lvPlayRecord = findViewById(R.id.test_audio_play_recording_content_lv);
    }

    private void play(String path) {
        if (audioTrack == null) {
            playRecorder();
        }
        if (playRecordThread != null) {
            playRecordThread.stopPlay();
        }
        playRecordThread = new PlayRecordThread(path, audioTrack);
        playRecordThread.start();
    }

    private void stopPlayRecorder() {
        if (playRecordThread != null) {
            playRecordThread.stopPlay();
        }
    }

    private void playRecorder() {
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT),
                AudioTrack.MODE_STREAM);
        audioTrack.play();
    }

    private void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    16000,
                    AudioFormat.CHANNEL_IN_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);
        }

//        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED
//                && bufferSize != AudioRecord.ERROR_BAD_VALUE) {
//            Log.v(TAG, "AudioRecord.STATE_INITIALIZED");
//            return;
//        }

        String path = PathUtil.getAudioPcmRecorderSavePath();
        Log.v(TAG, path);
        recordOutputThread = new RecordOutputThread(path, audioRecord);
        recordOutputThread.start();
        audioRecord.startRecording();
    }

    private void stopRecording() {
        if (audioRecord != null) {
            audioRecord.stop();
        }
        if (recordOutputThread != null && recordOutputThread.isAlive()) {
            recordOutputThread.startOutput = false;
            recordOutputThread.interrupt();
        }
    }

    public static class RecordOutputThread extends Thread {

        private String output;
        private boolean startOutput = true;
        private AudioRecord audioRecord;

        public RecordOutputThread(String output, AudioRecord audioRecord) {
            this.output = output;
            this.audioRecord = audioRecord;
        }

        public void stopRecord() {
            startOutput = false;
        }

        @Override
        public void run() {
            byte[] bytes = new byte[1024];
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(output);
//                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                while (startOutput) {
                    int size = audioRecord.read(bytes, 0, 1024);
                    if (size >= 0) {
                        fileOutputStream.write(bytes);
                    }

                    if (size == AudioRecord.ERROR_INVALID_OPERATION || size == AudioRecord.ERROR_BAD_VALUE) {
                        Log.v(TAG, "AudioRecord.ERROR_INVALID_OPERATION");
//                        break;
                    }
                    Log.v(TAG, "size:" + size);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class PlayRecordThread extends Thread {
        private String inputPath;
        private AudioTrack audioTrack;
        private boolean playing = true;

        public PlayRecordThread(String inputPath, AudioTrack audioTrack) {
            this.inputPath = inputPath;
            this.audioTrack = audioTrack;
        }

        public void stopPlay() {
            playing = false;
        }

        @Override
        public void run() {
            super.run();
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(inputPath);
                byte[] bytes = new byte[1024];
                while (playing) {
                    int size = fileInputStream.read(bytes);
                    if (size > 0) {
                        audioTrack.write(bytes, 0, size);
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playRecordThread != null) {
            playRecordThread.stopPlay();
            playRecordThread = null;
        }

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }

        if (recordOutputThread != null) {
            recordOutputThread.stopRecord();
            recordOutputThread = null;
        }

        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }


    }
}
