package com.keelim.keelchat.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.keelim.keelchat.R
import com.keelim.keelchat.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mSignInClient: GoogleSignInClient
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        mFirebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signBtnSignin.setOnClickListener {
            mSignInClient.signInIntent.apply {
                startActivityForResult(this, CODE_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result!!.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            } else {
                Toast.makeText(this, "구글 로그인을 실패하였습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->

            if (!task.isSuccessful) { //실패했다면
                Toast.makeText(this@SignInActivity, "인증 실패하였습니다", Toast.LENGTH_LONG).show()

            } else {
                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        private const val CODE_SIGN_IN = 1000
    }
}