package com.example.listadapterpractice.view

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadapterpractice.R
import com.example.listadapterpractice.adapter.UserAdapter
import com.example.listadapterpractice.dataSource.UserDataSourceImpl
import com.example.listadapterpractice.database.UserDatabase
import com.example.listadapterpractice.model.User
import com.example.listadapterpractice.repository.UserRepositoryImpl
import com.example.listadapterpractice.viewModel.MyViewModel
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val adapter: UserAdapter by lazy {
        UserAdapter(object : UserAdapter.ItemClickListener {
            override fun itemClick(user: User) {

                val dialogView = layoutInflater.inflate(R.layout.dialog_sample, null)
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                    .setView(dialogView)
                    .create()

                val userName = dialogView.findViewById<EditText>(R.id.userName)
                userName.hint = user.name

                val editBtn = dialogView.findViewById<Button>(R.id.editBtn)
                editBtn.setOnClickListener {
                    viewModel.updateUser(user)
                    //viewModel.clickedPosition = position
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }
        },
            object : UserAdapter.SearchBtnListener {
                override fun itemSearch(userName: String) {
                    lifecycleScope.launch {
                        adapter.submitList(viewModel.getUser(userName))
                    }
                }
            })
    }

    private val viewModel: MyViewModel by viewModels {
        viewModelFactory {
            initializer {
                MyViewModel(
                    UserRepositoryImpl(
                        UserDataSourceImpl(
                            UserDatabase.getInstance(
                                applicationContext
                            ).userDao()
                        )
                    )
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logger.addLogAdapter(AndroidLogAdapter())

        val userName: EditText = findViewById<EditText>(R.id.userName)
        val button: Button = findViewById<Button>(R.id.insertButton)
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.user_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        button.setOnClickListener {
            viewModel.insertUser(userName.text.toString())
            userName.setText("")

            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        viewModel.usersList.observe(this, Observer<List<User>> { data ->
            Logger.v("viewModel observer Called")
            //해결법1
//            val copied = data?.toMutableList()
//            UserAdapter.afterItem = copied!!.get(0)
//            Logger.v("check this "+(data === copied).toString())
//            //todo : 핵심은 깊은복사를 해야한다는 점이다. 현재는 얕은복사이기 때문에 안된다. 여기서 값을 잘 넘겨줘야지 areContentsTheSame에서 false가 반환될 것이다.
//            adapter.submitList(copied)

            //해결법2 (나쁜 방법)
//            adapter.currentList.clear()
//            adapter.currentList.addAll(data)


            //adapter.notifyItemChanged(viewModel.clickedPosition)
            adapter.submitList(viewModel.searchUserUI(data))

            Logger.v(adapter.currentList.size.toString())

            //adapter.submitList(data)
        })


    }
}