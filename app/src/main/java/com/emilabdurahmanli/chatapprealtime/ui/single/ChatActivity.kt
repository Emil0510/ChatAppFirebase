package com.emilabdurahmanli.chatapprealtime.ui.single

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emilabdurahmanli.chatapprealtime.ui.adapter.ChatAdapter
import com.emilabdurahmanli.chatapprealtime.data.response.model.User
import com.emilabdurahmanli.chatapprealtime.databinding.ActivityChatBinding
import com.emilabdurahmanli.chatapprealtime.network.FirebaseNetwork
import com.emilabdurahmanli.chatapprealtime.network.FirebaseNetwork.Companion.auth
import com.emilabdurahmanli.chatapprealtime.ui.view_model.SingleViewModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding
    private lateinit var secondUser : User
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var viewModel : SingleViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[SingleViewModel::class.java]
        secondUser = intent.getSerializableExtra("User") as User
        //Set image and names
        Picasso.get()
            .load(auth.currentUser?.photoUrl)
            .transform(CropCircleTransformation()).resize(150, 150)
            .into(binding.currnetUserImage)
        Picasso.get()
            .load(secondUser.photoUrl)
            .transform(CropCircleTransformation()).resize(150, 150)
            .into(binding.secondUserImage)
        binding.currentUserNameText.text = auth.currentUser?.displayName.toString()
        binding.secondUserNameText.text = secondUser.name


        //Recycler View
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.messagesRecyclerView.layoutManager = layoutManager
        viewModel.getMessages(secondUser.uid.toString())
        viewModel.observeMessages().observe(this, Observer {
            binding.messagesRecyclerView.adapter = ChatAdapter(it,FirebaseNetwork.auth.currentUser?.uid.toString())
            binding.messagesRecyclerView.adapter?.notifyDataSetChanged()
            binding.messagesRecyclerView.scrollToPosition(it.size-1)
        })


        setUpClickListeners()
    }
    fun setUpClickListeners(){
        binding.sendButton.setOnClickListener {
            if(binding.chatEditText.text.toString().trim().length != 0){
                viewModel.sendMessage(secondUser.uid.toString(),binding.chatEditText.text.toString().trim(), secondUser.fcmToken.toString() )
                binding.chatEditText.setText("")
            }
        }
    }
}