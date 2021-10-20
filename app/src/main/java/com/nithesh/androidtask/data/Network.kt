package com.nithesh.androidtask.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.io.Serializable


private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()
val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
interface Network{
    @GET(value="users")
    suspend fun getEmployeeList():List<Employee>
}
val employeeData: Network by lazy{
    retrofit.create(Network::class.java)
}
@Parcelize
data class Employee(
    val id: Int?,
    val name: String?,
    val username: String?,
    val email: String?,
    val address: Address,
    val phone: String?,
    val website: String?,
    val company: Company?
) : Parcelable
@Parcelize
data class Address(
    val street: String?,
    val suite: String?,
    val city: String?,
    val zipcode: String?,
    val geo: Geo?

):Parcelable
@Parcelize
data class Geo(
    val lat: String?,
    val lng: String?
) : Parcelable
@Parcelize
data class Company(
    val name: String?,
    val catchPhrase: String?,
    val bs: String?
):Serializable, Parcelable