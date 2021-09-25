package com.example.qma_fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText nric,fullname,password,no_phone,quarantine_location,track_from,track_arrival;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton, positive, negative;
    Button btnSignUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nric = findViewById(R.id.editTextNRIC);
        fullname = findViewById(R.id.editTextFullname);
        password = findViewById(R.id.editTextPassword);
        no_phone = findViewById(R.id.editTextNoPhone);
        quarantine_location = findViewById(R.id.editTextQuarantineLocation);
        track_from = findViewById(R.id.editTextTrackFrom);
        track_arrival = findViewById(R.id.editTextTrackArrival);

        radioGroup = findViewById(R.id.radio_covid_status);
        positive = findViewById(R.id.radio_positive);
        negative = findViewById(R.id.radio_negative);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        btnSignUp = findViewById(R.id.buttonSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NRIC = nric.getText().toString().trim();
                String Fullname = fullname.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String No_Phone = no_phone.getText().toString().trim();
                String Quarantine_Location = quarantine_location.getText().toString().trim();
                int covid19_status = radioGroup.getCheckedRadioButtonId();
                selectedRadioButton = (RadioButton)findViewById(covid19_status);
                String covid_status = selectedRadioButton.getText().toString();
                String Track_From = track_from.getText().toString().trim();
                String Track_Arrival = track_arrival.getText().toString().trim();


                if (NRIC.isEmpty() || Password.isEmpty() || Fullname.isEmpty() ||  No_Phone.isEmpty() || Quarantine_Location.isEmpty() || covid_status.isEmpty() || Track_From.isEmpty() || Track_Arrival.isEmpty() ) {
                    message("The fields cannot be empty");
                } else {
                    progressDialog.setTitle("Updating...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.REGISTER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                    message(response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            message(error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> Params = new HashMap<>();

                            Params.put("nric",NRIC);
                            Params.put("password",Password);
                            Params.put("fullname",Fullname);
                            Params.put("no_phone",No_Phone);
                            Params.put("quarantine_location",Quarantine_Location);
                            Params.put("covid_status",covid_status);
                            Params.put("track_from",Track_From);
                            Params.put("track_arrival",Track_Arrival);
                            return Params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                    queue.add(stringRequest);
                }
            }
        });
    }
    public void message (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
