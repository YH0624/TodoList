package com.example.todolist;

//AndroidX
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText editTextMemo, editTextId;
    private TestOpenHelper helper;
    private SQLiteDatabase db;
    private List<String> itemNames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMemo   = findViewById(R.id.edit_text_memo);
        editTextId = findViewById(R.id.edit_text_id);

        //DB読み込み
        readData();

        //登録ボタン処理
        Button insertButton = findViewById(R.id.button_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getWritableDatabase();
                }

                String id   = editTextId.getText().toString();
                String memo = editTextMemo.getText().toString();

                if((id.length()==0)) {
                    //入力されたIDとMemoの初期化
                    editTextId.getEditableText().clear();
                    return;
                }

                if(memo.length()==0){
                    editTextMemo.getEditableText().clear();
                    return;
                }

                insertData(db, memo, Integer.valueOf(id));

                //入力されたIDとMemoの初期化
                editTextId.getEditableText().clear();
                editTextMemo.getEditableText().clear();
                //DB登録後、再度読み込み
                readData();
            }
        });
    }

    //DB読込み処理メソッド
    private void readData(){
        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }

        if(db == null){
            db = helper.getReadableDatabase();
        }
        Log.d("debug","**********Cursor");

        Cursor cursor = db.query(
                "todolistdb",
                new String[] { "ID","memo" },
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append("ID:");
            sbuilder.append(cursor.getInt(0));
            sbuilder.append(" ");
            sbuilder.append(cursor.getString(1));
            sbuilder.append("\n");
            cursor.moveToNext();
        }

        cursor.close();

        Log.d("debug","**********"+"\n"+sbuilder.toString());

        //配列へ保存
        String[] memoList = sbuilder.toString().split("\n").clone();

        //Listへ表示
        final ListView list = findViewById(R.id.list);

        list.post(new Runnable() {
            @Override public void run() {
                list.smoothScrollToPositionFromTop(0, 0, 1);
            }
        });

        //配列へArrayListにコピー
        itemNames = new ArrayList<>(Arrays.asList(memoList));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
                , android.R.layout.simple_list_item_1, itemNames ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView)super.getView(position, convertView, parent);
                view.setTextSize(16);
                return view;
            }
        };

        list.setAdapter(adapter);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

        // リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, final long id) {

                String[] alert_menu = {"更新", "削除", "キャンセル"};

                ListView list = (ListView) parent;
                String selectedItem = (String) list.getItemAtPosition(position);

                if(selectedItem.length()==0){
                    String insertId   = editTextId.getText().toString();
                    String memo       = editTextMemo.getText().toString();
                    if(insertId.length()==0 || memo.length()==0){
                        return;
                    }else{
                        String InsertId   = editTextId.getText().toString();
                        String InsertMemo = editTextMemo.getText().toString();
                        editTextId.getEditableText().clear();
                        editTextMemo.getEditableText().clear();
                        insertData(db, InsertMemo, Integer.valueOf(InsertId));
                        editTextId.getEditableText().clear();
                        editTextMemo.getEditableText().clear();
                        readData();
                        return;
                    }
                }

                //ID取得とメモ内容取得
                String[] selectedItem_get = selectedItem.split(" ");
                final String ID  = selectedItem_get[0].replace("ID:", "");
                final String Memo = selectedItem_get[1];

                alert.setItems(alert_menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int idx) {
                        // リストアイテムを選択したときの処理
                        // アイテムの更新
                        if (idx == 0) {
                            final String changeId   = editTextId.getText().toString();
                            final String changeMemo = editTextMemo.getText().toString();

                            if((changeId.length()==0) || changeMemo.length()==0) {
                                //入力されたIDとMemoの初期化
                                editTextId.getEditableText().clear();
                                editTextMemo.getEditableText().clear();
                                return;
                            }

                            // ダイアログの設定
                            alertDialog.setTitle("メモ内容");
                            alertDialog.setMessage("変更しますか?");

                            // OKボタンの設定
                            alertDialog.setPositiveButton("変更する", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OKボタン押下時の処理
                                    Log.d("AlertDialog", "Positive which :" + which);
                                    updateData(db,changeId,changeMemo,ID,Memo);
                                    editTextId.getEditableText().clear();
                                    editTextMemo.getEditableText().clear();
                                    readData();
                                }
                            });

                            // NGボタンの設定
                            alertDialog.setNegativeButton("変更しない", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // NGボタン押下時の処理
                                    Log.d("AlertDialog", "Negative which :" + which);
                                    editTextId.getEditableText().clear();
                                    editTextMemo.getEditableText().clear();
                                }
                            });

                            // ダイアログの作成と描画
                            alertDialog.show();
                        }
                        // アイテムの削除
                        else if (idx == 1) {
                            // ダイアログの設定
                            alertDialog.setTitle("メモ内容");
                            alertDialog.setMessage("削除しますか?");

                            // OKボタンの設定
                            alertDialog.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // OKボタン押下時の処理
                                    Log.d("AlertDialog", "Positive which :" + which);
                                    deleteData(db,ID,Memo);
                                    readData();
                                }
                            });

                            // NGボタンの設定
                            alertDialog.setNegativeButton("削除しない", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // NGボタン押下時の処理
                                    Log.d("AlertDialog", "Negative which :" + which);
                                }
                            });

                            // ダイアログの作成と描画
                            alertDialog.show();
                        }
                        // cancel
                        else {
                            Log.d("debug", "cancel");
                        }
                    }
                });
                alert.show();
            }
        });
    }

    //登録処理のメソッド
    private void insertData(SQLiteDatabase db, String memo, int id){
        ContentValues values = new ContentValues();
        values.put("memo", memo);
        values.put("ID", id);
        try {
            db.insert("todolistdb", null, values);
        }catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }

    //削除処理のメソッド
    private void deleteData(SQLiteDatabase db, String id, String memoInput) {
        try {
            db.delete("todolistdb", "id = ? AND memo = ?", new String[]{id,memoInput});
        } catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }

    //変更処理のメソッド
    private void updateData(SQLiteDatabase db,String changeId,String changeMemo,String id,String memo){
        ContentValues values = new ContentValues();
        values.put("memo", changeMemo);
        values.put("ID", changeId);
        try {
            db.update("todolistdb", values, "id = ? AND memo = ?", new String[]{id,memo});
        }catch (SQLException e) {
            Log.e("ERROR", e.toString());
        }
    }
}
