package com.waterworks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.waterworks.utils.AppConfiguration;

public class LevelCalculatorActivity extends Activity {
	
	Button btnYes,btnNoNotSure;
	TextView txtQuestions;
	
	String questions = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_calculator_activity);
		
		btnYes = (Button)findViewById(R.id.btnYes);
		btnNoNotSure = (Button)findViewById(R.id.btnNoNotSure);
		txtQuestions = (TextView)findViewById(R.id.txtQuestions);
		

		// fetching header view

        View view = findViewById(R.id.header);
        TextView title = (TextView)view.findViewById(R.id.action_title);
        title.setText("Register a Child");
        ImageButton ib_menusad = (ImageButton)view.findViewById(R.id.ib_menusad);
        ib_menusad.setBackgroundResource(R.drawable.back_arrow);
        Button relMenu = (Button)view.findViewById(R.id.relMenu);
        relMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(questions.equals("1"))
				{
					txtQuestions.setText(R.string.question_2a);
					questions = "2a";
				}
				
				else if(questions.equals("2a"))
				{
					txtQuestions.setText(R.string.question_3a);
					questions = "3a";
				}
				else if(questions.equals("3a"))
				{
					txtQuestions.setText(R.string.question_4yes);
					questions = "4yes";
				}
				else if(questions.equals("4yes"))
				{
					txtQuestions.setText(R.string.question_5);
					questions = "5";
					
				}
				else if(questions.equals("5"))
				{
					txtQuestions.setText(R.string.question_6);
					questions = "6";
					
					
				}
				else if(questions.equals("6"))
				{
					AppConfiguration.levelTypes = "11";
					
					finish();
				}
				
				else if(questions.equals("2b"))
				{
					questions = "2b";
					AppConfiguration.levelTypes = "4";
					
					finish();
				}
				
				else if(questions.equals("3b"))
				{
					questions = "3b";
					AppConfiguration.levelTypes = "3";
					
					finish();
				}
				
				else if(questions.equals("4no"))
				{
					
					AppConfiguration.levelTypes = "2";
					
					finish();
				}
			}
		});
		
		
		btnNoNotSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(questions.equals("1"))
				{
					txtQuestions.setText(R.string.question_2b);
					questions = "2b";
				}
				else if(questions.equals("2b"))
				{
					txtQuestions.setText(R.string.question_3b);
					questions = "3b";
				}
				else if(questions.equals("3b"))
				{
					txtQuestions.setText(R.string.question_4no);
					questions = "4no";
				}
				else if(questions.equals("4no"))
				{
					//txtQuestions.setText(R.string.question_5);
					
					AppConfiguration.levelTypes = "1";
					
					finish();
				}
				
				else if(questions.equals("2a"))
				{
					//txtQuestions.setText(R.string.question_3a);
					questions = "2a";
					AppConfiguration.levelTypes = "5";
					
					finish();
				}
				
				else if(questions.equals("3a"))
				{
					//txtQuestions.setText(R.string.question_3a);
					questions = "3a";
					AppConfiguration.levelTypes = "7";
					
					finish();
					
				}
				
				else if(questions.equals("4yes"))
				{
					//txtQuestions.setText(R.string.question_5);
					questions = "4yes";
					AppConfiguration.levelTypes = "8";
					
					finish();
				}
				
				else if(questions.equals("5"))
				{
					//txtQuestions.setText(R.string.question_5);
					
					AppConfiguration.levelTypes = "9";
					
					finish();
				}
				
				else if(questions.equals("6"))
				{
					//txtQuestions.setText(R.string.question_5);
					
					AppConfiguration.levelTypes = "10";
					
					finish();
				}
			}
		});
		
	}
}
