package com.example.notepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    // Realmクラスのプロパティを準備
    // onCreate内で初期化をするためにlateinit修飾子をつけた
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Realクラスのインスタンスを取得
        realm = Realm.getDefaultInstance()
        // 作成したリストビューにアダプターを設定する
        val notes = realm.where<Note>().findAll()
        val recyclerView = noteList
        val adapter = NoteAdapter(this, notes) { note ->
            startActivity<EditActivity>("note_id" to note.id)
        }

        recyclerView.adapter = adapter
        // recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layout.stackFromEnd = true
        recyclerView.layoutManager = layout

        // fabボタンを押した時の処理
        fab.setOnClickListener {
            startActivity<EditActivity>() //Ankoによる画面遷移
        }

    }

    // アクティビティの終了処理
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
