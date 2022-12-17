const axios = require('axios');
const path = require('path');
const fs = require('fs');
const dlDir = "Downloads";

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
     * Get posts from a certain subreddit with a limit of post.
     * @param  {String} subject   A name in case of error
     * @param  {String} subreddit The subreddit to take post from
     * @param  {Number} limit     The limit of post to request
     * @return {Array<String>}    A list of Reddit post's object
     */
    static async getPostsAsync(subject, subreddit, limit){

        try {
            const response =  await axios.get(`https://www.reddit.com/r/${subreddit}/new.json?limit=${limit}`);
            return response.data.data;
        } catch (e) {
            console.error(`Error on the ${subject} request \n${e}`);
            throw(`[ERROR] While getting posts of ${subject} on ${subreddit} sub.`);
        }

    }

    /**
     * Download the media with the URL given.
     * @param {String}  mediaURL The URL of the thumbnail
     * @param {Boolean} isVideo  True if it's a video
     */
    static saveMedia(mediaURL, isVideo){

        // Check if download directory exist
        fs.access(dlDir, err => {
            if (err) fs.mkdirSync( dlDir, { recursive: true })
        });

        return new Promise( (resolve, reject) => {
            const fileName = `${this.count}`;
            const localVideoFilePath = path.resolve(__dirname, "../" + dlDir, (isVideo ? fileName+"-video.mp4" : fileName));
            const localAudioFilePath = path.resolve(__dirname, "../" + dlDir, (isVideo ? fileName+"-audio.mp4" : fileName));

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

    /**
     * Download the media with the URL given.
     * @param {String}  mediaURL The URL of the thumbnail
     * @param {Boolean} isVideo  True if it's a video
     */
    static async saveVideos(mediaURL){

        return new Promise( (resolve, reject) => {
            const fileName = `${this.count}`;
            const localVideoFilePath = path.resolve(__dirname, "../" + dlDir, fileName+"-video.mp4");
            const localAudioFilePath = path.resolve(__dirname, "../" + dlDir, fileName+"-audio.mp4");

            // Get video or image data.
            axios
                .get(mediaURL, {responseType: 'stream'})
                .then(res => {
                    res.data.pipe(fs.createWriteStream(localVideoFilePath));

                    // Get audio data.
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


                })
                .catch(err => console.error(`Code : ${err.response.status}, message : ${err.response.statusText}, path : ${err.request.path}`));



            this.count++;
        } )
    }

    /**
     * Download the image given.
     * @param {String} thumbnailURL The URL of the thumbnail
     */
    static async saveThumbnail(thumbnailURL){

        const localVideoFilePath = path.resolve(__dirname, "../" + dlDir, "thumbnail.jpeg");

        fs.access(dlDir, err => { if (err) fs.mkdirSync( dlDir, { recursive: true }) }); // Check if download directory exist

        try {

            const image = await axios.get(thumbnailURL, {responseType: 'stream'});
            await image.data.pipe(fs.createWriteStream(localVideoFilePath));

        } catch (e) {
            console.error(`Code : ${e.response.status}, message : ${e.response.statusText}, message : ${e.request.message}`);
            throw("[ERROR] While getting thumbnail.");
        }

    }

}