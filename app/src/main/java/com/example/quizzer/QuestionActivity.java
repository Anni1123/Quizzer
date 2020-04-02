package com.example.quizzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestionActivity extends AppCompatActivity {

    private Button add,excel;
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private Dialog load;
    private DatabaseReference mref;
    public static List<QuestionsModel> list;
    public static final int cellCount=6;
    private int set;
    private String name;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar=findViewById(R.id.toolbar4);
        mref= FirebaseDatabase.getInstance().getReference();
        load=new Dialog(this);
        load.setContentView(R.layout.loading);
        load.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner));
        load.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        load.setCancelable(false);
        text=load.findViewById(R.id.textv);
        name=getIntent().getStringExtra("category");
        set=getIntent().getIntExtra("setNo",1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name+"/set"+set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add=findViewById(R.id.addbtn);
        excel=findViewById(R.id.excel);
        recyclerView=findViewById(R.id.review);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
         list=new ArrayList<>();
        adapter=new QuestionAdapter(list, name, new QuestionAdapter.DeleteListener() {
            @Override
            public void onLongClick(final int position, final String id) {
                new AlertDialog.Builder(QuestionActivity.this,R.style.Theme_AppCompat_Light_Dialog).
                        setTitle("Delete Question").setMessage("Are you Sure to delete this Delete?").
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                load.show();
                                mref.child("SETS").child(name).child("questions").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                        list.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        }
                                        load.dismiss();
                                    }
                                });
                            }
                        });
            }
        });
        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(QuestionActivity.this , Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectfile();
                }
                else {
                    ActivityCompat.requestPermissions(QuestionActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        getData(name,set);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuestion=new Intent(QuestionActivity.this,AddNewQuestionActivity.class);
                addQuestion.putExtra("category",name);
                addQuestion.putExtra("set",set);
                startActivity(addQuestion);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==101){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectfile();
            }else {
                Toast.makeText(QuestionActivity.this,"Permission Not granted",Toast.LENGTH_LONG).show();
            }
        }

    }
    private void selectfile(){
Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
intent.setType("*/*");
intent.addCategory(Intent.CATEGORY_OPENABLE);
startActivityForResult(Intent.createChooser(intent, "Select File"),102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102){
            if(resultCode==RESULT_OK){
                String filepath=data.getData().getPath();
                if(true){
                  readfile(data.getData());
                }
                else {
                    Toast.makeText(this,"Please Select an Excel file",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void readfile(Uri file){
        text.setText("Scanning Question....");
        load.show();

        HashMap<String ,Object> parentmap=new HashMap<>();
        final List<QuestionsModel> models=new ArrayList<>();

        try {
            InputStream inputStream=getContentResolver().openInputStream(file);
            XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
            XSSFSheet sheet=workbook.getSheetAt(0);
            FormulaEvaluator formulaEvaluator=workbook.getCreationHelper().createFormulaEvaluator();
            int rowscount=sheet.getPhysicalNumberOfRows();
            if(rowscount>0){
                for (int r=0;r<rowscount;r++){
                    Row row=sheet.getRow(r);
                    if(row.getPhysicalNumberOfCells()==cellCount) {
                        String question = getCellData(row,0,formulaEvaluator);
                        String A = getCellData(row,1,formulaEvaluator);
                        String B = getCellData(row,2,formulaEvaluator);
                        String C = getCellData(row,3,formulaEvaluator);
                        String D = getCellData(row,4,formulaEvaluator);
                        String correctans = getCellData(row,5,formulaEvaluator);
                        if (correctans.equals(A)||correctans.equals(B)||correctans.equals(C)||correctans.equals(D)){
                            HashMap<String,Object> quetionmap=new HashMap<>();
                            quetionmap.put("question",question);
                            quetionmap.put("optiona",A);
                            quetionmap.put("optionb",C);
                            quetionmap.put("optionc",D);
                            quetionmap.put("optiond",D);
                            quetionmap.put("correctans",correctans);
                            quetionmap.put("setNo",set);
                            String id= UUID.randomUUID().toString();
                            parentmap.put(id,quetionmap);
                            models.add(new QuestionsModel(id,question,A,B,C,D,correctans,set));
                        }
                        else {
                            text.setText("Loading");
                            load.dismiss();
                            Toast.makeText(this,"No Correct answer",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    else {
                        text.setText("Loading");
                        load.dismiss();
                        Toast.makeText(QuestionActivity.this,"row no. "+r+1+" has incorrect data",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                text.setText("Uploading....");
                FirebaseDatabase.getInstance().getReference().child("SETS").child(name).
                        child("questions").updateChildren(parentmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       list.addAll(models);
                       adapter.notifyDataSetChanged();
                   }else {
                       text.setText("Loading");
                       Toast.makeText(QuestionActivity.this,"Sometjing went wrond",Toast.LENGTH_LONG).show();
                   }
                        load.dismiss();
                    }
                });
            }else {
                text.setText("Loading");
                load.dismiss();
                Toast.makeText(QuestionActivity.this,"File is empty",Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            text.setText("Loading....");
            load.dismiss();
            Toast.makeText(QuestionActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
            text.setText("Loading....");
            load.dismiss();
            Toast.makeText(QuestionActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private String getCellData(Row row,int cellposition,FormulaEvaluator formulaEvaluator){
        String value="";
        Cell cell=row.getCell(cellposition);
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_BOOLEAN:
                return value+cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return value+cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return value+cell.getStringCellValue();
            default:
                    return value;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData(String name, final int set){
        load.show();
       mref.child("SETS").child(name).child("questions").orderByChild("setNo").equalTo(set).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String id=dataSnapshot1.getKey();
                    String question=dataSnapshot1.child("question").getValue().toString();
                    String a=dataSnapshot1.child("optiona").getValue().toString();
                    String b=dataSnapshot1.child("optionb").getValue().toString();
                    String c=dataSnapshot1.child("optionc").getValue().toString();
                    String d=dataSnapshot1.child("optiond").getValue().toString();
                    String correctans=dataSnapshot1.child("correctans").getValue().toString();
                    list.add(new QuestionsModel(id,question,a,b,c,d,correctans,set));
                }
                load.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuestionActivity.this,"Error Found",Toast.LENGTH_LONG).show();
                load.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
}
