package com.keelim.keelchat.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.keelim.commonlibrary.BaseSplashActivity
import com.keelim.keelchat.R
import com.keelim.keelchat.databinding.ActivityMainBinding
import com.keelim.keelchat.model.ChatMessage
import com.keelim.keelchat.ui.SignInActivity
import de.hdodenhof.circleimageview.CircleImageView

//데이터베이스는 파이어베이스의 리얼타임 데이터베이스를 사용하였다.
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseUser: FirebaseUser
    private lateinit var mFirebaseDatabaseReference: DatabaseReference

    private var mUsername: String? = null
    private var mPhotoUrl: String? = null

    private lateinit var binding: ActivityMainBinding

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.message_tv_name)
        var messageImageView: ImageView = itemView.findViewById(R.id.message_iv_imagemessage)
        var messageTextView: TextView = itemView.findViewById(R.id.message_tv_message)
        var photoImageView: CircleImageView = itemView.findViewById(R.id.message_iv_profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, BaseSplashActivity::class.java).apply {
            startActivity(this)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference //파이어베이스 realtime db 시작지점을 가리키는 레퍼런스이다. (파이어베이스 리얼타임 데이터베이스 초기화)

        //전송버튼
        binding.mainBtnSend.setOnClickListener { v: View? ->
            val chatMessage = ChatMessage(binding.mainEtMessage.text.toString(), mUsername, mPhotoUrl, null) //파이어베이스에 저장할 ChatMessage 객체 (채팅아이템)
            mFirebaseDatabaseReference.child(MESSAGES_CHILD) //DB에 (MESSAGES_CHILD)messages라는 이름의 하위디렉토리(?)라는걸 만들고 여기다 데이터를 넣겠다고 생각하면된다.
                    .push() //ChatMessage에서는 id값을 설정을 따로 안했으므로 DB에서 알아서 id를 부여하고 저장해준다. 꺼내올때는 그 부여받은 id로 데이터를 꺼내올 수 있다.
                    .setValue(chatMessage) //DB에 데이터넣음
            binding.mainEtMessage.setText("")
        }

        //파이어베이스 인증
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth.currentUser!!
        if (mFirebaseUser == null) { //만약 인증이 안된 유저라면 SignInActivity(로그인화면) 띄워준다.
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        } else { //인증된 유저라면 해당 유저의 아이디(이름)과 프로필사진을 가져와 변수에 저장한다.
            mUsername = mFirebaseUser!!.displayName
            if (mFirebaseUser!!.photoUrl != null) {
                mPhotoUrl = mFirebaseUser!!.photoUrl.toString()
            }
        }
        val query: Query = mFirebaseDatabaseReference!!.child(MESSAGES_CHILD) //쿼리문의 수행위치 저장 (파이어베이스 리얼타임데이터베이스의 하위에있는 MESSAGES_CHILD에서 데이터를 가져오겠다는 뜻이다. ==> 메세지를 여기다 저장했으므로)
        val options = FirebaseRecyclerOptions.Builder<ChatMessage>() //어떤데이터를 어디서갖고올거며 어떠한 형태의 데이터클래스 결과를 반환할거냐 옵션을 정의한다.
                .setQuery(query, ChatMessage::class.java)
                .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: ChatMessage) {
                holder.messageTextView.text = model.text
                holder.nameTextView.text = model.name
                if (model.photoUrl == null) { //프로필사진없는 경우 기본이미지로 세팅
                    holder.photoImageView.setImageDrawable(ContextCompat.getDrawable(this@MainActivity,
                            R.drawable.ic_account_circle_black_24dp)) //벡터이미지 넣을떈 이런식으로 넣어줘야한다.
                } else { //사진이있을 경우(Glide 사용)
                    Glide.with(this@MainActivity).load(model.photoUrl).into(holder.photoImageView)

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
                return MessageViewHolder(view)
            }
        }


        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.messageRecyclerView.layoutManager = layoutManager
        binding.messageRecyclerView.adapter = mFirebaseAdapter


        // 새로운 글이 추가되면 제일 하단으로 포지션 이동
        mFirebaseAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                val friendlyMessageCount = mFirebaseAdapter.itemCount
                val layoutManager = binding.messageRecyclerView.getLayoutManager() as LinearLayoutManager?
                val lastVisiblePosition = layoutManager!!.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    binding.messageRecyclerView.scrollToPosition(positionStart)
                }
            }
        })

        binding.messageRecyclerView.addOnLayoutChangeListener(View.OnLayoutChangeListener { view: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
            if (bottom < oldBottom) {
                view.postDelayed({ binding.messageRecyclerView.smoothScrollToPosition(mFirebaseAdapter.getItemCount()) }, 100)
            }
        })
    }

    override fun onStart() {
        mFirebaseAdapter!!.startListening()
        super.onStart()
    }

    override fun onStop() {
        mFirebaseAdapter!!.stopListening()
        super.onStop()
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}