package com.iiysoftware.localtransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button login;
    private EditText email, pass;
    private ProgressDialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String user;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login_btn);
        email = findViewById(R.id.email_id_edit);
        pass = findViewById(R.id.pass_edit);

        dialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setTitle("Logging");
                dialog.setMessage("please wait we are logging you in");
                dialog.show();

                final String em = email.getText().toString();
                final String pa = pass.getText().toString();
                if (!TextUtils.isEmpty(em) || !TextUtils.isEmpty(em))
                {
                    loginProceed(em, pa);

                }else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void loginProceed(String em, String pa) {
        mAuth.signInWithEmailAndPassword(em, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    user = firebaseUser.getUid();
                    flag = 0;

                    db.collection("LocalTransport").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if(doc.get("uid").toString().equals(user))
                                    {
                                        flag = 1;
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                }
                                if(flag == 0) {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Sorry, You don't have authorities to login by this email id", Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                }
                            }
                        }
                    });

                }
                else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
