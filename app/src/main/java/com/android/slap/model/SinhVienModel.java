package com.android.slap.model;

import androidx.annotation.NonNull;

import com.android.slap.dao.SinhVienDAO;
import com.android.slap.event.SinhVienModelEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SinhVienModel {
    private SinhVienModelEvent sinhVienModelEvent;
    public List<SinhVienDAO> listSinhVienDAO = new ArrayList<>();

    public SinhVienModel(SinhVienModelEvent sinhVienModelEvent){
        this.sinhVienModelEvent = sinhVienModelEvent;
    }

    public void checkIn(SinhVienDAO sv, int sessionId){
        handleCheck(sv, sessionId,true);
    }

    public void checkOut(SinhVienDAO sv,int sessionId){
        handleCheck(sv, sessionId,false);
    }

    public void getData(){
        FSInstance.db.collection("student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<SinhVienDAO> sinhVienDTOs = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        SinhVienDAO studentModel = new SinhVienDAO(doc);
                        sinhVienDTOs.add(studentModel);
                    }
                }
                listSinhVienDAO = sinhVienDTOs;
                sinhVienModelEvent.afterGetData(sinhVienDTOs);
            }
        });
    }

    public SinhVienDAO getByKey(int key){
        for(SinhVienDAO sv : listSinhVienDAO){
            if(sv.key == key) return sv;
        }
        SinhVienDAO sv =new SinhVienDAO();
        sv.key = key;
        return sv;
    }

    public void save(SinhVienDAO sv){
        FSInstance.db.collection("student")
                .document(""+sv.key)
                .set(sv)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sinhVienModelEvent.afterSave();
                    }
                });
    }

    private void handleCheck(SinhVienDAO sv, int sessionId, boolean status){
        sv.checks.put(""+sessionId, status);

        FSInstance.db.collection("student")
                .document(sv.key+"")
                .update("checks",sv.checks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(status)
                            sinhVienModelEvent.onSuccessIn(sv);
                        else
                            sinhVienModelEvent.onSuccessOut(sv);
                    }
                });
    }

}
