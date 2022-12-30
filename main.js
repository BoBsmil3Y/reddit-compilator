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
  new Subreddit("Unexpected", 15, 4, 40),
  new Subreddit("AbruptChaos", 15, 10, 50),
  new Subreddit("StoppedWorking", 5, 8, 25),
  new Subreddit("AAAAAAAAAAAAAAAAA", 5, 2, 20),
  new Subreddit("WhyWereTheyFilming", 5, 5, 50),
  new Subreddit("Whatcouldgowrong", 25, 4, 40),
  new Subreddit("perfectlycutscreams", 20, 3, 30),
  new Subreddit("funny", 10, 5, 30)
];


/**
 * Will launch the search, download and merged of videos.
 */
async function videosAsync() {

    let videos = [];

    for(const subreddit of subreddits){
        //(subreddit.pourcentage*20 >= 100 ? subreddit.pourcentage*20 : 100)
        const posts = await RRequest.getPostsAsync(`${subreddit.name}'s videos`, subreddit.name, 150);

        videos = videos.concat(MediaController.chooseVideos(subreddit, posts.children, videoLength*60));
    }
    //shuffleArray(videos);

    for(const video of videos)
        RRequest.saveVideo(video.url, true)

}


/**
 * Will download the image of the day with the highest score from the subreddit 'cursed_images'.
 */
// TODO Maybe add a request to `cursedimages` as a backup plan
async function thumbnailAsync(){
    let thumbnail = null, thumbnails = [], images = [];

    try {
        images = await RRequest.getPostsAsync("thumbnail", "cursed_images", 40);
        thumbnails = await MediaController.chooseThumbnail(images);
    } catch (e) {
        console.error(e)
    }

    while(thumbnail == null && thumbnails.length > 0){
        thumbnail = thumbnails.pop();

        try {

            await RRequest.saveThumbnail(thumbnail.url);

        } catch (error) {
            thumbnail = null;
        }

    }
}
function shuffleArray(array) {
    let i,j,temp, size =  array.length;
    for (i = size- 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

/**
 * Launch the whole process asynchonous.
 */
function downloadMedias(){
    thumbnailAsync();
    videosAsync();
}

downloadMedias();