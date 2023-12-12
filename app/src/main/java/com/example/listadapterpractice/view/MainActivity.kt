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
import com.example.listadapterpractice.model.ViewType
import com.example.listadapterpractice.repository.UserRepositoryImpl
import com.example.listadapterpractice.viewModel.MyViewModel
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val adapter: UserAdapter by lazy {
        UserAdapter(
            object : UserAdapter.ItemClickListener {
                override fun itemClick(viewType: ViewType) {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_sample, null)
                    val alertDialog = AlertDialog.Builder(this@MainActivity)
                        .setView(dialogView)
                        .create()

                    val userName = dialogView.findViewById<EditText>(R.id.userName)
                    val editBtn = dialogView.findViewById<Button>(R.id.editBtn)

                    editBtn.setOnClickListener {
                        viewModel.updateUser(userName.text.toString(), viewType as User)
                        alertDialog.dismiss()
                    }
                    alertDialog.show()
                }
            },
            object : UserAdapter.SearchBtnListener {
                override fun itemSearch(userName: String) {
                    lifecycleScope.launch {
                        viewModel.searchUser(userName)
                        //adapter.submitList(viewModel.getUser(userName))
                    }
                }
            },

            )
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


        viewModel.userList.observe(this, Observer<List<ViewType>> { data ->
            adapter.submitList(data)
        })

    }
}