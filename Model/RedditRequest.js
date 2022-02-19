const axios = require('axios');
const path = require('path');
const fs = require('fs');

module.exports = class RedditRequest {

    static count = 0;

    /**
     * Get posts from a certain subreddit with a limit of post.
     * @param  {String} subject   A name in case of error
     * @param  {String} subreddit The subreddit to take post from
     * @param  {Number} limit     The limit of post to request
     * @return {Array<String>}    A list of Reddit post's object
     */
    static getPosts(subject, subreddit, limit){
        const data = axios
                    .get(`https://www.reddit.com/r/${subreddit}/new.json?limit=${limit}`)
                    .then(res => res.data.data)
                    .catch(error => console.error(`Error on the ${subject} request \n${error}`));

        return data;
    }

    /**
     * Download the media with the URL given.
     * @param {String}  mediaURL The URL of the thumbnail
     * @param {Boolean} isVideo s True if it's a video
     */
    static saveMedia(mediaURL, isVideo){

        const fileName = " " + this.count;
        const localFilePath = path.resolve(__dirname, "../Downloads", (isVideo ? fileName+".mp4" : fileName));

        this.count++;

        try {
            axios
                .get(mediaURL, {responseType: 'stream'})
                .then(res => res.data.pipe(fs.createWriteStream(localFilePath)))
                .catch(err => console.error(err));

        } catch (err) {
            throw `Error on the ${fileName} request \n${error}`;
        }
    }

}