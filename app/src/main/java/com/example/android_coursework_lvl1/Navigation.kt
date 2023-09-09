package com.example.android_coursework_lvl1

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController

class Navigation(private val navController: NavController) {


    private fun getCurrentDestinationId(): Int? {
        return navController.currentDestination?.id
    }

    private fun navigateWithCategory(actionId: Int, args: Bundle?) {

        when (val currentDestinationId = getCurrentDestinationId()) {
            R.id.homepage, R.id.allMovies, R.id.profile, R.id.allStaff -> {
                navController.navigate(actionId, args)
            }

            R.id.search, R.id.moviePage, R.id.serialPage -> {
                navController.navigate(actionId, args)
            }

            else -> throw IllegalStateException("Cannot navigate to the specified destination from $currentDestinationId")
        }
    }

    fun navigateToMoviePage(movieId: Int?) {
        val bundle = bundleOf("id" to movieId)
        navigateWithCategory(R.id.action_homepage_to_moviePage, bundle)
    }

    fun navigateToSerialPage(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_homepage_to_serialPage, bundle)
    }

    fun navigateToAllMovies() {
        navController.navigate(R.id.action_moviePage_to_allStaff)
    }

    fun navigateMovieToAllActors(id: Int?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "actor", "movie_or_serial" to "movie")
        navigateWithCategory(R.id.action_moviePage_to_allStaff, bundle)
    }

    fun navigateMovieToAllStaff(id: Int?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "staff", "movie_or_serial" to "movie")
        navigateWithCategory(R.id.action_moviePage_to_allStaff, bundle)
    }

    fun navigateSerialToAllActors(id: Int?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "actor", "movie_or_serial" to "serial")
        navigateWithCategory(R.id.action_serialPage_to_allStaff, bundle)
    }

    fun navigateSerialToAllStaff(id: Int?) {
        val bundle = bundleOf("staff_id" to id, "actor_or_staff" to "staff", "movie_or_serial" to "serial")
        navigateWithCategory(R.id.action_serialPage_to_allStaff, bundle)
    }

    fun navigateSerialToImage(imageUrl: String?) {
        val bundle = bundleOf("imageUrl" to imageUrl)
        navigateWithCategory(R.id.action_serialPage_to_imagePage, bundle)
    }

    fun navigateMovieToImage(imageUrl: String?) {
        val bundle = bundleOf("imageUrl" to imageUrl)
        navigateWithCategory(R.id.action_moviePage_to_imagePage, bundle)
    }


    fun navigateSerialToGallery(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_serialPage_to_gallery, bundle)
    }

    fun navigateSerialToActor(id: Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_serialPage_to_actorPage, bundle)
    }

    fun navigateMovieToActor(id: Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_actorPage, bundle)
    }

    fun navigateAllStaffToActor(id: Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_allStaff_to_actorPage, bundle)
    }


    fun navigateMovieToGallery(id: Int) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_gallery, bundle)
    }

    fun navigateMovieToSimilar(id: Int) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_moviePage_to_similar, bundle)
    }

    fun navigateSerialToSimilar(id: Int?) {
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_serialPage_to_similar, bundle)
    }

    fun navigateSimilarToSerial(id:Int?){
        val bundle = bundleOf("id" to id)
        navigateWithCategory(R.id.action_similar_to_serialPage, bundle)
    }


}
