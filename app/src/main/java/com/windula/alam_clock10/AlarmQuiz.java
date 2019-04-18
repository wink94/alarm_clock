package com.windula.alam_clock10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Random;

public class AlarmQuiz extends AppCompatActivity {

    // Instantiate the RequestQueue.

    private String URL ="https://opentdb.com/api.php?amount=1&difficulty=easy&type=multiple";
    private RequestQueue queue;
    private TextView quiz;
    private RadioButton ans0;
    private RadioButton ans1;
    private RadioButton ans2;
    private RadioButton ans3;
    private Button check;
    //
    private Random rand;

    private Quiz setQuiz;

    private String checkedAnswer;




    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_quiz);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-30;
        getWindow().setAttributes(params);

        //get layout ids
        quiz=(TextView) findViewById(R.id.quiz_tv);
        ans0=(RadioButton)findViewById(R.id.rb0);
        ans1=(RadioButton)findViewById(R.id.rb1);
        ans2=(RadioButton)findViewById(R.id.rb2);
        ans3=(RadioButton)findViewById(R.id.rb3);
        check=(Button)findViewById(R.id.check);


        rand=new Random();
        queue = Volley.newRequestQueue(getApplicationContext());
        //set Quiz
        setQuiz=new Quiz();

        setUpQuiz();

        setButtonClick();
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {

        protected String doInBackground(URL... urls) {
            QuizGenAPI();
            return "";
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute() {
            //showDialog("Downloaded " + result + " bytes");
            setUpView();
        }
    }

    private void QuizGenAPI() {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Response json","Response: " + response.getJSONArray("results").getJSONObject(0).getString("question")+
                                    " answer "
                                    +response.getJSONArray("results").getJSONObject(0).getString("correct_answer")+" incorrect"+
                                    response.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers").getString(0));

                            setQuiz.setQuestion(response.getJSONArray("results").getJSONObject(0).getString("question"));
                            setQuiz.setCorrect_answer(response.getJSONArray("results").getJSONObject(0).getString("correct_answer"));
                            String[] array=setQuiz.convertJsonArray(response.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers"));
                            setQuiz.setIncorrect_answers(array);

                            setUpView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


        queue.add(jsonObjectRequest);
    }



    private void setUpQuiz(){

        int quizType=rand.nextInt(2);

        if (quizType==0){
            setQuiz.MathQuizGen();
            setUpView();
        }
        else {

            //QuizGenAPI();
            DownloadFilesTask task=new DownloadFilesTask();
            task.execute();
            //task.onPostExecute();
        }
    }

    private void setUpView(){

        if(AlarmService.alarmID!=null) {

            if (setQuiz.isQuizSet()) {

                //play alarm
                int value=getResources().getIdentifier(AlarmService.alarmID,"raw",getPackageName());
                mediaPlayer = MediaPlayer.create(this,value);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);

                quiz.setText("Q. "+setQuiz.getQuestion());

                ans0.setText(setQuiz.getCorrect_answer());
                ans1.setText(setQuiz.getIncorrect_answers()[0]);
                ans2.setText(setQuiz.getIncorrect_answers()[1]);
                ans3.setText(setQuiz.getIncorrect_answers()[2]);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }



    private void setButtonClick(){
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(checkedAnswer.isEmpty()){
                   Toast.makeText(v.getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
               }
               else {
                   if(setQuiz.isQuizSet()){
                       if(checkedAnswer.equals(setQuiz.getCorrect_answer())){
                           mediaPlayer.stop();
                           finish();
                       }
                       else {
                           Toast.makeText(v.getContext(), "Incorrect Answer!! Please Try again!!", Toast.LENGTH_SHORT).show();
                       }
                   }
                   else {
                       Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                   }
               }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb0:
                if (checked)
                   checkedAnswer=ans0.getText().toString();
                    break;
            case R.id.rb1:
                if (checked)
                    checkedAnswer=ans1.getText().toString();
                    break;
            case R.id.rb2:
                if (checked)
                    checkedAnswer=ans2.getText().toString();
                    break;
            case R.id.rb3:
                if (checked)
                    checkedAnswer=ans3.getText().toString();
                    break;

                default:
                    checkedAnswer="";
        }
    }


}
