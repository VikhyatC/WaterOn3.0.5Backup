package com.wateron.smartrhomes.realtime;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Paranjay on 27-12-2017.
 */

public class RealTimeLoader {


    private  FirebaseFirestore db ;
    private  RealTimeProvider provider;
    private String isd,mobile;
    private DocumentReference userref;
    public RealTimeLoader(RealTimeProvider provider,String mobile, String isd){
        db = FirebaseFirestore.getInstance();
        this.provider = provider;
        this.mobile=mobile;
        this.isd=isd;
    }

    public void loadUser(){
        userref = db.collection("users").document(isd+"-"+mobile);
        userref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot != null&& documentSnapshot.exists()){
                    Map<String,Object> data = documentSnapshot.getData();
                    provider.setUserValue(data);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error","cant get user data");
                e.printStackTrace();
            }
        });
    }

    public void loadApartmentsData(Object aptIds){
        List<String> apts = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(aptIds.toString());
            for(int i=0;i<array.length();i++){
                apts.add(array.get(i).toString());
            }
            for (String a : apts) {
                db.collection("apartments").document(a).get().addOnSuccessListener(((RealTimeDash) provider).getActivity(), new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot !=null && documentSnapshot.exists()){
                            Map<String, Object> map=documentSnapshot.getData();
                            try{
                                provider.apartmentsDetail(map);
                            }catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                    }
                })
                .addOnFailureListener(((RealTimeDash) provider).getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("apts",apts.toString());
    }

    public void listenForMeter(String apt){

    }

    public void stopListening(){
        if(listenerRegistrations==null){
            listenerRegistrations=new ArrayList<>();
        }
        for(int i=0;i<listenerRegistrations.size();i++){
            listenerRegistrations.get(i).remove();
        }
        documentReferenceList = new ArrayList<>();
        listeners = new ArrayList<>();
    }
    List<DocumentReference> documentReferenceList = new ArrayList<>();
    List<EventListener<DocumentSnapshot>> listeners = new ArrayList<>();

    List<ListenerRegistration> listenerRegistrations = new ArrayList<>();
    public void startListnersForApartments(Object aptIds) {
        Log.d("aptids",aptIds.toString());
        List<String> apts = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(aptIds.toString());
            for(int i=0;i<array.length();i++){
                apts.add(array.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("apts",apts.toString());
        for (String a : apts) {
            listeners.add(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if(documentSnapshot !=null && documentSnapshot.exists()){
                        Map<String, Object> map=documentSnapshot.getData();
                        try{
                            provider.apartmentUpdateEvent(map);
                        }catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            });
            documentReferenceList.add(db.collection("apartments").document(a));
            if(documentReferenceList.size()!=0 && listeners.size()!=0)
            documentReferenceList.get(documentReferenceList.size()-1).addSnapshotListener(((RealTimeDash) provider).getActivity(), listeners.get(listeners.size()-1));
        }
    }

    public void loadMeterForSelectedApt(int selectedAptId) {
        db.collection("apartments").document(selectedAptId+"").collection("meters").get().addOnSuccessListener(((RealTimeDash) provider).getActivity(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if(documentSnapshots !=null && !documentSnapshots.isEmpty()){
                    List<Map<String,Object>> meters = new ArrayList<>();
                    for(DocumentSnapshot doc:documentSnapshots){
                        meters.add(doc.getData());
                    }
                    try{
                        provider.metersLoaded(meters);
                    }catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
        })
        .addOnFailureListener(((RealTimeDash) provider).getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void startListeningForMeters(int selectedAptId,final int id) {
//        stopListening();
        Log.d("data","selectedapt"+String.valueOf(selectedAptId));
        documentReferenceList.add(db.collection("apartments")
                .document(selectedAptId+"")
                .collection("meters")
                .document(id+"")
                .collection("consumeption")
                .document("dailyData"));
        listeners.add(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                try{
                    if(e!=null){
                        return;
                    }
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Map<String,Object> map=documentSnapshot.getData();
                        Log.d("meter",map.toString());
                        provider.dailyDataForMeterLoaded(id,map);
                    }
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        listenerRegistrations.add(documentReferenceList.get(documentReferenceList.size()-1).addSnapshotListener(((RealTimeDash) provider).getActivity(),listeners.get(listeners.size()-1)));

        documentReferenceList.add(db.collection("apartments")
                .document(selectedAptId+"")
                .collection("meters")
                .document(id+"")
                .collection("consumeption")
                .document("todayHourData"));
        listeners.add(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                try{
                    if(e!=null){
                        return;
                    }
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Map<String,Object> map=documentSnapshot.getData();
                        provider.twoHourDataTodayUpdate(id,map);
                    }
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        listenerRegistrations.add(documentReferenceList.get(documentReferenceList.size()-1).addSnapshotListener(((RealTimeDash) provider).getActivity(),listeners.get(listeners.size()-1)));

        documentReferenceList.add(db.collection("apartments")
                .document(selectedAptId+"")
                .collection("meters")
                .document(id+"")
                .collection("consumeption")
                .document("yesterDayHourData"));
        listeners.add(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                try{
                    if(e!=null){
                        return;
                    }
                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Map<String,Object> map=documentSnapshot.getData();
                        provider.twoHourDataYesterdayUpdate(id,map);
                    }
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        listenerRegistrations.add(documentReferenceList.get(documentReferenceList.size()-1).addSnapshotListener(((RealTimeDash) provider).getActivity(),listeners.get(listeners.size()-1)));
    }
}
