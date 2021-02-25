package com.example.parkingapp.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.parkingapp.MainActivity;
import com.example.parkingapp.ParkedListActivity;
import com.example.parkingapp.R;
import com.example.parkingapp.helper.ConvertDateHelper;
import com.example.parkingapp.helper.DateTimeHelper;
import com.example.parkingapp.helper.FileUtils;
import com.example.parkingapp.helper.PhotoHelper;
import com.example.parkingapp.helper.ProgressDialogHelper;
import com.example.parkingapp.model.DailyData;
import com.example.parkingapp.model.MemberDTO;
import com.example.parkingapp.model.MetaDTO;
import com.example.parkingapp.model.ParkedDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.checkerframework.checker.nullness.compatqual.NullableType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class InAndOutFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    Context context;
    Activity activity;
    RadioGroup radioGroup;
    RadioButton in, out;

    Button getList, buttonApply, buttonPay, buttonTakePhoto, buttonRetry;

    ImageView imageViewOut, imageViewIn;
    EditText edplateOfNumber, edinTime, edtotalTime, edprice, editTextTakePlateOfNumber, editTextTakeInTime;
    LinearLayout LayoutForOut, LayoutForIn;
    TextView textViewTime;

    int YEAR, MONTH, DAY, HOUR, MINUTE, SECOND;
    boolean imageSwitch = false;

    String plateOfNumber;
    long currentParked, totalParked;
    AsyncTess asyncTess;
    File file;
    String filepath;

    Uri image_rui = null;

    TessBaseAPI tessBaseAPI;
    MetaDTO metaDTO;
    ParkedDTO parkedDTO;
    MemberDTO memberDTO;
    long expectCost = 0;
    int usedCount;

    public InAndOutFragment() {
    }

    public InAndOutFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_and_out, container, false);
        context = getContext();

        radioGroup = view.findViewById(R.id.radioGroup);
        in = view.findViewById(R.id.radioButtonIn);
        out = view.findViewById(R.id.radioButtonOut);
        activity = getActivity();
        LayoutForOut = view.findViewById(R.id.LayoutForOut);
        LayoutForIn = view.findViewById(R.id.LayoutForIn);

        imageViewOut = view.findViewById(R.id.imageViewOut);
        imageViewIn = view.findViewById(R.id.imageViewIn);

        textViewTime = view.findViewById(R.id.textViewTime);
        edplateOfNumber = view.findViewById(R.id.editTexNumberOfCar);
        edinTime = view.findViewById(R.id.editTextParkedTime);
        edtotalTime = view.findViewById(R.id.editTextTotalParkedTime);
        edprice = view.findViewById(R.id.editTextPrice);

        getList = view.findViewById(R.id.getList);
        buttonPay = view.findViewById(R.id.buttonPay);
        buttonApply = view.findViewById(R.id.buttonApply);
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto);
        buttonRetry = view.findViewById(R.id.buttonRetry);

        editTextTakePlateOfNumber = view.findViewById(R.id.editTextTakePlateOfNumber);
        editTextTakeInTime = view.findViewById(R.id.editTextTakeInTime);

        buttonApply.setOnClickListener(this);
        getList.setOnClickListener(this);
        buttonPay.setOnClickListener(this);
        buttonTakePhoto.setOnClickListener(this);
        buttonRetry.setOnClickListener(this);

        LayoutForOut.setVisibility(View.GONE);
        LayoutForIn.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(this);
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("meta");

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                metaDTO = snapshot.getValue(MetaDTO.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonPay.setVisibility(View.GONE);
        buttonApply.setVisibility(View.GONE);

        tessBaseAPI = new TessBaseAPI();
        String dir = context.getFilesDir() + "/tesseract";
        if (checkLanguageFile(dir + "/tessdata"))
            tessBaseAPI.init(dir, "kor");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDefaultDate();
    }

    private void setDefaultDate() {
        int[] date = DateTimeHelper.getInstance().getDate();
        int[] time = DateTimeHelper.getInstance().getTime();
        YEAR = date[0];
        MONTH = date[1];
        DAY = date[2];
        HOUR = time[0];
        MINUTE = time[1];
        SECOND = time[2];

        textViewTime.setText(String.format("%04d-%02d-%02d", YEAR, MONTH, DAY));
        editTextTakeInTime.setText(String.format("%02d-%02d %02d : %02d", MONTH, DAY, HOUR, MINUTE));
        editTextTakeInTime.setEnabled(true);

        String result[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
        DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "").child(result[0]).child(result[1]);
        def.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalParked = 0;
                int count = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals("parkedList")) {
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            String state = ds2.child("state").getValue(String.class);

                            totalParked += 1;

                            if (state.equals("in")) {
                                count += 1;
                            }
                        }
                    }
                    if (!snapshot.hasChild("totalAccount")) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("totalAccount", 0);
                        def.updateChildren(hashMap);
                    }
                    if (!snapshot.hasChild("totalParked")) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("totalParked", 0);
                        def.updateChildren(hashMap);
                    }

                }
                currentParked = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onClick(View v) {
        onResume();
        switch (v.getId()) {
            case R.id.buttonTakePhoto:
                imageViewIn.setVisibility(View.VISIBLE);
                showListDialog();

                break;
            case R.id.buttonPay:


                String plateOfNum = String.valueOf(edplateOfNumber.getText()).replace(" ", "").trim();
                String willpay = String.valueOf(edprice.getText());

                if (willpay.equals("") && plateOfNum.equals("")) {
                    Toast.makeText(context, "차량을 선택하거나, 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (willpay.equals("") && !plateOfNum.equals("")) {
                    takeDate();
                } else {
                    updateOut();
                }


                break;

            case R.id.buttonApply:

                updateIn();
                imageViewIn.setVisibility(View.GONE);

                break;
            case R.id.getList:
                Intent intent = new Intent(context, ParkedListActivity.class);
                intent.putExtra("YEAR", YEAR + "");
                intent.putExtra("MONTH", MONTH + "");
                intent.putExtra("DAY", DAY + "");
                startActivityForResult(intent, 200);

                break;
            case R.id.buttonRetry:
                resetText();

        }

    }

    private void resetText() {
        editTextTakeInTime.setText("");
        editTextTakePlateOfNumber.setText("");
        edplateOfNumber.setText("");
        edinTime.setText("");
        edprice.setText("");
        edtotalTime.setText("");

    }

    private void takeDate() {
        Toast.makeText(context, "차량번호 조회 후 정산", Toast.LENGTH_LONG).show();
    }


    private void updateOut() {
        long currentTime = System.currentTimeMillis();
        boolean ismeber = false;
        if (!edplateOfNumber.getText().equals("")) {
            String result[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
            DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "").child(result[0]).child(result[1]);
            if (parkedDTO.isMember()) {
                ismeber = true;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("inTime", parkedDTO.getInTime());
            hashMap.put("outTime", currentTime);
            hashMap.put("state", "out");
            hashMap.put("coupon", ismeber);
            hashMap.put("paid", expectCost);
            def.child("parkedList").child(plateOfNumber).updateChildren(hashMap);
        }

        DatabaseReference def2 = FirebaseDatabase.getInstance().getReference("member").child(plateOfNumber);
        usedCount = 0;
        if(def2.getKey() != null){
            def2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usedCount = snapshot.child("usedCount").getValue(int.class)+1;
                    Log.d("[test]","이용횟수? "+ usedCount);
                    def2.child("usedCount").setValue(usedCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                //save
                }
            });



        }


        Toast.makeText(context, plateOfNumber + "가 출차되었습니다", Toast.LENGTH_LONG).show();
        edplateOfNumber.setText("");
        edinTime.setText("");
        edprice.setText("");
        edtotalTime.setText("");
        memberDTO = null;
        parkedDTO = null;
        plateOfNumber = "";

    }


    private void updateIn() {
        plateOfNumber = String.valueOf(editTextTakePlateOfNumber.getText()).trim();
        plateOfNumber = plateOfNumber.replaceAll(" ", "");
        //String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("inTime", System.currentTimeMillis());
        hashMap.put("state", "in");

        HashMap<String, Object> hashMap2 = new HashMap<>();
        currentParked += 1;
        totalParked += 1;
        hashMap2.put("currentUsed", currentParked);
        hashMap2.put("totalParked", totalParked);

        String result[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
        DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "").child(result[0]).child(result[1]);
        if (plateOfNumber.equals("") | plateOfNumber == null) {
            Toast.makeText(context, "번호판을 입력시켜주세요.", Toast.LENGTH_LONG).show();
        } else {
            def.child("parkedList").child(plateOfNumber).updateChildren(hashMap);
            def.updateChildren(hashMap2);

        }

        Toast.makeText(context, plateOfNumber + " 입차되었습니다.", Toast.LENGTH_LONG).show();
        editTextTakeInTime.setText("");
        editTextTakePlateOfNumber.setText("");

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButtonIn:
                resetText();
                switchForLayout(0);
                buttonPay.setVisibility(View.GONE);
                buttonApply.setVisibility(View.VISIBLE);
                break;
            case R.id.radioButtonOut:
                resetText();
                switchForLayout(1);
                buttonPay.setVisibility(View.VISIBLE);
                buttonApply.setVisibility(View.GONE);
                break;

        }
    }


    public void switchForLayout(int power) {
        if (power == 0) {
            LayoutForIn.setVisibility(View.VISIBLE);
            LayoutForOut.setVisibility(View.GONE);
        } else if (power == 1) {
            LayoutForIn.setVisibility(View.GONE);
            LayoutForOut.setVisibility(View.VISIBLE);
        }
    }

    //이미지 보이기
    public void getImageSwitch(int type) {
        if (type == 1) {
            imageSwitch = true;
        }
    }

    private void showListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                switch (which) {
                    case 0: // 새로 촬영하기 기능
                        filepath = PhotoHelper.getInstance().getNewPhotoPath();
                        // 카메라 앱 호출
                        file = new File(filepath);
                        image_rui = null;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            image_rui = FileProvider.getUriForFile(context,
                                    context.getApplicationContext().getPackageName() + ".fileprovider", file);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        } else {
                            image_rui = Uri.fromFile(file);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
                        intent.putExtra(context.AUDIO_SERVICE, false);
                        startActivityForResult(intent, 100);
                        break;
                    case 1: // 갤러리에서 가져오기
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(intent, 101);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:   // 카메라 앱 호출 후
                    Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(filepath));
                    //String filePath = FileUtils.getPath(context, data.getData());
                    //Log.d("[test]", "filepath?" + filepath);

                    Glide.with(this).load(filepath).into(imageViewIn);
                    String getNumFromPh = getData(filepath);

                    //String getNumFromPh = getData(filePath);
                    if (getNumFromPh != null && !getNumFromPh.equals("")) {
                        editTextTakePlateOfNumber.setText(getNumFromPh);
                        Toast.makeText(context, getNumFromPh + "를 확인해주세요", Toast.LENGTH_LONG).show();
                        // Log.d("[test]", "넘버? " + getNumFromPh);
                        //sendImageMessage(image_rui);
                    }


                    break;
                case 101:   // 갤러리 앱 호출 후
                    String uri = data.getData().toString();
                    String fileName = uri.substring(uri.lastIndexOf("/") + 1);
                    //Log.d("[TEST]", "fileName = " + fileName);


                    String newfilePath = FileUtils.getPath(context, data.getData());
                    String getNumFromPh2 = getData(newfilePath);
                    Glide.with(this).load(newfilePath).into(imageViewIn);
                    if (getNumFromPh2 != null && !getNumFromPh2.equals("")) {
                        editTextTakePlateOfNumber.setText(getNumFromPh2);
                        Toast.makeText(context, getNumFromPh2 + "를 확인해주세요", Toast.LENGTH_LONG).show();
                        // Log.d("[test]", "넘버? " + getNumFromPh);
                        image_rui = null;
                        image_rui = data.getData();
                        //sendImageMessage(image_rui);
                    }
                    break;

                case 200:
                    metaDTO = (MetaDTO) data.getSerializableExtra("metaDTO");
                    parkedDTO = (ParkedDTO) data.getSerializableExtra("parkedDTO");
                    plateOfNumber = parkedDTO.getPlateNumber();
                    edplateOfNumber.setText(parkedDTO.getPlateNumber());
                    java.util.Calendar cal = java.util.Calendar.getInstance(Locale.KOREAN);
                    cal.setTimeInMillis(parkedDTO.getInTime());
                    String dateTime = DateFormat.format("MM/dd hh:mm aa", cal).toString();
                    edinTime.setText(dateTime);

                    long currentTime = System.currentTimeMillis() - parkedDTO.getInTime();
                    long calTime = currentTime / 1000 / 60;

                    String gepTime = calTime + "분";
                    if (calTime > 60) {
                        long hour = calTime / 60;
                        long min = calTime % 60;
                        gepTime = hour + "시간 " + min + "분";
                    }

                    edtotalTime.setText(gepTime);


                    if (calTime < metaDTO.getBaseTime()) {
                        expectCost = metaDTO.getBaseCost();
                    } else if (calTime >= metaDTO.getFlatTime() * 60) {
                        expectCost = metaDTO.getFlatCost();
                    } else {
                        expectCost = ((calTime - metaDTO.getBaseTime()) / metaDTO.getAdditionalTime() * metaDTO.getAdditionalCost()) + metaDTO.getBaseCost();
                    }

                    edprice.setText(expectCost + "원");

                    DatabaseReference def = FirebaseDatabase.getInstance().getReference("member");

                    def.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){
                                memberDTO = ds.getValue(MemberDTO.class);
                                memberDTO.setPlateNumber(ds.getKey());
                                //Log.d("[test]",memberDTO.getPlateNumber()+" 멤버의 레퍼런스");
                                if (memberDTO.getPlateNumber().equals(plateOfNumber)){
                                    edprice.setText("회원차량");
                                    expectCost = 0;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    break;

            }
        }
    }

    private Boolean checkRec(String getNumFromPh) {
        boolean result = false;
        //8~9자인지?

        //Log.d("[test]","정규식 테스트 시작 with " + getNumFromPh);
        if (getNumFromPh.length() == 7 | getNumFromPh.length() == 8) {
            //Log.d("[test]","7~8자리 번호판");
            boolean re = Pattern.matches("^[0-9]{2,3}[ㄱ-ㅣ가-힣]\\d{4}", getNumFromPh);
            if (re) {
                //Log.d("[test]","정규식 진입.");
                result = true;
            }
        }
        return result;
    }

    private String getData(String image_rui) {
        //Log.d("[test]","파이썬 진입.");

        if (!Python.isStarted()) {
            //Log.d("[test]","파이썬 구동.");
            Python.start(new AndroidPlatform(context));
        }
        Python py = Python.getInstance();
        PyObject pywork = py.getModule("android");


        /*
        List charVoting = new ArrayList();
        List voting = new ArrayList();
        int cnt = 0;

        while (cnt < 11) {

        }

         */

        //Log.d("[test]","파이썬 모듈실행");
        //Log.d("[test]","파이썬 함수 구동후");
        PyObject croppedImagePath = pywork.callAttr("run", image_rui);
        int findLastChar = croppedImagePath.toString().lastIndexOf("/") + 1;
        //Log.d("[test]","findLastChar? "+ findLastChar);
        String imageName = croppedImagePath.toString().substring(findLastChar); // 25
        String filepath2 = croppedImagePath.toString().substring(0, findLastChar);
        File sd = Environment.getExternalStorageDirectory();
//        Log.d("[test","imagename :"+ imageName);
//        Log.d("[test","filepath2 :"+ filepath2);
//        Log.d("[test","sd :"+ sd);
        //File image = new File(sd+filePath, imageName);
        File image = new File(sd + "/DCIM/", imageName);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);

        //Log.d("[test]","비트맵 생성");
        asyncTess = new AsyncTess();
        String result = asyncTess.doInBackground(bitmap);
        String tryone = result.replace(" ", "").trim();

        //특수문자 제거하기
        tryone = tryone.replaceAll("'", "");
        tryone = tryone.replaceAll("。", "");

        boolean isRight = checkRec(tryone);

        Log.d("[test]", "번호판은? ? " + tryone);
        if (isRight) {
            return tryone;
        } else {


            //getData(image_rui);
            return "번호판을 수기로 입력해주세요.";
        }
    }

    //이미지 보내기
    private void sendImageMessage(Uri image_rui) {

        ProgressDialogHelper.getInstance().getProgressbar(context, "사진을 전송중입니다..");

        long timeStamp = System.currentTimeMillis();
        String fileNameAndPath = YEAR + "/" + MONTH + "/" + DAY + "/" + plateOfNumber;
        //이사진 이름을

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image_rui);

            ByteArrayOutputStream baos = null;
            baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath + image_rui.getLastPathSegment());
            String result[] = ConvertDateHelper.getInstance().adaptDate(MONTH, DAY);
            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 성공", Toast.LENGTH_LONG).show();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                if (plateOfNumber.equals("") || plateOfNumber == null) {
                                    Toast.makeText(context, "번호판을 입력 후에 업데이트 가능.", Toast.LENGTH_LONG).show();
                                } else {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("downloadUri", downloadUri);
                                    DatabaseReference def = FirebaseDatabase.getInstance().getReference("parkingList").child(YEAR + "")
                                            .child(result[0]).child(result[1]).child("parkedList").child(plateOfNumber);
                                    def.updateChildren(hashMap);
                                }

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ProgressDialogHelper.getInstance().removeProgressbar();
                            Toast.makeText(context, "업로드 실패", Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class AsyncTess extends AsyncTask<Bitmap, Integer, String> {
        @Override
        protected String doInBackground(Bitmap... mRelativeParams) {
            tessBaseAPI.setImage(mRelativeParams[0]);
            return tessBaseAPI.getUTF8Text();
        }
    }

    boolean checkLanguageFile(String dir) {
        File file = new File(dir);
        if (!file.exists() && file.mkdirs())
            createFiles(dir);
        else if (file.exists()) {
            String filePath = dir + "/kor.traineddata";
            File langDataFile = new File(filePath);
            if (!langDataFile.exists())
                createFiles(dir);
        }
        return true;
    }

    private void createFiles(String dir) {
        AssetManager assetMgr = context.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetMgr.open("kor.traineddata");

            String destFile = dir + "/kor.traineddata";

            outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}