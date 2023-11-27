package com.example.listadapterpractice.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listadapterpractice.R
import com.example.listadapterpractice.model.User
import com.orhanobut.logger.Logger


class UserAdapter(
    private val itemClickListener: ItemClickListener,
    private val searchBtnListener: SearchBtnListener
) :
    ListAdapter<User, RecyclerView.ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return currentList.get(position).viewType
    }

    //todo : 반드시 이 메소드를 오버라이드 해야하는지 확인해보기
//    override fun getItemCount(): Int {
//        return super.getItemCount()
//    }

    interface SearchBtnListener {
        fun itemSearch(userName: String)
    }

    interface ItemClickListener {
        fun itemClick(user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LayoutInflater.from(parent.context)

        if (viewType == ViewType.SEARCH) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.search_user, parent, false)
            return SearchViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.user_itme_list, parent, false)
            return SearchResultViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            holder.bind()

        } else if (holder is SearchResultViewHolder) {
            holder.bind(getItem(position))
        }
    }


    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val searchKeyWord: EditText = itemView.findViewById(R.id.searchKeyWord)
        val searchBtn: Button = itemView.findViewById(R.id.searchButton)


        fun bind() {
            searchBtn.setOnClickListener {
                searchBtnListener.itemSearch(searchKeyWord.text.toString())
            }
        }

    }

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        fun bind(user: User) {
            userName.text = user.name
        }

        init {
            itemView.setOnClickListener {
                itemClickListener.itemClick(getItem(adapterPosition))
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

                // User properties may have changed if reloaded from the DB, but ID is fixed.
                //추가적으로 좀 검색을해서 찾아보니까 여기서 말하는 getId() 가 DB에서 식별이 가능한 PK 값 같은 것을 의미한다고 한다.
                // return oldItem.getId() == newItem.getId()
                Logger.v("areItemTheSame : ${oldItem.name == newItem.name}")
                return oldItem.name == newItem.name
            }

            //todo : 애초에 oldItem 이랑 newItem 객체가 같은 시점부터 답이 없다. 뭔짓을 해도 true를 반환할거 아닌가.
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

                // 아래 로그가 서로 같은 시점부터 걍 망한거다.
                Logger.v(oldItem.hashCode().toString())
                Logger.v(newItem.hashCode().toString())

                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                // 동등성 비교를 해야한다. 객체안의 서로 내용이 같은지를 비교해야함.
                return oldItem == newItem
            }

        }
    }

}