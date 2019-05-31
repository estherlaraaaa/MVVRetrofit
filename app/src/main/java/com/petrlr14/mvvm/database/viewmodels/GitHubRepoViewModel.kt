package com.petrlr14.mvvm.database.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.petrlr14.mvvm.database.RoomDB
import com.petrlr14.mvvm.database.entities.GitHubRepo
import com.petrlr14.mvvm.database.repositories.GitHubRepoRepository
import com.petrlr14.mvvm.service.retrofit.GithubService
import kotlinx.coroutines.launch

class GitHubRepoViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: GitHubRepoRepository

    //esta es la misma funcion de getService pero para retrofit
    init {
        val repoDao=RoomDB.getInstance(app).repoDao()
        val githubService = GithubService.getGithubService()
        repository= GitHubRepoRepository(repoDao, githubService)
    }

    private suspend fun insert(repo:GitHubRepo)=repository.insert(repo)

    //esta funcion es para retrofit
    //el lauch se hace xq es asincrono
    fun retrieveRepo(user:String)= viewModelScope.launch {
        this@GitHubRepoViewModel.nuke()

        val response = repository.retrieveReposAsync(user).await()
        if (response.isSuccessful) with(response){
            this.body()?.forEach {
                this@GitHubRepoViewModel.insert(it)
            }
        }else with(response){
            when (response.code()){
                404->{
                    Toast.makeText(app, "rip", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    fun getAll():LiveData<List<GitHubRepo>>{
        return repository.getAll()
    }

    private suspend fun nuke()= repository.nuke()

}