package com.example.android_coursework_lvl1.navigation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.android_coursework_lvl1.R
class Navigation(private val navController: NavController) {

    private fun getCurrentDestinationId(): Int? {
        return navController.currentDestination?.id
    }

    private fun navigateWithCategory(actionId: Int, args: Bundle?) {

        when (val currentDestinationId = getCurrentDestinationId()) {
            R.id.homepage, R.id.allMovies, R.id.profile, R.id.allStaff, R.id.search, R.id.searchSettings, R.id.moviePage, R.id.filmography, R.id.personPage, R.id.similar, R.id.collection -> {
                navController.navigate(actionId, args)
            }

            else -> throw IllegalStateException("Cannot navigate to the specified destination from $currentDestinationId")
        }
    }

    fun navigateHomePageToMoviePage(movieId: Int?) {
        val bundle = bundleOf("id" to movieId)
        navigateWithCategory(R.id.action_homepage_to_moviePage, bundle)
    }

    fun navigateAllMoviesToMoviePage(movieId: Int?) {
        val bundle = bundleOf("id" to movieId)
        navController.navigate(R.id.action_allMovies_to_moviePage, bundle)
    }


    fun navigateActorToFilmography(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_personPage_to_filmography, bundle)
    }

    fun navigateMovieToAllActors(id: Int?,type:Boolean?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "actor", "type" to type)
        navigateWithCategory(R.id.action_moviePage_to_allStaff, bundle)
    }

    fun navigateMovieToAllStaff(id: Int?,type:Boolean?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "staff", "type" to type)
        navigateWithCategory(R.id.action_moviePage_to_allStaff, bundle)
    }

    fun navigateMovieToImage(imageUrl: String?) {
        val bundle = bundleOf("imageUrl" to imageUrl)
        navigateWithCategory(R.id.action_moviePage_to_imagePage, bundle)
    }

    fun navigateMovieToSeasons(id: Int) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_seasonsPage, bundle)
    }

    fun navigateMovieToActor(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_actorPage, bundle)
    }

    fun navigateAllStaffToActor(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_allStaff_to_actorPage, bundle)
    }

    fun navigateMovieToGallery(id: Int) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_gallery, bundle)
    }

    fun navigateMovieToSimilar(id: Int?, type: String) {
        val bundle = bundleOf("id" to id, "type" to type)
        navigateWithCategory(R.id.action_moviePage_to_similar, bundle)
    }

    fun navigateSimilarToMovie(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_similar_to_moviePage, bundle)
    }

    fun navigateProfileToMovie(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_profile_to_moviePage, bundle)
    }

    fun navigateProfileToCollection(type:String?,isFirstCustomCollection:Boolean?){
        val bundle = bundleOf("type" to type,"firstCustom" to isFirstCustomCollection )
        navigateWithCategory(R.id.action_profile_to_collection,bundle)
    }

    fun navigateCollectionToMovie(id: Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_collection_to_moviePage,bundle)
    }
    fun navigatePersonToMovie(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_personPage_to_moviePage, bundle)
    }

    fun navigateFilmographyToMovie(id:Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_filmography_to_moviePage, bundle)
    }

    fun navigateSearchSettingsToSearch(type:String,country:String,genre:String,yearFrom:Int,yearTo:Int,ratingFrom:Double,ratingTo:Double,order:String,watched:Boolean){
        val bundle = bundleOf("type" to type,"country" to country,"genre" to genre,"yearFrom" to yearFrom,"yearTo" to yearTo,"ratingFrom" to ratingFrom,"ratingTo" to ratingTo,"order" to order,"watched" to watched)
        navigateWithCategory(R.id.action_searchSettings_to_search, bundle)
    }

    fun navigateSearchToMovie(id: Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_search_to_moviePage,bundle)
    }


}
