const axios = require('axios');

const nbOfPost = 15;
const nbForThumbnails = 500;
const scoreMiniThumbnail = 199;

let thumbnails = [];


const subreddit = {
  "unexpected": 5,
  "abruptchaos": 5,
  "whatcouldgowrong": 10,
  "facepalm": 5,
  "stopworking": 1,
  "okbuddyretard": 1,
  "AAAAAAAAAAAAAAAAA": 2,
  "whyweretheyfilming": 15,
  "blackmagicfuckery": 5 
};
/*
axios
  .get(`https://www.reddit.com/r/Unexpected/hot.json?limit=${nbOfPost}`)
  .then(res => {
    //console.log(res.data)
    displayGreatVideos(res.data.data)
  })
  .catch(error => {
    console.error("error")
  })
*/


//En dessous d'un certain temps
function displayGreatVideos(data){
  for(i = 0; i < nbOfPost; i++){
    let post = data.children[i].data;
    if(post.is_video && 
      ! post.over_18 && 
      post.media.reddit_video.duration > 5  && 
      post.media.reddit_video.duration < 50 && 
      post.media.reddit_video.height >= 720 && 
      ! post.media.reddit_video.is_gif) {
      
      console.log("Credits : u/" + post.author + " | Title : " + post.title + "H : " + post.media.reddit_video.height + " | W : " + post.media.reddit_video.width + " | Time : " + post.media.reddit_video.duration);
      console.log(post.media.reddit_video.fallback_url);
      
    }
  }
}



const getThumbnail = axios
  .get(`https://www.reddit.com/r/Cursed_Images/new.json?limit=${nbForThumbnails}`)
  .then(res => {
    checkValidThumbnail(res.data.data)
    display();
  })
  .catch(error => {
    console.error("error")
    console.error(error);
  })

// -ne doit pas contenir ("Removed by reddit")
// -200 de scores
function checkValidThumbnail(data) {
  for(i = 1; i < data.children.length; i++){
    resp = data.children[i].data;
    if(! resp.is_video &&
      ! resp.over_18 &&
      resp.score > scoreMiniThumbnail &&
      resp.upvote_ratio >= 0.8 &&
      resp.title.includes("Cursed")&&
      ! resp.title.includes("Removed by reddit")){
        console.log("url : " + resp.url);
        thumbnails.push(resp.url);
      }
  }
}


function display(){
  console.log("taille " + thumbnails.length);
  for(i = 0; i < thumbnails.length; i++){
    console.log("Thumbnail proposée n°"+ i + " :" + thumbnails[i] + "\n");
  }
}

let data = getThumbnail;

/* DOC

var dict = {};

// Adding key-value -pairs
dict['key'] = 'value'; // Through indexer
dict.anotherKey = 'anotherValue'; // Through assignment

// Looping through
for (var item in dict) {
  console.log('key:' + item + ' value:' + dict[item]);
  // Output
  // key:key value:value
  // key:anotherKey value:anotherValue
}

// Non existent key
console.log(dict.notExist); // undefined

// Contains key?
if (dict.hasOwnProperty('key')) {
  // Remove item
  delete dict.key;
}

// Looping through
for (var item in dict) {
  console.log('key:' + item + ' value:' + dict[item]);
  // Output
  // key:anotherKey value:anotherValue
}

*/