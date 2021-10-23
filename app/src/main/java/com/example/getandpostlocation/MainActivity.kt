package com.example.getandpostlocation

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.getandpostlocation.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun getUsers(v:View) {
        if(binding.etSearchToGet.text.isNotEmpty()) {
            //show progress Dialog
            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setMessage("Please wait")
            progressDialog.show()

            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            var data: Users? = null
            val call: Call<Users?>? = apiInterface!!.getUsersInfo()

            call?.enqueue(object : Callback<Users?> {
                override fun onResponse(
                    call: Call<Users?>?,
                    response: Response<Users?>
                ) {
                    progressDialog.dismiss()
                    data = response.body()

                    data?.let { getUserLocation(it) }

                }

                override fun onFailure(call: Call<Users?>, t: Throwable?) {
                    Toast.makeText(applicationContext, "Unable to load data!", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
        }else
            Toast.makeText(applicationContext, "Please do not leave it empty!", Toast.LENGTH_SHORT).show()

    }
    fun getUserLocation(data:Users){
        val arrayNames=ArrayList<User>()
        //here if two users with same name print them all
        for(i in data)
        {
            if(i.name!!.lowercase()==binding.etSearchToGet.text.toString().lowercase())
            {
                arrayNames.add(i)
            }
        }
        Log.d("23rr","size of array ${arrayNames.size}")
        //after fetching for all names matching the user input print if location is different
        binding.tvResult.text=arrayNames[0].name+": "+arrayNames[0].location
        for(i in 1 until arrayNames.size)
        {
            if(arrayNames[i-1].location!=arrayNames[i].location)
            {
                binding.tvResult.append("\n"+arrayNames[i].name+": "+arrayNames[i].location)
            }
        }

    }

    fun addUser(v:View){
        //check if user inputs are not empty
        if(binding.etName.text.isNotEmpty()&&binding.etLocation.text.isNotEmpty()) {
            val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
            val user = User(binding.etLocation.text.toString(),binding.etName.text.toString())
            val call: Call<User> = apiInterface!!.addUsersInfo(user)

            call?.enqueue(object : Callback<User?> {
                override fun onResponse(
                    call: Call<User?>?,
                    response: Response<User?>
                ) {
                    Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Toast.makeText(applicationContext, "Unable to add person.", Toast.LENGTH_SHORT)
                        .show()
                    call.cancel()
                }
            })
        }
        else {
            Toast.makeText(applicationContext, "Please do not leave it empty!", Toast.LENGTH_SHORT).show()
        }
    }

}