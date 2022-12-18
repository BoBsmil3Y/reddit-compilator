const axios = require('axios');
const path = require('path');
const fs = require('fs');
const MediaController = require("../Controller/MediaController");
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
    static async getPostsAsync(subject, subreddit, limit){

        try {
            const response = await axios.get(`https://www.reddit.com/r/${subreddit}/new.json?limit=${limit}`);
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
    static async saveVideo(mediaURL){

        const fileName = `${this.count}`;
        const localVideoFilePath = path.resolve(__dirname, "../" + dlDir, fileName+"-video.mp4");
        const localAudioFilePath = path.resolve(__dirname, "../" + dlDir, fileName+"-audio.mp4");

        // Get video and audio.
        try {
            const video = await axios.get(mediaURL, {responseType: 'stream'});
            video.data.pipe(fs.createWriteStream(localVideoFilePath));

            const audio = await axios.get(mediaURL.replace(/DASH_[0-9]+/, "DASH_audio"), {responseType: 'stream'});
            audio.data.pipe(fs.createWriteStream(localAudioFilePath));
        }catch (e) {
            console.error(`Code : ${e.response.status}, message : ${e.response.statusText}, path : ${e.request.path}`)
        }

        try {
            MediaController.merge(this.count);
        }catch (e) {
            console.error(`Error while merge`);
        }

        this.count++;
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