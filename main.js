const RRequest = require("./model/RedditRequest.js");
const Controller = require("./controller/Controller.js");

//"name" = [% , minDuration, maxDuration, (TODO)miniScore] 
const subreddit = {
  "unexpected": [25, 4, 40],
  "abruptchaos": [10, 10, 50],
  "whatcouldgowrong": [25, 4, 40],
  "facepalm": [10, 5, 30],
  "stopworking": [5, 8, 25],
  "AAAAAAAAAAAAAAAAA": [5, 5, 20],
  "whyweretheyfilming": [15, 5, 50],
  "blackmagicfuckery": [5, 8, 25]
};


function main(){
  let thumbnail;
  RRequest.getPosts("thumbnail", "cursed_images", 50).then(data => {
    try {
      thumbnail = Controller.chooseThumbnail(data);
    } catch (error) {
      console.error("ERREUR \n" + error);
    }
    console.log("Name : " + thumbnail.name + " | Url : " + thumbnail.url);
  });
  
  
  
  console.log("fin main");
}

main();