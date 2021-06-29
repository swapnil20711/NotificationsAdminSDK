package com.swapnil.helloworld;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.swapnil.helloworld.databinding.ActivityAuthUiBinding;

import java.util.Arrays;
import java.util.List;

public class FirebaseAuthUIActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAuthUiBinding binding=ActivityAuthUiBinding.inflate(getLayoutInflater());
        // It is as same as R.layout.activity
        setContentView(binding.getRoot());
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(FirebaseAuthUIActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    }
            );
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build());

// Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Theme_Helloworld)
                    .build();
            signInLauncher.launch(signInIntent);
        }

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent=new Intent(FirebaseAuthUIActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }
}
