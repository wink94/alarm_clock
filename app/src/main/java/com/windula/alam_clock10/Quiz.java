package com.windula.alam_clock10;

import org.json.JSONArray;

import java.util.Random;

public class Quiz {

    private String question;
    private String correct_answer;
    private String[] incorrect_answers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question.replaceAll("&quot;","\"");
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer.replaceAll("&quot;","\"");
    }

    public String[] getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(String[] incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public boolean isQuizSet(){
        return (this.question!=null && this.correct_answer!=null && this.incorrect_answers.length==3)?true:false;
    }

    public void MathQuizGen(){

        Random rand=new Random();
        this.incorrect_answers=new String[3];

        String[] ops={"+","-","*","/"};

        int num1=rand.nextInt(10);
        String op=ops[rand.nextInt(4)];
        int num2 = rand.nextInt(10);


        this.question="What's the answer for "+Integer.toString(num1)+" "+op+" "+Integer.toString(num2)+ " ?";

        switch (op){
            case "+":
                this.correct_answer=Integer.toString(num1+num2);
                break;
            case "-":
                this.correct_answer=Integer.toString(num1-num2);
                break;
            case "*":
                this.correct_answer=Integer.toString(num1*num2);
                break;
            default:
                this.correct_answer=Integer.toString(num1/num2);
                break;

        }

        this.incorrect_answers[0]=Integer.toString(rand.nextInt(100));
        this.incorrect_answers[1]=Integer.toString(rand.nextInt(100));
        this.incorrect_answers[2]=Integer.toString(rand.nextInt(100));

    }

    public String[] convertJsonArray(JSONArray array){
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i).replaceAll("&quot;","\"");;
        }
        return arr;
    }


}
