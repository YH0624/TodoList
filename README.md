# メモ帳アプリ
  メイン画面<br>
  <img src="https://github.com/YH0624/TodoList/blob/master/%E3%83%A1%E3%82%A4%E3%83%B3%E7%94%BB%E9%9D%A2.png">
  
  メモ帳登録画面<br>
  <img src="https://github.com/YH0624/TodoList/blob/master/%E3%83%A1%E3%83%A2%E5%B8%B3%E7%99%BB%E9%8C%B2.png">
  
# 開発環境<br>
  Android Studio Ver 3.6.1<br>
  SQLite(DBA)
  
# プログラム言語<br>
  JAVA
  
# 概要<br>
  IDとメモ記入すると、メモ内容が登録される仕様<br>
  登録されたメモ内容をタップすることで<br>
  「更新」、「削除」、「キャンセル」も可能です。

# 使い方<br>
  ■更新方法<br>
  IDとメモ記入欄を入力し、+ボタンを押す。<br>
  メモが登録される。<br>
  登録されたメモをタップすると、「更新」、「削除」、「キャンセル」を選択<br>
  メッセージボックスが表示される。<br>
  IDとメモ記入欄を入力した状態で、登録されたメモをタップ<br>
  メッセージボックスで更新をタップする。
  
  ■削除方法<br>
   削除したいメモをタップ<br>
   メッセージボックスで「削除」をタップ<br>
   
  ■キャンセル<br>
   更新及び削除をしない場合は、メッセージボックスで「キャンセル」を押す。
   
# 注力したソースコード<br>  
  MainActivity.java(onItemClick function参照)<br>
	
# 実際に書いたコード
  MainActivity.java<br> 
  https://github.com/YH0624/TodoList/blob/master/TodoList/app/src/main/java/com/example/todolist/MainActivity.java
  
  TestOpenHelper.java<br>
  https://github.com/YH0624/TodoList/blob/master/TodoList/app/src/main/java/com/example/todolist/TestOpenHelper.java
 
  activity_main.xml<br>
  https://github.com/YH0624/TodoList/blob/master/TodoList/app/src/main/res/layout/activity_main.xml
  
  strings.xml<br>
  https://github.com/YH0624/TodoList/blob/master/TodoList/app/src/main/res/values/strings.xml
  
