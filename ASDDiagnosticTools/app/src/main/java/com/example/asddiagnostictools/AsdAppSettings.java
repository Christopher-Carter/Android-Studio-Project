package com.example.asddiagnostictools;

import android.content.Context;

import java.io.IOException;

// Static variables hold values since I'm not familiar with how to pass data
// between activities yet.  May change this later if time permits, this is a dirty solution
public class AsdAppSettings {

    private static AQ10QuestionPack _aq10QuestionPack = null;

    public static AQ10QuestionPack getAQ10QuestionPack ()
    {
        return _aq10QuestionPack;
    }

    public static void LoadAq10QuestionPack(Context context, String assetName)
    {
        try {
            _aq10QuestionPack = new AQ10QuestionPack(context, assetName);
        }
        catch (IOException e) {
            // Do something with this, file could not be loaded...

        }
    }


}
