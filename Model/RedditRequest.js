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
     * @param {Boolean} isVideo  True if it's a video
     */
    static saveMedia(mediaURL, isVideo){

        return new Promise( (resolve, reject) => {
            const fileName = `${this.count}`;
            const localVideoFilePath = path.resolve(__dirname, "../Downloads", (isVideo ? fileName+"-video.mp4" : fileName));
            const localAudioFilePath = path.resolve(__dirname, "../Downloads", (isVideo ? fileName+"-audio.mp4" : fileName));

            // Get video or image data.
            axios
                .get(mediaURL, {responseType: 'stream'})
                .then(res => {
                    res.data.pipe(fs.createWriteStream(localVideoFilePath));
                    
                    // Get audio data if it's a video.
                    if(isVideo){
                        axios
                            .get(mediaURL.replace(/DASH_[0-9]+/, "DASH_audio"), {responseType: 'stream'})
                            .then(res => {
                                res.data.pipe(fs.createWriteStream(localAudioFilePath))
                                        .on('finish', (err) => {
                                            if (err) reject(err);
                                            else resolve(this.count);
                                        })
                                resolve
                            })
                            .catch(err => console.error(`Code : ${err.response.status}, message : ${err.response.statusText}, path : ${err.request.path}`));
                    }

                })
                .catch(err => console.error(`Code : ${err.response.status}, message : ${err.response.statusText}, path : ${err.request.path}`));

            
            
            this.count++;
        } )
    }

}