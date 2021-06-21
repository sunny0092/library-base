//package net.ihaha.sunny.location.utils
//
//import android.annotation.SuppressLint
//import android.os.AsyncTask
//import android.util.Log
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.MalformedURLException
//import java.net.URL
//import java.util.*
//
//
///**
// * Date: 12/04/2021.
// * @author SANG.
// * @version 1.0.0.
// */
//class FetchTask:AsyncTask<Any,String,String>(){
//    var googlePlacesData:String? = null
//    var mMap:GoogleMap? = null
//    var url:String? = null
//
//
//
//    override fun doInBackground(vararg objects:Any): String {
//        mMap = objects[0] as GoogleMap
//        url = objects[1] as String
//
//        val downloadURL = DownloadURL()
//        try{
//            googlePlacesData = downloadURL.readUrl(url!!)
//        }
//        catch (e:IOException){
//            e.printStackTrace()
//        }
//
//        return googlePlacesData!!
//    }
//
//    override fun onPostExecute(result: String) {
//        val nearbyPlaceList: List<HashMap<String,String>>
//        val parser = DataParser()
//        nearbyPlaceList = parser.parse(result)
//        showNearbyPlaces(nearbyPlaceList)
//    }
//
//    @SuppressLint("LogNotTimber")
//    private fun showNearbyPlaces(nearbyPlaceList: List<HashMap<String,String>>){
//
//        for (i in nearbyPlaceList.indices){
//            val markerOptions = MarkerOptions()
//            val googlePlace = nearbyPlaceList.get(i)
//
//            val placeName = googlePlace["place_name"]
//            val vicinity = googlePlace["vicinity"]
//            val lat = java.lang.Double.parseDouble(googlePlace.get("lat")!!)
//            val lng = java.lang.Double.parseDouble(googlePlace.get("lng")!!)
//
//            Log.d("CheckResults", "data= $nearbyPlaceList")
//
//            val latLng = LatLng(lat,lng)
//            markerOptions.position(latLng)
//            markerOptions.title("$placeName : $vicinity")
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//
//            mMap!!.addMarker(markerOptions)
//            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
//        }
//    }
//}
//
//class DownloadURL {
//
//    @SuppressLint("LogNotTimber")
//    @Throws(IOException::class)
//    fun readUrl(myUrl: String): String {
//
//        var data = ""
//        var inputStream: InputStream? = null
//        var urlConnection: HttpURLConnection? = null
//
//        try {
//            val url = URL(myUrl)
//            urlConnection = url.openConnection() as HttpURLConnection
//            urlConnection.connect()
//
//            inputStream = urlConnection.inputStream
//            val br = BufferedReader(InputStreamReader(inputStream!!))
//            val sb = StringBuffer()
//
//            val line = ""
//            while (true) sb.append(line)
//
//            data = sb.toString()
//            br.close()
//        } catch (e: MalformedURLException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            inputStream!!.close()
//            urlConnection!!.disconnect()
//        }
//
//        Log.d("CheckResults", "Returning data= $data")
//        return data
//    }
//}
//
//
