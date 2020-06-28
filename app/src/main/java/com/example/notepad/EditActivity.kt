package com.example.notepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class EditActivity : AppCompatActivity() {

    // Realmクラスのプロパティを準備
    // onCreate内で初期化をするためにlateinit修飾子をつけた
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        realm = Realm.getDefaultInstance()


        // MainActivityでリストのセルを選択してEditActivityに遷移した時の処理
        val noteId = intent.getLongExtra("note_id", -1L)
        if (noteId != -1L) {
            val note = realm.where<Note>().equalTo("id", noteId).findFirst()
            titleEdit.setText(note?.title ?: "")
            contentEdit.setText(note?.content ?: "")
            delete.visibility = View.VISIBLE
        } else {
            delete.visibility = View.INVISIBLE
        }

        save.setOnClickListener {
            // タイトルと本文が入力されているかを確認するis_valid
            var isValid = true
            when (noteId) {
                -1L -> {
                    // タイトルの入力確認　空欄ならisValidをfalse
                    if (titleEdit.text.isEmpty()) {
                        titleEdit.error = "タイトルを入力してください"
                        isValid = false
                    }
                    // 本文の入力確認　空欄ならisValidをfalse
                    if (contentEdit.text.isEmpty()) {
                        contentEdit.error = "本文を入力してください"
                        isValid = false
                    }

                    if (isValid) {
                        realm.executeTransaction {
                            val maxId = realm.where<Note>().max("id")
                            val nextId = (maxId?.toLong() ?: 0L) + 1
                            val note = realm.createObject<Note>(nextId)
                            note.title = titleEdit.text.toString()
                            note.content = contentEdit.text.toString()
                        }
                        alert("追加しました") {
                                yesButton { finish() }
                        }.show()
                    }
                }
                else -> {
                    // タイトルの入力確認　空欄ならisValidをfalse
                    if (titleEdit.text.isEmpty()) {
                        titleEdit.error = "タイトルを入力してください"
                        isValid = false
                    }
                    // 本文の入力確認　空欄ならisValidをfalse
                    if (contentEdit.text.isEmpty()) {
                        contentEdit.error = "本文を入力してください"
                        isValid = false
                    }
                    if (isValid) {
                        realm.executeTransaction {
                            val note = realm.where<Note>().equalTo("id", noteId).findFirst()
                            note?.title = titleEdit.text.toString()
                            note?.content = contentEdit.text.toString()
                        }
                        alert("編集しました") {
                                yesButton { finish() }
                        }.show()
                    }
                }
            }
        }

        delete.setOnClickListener {
            realm.executeTransaction {
                val note = realm.where<Note>().equalTo("id", noteId)?.findFirst()
                note?.deleteFromRealm()
            }
            alert("削除しますか？") {
                yesButton {
                    finish()
                }
                noButton {}
            }.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
