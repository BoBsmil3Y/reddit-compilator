const RRequest = require("./model/RedditRequest.js");
const Controller = require("./controller/Controller.js");
const fs = require('fs');

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
      RRequest.downloadImage(thumbnail.downloadLink).then(image => { 

        const buffer = Buffer.from(image.data, 'base64');
        fs.writeFile('downloaded.jpg', buffer,  (err) => { if(err) console.error("Error on writefile : " + err);});

      }).catch(error => console.error("ERREUR \n" + error));
    
    } catch (error) {
      console.error("ERREUR \n" + error);
    }
  });
  
}

// Pour les vidéos ------------------------------
//
// Trier les posts par score
// EN partant du meilleur ->
//    Prendre sa durée
//    Prendre son URL
//    Check si on atteint la limite (%) du subreddit
//    

main();