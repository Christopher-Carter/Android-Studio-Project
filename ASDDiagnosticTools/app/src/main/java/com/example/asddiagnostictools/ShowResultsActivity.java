package com.example.asddiagnostictools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ShowResultsActivity extends AppCompatActivity {

    Interpreter tensorflowInterpreter;
    TextView textPrediction;
    Button predictionButton = null;
    Button questionButton = null;
    EditText aq1, aq2, aq3, aq4, aq5, aq6, aq7, aq8, aq9, aq10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        //predictionButton = (Button)findViewById(R.id.buttonPredict);
        //questionButton = (Button)findViewById(R.id.buttonQuestions);
        //aq1 = (EditText)findViewById(R.id.editTextAq1);
        //aq2 = (EditText)findViewById(R.id.editTextAq2);
        //aq3 = (EditText)findViewById(R.id.editTextAq3);
        //aq4 = (EditText)findViewById(R.id.editTextAq4);
        //aq5 = (EditText)findViewById(R.id.editTextAq5);
        //aq6 = (EditText)findViewById(R.id.editTextAq6);
        //aq7 = (EditText)findViewById(R.id.editTextAq7);
        //aq8 = (EditText)findViewById(R.id.editTextAq8);
        //aq9 = (EditText)findViewById(R.id.editTextAq9);
        //aq10 = (EditText)findViewById(R.id.editTextAq10);
        //textPrediction = (TextView)findViewById(R.id.textPrediction);

        // Try to Load a Question Pack (as a test)
        //try {
            //_aq10QuestionPack = new AQ10QuestionPack(this, "aq10childQuestions");

        //}
        //catch (IOException ex) {
            //_aq10QuestionPack = null;

        //}

        try
        {
            tensorflowInterpreter = new Interpreter(loadModelFile());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void onFinishClicked(View view)
    {
        moveToMainActivity();
    }

    private void moveToMainActivity()
    {
        Intent getMainActivityIntent = new Intent(this,
                MainActivity.class);

        getMainActivityIntent.putExtra("callingActivity", "PersonalQuestionsActivity");
        getMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION );

        startActivity(getMainActivityIntent);
        this.overridePendingTransition(0, 0);
    }

    public void onPredictionClick(View view)
    {
        float prediction = doInference(aq1.getText().toString(),
                aq2.getText().toString(),
                aq3.getText().toString(),
                aq4.getText().toString(),
                aq5.getText().toString(),
                aq6.getText().toString(),
                aq7.getText().toString(),
                aq8.getText().toString(),
                aq9.getText().toString(),
                aq10.getText().toString());

        textPrediction.setText(String.format("%.4f", prediction));
    }

    public float doInference(String aq1, String aq2, String aq3, String aq4, String aq5,
                             String aq6, String aq7, String aq8, String aq9, String aq10)
    {
        float[] inputValues = new float[10];
        inputValues[0] = Float.valueOf(aq1);
        inputValues[1] = Float.valueOf(aq2);
        inputValues[2] = Float.valueOf(aq3);
        inputValues[3] = Float.valueOf(aq4);
        inputValues[4] = Float.valueOf(aq5);
        inputValues[5] = Float.valueOf(aq6);
        inputValues[6] = Float.valueOf(aq7);
        inputValues[7] = Float.valueOf(aq8);
        inputValues[8] = Float.valueOf(aq9);
        inputValues[9] = Float.valueOf(aq10);

        float[][] outputValues = new float[1][1];

        tensorflowInterpreter.run(inputValues, outputValues);

        float inferredValue = outputValues[0][0];

        return inferredValue;

    }


    private MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("converted_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
