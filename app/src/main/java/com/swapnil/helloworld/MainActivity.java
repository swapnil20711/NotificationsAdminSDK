package com.swapnil.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swapnil.helloworld.databinding.ActivityMain2Binding;
import com.swapnil.helloworld.notifications.FCMSender;
import com.swapnil.helloworld.notifications.NotificationMessage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMain2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseMessaging.getInstance().subscribeToTopic("messaging");
        setUpButtons();
    }

    private void setUpButtons() {
        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this,FirebaseAuthUIActivity.class));
                        finish();
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("messaging");
                    }
                });
            }
        });
        binding.sendNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.notifMessage.getText().toString().isEmpty()&&(!binding.notifNumber.getText().toString().isEmpty())){
                    new FCMSender().send(String.format(NotificationMessage.message,"messaging", binding.notifMessage.getText().toString(), binding.notifNumber.getText().toString()), new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code()==200){
                                        Toast.makeText(MainActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                                        hideKeyboard(MainActivity.this);
                                    }
                                }
                            });
                        }
                    });
                }else{
                    if (binding.notifNumber.getText().toString().isEmpty()){
                        binding.notifNumber.setError("Please enter the mobile number");
                    }if (binding.notifMessage.getText().toString().isEmpty()){
                        binding.notifMessage.setError("Please enter the message you want to send");
                    }
                }
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}