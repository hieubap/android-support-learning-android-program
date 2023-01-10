package com.android.slap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.ActivityMainBinding;
import com.android.slap.databinding.FragmentLoginBinding;
import com.android.slap.event.SinhVienModelEvent;
import com.android.slap.model.FSInstance;
import com.android.slap.model.SinhVienModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyPermission;

public class AuthActivity extends AppCompatActivity implements SinhVienModelEvent {
    private FragmentLoginBinding binding;
    private String adminCode = "";
    private SinhVienModel sinhVienModel;
    private Dialog dialogUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sinhVienModel = new SinhVienModel(this);

        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FSInstance.db.collection("admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        String s = (String)doc.getData().get("code");
                        if(s != null) {
                            adminCode = s;
                        }
                    }
                }
            }
        });

        Button btnThay = binding.btnThay;
        btnThay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialogUpdate = new Dialog(view.getContext());
                dialogUpdate.setCancelable(true);
                dialogUpdate.setContentView(R.layout.input_login_layout);
                dialogUpdate.show();
                Button btn = dialogUpdate.findViewById(R.id.save);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText password = dialogUpdate.findViewById(R.id.password);

                        if((String.valueOf(password.getText())).equals(adminCode)){
                            MainActivity.THAY_TUAN = true;
                            Intent myIntent = new Intent(AuthActivity.this, MainActivity.class);
                            AuthActivity.this.startActivity(myIntent);
                        }else{
                            Toast.makeText(view.getContext(),"Code không chính xác",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        Button btnSv = binding.btnSv;
        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.show();
            }
        });

        dialogUpdate = new Dialog(btnSv.getContext());
        dialogUpdate.setCancelable(true);
        dialogUpdate.setContentView(R.layout.input_login_student_layout);

        Button btn = dialogUpdate.findViewById(R.id.login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sinhVienModel.getData();
            }
        });
    }

    @Override
    public void afterGetData(List<SinhVienDAO> list) {
        EditText password = dialogUpdate.findViewById(R.id.password);
        Map<String,SinhVienDAO> map = new HashMap<>();

        for (SinhVienDAO sv : list){
            map.put(sv.mssv,sv);
        }
        MainActivity.MAP_STUDENT = map;
        if(map.containsKey(String.valueOf(password.getText()))){
            MainActivity.USER_ID = String.valueOf(password.getText());
            Intent myIntent = new Intent(AuthActivity.this, MainActivity.class);
            AuthActivity.this.startActivity(myIntent);
            return;
        }
        Toast.makeText(getWindow().getContext(), "MSSV không tồn tại",Toast.LENGTH_LONG).show();
    }

    @Override
    public void afterSave() {

    }
}