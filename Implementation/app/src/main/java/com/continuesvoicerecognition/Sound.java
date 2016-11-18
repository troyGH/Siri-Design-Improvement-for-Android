package com.continuesvoicerecognition;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.HashMap;

/**
 * Created by Penguins94 on 5/8/2016.
 */
public class Sound extends UtteranceProgressListener{
    protected AudioManager mAudioManager;

    public Sound (Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void muteSound (boolean on) {
        if (on) {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
        else {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }

    public void initiateCall(TextToSpeech speakresult, String message) {

        HashMap<String, String> listenerParameter = new HashMap<String, String>();
        listenerParameter.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
        speakresult.speak(message, TextToSpeech.QUEUE_FLUSH, listenerParameter);
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId, int errorCode) {
        super.onError(utteranceId, errorCode);
    }
}
