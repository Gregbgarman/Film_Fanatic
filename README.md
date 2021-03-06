# Film Fanatic (Solo Project)

<img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage2.PNG" width=250> &nbsp;&nbsp; &nbsp;  <img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage4.PNG" width=250> &nbsp;&nbsp; &nbsp;  <img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage3.PNG" width=250><br>

<img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage7.PNG" width=250> &nbsp;&nbsp; &nbsp;  <img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage5.PNG" width=250> &nbsp;&nbsp; &nbsp;  <img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/FFgoodimage6.PNG" width=250><br>

## Overview
Film Fanatic is a movie app that allows users to gain information on current movies playing and nearby theaters

## Motivation for App creation
As a student, I saw this application as an achievable solo project that would help grow my Android development skills. The app
contains diverse features such as watching YouTube trailers, leaving movie reviews, and using the Google Maps API. I felt that
if I was able to successfully implement all of these desired features, the development process would serve as a great 
opportunity for learning new skills and reinforcing old ones. 



## Features
- User account creation using Parse
- List of movies currently playing in theaters is viewed using The Movie Database
- User can see a description of each movie, who the director is, leading roles, and a critic score
- Users can watch a Youtube trailer of each of these movies using a YouTubePlayer
- Users can leave a review for any movie they would like to as well as see reviews left by other app users
- Users can save movies they would like to see to their "Wishlist"
- Google Maps used to show user's location and a list of nearby theaters both on the map and in a list
- Users can see a picture of each theater and see real Google reviews, for each theater, on the app itself
- Users can load each theater's website or phone number directly from the app


## Walkthrough videos of features

### creating account and Viewing movies currently playing
<img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/walkthrough1.gif" width=250><br>


### Leaving movie review and adding movie to Wishlist
<img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/filmfan2.gif" width=250><br>


### Google Maps features
<img src="https://github.com/Gregbgarman/Film_Fanatic/blob/master/filmfan3.gif" width=250><br>



## Screen Archetypes
      * Splash Screen
            * Briefly shown upon user entering app
            
      * Login/Register Screen
            * Login/Registration Fields
      
      * Home Screen
            * Displays movies currently playing in a list (recyclerview)
     
     * Movie Screen
            * Contains YouTube PLayer
            * Shows movie director and leading roles/actors
            * Contains button to add the movie to the "Wishlist"           
      
     * Movie Review Screen
            * Contains a list of reviews left by all users for a given movie
            * User can enter a dialog to leave review
            
     * Wishlist Screen
            * Contains all movies in a list (recylcerview) that the user has added to their "Wishlist"
                 
     * WishList Movie Screen
            * Displays YouTube Player to watch movie trailer and movie description
            * Contains button to remove movie from Wishlist
     
     * Google Maps Screen
            * Displays user location and theaters on the map
            * Displays a list of nearby theaters in a recyclerview
            * User can click button to gain information for a theater
            
     * Theater Screen
            * Displays name, address, and an image for the theater
            * Contains a list of real Google reviews that Google users have left for the theater
     
     * Settings Screen
           * Pressing button allows user to logout
           * Pressing button allows user to change profile picture by loading from photo gallery
            
## Models
**Film (stored in Parse backend)**
| Property | Type         | Description                    |
| -------- | ------------ | ------------------------------ |
| objectId | String       | ObjectId generated by Parse    |
| createdAt | Date        | Date that film was added to backend |
| FilmTitle  | String        | Title of movie           |
| FilmDescription | String        | Description of movie             |
| FilmRating | double       | Critic score of movie        |
| FilmPoster | String      | URL of movie poster image        |
| FilmVideo     | String         | Key required to start a youtube video |
| FilmLargePoster     | String          | URL of large movie poster image |


**Review (stored in Parse backend)**
| Property | Type       | Description                         |
| ------- | ---------- | ----------------------------------- |
| objectId | String    | ObjectId generated by Parse         |
| createdAt | Date     | Date that review was added to backend  |
| ReviewUser | String | Username of account that makes review |
| RatingValue        | double   | Value out of 5 stars in review |
| TextReview | String  | Comment given with review |
| FilmTitle     | String         | Title of film review is left for |
| ProfileImage     | File          | Profile picture of account that makes review |
            


## Future features that could enhance app
- Having access to information regarding each theater's movie showing times
- Having a list of movie trailers in a fragment or activity
- Upcoming movies can also be viewed by the user

## Other Information
- API keys needed for this app are:
      Google Maps API,
      The Movie Database API,
      Youtube API
