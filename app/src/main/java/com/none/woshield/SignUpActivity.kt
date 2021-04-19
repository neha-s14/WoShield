package com.none.woshield

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.system.exitProcess

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signin_btn_signup.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        signup_btn_signup.setOnClickListener {
            var fullname=full_name_signup.text.toString()
            var emailid=emailid_signup.text.toString()
            var phonenumber=phone_number_signup.text.toString()
            var password=password_signup.text.toString()

            when{
                fullname.isEmpty()->
                {
                    Toast.makeText(this,"Enter your Name", Toast.LENGTH_SHORT).show()
                }
                emailid.isEmpty()->                {
                    Toast.makeText(this,"Enter your Email", Toast.LENGTH_SHORT).show()
                }
                phonenumber.isEmpty()->
                {
                    Toast.makeText(this,"Enter your Phone Number", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty()->
                {
                    Toast.makeText(this,"Enter your Password", Toast.LENGTH_SHORT).show()
                }
                else->{
                    var progressDialog= ProgressDialog(this)
                    progressDialog.setTitle("Sign Up")
                    progressDialog.setMessage("Please wait this may take a while")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()
                    val mAuth:FirebaseAuth= FirebaseAuth.getInstance()
                    mAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener {task->
                        if(task.isSuccessful)
                        {
                            saveUserInfo(fullname,emailid,phonenumber,progressDialog)
                        }
                        else
                        {
                            val message=task.exception.toString()
                            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
                }
            }

        }
    }

    private fun saveUserInfo(fullname: String, emailid: String, phonenumber: String, progressDialog: ProgressDialog) {
var currentUserID=FirebaseAuth.getInstance().currentUser.uid
        var usersRef=FirebaseDatabase.getInstance().reference.child("Users")
        var userMap=HashMap<String,Any>()
        userMap.put("name",fullname)
        userMap.put("emailid",emailid)
        userMap.put("phonenumber",phonenumber)
        usersRef.child(currentUserID).setValue(userMap).addOnCompleteListener {task->
            if(task.isSuccessful)
            {
                progressDialog.dismiss()
                Toast.makeText(this,"Account has been successfully created",Toast.LENGTH_SHORT).show()
val intent= Intent(this,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else{
                val message=task.exception.toString()
                Toast.makeText(this,"Error: ${message}",Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }
        }


    }
}