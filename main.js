const RRequest = require("./Model/RedditRequest.js");
const MediaController = require("./Controller/MediaController.js");
const Subreddit = require("./Components/Subreddit.js");

/** Length of the wanted compilation video in minute. */
const videoLength = 13;

/** 
 * The list of subreddit to take videos from.
 * Args : Name, % of time in the video, minimun duration, maximum duration.
 */
const subreddits = [
  new Subreddit("Unexpected", 26, 4, 40),
  new Subreddit("AbruptChaos", 12, 10, 50),
  new Subreddit("StoppedWorking", 5, 8, 25),
  new Subreddit("AAAAAAAAAAAAAAAAA", 1, 2, 20),
  new Subreddit("WhyWereTheyFilming", 15, 5, 50),
  new Subreddit("blackmagicfuckery", 5, 8, 25),
  new Subreddit("Whatcouldgowrong", 26, 4, 40)
];

/**
 * Will launch the search and download of all videos.
 */
function videos(){

  subreddits.forEach(subreddit => {

    //(subreddit.pourcentage*20 >= 100 ? subreddit.pourcentage*20 : 100)
    RRequest.getPosts(`${subreddit.name}'s videos`, subreddit.name, 200).then(data => {

      const videos = MediaController.chooseVideos(subreddit, data.children, videoLength*60);
      console.log("Vidéos à télécharger = " + videos.length);
      
      videos.forEach( video => {
        RRequest.saveMedia(video.url, true)
          .then(count => {
            setTimeout(() => {
              MediaController.mergeAudioAndVideo(count);
            }, 1500);
          });
      });

    });

  });

}

/**
 * Will download the image of the day with the highest score from the subreddit 'cursed_images'.
 */
// TODO Maybe add a request to `cursedimages` as a backup plan
function thumbnail(){
  
  RRequest.getPosts("thumbnail", "cursed_images", 40).then(data => {
    try {

      const thumbnail = MediaController.chooseThumbnail(data);      
      RRequest.saveMedia(thumbnail.url, false);
    
    } catch (error) {
      console.error("ERREUR \n" + error);
    }

  });

}

/**
 * Launch the whole process asynchonous.
 */
function downloadMedias(){
  thumbnail();
  videos();
}

downloadMedias();