package com.example.user.music;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class AudioService extends Service {
    private final IBinder mBinder = (IBinder) new AudioServiceBinder();
    private MediaPlayer mMediaPlayer;
    private boolean isPrepared;
    private int mCurrentPosition;
//    private AudioAdapter.AudioItem mAudioItem;
    private ArrayList<Long> mAudioIds = new ArrayList<>();






    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                mp.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPrepared = false;
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPrepared = false;
                return false;
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {

            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


//간단히 AudioIds 리스트를 받아서 Service 내부 변수에 저장하는 간단한 기능만 갖고 있습니다.
    public void setPlayList(ArrayList<Long> audioIds) {
        if (!mAudioIds.equals(audioIds)) {
            mAudioIds.clear();
            mAudioIds.addAll(audioIds);
        }
    }


//재생목록 기준으로 사용자로부터 재생할 목록의 position값을 받아서 현재 재생할 음악에 대한 정보를 불러와 AudioItem에 저장 합니다.
    private void queryAudioItem(int position) {
        mCurrentPosition = position;
        long audioId = mAudioIds.get(position);
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        String selection = MediaStore.Audio.Media._ID + " = ?";
        String[] selectionArgs = {String.valueOf(audioId)};
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
//                mAudioItem = AudioAdapter.AudioItem.bindCursor(cursor);
            }
            cursor.close();
        }
    }
    //MediaPlayer를 재생가능한 상태로 만들어주는 prepare , 오디오 스트림 타입은 STREAM_MUSIC 으로 지정합니다.
    private void prepare() {
        try {
//            mMediaPlayer.setDataSource(mAudioItem.mDataPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //position 인자값을 갖는 play 함수는 최초 재생시에 호출하게 될 목적으로 구현 되었습니다. 호출됨과 동시에 AudioItem을 셋팅하는
    // queryAudioItem함수를 호출하고, 기존에 재생하고 있던 음악을 종료하는 stop을 호출한 다음 prepare함수를 호출하게 됩니다.
    //position 인자값이 없는 play 함수는 최초 재생이후 prepare가 되어있는 상태의 MediaPlayer를 start 하는 용도로 사용하게 됩니다.
    public void play(int position) {
        queryAudioItem(position);
        mMediaPlayer.stop();
        prepare();
    }

    public void play() {
        if (isPrepared) {
            mMediaPlayer.start();
        }
    }
// 음악을 일시정지하는 pause
    public void pause() {
        if (isPrepared) {
            mMediaPlayer.pause();
        }
    }

    //음악을 종료하는 stop
    private void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
    }

    // 이전 다음곡으로 이동할 수 있는 forwared, rewind
    public void forward() {
        if (mAudioIds.size() - 1 > mCurrentPosition) {
            mCurrentPosition++; // 다음 포지션으로 이동.
        } else {
            mCurrentPosition = 0; // 처음 포지션으로 이동.
        }
        play(mCurrentPosition);
    }

    public void rewind() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--; // 이전 포지션으로 이동.
        } else {
            mCurrentPosition = mAudioIds.size() - 1; // 마지막 포지션으로 이동.
        }
        play(mCurrentPosition);
    }


}
