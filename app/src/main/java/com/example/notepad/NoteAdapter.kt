package com.example.notepad


import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_row.view.*

class NoteAdapter(context: Context, private val notes: OrderedRealmCollection<Note>, val onItemClicked: (Note) -> Unit) :
    RealmRecyclerViewAdapter<Note, NoteAdapter.MyViewHolder>(notes, true) {

    //<NoteAdapter.MyViewHolder>()

    // レイアウトビューからビューを生成するためのinflater
    private val inflater = LayoutInflater.from(context)

    // 表示する項目の数を取得
    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // viewの生成
        val view = inflater.inflate(R.layout.list_row, parent, false)
        // ビューホルダーの生成
        val viewHolder = MyViewHolder(view)
        // ビューをタップした時の処理
        view.setOnClickListener {
            // アダプター上の位置を取得
            val position = viewHolder.adapterPosition
            // 位置からタイムゾーンを取得
            val note = notes[position]
            onItemClicked(note)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 位置に応じたタイムゾーンを取得
        val note = notes[position]
        // 表示内容を更新
        holder.title.text = note.title
        holder.date.text = DateFormat.format("yyyy年MM月dd日", note.date)
    }

    // ViewHolderの型を定義
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // ビューホルダーの構成を定義
        val title = view.findViewById<TextView>(R.id.titleRow)
        val date = view.findViewById<TextView>(R.id.dateRow)
    }
}
