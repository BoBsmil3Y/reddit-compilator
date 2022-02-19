const RRequest = require("./model/RedditRequest.js");
const Controller = require("./controller/Controller.js");
const Subreddit = require("./Components/Subreddit.js");

const videoLength = 13; //In minutes

const subreddits = [
  new Subreddit("Whatcouldgowrong", 26, 4, 40),
  
];

/*
  new Subreddit("Unexpected", 26, 4, 40),
  new Subreddit("AbruptChaos", 12, 10, 50),
  new Subreddit("StoppedWorking", 5, 8, 25),
  new Subreddit("AAAAAAAAAAAAAAAAA", 1, 2, 20),
  new Subreddit("WhyWereTheyFilming", 15, 5, 50),
  new Subreddit("blackmagicfuckery", 5, 8, 25)
*/


function videos(){

  subreddits.forEach(subreddit => {

    //(subreddit.pourcentage*20 >= 100 ? subreddit.pourcentage*20 : 100)
    RRequest.getPosts(`${subreddit.name}'s videos`, subreddit.name, 25).then(data => {

      const videos = Controller.chooseVideos(subreddit, data.children, videoLength*60);
      videos.forEach( video => RRequest.saveMedia(video.url, true));

    });

  });

}

function thumbnail(){
  
  RRequest.getPosts("thumbnail", "cursed_images", 20).then(data => {
    try {

      const thumbnail = Controller.chooseThumbnail(data);      
      RRequest.saveMedia(thumbnail.url, false);
    
    } catch (error) {
      console.error("ERREUR \n" + error);
    }

  });

}
  

videos();
thumbnail();