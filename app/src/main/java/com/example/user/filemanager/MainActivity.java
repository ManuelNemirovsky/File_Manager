package com.example.user.filemanager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    Button buttonOpenDialog;
    Button buttonUp;
    TextView textFolder;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_listView;

    File root;
    File curFolder;

    private List<String> fileList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOpenDialog = (Button)findViewById(R.id.openDialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

        File root = new File(Environment.getRootDirectory().getAbsolutePath());
        curFolder = root;
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;

        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("Custom dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonUp = (Button) dialog.findViewById(R.id.up);

                buttonUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_listView = (ListView) dialog.findViewById(R.id.dialogList);
                dialog_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File selected = new File(fileList.get(position));
                        if (selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(MainActivity.this, selected.toString() + " selected", Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);
                        }
                    }
                });
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        super.onPrepareDialog(id, dialog);
        switch (id){
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if (f.equals(root)){
            buttonUp.setEnabled(false);
        }else{
            buttonUp.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files)
        {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , fileList);
        dialog_listView.setAdapter(directoryList);
    }
}
