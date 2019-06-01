package com.petrlr14.mvvm.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.petrlr14.mvvm.database.daos.GitHubDAO
import com.petrlr14.mvvm.database.entities.GitHubRepo
import com.petrlr14.mvvm.service.retrofit.GithubService
import kotlinx.coroutines.Deferred
import retrofit2.Response

class GitHubRepoRepository (private val repoDao:GitHubDAO, private val githubService: GithubService){

    @WorkerThread
    suspend fun insert(repo:GitHubRepo){
        repoDao.insert(repo)
    }

    fun getAll():LiveData<List<GitHubRepo>>{
        return repoDao.getAllRepos()
    }

    @WorkerThread
    suspend fun nuke(){
        return repoDao.nukeTable()
    }

    //aca empieza lo nuevo para retrofit, lo demas es lo mismo de la vez pasada
    //deferred me devuelve todo de un solo, sin hacerlo a pata

    fun retrieveReposAsync(user:String):Deferred<Response<List<GitHubRepo>>>{
        return GithubService.getGithubService().getRepos(user)
    }

}