package com.example.testapplication

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.example.testapplication.components.ComponentTest
import com.example.testapplication.components.DaggerComponentTest
import com.example.testapplication.endpoints.MealAPI
import com.example.testapplication.models.Post
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var tvMenuTitle: TextView
    private lateinit var myRv: RecyclerView
    private lateinit var myAdapter: MyAdapter
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(localClassName, "${exception.message} handled !")
    }
    @Inject
    lateinit var retrofit: Retrofit

    lateinit var daggerComponentTest: ComponentTest
    lateinit var mealAPI: MealAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        tvMenuTitle = findViewById(R.id.tvMenuTitle)
        myRv = findViewById(R.id.rvItems)
        /*val gm: GridLayoutManager =
            GridLayoutManager(this, 6, LinearLayoutManager.HORIZONTAL, false)
        myRv.layoutManager = gm
        myAdapter = MyAdapter(6)
        myRv.adapter = myAdapter
        myRv.addItemDecoration(
            DividerItemDecoration(
                myRv.context,
                DividerItemDecoration.HORIZONTAL
            )
        )*/

        daggerComponentTest =
            DaggerComponentTest.builder().mealAPIURL(getString(R.string.meal_api)).build()

        daggerComponentTest.inject(this)

        if (retrofit != null)
            Log.d(TAG, "Retrofit Object has been initialized")
        else
            Log.d(TAG, "Failed to initialize Retrofit Object")
        mealAPI = retrofit.create(MealAPI::class.java)
        retrofit.baseUrl().toUri().toURL().toString()
        lifecycleScope.launchWhenStarted {
            //            mealCategories()
            tryVolleyRequest()
        }
    }

    suspend fun mealCategories() {
        /*mealAPI.getMeals().enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d(TAG, t.message)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d(TAG, response.body().toString())
            }

        })*/
        withContext(Dispatchers.IO) {
            Log.d(TAG, "Getting Messages")
            delay(2000)
            val posts: List<Post>? = mealAPI.getMeals().body()
            Log.d(TAG, "Posts count: " + posts?.size)
        }

    }

    suspend fun tryVolleyRequest() {
        Toast.makeText(this, "Trying a VOlley Sync Request", Toast.LENGTH_SHORT).show()
        val cache = DiskBasedCache(cacheDir, 5 * 1024 * 1024) // 2MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the requestqueue with the cache and network.
        val requestqueue = RequestQueue(cache, network)
        requestqueue.start()

        val requestFuture: RequestFuture<String> = RequestFuture.newFuture()
        val serverRequest = object :
            StringRequest(
                Method.POST,
                "https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata",
                requestFuture as Response.Listener<String>,
                requestFuture
            ) {

            override fun getPriority(): Priority {
                return Priority.IMMEDIATE
            }
        }
//        serverRequest.setRequestQueue(requestqueue)
        requestqueue.add(serverRequest)
        withContext(Dispatchers.IO) {
            try {
                val result: String = requestFuture.get(30, TimeUnit.SECONDS)
                Log.d(TAG, "String Result: " + result)
            } catch (e: Exception) {
                Log.d(TAG, "Exception: " + e.message + "\n" + e)
            }
        }
    }
}
