const axios = require('axios');

const nbOfPost = 15;

axios
  .get(`https://www.reddit.com/r/Unexpected/hot.json?limit=${nbOfPost}`)
  .then(res => {
    //console.log(res.data)
    displayGreatVideos(res.data.data)
  })
  .catch(error => {
    console.error("error")
  })

//En dessous d'un certain temps
function displayGreatVideos(data){
  for(i = 0; i < nbOfPost; i++){
    let post = data.children[i].data;
    if(post.is_video && ! post.over_18 && post.media.reddit_video.duration > 5  && post.media.reddit_video.duration < 50 && post.media.reddit_video.height >= 720 && ! post.media.reddit_video.is_gif){
      
      console.log("Credits : u/" + post.author + " | Title : " + post.title + "H : " + post.media.reddit_video.height + " | W : " + post.media.reddit_video.width + " | Time : " + post.media.reddit_video.duration);
      console.log(post.media.reddit_video.fallback_url);
      
    }
  }
}