package com.example.dwarfia.models

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.dwarfia.R
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.database.DwarfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class DwarfsViewModel(private val repository: DwarfRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val stars: LiveData<List<Dwarf>> = repository.stars.asLiveData()
    val not_visisted: LiveData<List<Dwarf>> = repository.not_visited.asLiveData()
    val your_collection: LiveData<List<Dwarf>> = repository.your_collection.asLiveData()

    var name = ""

    //var dwarf = repository.getSingleDwarf(name)


    fun markAsVisited(name: String) {
        repository.markAsVisited(name)
        var res = 5 + 3

    }

    suspend fun markAsNotVisited(name: String){
        repository.markAsNotVisited(name)
    }

    suspend fun markAsHearted(name: String){
        repository.markAsHearted(name)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()){
                Glide.with(view.context)
                    .load(url)
                    .placeholder((R.drawable.loading_animation))
                    .error(R.drawable.ic_broken_image)
                    .transform(CenterCrop())
                    .into(view)
            }
        }



    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
}

class DwarfViewModelFactory(private val repository: DwarfRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DwarfsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DwarfsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}