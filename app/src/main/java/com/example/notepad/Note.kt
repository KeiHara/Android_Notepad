package com.example.notepad

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Note: RealmObject() {
    @PrimaryKey
    var id: Long = 0 // 主キー
    var title: String = "" // タイトル
    var content: String = "" //本文
    var date: Date = Date() // 日付
}