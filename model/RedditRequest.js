const axios = require('axios');

module.exports = class RedditRequest {

    //prendre en compte une durée global
    //faire un appel pour chaque subreddit (avec leur durée perso)
    static getPosts(subject, subreddit, limit){
        return new Promise(
            (resolve, reject) => {
                try {
                    const posts = makeRequest(subject, `https://www.reddit.com/r/${subreddit}/new.json?limit=`, limit);
                    resolve(posts);
                }
                catch(error) {
                    reject(error);
                }
            }
        );
    }

}

async function makeRequest(name, url, nbPost){
    const data = await axios
                .get(url + nbPost)
                .then(res => {
                    return res.data.data;
                })
                .catch(error => {
                    console.error(`Error on the ${name} request \n${error}`);
                });

    return data;
}

/*
function displayGreatVideos(data){
    let temp = [];
    for(i = 0; i < nbOfPost; i++){
        let post = data.children[i].data;
        if(post.is_video && 
        ! post.over_18 && 
        post.media.reddit_video.duration > miniDuration  && 
        post.media.reddit_video.duration < maxDuration && 
        post.media.reddit_video.height >= miniVideoHeight && 
        ! post.media.reddit_video.is_gif) {
        
            console.log("Credits : u/" + post.author + " | Title : " + post.title + "H : " + post.media.reddit_video.height + " | W : " + post.media.reddit_video.width + " | Time : " + post.media.reddit_video.duration);
            console.log(post.media.reddit_video.fallback_url);
            temp.push(post.media.reddit_video.fallback_url);
        }
    }
}

*/


/*
Save requête thumbnail :
axios
    .get(`https://www.reddit.com/r/Cursed_Images/new.json?limit=${nbOfPostThumbnail}`)
    .then(res => {
        thumbnail = checkValidThumbnail(res.data.data);
        display();
    })
    .catch(error => {
        console.error("Error on the thumbnail request");
        console.error(error);
    });


function request(url, nbPost){
    axios
        .get(url + nbPost)
        .then(res => {
            return res.data.data;
        })
        .catch(error => {
            console.error("Error on the thumbnail request");
            console.error(error);
        });
}

function displayGreatVideos(data){
    let temp = [];
    for(i = 0; i < nbOfPost; i++){
        let post = data.children[i].data;
        if(post.is_video && 
        ! post.over_18 && 
        post.media.reddit_video.duration > miniDuration  && 
        post.media.reddit_video.duration < maxDuration && 
        post.media.reddit_video.height >= miniVideoHeight && 
        ! post.media.reddit_video.is_gif) {
        
            console.log("Credits : u/" + post.author + " | Title : " + post.title + "H : " + post.media.reddit_video.height + " | W : " + post.media.reddit_video.width + " | Time : " + post.media.reddit_video.duration);
            console.log(post.media.reddit_video.fallback_url);
            temp.push(post.media.reddit_video.fallback_url);
        }
    }
}

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
*/