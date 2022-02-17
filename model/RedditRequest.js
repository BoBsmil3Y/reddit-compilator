const axios = require('axios');

module.exports = class RedditRequest {

    /**
     * Get posts from a certain subreddit with a limit of post.
     * @param  {String} subject   A name in case of error
     * @param  {String} subreddit The subreddit to take post from
     * @param  {Number} limit     The limit of post to request
     * @return {Object}           A list of Reddit post's object
     */
    static getPosts(subject, subreddit, limit){
        return makeRequest(subject, `https://www.reddit.com/r/${subreddit}/new.json?limit=`, limit);
        
    }

    /**
     * Download the chosen thumbnail with the url given.
     * @param  {String} url The URL of the thumbnail
     * @return {Object}     The image downloaded
     */
    static downloadImage(url){
        return makeRequest("download image", url, '');
    }

}


async function makeRequest(name, url, nbPost){
    const option = nbPost == '' ? {responseType: 'arraybuffer'} : {}; 
    const data = await axios
                .get(url + nbPost, option)
                .then(res => {
                    // If it's a reddit post's request
                    if(url.includes(".json")) return res.data.data;
                    // Basic request
                    return res;
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