package com.petrlr14.mvvm.service.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.petrlr14.mvvm.database.entities.GitHubRepo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

const val GITHUB_BASE_URL="https://api.github.com/"

interface GithubService {

    //metodo de la http con el que se hara la peticion
    //lo que esta adentro del get es el endPoint

    @GET("users/{user}/repos")
    fun getRepos(@Path("user") user:String):Deferred<Response<List<GitHubRepo>>>
    //deferred da las propiedad para ver si ya vino o no la lista
    //response devuelve toda la info (header, body, etc)

    companion object {

        var INSTACE : GithubService? = null


        //esta es una funcion estatica porque esta en un companion object y es la manera de definir en kt un metodo estatico
        //construye nuestra interfaz para llamarla y construir sus metodos
        //este es un singleton en pocas palabras
        fun getGithubService():GithubService{
            return if (INSTACE != null){
                INSTACE!!
            }else{
                //retrofit me devuelve ya de un solo el objeto para usarlo gracias al convertFactory
                //objeto que ya tiene las funcionalidades del deferred y response
                INSTACE = Retrofit
                    .Builder()
                    .baseUrl(GITHUB_BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build()
                    .create(GithubService::class.java)
                return INSTACE!!
            }
        }
    }

}

